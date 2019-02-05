/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.os.fsys.service;

import com.neptuo.os.core.data.dao.UserRoleDAO;
import com.neptuo.os.fsys.data.model.Directory;
import com.neptuo.os.fsys.data.model.File;
import com.neptuo.os.core.data.model.User;
import com.neptuo.os.core.data.model.UserRole;
import com.neptuo.data.DataStorageException;
import com.neptuo.data.dao.BaseDAO;
import com.neptuo.service.annotation.ServiceClass;
import com.neptuo.service.annotation.ServiceMethod;
import com.neptuo.service.io.Serializer;
import com.neptuo.os.fsys.NoSuchFolderException;
import com.neptuo.os.fsys.data.dao.FileDAO;
import com.neptuo.os.fsys.data.dao.FolderDAO;
import com.neptuo.os.fsys.data.model.FileSystemPermission;
import com.neptuo.os.fsys.util.Path;
import com.neptuo.os.fsys.util.ResolveResult;
import com.neptuo.service.HttpException;
import com.neptuo.service.annotation.RequestInput;
import com.neptuo.service.result.CollectionResult;
import com.neptuo.service.result.MultiCollectionResult;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mara
 */
@ServiceClass(url = "/fsys/folders")
public class FolderService {
    public static final String ROOT_NAME = "folderItems";
    public static final String RECORD_NAME = "folderItem";

    @ServiceMethod
    public MultiCollectionResult getItemsFromFolder(User current, BaseDAO dao, Serializer s,
            @RequestInput("folders") List<FolderItem> items, @RequestInput("foldersOnly") Boolean foldersOnly,
            @RequestInput("filterName") String filterName, @RequestInput("filterExtension") String filterExtension)
            throws DataStorageException, HttpException {
        List<UserRole> roles = dao.createDAO(UserRoleDAO.class).findManageable(current);
        List<Directory> folderItems = new ArrayList<Directory>();
        List<File> fileItems = new ArrayList<File>();

        for (FolderItem item : items) {
            if (item.getPath() != null) {
                // Find by path
                ResolveResult result = Path.resolve(dao, item.getPath(), current);
                if (result != null && result.getResult() != null && result.isFolder()) {
                    folderItems.addAll(dao.createDAO(FolderDAO.class).findFolders((Directory) result.getResult(), current));
                    if (foldersOnly == null || !foldersOnly) {
                        fileItems.addAll(dao.createDAO(FileDAO.class).findFiles((Directory) result.getResult(), current, filterName, filterExtension));
                    }
                } else {
                    throw new NoSuchFolderException("Passed path to file!");
                }
            } else {
                // Find by id
                Directory parent = dao.createDAO(FolderDAO.class).find(item.getId());
                folderItems.addAll(dao.createDAO(FolderDAO.class).findFolders(parent, current));
                if (foldersOnly == null || !foldersOnly) {
                    fileItems.addAll(dao.createDAO(FileDAO.class).findFiles(parent, current, filterName, filterExtension));
                }
            }
        }

        return new MultiCollectionResult(ROOT_NAME, RECORD_NAME, folderItems, fileItems);
    }

    @ServiceMethod(name="itemsInfo")
    public CollectionResult<Directory> getItemsInfo(User current, FolderDAO dao, Serializer s, @RequestInput("folders") List<FolderItem> items) throws DataStorageException, HttpException {
        List<Directory> folderItems = new ArrayList<Directory>();

        for (FolderItem item : items) {
            if (item.getPath() != null) {
                ResolveResult result = Path.resolve(dao, item.getPath(), current);
                if (result.isFolder()) {
                    Directory dir = dao.find(result.getResult().getId());
                    dir.setPath(Path.compose(dao, dir, current));
                    folderItems.add(dir);
                } else {
                    throw new NoSuchFolderException("Passed path to file!");
                }
            } else {
                Directory dir = dao.find(item.getId());
                dir.setPath(Path.compose(dao, dir, current));
                folderItems.add(dir);
            }
        }

        return new CollectionResult<Directory>(ROOT_NAME, RECORD_NAME, folderItems);
    }

    @ServiceMethod(name = "create")
    public CollectionResult<Directory> createFolder(User current, Serializer s, FolderDAO dao, @RequestInput("folders") List<Directory> dirs) throws DataStorageException, HttpException {
        List<Directory> result = new ArrayList<Directory>();
        for (Directory d : dirs) {
            d.setParent(dao.find(d.getParent().getId()));
            result.add(dao.create(d, current, true));
        }
        return new CollectionResult<Directory>(ROOT_NAME, RECORD_NAME, result);
    }

    @ServiceMethod(name = "delete")
    public void deleteFolder(User current, @RequestInput("folders") List<Directory> dirs, FolderDAO dao) throws DataStorageException, HttpException {
        for (Directory d : dirs) {
            dao.delete(d.getId(), current);
        }
    }

    @ServiceMethod(name = "rename")
    public CollectionResult<Directory> renameFolder(User current, FolderDAO dao, @RequestInput("folders") List<Directory> dirs) throws DataStorageException, HttpException {
        List<Directory> result = new ArrayList<Directory>();
        for (Directory d : dirs) {
            result.add(dao.rename(d.getId(), d.getName(), current));
        }
        return new CollectionResult<Directory>(FolderService.ROOT_NAME, FolderService.RECORD_NAME, result);
    }

    @ServiceMethod(name = "setIsPublic")
    public CollectionResult<Directory> setIsPublic(User current, FolderDAO dao, @RequestInput("folders") List<Directory> dirs) throws DataStorageException, HttpException {
        List<Directory> result = new ArrayList<Directory>();
        for (Directory d : dirs) {
            result.add(dao.setIsPublic(d.getId(), current, d.getIsPublic()));
        }
        return new CollectionResult<Directory>(ROOT_NAME, RECORD_NAME, result);
    }

    @ServiceMethod(name = "getPermissions")
    public CollectionResult<FileSystemPermission> getPermissions(User current, FolderDAO dao, @RequestInput("folders") List<FolderItem> items) throws DataStorageException, HttpException {
        List<FileSystemPermission> perms = new ArrayList<FileSystemPermission>();
        for (FolderItem item : items) {
            perms.addAll(dao.getPermissions(item.getId(), current));
        }

        return new CollectionResult<FileSystemPermission>("permissions", perms);
    }

    @ServiceMethod(name = "addPermissions")
    public CollectionResult<FileSystemPermission> addPermissions(User current, FolderDAO dao, @RequestInput("permissions") List<FileSystemPermission> perms) throws DataStorageException, HttpException {
        List<FileSystemPermission> newPerms = new ArrayList<FileSystemPermission>();
        for (FileSystemPermission perm : perms) {
            perm = dao.createPermission(perm.getIdentityId(), perm.getTypeId(), perm.getTargetId());
            FileSystemPermission fsp = dao.addPermission(perm.getTargetId(), perm, current);
            if (fsp != null) {
                newPerms.add(fsp);
            }
        }
        return new CollectionResult<FileSystemPermission>("permissions", newPerms);
    }

    @ServiceMethod(name = "removePermissions")
    public void removePermissions(User current, FolderDAO dao, @RequestInput("permissions") List<FileSystemPermission> perms) throws DataStorageException, HttpException {
        for (FileSystemPermission perm : perms) {
            perm = dao.getPermission(perm.getIdentityId(), perm.getTypeId(), perm.getTargetId());
            dao.removePermission(perm.getTargetId(), perm, current);
        }
    }

    @ServiceMethod(name="move")
    public CollectionResult<Directory> move(User current, FolderDAO dao, @RequestInput("folders") List<FolderCopyMoveItem> folders) throws DataStorageException, HttpException {
        List<Directory> result = new ArrayList<Directory>();
        for(FolderCopyMoveItem item : folders) {
            result.add(dao.move(item.getFolderId(), item.getTargetId(), current));
        }
        return new CollectionResult<Directory>(FolderService.ROOT_NAME, FolderService.RECORD_NAME, result);
    }

    @ServiceMethod(name="copy")
    public CollectionResult<Directory> copy(User current, FolderDAO dao, @RequestInput("folders") List<FolderCopyMoveItem> folders) throws DataStorageException, HttpException {
        List<Directory> result = new ArrayList<Directory>();
        for(FolderCopyMoveItem item : folders) {
            result.add(dao.copy(item.getFolderId(), item.getTargetId(), current));
        }
        return new CollectionResult<Directory>(FolderService.ROOT_NAME, FolderService.RECORD_NAME, result);
    }
}

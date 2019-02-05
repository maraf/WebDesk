/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.os.fsys.service;

import com.neptuo.os.core.data.model.User;
import com.neptuo.data.DataStorageException;
import com.neptuo.data.UploadStorage;
import com.neptuo.os.fsys.data.model.File;
import com.neptuo.service.annotation.ServiceClass;
import com.neptuo.service.annotation.ServiceMethod;
import com.neptuo.service.io.Serializer;
import com.neptuo.os.fsys.data.dao.FileDAO;
import com.neptuo.os.fsys.data.dao.FolderDAO;
import com.neptuo.os.fsys.data.model.FileSystemPermission;
import com.neptuo.os.fsys.util.Files;
import com.neptuo.service.HttpException;
import com.neptuo.service.HttpNotAcceptableException;
import com.neptuo.service.annotation.RequestInput;
import com.neptuo.service.result.CollectionResult;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.fileupload.FileItem;

/**
 *
 * @author Mara
 */
@ServiceClass(url = "/fsys/files")
public class FileService {

    @ServiceMethod(name = "create")
    public CollectionResult<File> createFile(User current, Serializer s, FileDAO fidao, FolderDAO fodao, UploadStorage uploads, @RequestInput("files") List<File> files)
            throws DataStorageException, HttpException, IOException {
        List<File> newFiles = new ArrayList<File>();
        for (File f : files) {
            FileItem item = uploads.get(f.getUploadKey());
            if (item != null) {
                if (f.getName() == null || "".equals(f.getName())) {
                    f.setName(item.getName());
                }
                f.setType(item.getContentType());
                f.setParent(fodao.find(f.getParent().getId()));
                newFiles.add(fidao.create(f, current, item));
                uploads.remove(f.getUploadKey());
            } else {
                throw new HttpNotAcceptableException("Missing upload file");
            }
        }

        return new CollectionResult<File>(FolderService.ROOT_NAME, FolderService.RECORD_NAME, newFiles);
    }

    @ServiceMethod(name = "createEmpty")
    public CollectionResult<File> createEmptyFile(User current, Serializer s, FileDAO fidao, FolderDAO fodao, @RequestInput("files") List<File> files) throws DataStorageException, HttpException, IOException {
        List<File> result = new ArrayList<File>();
        for (File f : files) {
            f.setParent(fodao.find(f.getParent().getId()));
            f.setType(Files.extensionToMimeType(f.getName()));
            result.add(fidao.createEmpty(f, current));
        }
        return new CollectionResult<File>(FolderService.ROOT_NAME, FolderService.RECORD_NAME, result);
    }

    @ServiceMethod(name = "delete")
    public void deleteFile(User current, FileDAO dao, @RequestInput("files") List<File> files) throws DataStorageException, HttpException {
        for (File f : files) {
            dao.delete(f.getId(), current);
        }
    }

    @ServiceMethod(name = "rename")
    public CollectionResult<File> renameFile(User current, FileDAO dao, @RequestInput("files") List<File> files) throws DataStorageException, HttpException {
        List<File> result = new ArrayList<File>();
        for (File f : files) {
            result.add(dao.rename(f.getId(), f.getName(), current));
        }
        return new CollectionResult<File>(FolderService.ROOT_NAME, FolderService.RECORD_NAME, result);
    }

    @ServiceMethod(name = "newVersion")
    public void uploadNewVersion(User current, FileDAO dao, UploadStorage uploads, @RequestInput("files") List<File> files) throws DataStorageException, HttpException, IOException {
        for (File f : files) {
            dao.uploadFile(f.getId(), current, uploads.get(f.getUploadKey()));
        }
    }

    @ServiceMethod(name = "setIsPublic")
    public CollectionResult<File> setIsPublic(User current, FileDAO dao, @RequestInput("folders") List<File> files) throws DataStorageException, HttpException {
        List<File> result = new ArrayList<File>();
        for (File f : files) {
            result.add(dao.setIsPublic(f.getId(), current, f.getIsPublic()));
        }
        return new CollectionResult<File>(FolderService.ROOT_NAME, FolderService.RECORD_NAME, result);
    }

    @ServiceMethod(name = "getPermissions")
    public CollectionResult<FileSystemPermission> getPermissions(User current, FileDAO dao, @RequestInput("files") List<File> items) throws DataStorageException, HttpException {
        List<FileSystemPermission> perms = new ArrayList<FileSystemPermission>();
        for (File item : items) {
            perms.addAll(dao.getPermissions(item.getId(), current));
        }

        return new CollectionResult<FileSystemPermission>("permissions", perms);
    }

    @ServiceMethod(name = "addPermissions")
    public CollectionResult<FileSystemPermission> addPermissions(User current, FileDAO dao, @RequestInput("permissions") List<FileSystemPermission> perms) throws DataStorageException, HttpException {
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
    public void removePermissions(User current, FileDAO dao, @RequestInput("permissions") List<FileSystemPermission> perms) throws DataStorageException, HttpException {
        for (FileSystemPermission perm : perms) {
            perm = dao.getPermission(perm.getIdentityId(), perm.getTypeId(), perm.getTargetId());
            dao.removePermission(perm.getTargetId(), perm, current);
        }
    }

    @ServiceMethod(name="move")
    public CollectionResult<File> move(User current, FileDAO dao, @RequestInput("files") List<FileCopyMoveItem> files) throws DataStorageException, HttpException {
        List<File> result = new ArrayList<File>();
        for(FileCopyMoveItem item : files) {
            result.add(dao.move(item.getFileId(), item.getTargetId(), current));
        }
        return new CollectionResult<File>(FolderService.ROOT_NAME, FolderService.RECORD_NAME, result);
    }

    @ServiceMethod(name="copy")
    public CollectionResult<File> copy(User current, FileDAO dao, @RequestInput("files") List<FileCopyMoveItem> files) throws DataStorageException, HttpException {
        List<File> result = new ArrayList<File>();
        for(FileCopyMoveItem item : files) {
            result.add(dao.copy(item.getFileId(), item.getTargetId(), current));
        }
        return new CollectionResult<File>(FolderService.ROOT_NAME, FolderService.RECORD_NAME, result);
    }
}

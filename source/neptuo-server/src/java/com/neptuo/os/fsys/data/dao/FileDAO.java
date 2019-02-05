/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.fsys.data.dao;

import com.neptuo.os.core.data.dao.AccessTypeDAO;
import com.neptuo.data.DataStorageException;
import com.neptuo.data.QueryBuilder;
import com.neptuo.data.QueryComparator;
import com.neptuo.data.QueryOrder;
import com.neptuo.os.core.data.dao.UserDAO;
import com.neptuo.os.fsys.data.model.Directory;
import com.neptuo.os.fsys.data.model.File;
import com.neptuo.os.core.data.model.User;
import com.neptuo.os.core.util.PublicHelper;
import com.neptuo.os.fsys.InvalidNameException;
import com.neptuo.os.fsys.NameMustBeUniqueException;
import com.neptuo.os.fsys.NullUploadException;
import com.neptuo.os.fsys.PermissionDeniedException;
import com.neptuo.os.fsys.PermissionsType;
import com.neptuo.os.fsys.UnderlayingFileSystemException;
import com.neptuo.os.fsys.util.Files;
import com.neptuo.os.fsys.util.Path;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.MimetypesFileTypeMap;
import javax.persistence.NoResultException;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Mara
 */
public class FileDAO extends FsysItemDAO<File> {

    @Override
    public File find(Long id) throws DataStorageException {
        return getDs().find(File.class, id);
    }

    @Override
    public File find(String publicId) throws DataStorageException {
        return (File) getResultOrNull(getDs().createQuery(getDs().factory().select().from(File.class, "d").where("publicId", QueryComparator.EQ)).setParameter("publicId", publicId));
    }

    public List<File> findFiles(Directory d, User current, String nameFilter, String extension) throws DataStorageException {
        QueryBuilder qb = getDs().factory().select("f", true).from(File.class, "f")
                .join("f", "permissions", "p");

        if(nameFilter != null) {
            qb.where("f", "name", QueryComparator.LIKE, "'" + nameFilter + "%'").and();
        }
        
        qb.where("f", "parent", QueryComparator.EQ)
                .and()
                .where("p", "identity", QueryComparator.IN)
                .and()
                .where("p", "type.level", QueryComparator.GE, createDAO(AccessTypeDAO.class).find(AT_READ).getLevel())
                .order("f", "id", QueryOrder.ASC);

        List<File> files = (List<File>) getDs()
                .createQuery(qb)
                .setParameter("parent", d)
                .setParameter("identity", createDAO(UserDAO.class).findManageable(current))
                .getResultList();

        String path = Path.compose(this, d, current);
        for(File f : files) {
            f.setSize(Files.size(this, f));
            f.setPath(path + f.getName());
        }

        if (extension != null) {
            List<File> extFiles = new ArrayList<File>();
            for (File file : files) {
                if (file.getName().endsWith(extension)) {
                    extFiles.add(file);
                }
            }
            return extFiles;
        } else {
            return files;
        }
    }

    @Override
    protected void setNonPersistent(File data, User current) throws DataStorageException {
            data.setSize(Files.size(this, data));
            data.setPath(Path.compose(this, data, current));
    }

    @Override
    protected QueryBuilder getQueryForNameCheck() throws DataStorageException {
        return getDs().factory()
                .select()
                .from(File.class, "f")
                .where("parent", QueryComparator.EQ)
                .and()
                .where("name", QueryComparator.EQ);
    }

    @Override
    protected QueryBuilder getQueryForNameCheckExceptId() throws DataStorageException {
        return getDs().factory()
                .select()
                .from(File.class, "f")
                .where("parent", QueryComparator.EQ)
                .and()
                .where("id", QueryComparator.NE)
                .and()
                .where("name", QueryComparator.EQ);
    }

    public void delete(Long id, User current) throws DataStorageException {
        File model = find(id);

        if(!canManage(model, current))
            throw new PermissionDeniedException("User can't delete this file!");

        new java.io.File(Path.real(this, model)).delete();
        getDs().remove(model);
    }

    public File createEmpty(File model, User current) throws DataStorageException, IOException {
        if(model.getParent() == null)
            throw new DataStorageException("Parent can't be null!");

        if(!isNameUsed(model.getName(), model.getParent()))
            throw new NameMustBeUniqueException();

        if(!checkNameValidity(model.getName()))
            throw new InvalidNameException();

        if(!createDAO(FolderDAO.class).canReadWrite(model.getParent(), current))
            throw new PermissionDeniedException("User can't write to parent folder!");

        java.io.File realFile = new java.io.File(Path.real(this, model));
        realFile.createNewFile();

        model.setCreated(new Date());
        model.setModified(new Date());
        model.setOwner(current);
        model.setType(new MimetypesFileTypeMap().getContentType(realFile));
        model.setIsPublic(false);
        model.setPublicId(PublicHelper.random());

        //Save
        getDs().persist(model);

        //If permissions are empty, use parent permissions, if parent is system, use default
        if(model.getPermissions().isEmpty()) {
            createPermissions(model, current, PermissionsType.DEFAULT);
        }

        model.setSize(0L);
        model.setPath(Path.compose(this, model, current));

        //Return managed instance
        return getDs().merge(model);
    }

    public File create(File model, User current, FileItem upload) throws DataStorageException, IOException {
        model = createEmpty(model, current);

        if(upload != null) {
            java.io.File f = new java.io.File(Path.real(this, model));
            try {
                upload.write(f);
            } catch (Exception ex) {
                Logger.getLogger(FileDAO.class.getName()).log(Level.SEVERE, null, ex);
                throw new UnderlayingFileSystemException("Error moving uploaded file on underlaying filesystem");
            }
        } else {
            throw new NullUploadException("Upload item can't be null!");
        }

        //Because of Date format in not loaded entities
        model = find(model.getId());

        model.setSize(Files.size(this, model));

        //Return managed instance
        return model;
    }

    public void uploadFile(Long id, User current, FileItem upload) throws DataStorageException {
        File model = find(id);

        if(!canReadWrite(model, current))
            throw new PermissionDeniedException("User can't write to this file");

        if(upload != null) {
            java.io.File f = new java.io.File(Path.real(this, model));
            try {
                upload.write(f);
                model.setModified(new Date());
                merge(model);
            } catch (Exception ex) {
                Logger.getLogger(FileDAO.class.getName()).log(Level.SEVERE, null, ex);
                throw new UnderlayingFileSystemException("Error moving uploaded file on underlaying filesystem");
            }
        } else {
            throw new NullUploadException("Upload item can't be null!");
        }
    }

    public byte[] getContent(String publicId) throws DataStorageException, IOException {
        File f = find(publicId);
        if(f != null) {
            return Files.read(this, f);
        }
        return new byte[0];
    }

    public void setContent(File f, User current, String content) throws DataStorageException, IOException {
        if(canReadWrite(f, current)) {
            Files.write(this, f, content);
            f.setModified(new Date());
            merge(f);
        } else {
            throw new PermissionDeniedException("User can't write file");
        }
    }

    @Override
    protected File moveOverride(File model, Directory newParent, User current) throws DataStorageException {
        if(!isNameUsed(model.getName(), newParent))
            throw new NameMustBeUniqueException();

        try {
            java.io.File f1 = new java.io.File(Path.real(this, model));
            model.setParent(newParent);
            java.io.File f2 = new java.io.File(Path.real(this, model));

            FileUtils.moveFile(f1, f2);

            model = merge(model);
            model.setSize(Files.size(f2));
        } catch (Exception ex) {
            Logger.getLogger(FileDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new UnderlayingFileSystemException("Error moving file on underlaying filesystem");
        }

        return model;
    }

    @Override
    protected File copyOverride(File model, Directory newParent, User current) throws DataStorageException {
        if (!isNameUsed(model.getName(), newParent)) {
            throw new NameMustBeUniqueException();
        }

        try {
            java.io.File f1 = new java.io.File(Path.real(this, model));

            File newFile = new File();
            newFile.setName(model.getName());
            newFile.setType(model.getType());
            newFile.setOwner(current);
            newFile.setCreated(new Date());
            newFile.setModified(new Date());
            newFile.setOwner(current);
            newFile.setIsPublic(false);
            newFile.setPublicId(PublicHelper.random());
            newFile.setParent(newParent);

            java.io.File f2 = new java.io.File(Path.real(this, newFile));
            f2.createNewFile();

            FileUtils.copyFile(f1, f2);

            newFile = save(newFile);
            newFile.setSize(Files.size(f2));
            createPermissions(newFile, current, PermissionsType.DEFAULT);
            
            return newFile;
        } catch (Exception ex) {
            Logger.getLogger(FileDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new UnderlayingFileSystemException("Error copying file on underlaying filesystem");
        }
    }
}

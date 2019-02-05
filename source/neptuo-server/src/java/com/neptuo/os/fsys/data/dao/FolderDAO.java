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
import com.neptuo.os.fsys.DriveFolderCantBeDeletedException;
import com.neptuo.os.fsys.InvalidNameException;
import com.neptuo.os.fsys.NameMustBeUniqueException;
import com.neptuo.os.fsys.PermissionDeniedException;
import com.neptuo.os.fsys.PermissionsType;
import com.neptuo.os.fsys.UnderlayingFileSystemException;
import com.neptuo.os.fsys.data.model.Drive;
import com.neptuo.os.fsys.util.Files;
import com.neptuo.os.fsys.util.Path;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Mara
 */
public class FolderDAO extends FsysItemDAO<Directory> {
    public static final String SYSTEM_NAME = "System";

    @Override
    public Directory find(Long id) throws DataStorageException {
        return getDs().find(Directory.class, id);
    }

    @Override
    public Directory find(String publicId) throws DataStorageException {
        Directory result = (Directory) getResultOrNull(getDs().createQuery(getDs().factory().select().from(Directory.class, "d").where("publicId", QueryComparator.EQ)).setParameter("publicId", publicId));
        if(result != null) {
            result.setPath(Path.compose(this, result));
        }
        return result;
    }

    public Directory find(String name, Directory parent) throws DataStorageException {
        return (Directory) getResultOrNull(getDs()
                .createQuery(getDs().factory().select(true).from(Directory.class, "d").where("name", QueryComparator.EQ).and().where("parent", QueryComparator.EQ))
                .setParameter("name", name)
                .setParameter("parent", parent));
    }

    public List<Directory> findFolders(Directory d, User current) throws DataStorageException {
        return findFolders(d, current, true);
    }
    public List<Directory> findFolders(Directory d, User current, boolean addCurrentAndParent) throws DataStorageException {
        List<Directory> result = new ArrayList<Directory>();
        String path = Path.compose(this, d, current);

        if (addCurrentAndParent) {
            d = find(d.getId());
            Directory parent = d.getParent();
            if (!canRead(parent, current)) {
                parent = null;
            }

            Directory cd = new Directory();
            cd.setId(d.getId());
            cd.setName(".");
            cd.setPath(path);
            result.add(cd);

            Directory cp = new Directory();
            if (parent != null) {
                cp.setId(parent.getId());
                cp.setName(parent.getName());
                cp.setName("..");
                result.add(cp);
            }
        }

        QueryBuilder qb = getDs().factory().select("d", true).from(Directory.class, "d")
                .join("d", "permissions", "p")
                .where("d", "parent", QueryComparator.EQ)
                .and()
                .where("p", "identity", QueryComparator.IN)
                .and()
                .where("p", "type.level", QueryComparator.GE, createDAO(AccessTypeDAO.class).find(AT_READ).getLevel())
                .order("d", "id", QueryOrder.ASC);
        List<Directory> folders = (List<Directory>) getDs()
                .createQuery(qb)
                .setParameter("parent", d)
                .setParameter("identity", createDAO(UserDAO.class).findManageable(current))
                .getResultList();

        for(Directory f : folders) {
            f.setPath(Path.combine(path, f.getName()));
            result.add(f);
        }
        
        return result;
    }

    @Override
    protected void setNonPersistent(Directory data, User current) throws DataStorageException {
            data.setPath(Path.compose(this, data, current));
    }

    @Override
    protected QueryBuilder getQueryForNameCheck() throws DataStorageException {
        return getDs().factory()
                .select()
                .from(Directory.class, "d")
                .where("parent", QueryComparator.EQ)
                .and()
                .where("name", QueryComparator.EQ);
    }

    @Override
    protected QueryBuilder getQueryForNameCheckExceptId() throws DataStorageException {
        return getDs().factory()
                .select()
                .from(Directory.class, "d")
                .where("parent", QueryComparator.EQ)
                .and()
                .where("id", QueryComparator.NE)
                .and()
                .where("name", QueryComparator.EQ);
    }

    public boolean isDrive(Directory d) throws DataStorageException {
        QueryBuilder qb = getDs().factory()
                .select()
                .from(Drive.class, "d")
                .where("directory.id", QueryComparator.EQ, d.getId());

        return getResultOrNull(getDs().createQuery(qb)) != null;
    }

    public void delete(Long id, User current) throws DataStorageException {
        Directory model = find(id);

        // If user can manage parent folder, than user can delete all items
        if(!canManage(model, current))
            throw new PermissionDeniedException("User can't delete this folder");

        if(isDrive(model))
            throw new DriveFolderCantBeDeletedException();

        //Check that user can delete all items under this item
        Query qd = getDs().createQuery(getDs().factory().select().from(Directory.class, "d").where("parent", QueryComparator.EQ));
        Query qf = getDs().createQuery(getDs().factory().select().from(File.class, "d").where("parent", QueryComparator.EQ));

        //Delete recursively all childs
        deleteChilds(model);

        //Delete directory on real fs
        if (!new java.io.File(Path.real(this, model)).delete()) {
            throw new UnderlayingFileSystemException("Error deleting directory from underlaying filesystem");
        }

        getDs().remove(model);
    }

    protected void deleteChilds(Directory model) throws DataStorageException {
        List<Directory> dirs = (List<Directory>) getDs()
                .createQuery(getDs().factory().select().from(Directory.class, "d").where("parent", QueryComparator.EQ))
                .setParameter("parent", model)
                .getResultList();

        for (Directory d : dirs) {
            deleteChilds(d);
            getDs().remove(d);

            if (!new java.io.File(Path.real(this, d)).delete()) {
                throw new UnderlayingFileSystemException("Error deleting directory from underlaying filesystem");
            }
        }

        List<File> files = getDs()
                .createQuery(getDs().factory().select().from(File.class, "d").where("parent", QueryComparator.EQ))
                .setParameter("parent", model)
                .getResultList();

        for (File f : files) {
            getDs().remove(f);

            if (!new java.io.File(Path.real(this, f)).delete()) {
                throw new UnderlayingFileSystemException("Error deleting directory from underlaying filesystem");
            }
        }
    }

    public Directory create(Directory model, User current, boolean createReal) throws DataStorageException {
        return create(model, current, createReal, PermissionsType.DEFAULT);
    }

    public Directory create(Directory model, User current, boolean createReal, PermissionsType perms) throws DataStorageException {
        return create(model, current, current, createReal, perms);
    }

    public Directory create(Directory model, User current, User asUser, boolean createReal, PermissionsType perms) throws DataStorageException {
        if(model.getParent() == null)
            throw new DataStorageException("Parent can't be null!");

        if(!isNameUsed(model.getName(), model.getParent()))
            throw new NameMustBeUniqueException();

        if(!checkNameValidity(model.getName()))
            throw new InvalidNameException();
        
        if(!canReadWrite(model.getParent(), asUser))
            throw new PermissionDeniedException("User can't write to parent folder!");

        model.setCreated(new Date());
        model.setModified(new Date());
        model.setIsPublic(false);
        model.setPublicId(PublicHelper.random());

        //Create real directory
        if(createReal) {
            if(!new java.io.File(Path.real(this, model)).mkdir()) {
                throw new UnderlayingFileSystemException("Error creating directory on underlaying filesystem");
            }
        }

        //Save
        getDs().persist(model);

        model.setPath(Path.compose(this, model, current));

        //If permissions are empty, use parent permissions, if parent is system, use default
        if(model.getPermissions().isEmpty()) {
            createPermissions(model, current, perms);
        }

        //Because of Date format in not loaded entities
        model = find(model.getId());

        //Return managed instance
        return model;
    }

    public Directory rename(Long id, String newName, User current) throws DataStorageException {
        Directory model = find(id);

        if(!isNameUsed(newName, id, model.getParent()))
            throw new NameMustBeUniqueException();

        if(!checkNameValidity(model.getName()))
            throw new InvalidNameException();

        if(!canManage(model, current))
            throw new PermissionDeniedException("User can't write to this folder");

        //Rename real directory
        String oldPath = Path.real(this, model);
        model.setName(newName);
        String newPath = Path.real(this, model);

        new java.io.File(oldPath).renameTo(new java.io.File(newPath));

        model.setModified(new Date());
        return merge(model);
    }

    public byte[] zip(String publicId) throws DataStorageException, IOException {
        Directory d = find(publicId);
        return Files.read(Files.zipFolder(this, d).getAbsolutePath());
    }

    @Override
    protected Directory moveOverride(Directory model, Directory newParent, User current) throws DataStorageException {
        if(!isNameUsed(model.getName(), newParent))
            throw new NameMustBeUniqueException();

        try {
            java.io.File f1 = new java.io.File(Path.real(this, model));
            model.setParent(newParent);
            java.io.File f2 = new java.io.File(Path.real(this, model));

            FileUtils.moveDirectory(f1, f2);

            model = merge(model);
        } catch (Exception ex) {
            Logger.getLogger(FileDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new UnderlayingFileSystemException("Error moving folder on underlaying filesystem");
        }

        return model;
    }

    @Override
    protected Directory copyOverride(Directory model, Directory newParent, User current) throws DataStorageException {
        if (!isNameUsed(model.getName(), newParent)) {
            throw new NameMustBeUniqueException();
        }

        try {
            java.io.File f1 = new java.io.File(Path.real(this, model));

            Directory newDirectory = new Directory();
            newDirectory.setName(model.getName());
            newDirectory.setCreated(new Date());
            newDirectory.setModified(new Date());
            newDirectory.setIsPublic(false);
            newDirectory.setPublicId(PublicHelper.random());
            newDirectory.setParent(newParent);

            java.io.File f2 = new java.io.File(Path.real(this, newDirectory));
            f2.mkdirs();

            newDirectory = save(newDirectory);
            createPermissions(newDirectory, current, PermissionsType.DEFAULT);

            for(Directory folder : findFolders(model, current, false)) {
                copyOverride(folder, newDirectory, current);
            }

            for(File file : createDAO(FileDAO.class).findFiles(model, current, null, null)) {
                createDAO(FileDAO.class).copyOverride(file, newDirectory, current);
            }

            return newDirectory;
        } catch (Exception ex) {
            Logger.getLogger(FileDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new UnderlayingFileSystemException("Error copying folder on underlaying filesystem");
        }
    }
}

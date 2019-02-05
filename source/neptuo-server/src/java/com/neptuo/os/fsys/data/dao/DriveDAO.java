/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.fsys.data.dao;

import com.neptuo.data.DataStorageException;
import com.neptuo.data.QueryBuilder;
import com.neptuo.data.QueryComparator;
import com.neptuo.data.QueryOrder;
import com.neptuo.data.dao.AbstractDAO;
import com.neptuo.os.core.data.dao.UserDAO;
import com.neptuo.os.fsys.data.model.Directory;
import com.neptuo.os.core.data.model.User;
import com.neptuo.os.fsys.NameMustBeUniqueException;
import com.neptuo.os.fsys.data.model.Drive;
import com.neptuo.os.fsys.PermissionDeniedException;
import com.neptuo.os.fsys.PermissionsType;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mara
 */
public class DriveDAO extends AbstractDAO<Drive> {

    @Override
    public Drive find(Long id) throws DataStorageException {
        return getDs().find(Drive.class, id);
    }

    public Drive find(String name) throws DataStorageException {
        return (Drive) getResultOrNull(getDs().createQuery(getDs().factory().select().from(Drive.class, "d").where("name", QueryComparator.EQ)).setParameter("name", name));
    }

    public Drive find(String name, User current) throws DataStorageException {
        QueryBuilder qb = getDs().factory()
                .select()
                .from(Drive.class, "d")
                .where("name", QueryComparator.EQ)
                .and()
                .where("owner", QueryComparator.EQ);
        return (Drive) getResultOrNull(getDs().createQuery(qb).setParameter("name", name).setParameter("owner", current));
    }

    /**
     * Finds user drives + system drives
     * @param current user
     * @return user drives + system drives
     * @throws DataStorageException
     */
    public List<Drive> findUserDrives(User current) throws DataStorageException {
        QueryBuilder qb = getDs().factory()
                .select()
                .from(Drive.class, "d")
                .where("owner", QueryComparator.EQ)
                .order("name", QueryOrder.ASC);

        List<Drive> roots = getDs()
                .createQuery(qb)
                .setParameter("owner", current)
                .getResultList();

        List<Drive> result = new ArrayList<Drive>();
        for(Drive d : roots) {
            if(createDAO(FolderDAO.class).canRead(d.getDirectory(), current))
                result.add(d);
        }

        result.addAll(findSystemDrives(current));
        return result;
    }

    // Finds "System" drive
    public Drive findSystemDrive() throws DataStorageException {
        QueryBuilder qb = getDs().factory()
                .select()
                .from(Drive.class, "d")
                .where("name", QueryComparator.EQ)
                .and()
                .where("owner", QueryComparator.ISNULL);

        return (Drive) getResultOrNull(getDs().createQuery(qb).setParameter("name", "System"));
    }

    // Finds "Users" drive, drive for users home dirs
    public Drive findUsersDrive() throws DataStorageException {
        QueryBuilder qb = getDs().factory()
                .select()
                .from(Drive.class, "d")
                .where("name", QueryComparator.EQ)
                .and()
                .where("owner", QueryComparator.ISNULL);

        return (Drive) getResultOrNull(getDs().createQuery(qb).setParameter("name", "Users"));
    }

    // Finds user home drive
    public Drive findUserHome(User current) throws DataStorageException {
        QueryBuilder qb = getDs().factory()
                .select()
                .from(Drive.class, "d")
                .where("name", QueryComparator.EQ)
                .and()
                .where("owner", QueryComparator.EQ);
        
        return (Drive) getResultOrNull(getDs().createQuery(qb).setParameter("name", "Home").setParameter("owner", current));
    }

    /**
     * Finds system drives only
     * @return system drives
     * @throws DataStorageException
     */
    public List<Drive> findSystemDrives(User current) throws DataStorageException {
        List<Drive> drives = getDs()
                .createQuery(getDs().factory().select().from(Drive.class, "d").where("owner", QueryComparator.ISNULL).order("name", QueryOrder.ASC))
                .getResultList();

        List<Drive> result = new ArrayList<Drive>();
        for(Drive d : drives) {
            if(createDAO(FolderDAO.class).canRead(d.getDirectory(), current))
                result.add(d);
        }
        return result;
    }

    public Drive findByFolder(Directory d) throws DataStorageException {
        d = createDAO(FolderDAO.class).merge(d);

        QueryBuilder qb = getDs().factory()
                .select()
                .from(Drive.class, "d")
                .where("directory", QueryComparator.EQ);

        return (Drive) getResultOrNull(getDs().createQuery(qb).setParameter("directory", d));
    }

    public boolean isNameUsed(String name, User current) throws DataStorageException {
        QueryBuilder qb = getDs().factory()
                .select()
                .from(Drive.class, "d")
                .where("name", QueryComparator.EQ)
                .and()
                .where("owner", QueryComparator.EQ);

        List<Drive> list = getDs()
                .createQuery(qb)
                .setParameter("name", name)
                .setParameter("owner", current)
                .getResultList();

        return !list.isEmpty();
    }

    public boolean isNameUsed(String name, Long exceptId, User current) throws DataStorageException {
        QueryBuilder qb = getDs().factory()
                .select()
                .from(Drive.class, "d")
                .where("name", QueryComparator.EQ)
                .and()
                .where("id", QueryComparator.NE)
                .and()
                .where("owner", QueryComparator.EQ);

        List<Drive> list = getDs()
                .createQuery(qb)
                .setParameter("name", name)
                .setParameter("id", exceptId)
                .setParameter("owner", current)
                .getResultList();

        return list.size() > 1;
    }

    @Override
    public Drive save(Drive model) throws DataStorageException {
        if (model.getOwner() != null) {
            model = getDs().merge(model);
            getDs().persist(model);
            model = getDs().merge(model);
            return model;
        } else {
            throw new PermissionDeniedException("Can't save system drive!");
        }
    }

    @Override
    public Drive merge(Drive model) throws DataStorageException {
        return getDs().merge(model);
    }

    @Override
    public void delete(Drive model) throws DataStorageException {
        if (model.getOwner() != null) {
            getDs().remove(model);
        } else {
            throw new PermissionDeniedException("Can't delete system drive!");
        }
    }

    public void deleteAll() throws DataStorageException {
        for(Drive d : (List<Drive>) getDs().createQuery(getDs().factory().select().from(Drive.class, "d")).getResultList()) {
            if(!d.getIsSystemValue()) {
                delete(d);
            }
        }
    }

    public Drive createUserHome(User current) throws DataStorageException {
        FolderDAO folders = createDAO(FolderDAO.class);

        Directory homeFolder = new Directory();
        homeFolder.setName(current.getUsername());
        homeFolder.setParent(findUsersDrive().getDirectory());
        homeFolder = folders.create(homeFolder, current, createDAO(UserDAO.class).findAdmin(), true, PermissionsType.FOR_USERHOME);

        Drive homeDrive = new Drive();
        homeDrive.setOwner(current);
        homeDrive.setDirectory(homeFolder);
        homeDrive.setName("Home");
        homeDrive = save(homeDrive);
        
        return homeDrive;
    }

    public Drive createUserDrive(String name, String label, Long directoryId, User current) throws DataStorageException {
        if(isNameUsed(name, current)) {
            throw new NameMustBeUniqueException();
        }

        Directory dir = createDAO(FolderDAO.class).find(directoryId);

        if (createDAO(FolderDAO.class).canRead(dir, current)) {
            Drive model = new Drive();
            model.setName(name);
            model.setLabel(label);
            model.setDirectory(dir);
            model.setOwner(current);
            model = save(model);

            return model;
        } else {
            throw new PermissionDeniedException("User can't read folder");
        }
    }

    public Drive updateUserDrive(Long id, String name, String label, Long directoryId, User current) throws DataStorageException {
        if(isNameUsed(name, id, current)) {
            throw new NameMustBeUniqueException();
        }

        Directory dir = createDAO(FolderDAO.class).find(directoryId);

        if (createDAO(FolderDAO.class).canRead(dir, current)) {
            Drive model = find(id);
            model.setName(name);
            model.setLabel(label);
            model.setDirectory(dir);
            model.setOwner(current);
            model = save(model);

            return model;
        } else {
            throw new PermissionDeniedException("User can't read folder");
        }
    }

    public void deleteUserDrive(Long id, User current) throws DataStorageException {
        Drive model = find(id);
        if(model.getOwner().equals(current)) {
            delete(model);
        }
    }
}

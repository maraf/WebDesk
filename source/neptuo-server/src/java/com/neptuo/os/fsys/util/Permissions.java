/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.os.fsys.util;

import com.neptuo.data.DataStorage;
import com.neptuo.data.DataStorageException;
import com.neptuo.os.core.data.dao.AccessTypeDAO;
import com.neptuo.os.core.data.dao.UserRoleDAO;
import com.neptuo.os.core.data.model.AccessType;
import com.neptuo.os.core.data.model.IdentityBase;
import com.neptuo.os.fsys.data.model.FileSystemItem;
import com.neptuo.os.fsys.data.model.FileSystemPermission;
import com.neptuo.os.core.data.model.UserRole;
import com.neptuo.data.dao.BaseDAO;
import com.neptuo.os.fsys.data.dao.FolderDAO;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Mara
 */
public class Permissions {

    public static Set<FileSystemPermission> clone(DataStorage ds, Set<FileSystemPermission> perms, FileSystemItem target) throws DataStorageException {
        Set<FileSystemPermission> news = new HashSet<FileSystemPermission>(perms.size());
        for (FileSystemPermission p : perms) {
            FileSystemPermission n = new FileSystemPermission(p.getType(), p.getIdentity(), target);
            ds.persist(n);
            news.add(n);
        }
        return news;
    }

    public static Set<FileSystemPermission> createDefault(DataStorage ds, BaseDAO dao, FileSystemItem target) throws DataStorageException {
        UserRoleDAO roleDAO = dao.createDAO(UserRoleDAO.class);
        AccessTypeDAO accessDAO = dao.createDAO(AccessTypeDAO.class);
        FolderDAO folderDAO = dao.createDAO(FolderDAO.class);

        UserRole admins = roleDAO.findAdmins();
        AccessType manage = accessDAO.find(FolderDAO.AT_MANAGE);

        Set<FileSystemPermission> news = new HashSet<FileSystemPermission>();
        FileSystemPermission p = new FileSystemPermission(manage, admins, target);
        ds.persist(p);
        news.add(p);

        return news;
    }

    public static Set<FileSystemPermission> createForIdentity(DataStorage ds, BaseDAO dao, FileSystemItem target, IdentityBase identity) throws DataStorageException {
        AccessTypeDAO accessDAO = dao.createDAO(AccessTypeDAO.class);
        FolderDAO folderDAO = dao.createDAO(FolderDAO.class);

        AccessType manage = accessDAO.find(FolderDAO.AT_MANAGE);

        Set<FileSystemPermission> news = new HashSet<FileSystemPermission>();
        FileSystemPermission p = new FileSystemPermission(manage, identity, target);
        ds.persist(p);
        news.add(p);

        return news;
    }

    public static Set<FileSystemPermission> createForIdentityHome(DataStorage ds, BaseDAO dao, FileSystemItem target, IdentityBase identity) throws DataStorageException {
        AccessTypeDAO accessDAO = dao.createDAO(AccessTypeDAO.class);
        FolderDAO folderDAO = dao.createDAO(FolderDAO.class);
        UserRoleDAO roleDAO = dao.createDAO(UserRoleDAO.class);

        UserRole admins = roleDAO.findAdmins();
        AccessType manage = accessDAO.find(FolderDAO.AT_MANAGE);

        Set<FileSystemPermission> news = new HashSet<FileSystemPermission>();
        FileSystemPermission p = new FileSystemPermission(manage, identity, target);
        ds.persist(p);
        news.add(p);

        return news;
    }
}

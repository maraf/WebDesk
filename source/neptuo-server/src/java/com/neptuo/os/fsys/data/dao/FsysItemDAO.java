/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.fsys.data.dao;

import com.google.common.collect.Lists;
import com.neptuo.data.DataStorageException;
import com.neptuo.data.QueryBuilder;
import com.neptuo.data.QueryComparator;
import com.neptuo.data.dao.BaseDAO;
import com.neptuo.os.core.data.dao.AccessTypeDAO;
import com.neptuo.os.core.data.dao.UserDAO;
import com.neptuo.os.core.data.dao.UserRoleDAO;
import com.neptuo.os.core.data.model.AccessType;
import com.neptuo.os.core.data.model.IdentityBase;
import com.neptuo.os.core.data.model.User;
import com.neptuo.os.fsys.InvalidNameException;
import com.neptuo.os.fsys.NameMustBeUniqueException;
import com.neptuo.os.fsys.PermissionDeniedException;
import com.neptuo.os.fsys.PermissionsType;
import com.neptuo.os.fsys.SourceAndDestinationFolderAreTheSameException;
import com.neptuo.os.fsys.data.model.Directory;
import com.neptuo.os.fsys.data.model.FileSystemItem;
import com.neptuo.os.fsys.data.model.FileSystemPermission;
import com.neptuo.os.fsys.util.Path;
import com.neptuo.os.fsys.util.Permissions;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.persistence.NoResultException;

/**
 *
 * @author Mara
 */
public abstract class FsysItemDAO<E extends FileSystemItem> extends BaseDAO {
    public static final String AT_READ = "Read";
    public static final String AT_READWRITE = "ReadWrite";
    public static final String AT_MANAGE = "Manage";

    public static final String[] INVALID_NAMES = new String[] {".", "..", ""};
    public static final String[] INVALID_CHARS = new String[] {"/", "\\", ":", "*", "?", "\"", "|", "<", ">"};

    protected abstract E find(Long id) throws DataStorageException;

    protected abstract E find(String publicId) throws DataStorageException;

    protected boolean containsPermission(FileSystemPermission perm) throws DataStorageException {
        try {
            FileSystemPermission fsp = (FileSystemPermission) getDs()
                .createQuery(getDs().factory().select().from(FileSystemPermission.class, "p").where("identity", QueryComparator.EQ).and().where("target", QueryComparator.EQ))
                .setParameter("identity", perm.getIdentity())
                .setParameter("target", perm.getTarget())
                .getSingleResult();

            return fsp != null;
        } catch (NoResultException e) {
            return false;
        }
    }

    protected boolean canUserDo(E d, User u, AccessType a) throws DataStorageException {
        if(d == null || u == null || a == null)
            return false;

        QueryBuilder qb = getDs().factory()
                .select()
                .from(FileSystemPermission.class, "p")
                .where("target", QueryComparator.EQ)
                .and()
                .where("type.level", QueryComparator.GE, a.getLevel())
                .and()
                .where("identity", QueryComparator.IN);

        List<FileSystemPermission> perms = getDs().createQuery(qb)
                .setParameter("target", d)
                .setParameter("identity", createDAO(UserDAO.class).findManageable(u))
                .getResultList();

        return perms.size() > 0 || d.getPermissions().isEmpty();
    }

    public boolean canRead(E d, User u) throws DataStorageException {
        AccessType read = createDAO(AccessTypeDAO.class).find(AT_READ);
        return canUserDo(d, u, read);
    }

    public boolean canReadWrite(E d, User u) throws DataStorageException {
        AccessType write = createDAO(AccessTypeDAO.class).find(AT_READWRITE);
        return canUserDo(d, u, write);
    }

    public boolean canManage(E d, User u) throws DataStorageException {
        AccessType manage = createDAO(AccessTypeDAO.class).find(AT_MANAGE);
        return canUserDo(d, u, manage);
    }

    public boolean checkNameValidity(String name) {
        if(Lists.newArrayList(INVALID_NAMES).contains(name))
            return false;

        for(String s : INVALID_CHARS) {
            if(name.contains(s)) {
                return false;
            }
        }

        return true;
    }

    protected abstract void setNonPersistent(E data, User current) throws DataStorageException;

    protected abstract QueryBuilder getQueryForNameCheck() throws DataStorageException;

    protected abstract QueryBuilder getQueryForNameCheckExceptId() throws DataStorageException;

    public boolean isNameUsed(String name, Directory parent) throws DataStorageException {
        return getResultOrNull(getDs().createQuery(getQueryForNameCheck()).setParameter("parent", parent).setParameter("name", name)) == null;
    }

    public boolean isNameUsed(String name, Long exceptFileId, Directory parent) throws DataStorageException {
        return getResultOrNull(getDs().createQuery(getQueryForNameCheckExceptId()).setParameter("parent", parent).setParameter("name", name).setParameter("id", exceptFileId)) == null;
    }

    protected E save(E model) throws DataStorageException {
        model = getDs().merge(model);
        getDs().persist(model);
        return model;
    }

    protected FileSystemPermission save(FileSystemPermission model) throws DataStorageException {
        model = getDs().merge(model);
        getDs().persist(model);
        return model;
    }

    protected E merge(E model) throws DataStorageException {
        return getDs().merge(model);
    }

    protected FileSystemPermission merge(FileSystemPermission perm) throws DataStorageException {
        return getDs().merge(perm);
    }

    public E setIsPublic(Long id, User current, boolean isPublic) throws DataStorageException {
        E model = find(id);

        if(!canManage(model, current))
            throw new PermissionDeniedException("User can't write here");

        if(model.getIsPublic() != isPublic) {
            model.setIsPublic(isPublic);
            model.setModified(new Date());
            setNonPersistent(model, current);
            model = merge(model);
        }
        return model;
    }

    public E rename(Long id, String newName, User current) throws DataStorageException {
        E model = find(id);

        if(!isNameUsed(newName, id, model.getParent()))
            throw new NameMustBeUniqueException();

        if(!checkNameValidity(model.getName()))
            throw new InvalidNameException();

        if(!canManage(model, current))
            throw new PermissionDeniedException("User can't write here");

        //Rename real directory
        String oldPath = Path.real(this, model);
        model.setName(newName);
        String newPath = Path.real(this, model);

        new java.io.File(oldPath).renameTo(new java.io.File(newPath));

        model.setModified(new Date());
        setNonPersistent(model, current);
        return merge(model);
    }

    public List<FileSystemPermission> getPermissions(Long id, User current) throws DataStorageException {
        E model = find(id);
        if(canManage(model, current)) {
            List<FileSystemPermission> perms = new ArrayList<FileSystemPermission>();
            List<IdentityBase> manageable = createDAO(UserDAO.class).findManageable(current);
            manageable.addAll(createDAO(UserDAO.class).findAll(current));
            for(FileSystemPermission perm : model.getPermissions()) {
                if(manageable.contains(perm.getIdentity())) {
                    perms.add(perm);
                }
            }
            return perms;
        }else {
            throw new PermissionDeniedException("User can't manage folder");
        }
    }

    public FileSystemPermission addPermission(Long id, FileSystemPermission perm, User current) throws DataStorageException {
        E model = find(id);
        if (canManage(model, current)) {
            if (createDAO(UserRoleDAO.class).canUserAssignIdentity(perm.getIdentity(), current) && !containsPermission(perm)) {
                perm = save(perm);
                model.getPermissions().add(perm);
                merge(model);
                return perm;
            } else {
                return null;
            }
        } else {
            throw new PermissionDeniedException("User can't manage folder");
        }
    }

    public void removePermission(Long id, FileSystemPermission perm, User current) throws DataStorageException {
        E model = find(id);
        if (canManage(model, current)) {
            if (createDAO(UserRoleDAO.class).canUserAssignIdentity(perm.getIdentity(), current) && containsPermission(perm)) {
                perm = merge(perm);
                model.getPermissions().remove(perm);
                merge(model);
                getDs().remove(perm);
            }
        } else {
            throw new PermissionDeniedException("User can't manage folder");
        }
    }

    public FileSystemPermission createPermission(Long identityId, Long accessTypeId, Long itemId) throws DataStorageException {
        E d = find(itemId);
        IdentityBase i = getDs().find(IdentityBase.class, identityId);
        AccessType t = createDAO(AccessTypeDAO.class).find(accessTypeId);
        return new FileSystemPermission(t, i, d);
    }

    public FileSystemPermission getPermission(Long identityId, Long typeId, Long targetId) throws DataStorageException {
        QueryBuilder qb = getDs().factory()
                .select()
                .from(FileSystemPermission.class, "perm")
                .join("identity", "i")
                .join("type", "a")
                .join("target", "t")
                .where("i", "id", QueryComparator.EQ, identityId)
                .and()
                .where("a", "id", QueryComparator.EQ, typeId)
                .and()
                .where("t", "id", QueryComparator.EQ, targetId);

        return (FileSystemPermission) getResultOrNull(getDs().createQuery(qb));
    }

    protected FileSystemItem createPermissions(FileSystemItem model, User current, PermissionsType perms) throws DataStorageException {
        DriveDAO drives = createDAO(DriveDAO.class);
        Set<FileSystemPermission> permissions;

        if (perms.equals(PermissionsType.FOR_USER)) {
            permissions = Permissions.createForIdentity(getDs(), this, model, current);
        } else if (perms.equals(PermissionsType.FOR_USERHOME)) {
            permissions = Permissions.createForIdentityHome(getDs(), this, model, current);
        } else {
            if (drives.findUsersDrive().getDirectory().equals(model.getParent()) || drives.findSystemDrive().getDirectory().equals(model.getParent())) {
                permissions = Permissions.createDefault(getDs(), this, model);
                if(!canManage((E) model.getParent(), current)) {
                    permissions.addAll(Permissions.createForIdentity(dataStorage, this, model, current));
                }
            } else if (drives.findUserHome(current).getDirectory().equals(model.getParent())) {
                permissions = Permissions.createForIdentity(getDs(), this, model, current);
            } else {
                permissions = Permissions.clone(getDs(), model.getParent().getPermissions(), model);
                if(!canManage((E) model.getParent(), current)) {
                    permissions.addAll(Permissions.createForIdentity(dataStorage, this, model, current));
                }
            }
        }

        model.getPermissions().addAll(permissions);
        return model;
    }

    public E move(Long id, Long newParentId, User current) throws DataStorageException {
        E model = find(id);
        Directory newParent = createDAO(FolderDAO.class).find(newParentId);

        if(!canManage(model, current) || !createDAO(FolderDAO.class).canReadWrite(newParent, current))
            throw new PermissionDeniedException();

        return moveOverride(model, newParent, current);
    }

    protected abstract E moveOverride(E model, Directory newParent, User current) throws DataStorageException;

    public E copy(Long id, Long newParentId, User current) throws DataStorageException {
        E model = find(id);
        Directory newParent = createDAO(FolderDAO.class).find(newParentId);

        if(!canManage(model, current) || !createDAO(FolderDAO.class).canReadWrite(newParent, current))
            throw new PermissionDeniedException();

        if(model.getParent().equals(newParent) || model.equals(newParent))
            throw new SourceAndDestinationFolderAreTheSameException();

        return copyOverride(model, newParent, current);
    }

    protected abstract E copyOverride(E model, Directory newParent, User current) throws DataStorageException;
}

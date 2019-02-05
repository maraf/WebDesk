/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.os.fsys.util;

import com.neptuo.data.DataStorageException;
import com.neptuo.data.dao.BaseDAO;
import com.neptuo.os.core.data.model.User;
import com.neptuo.os.fsys.data.dao.DriveDAO;
import com.neptuo.os.fsys.data.dao.FolderDAO;
import com.neptuo.os.fsys.data.model.Directory;
import com.neptuo.os.fsys.data.model.Drive;
import com.neptuo.os.fsys.data.model.File;
import com.neptuo.os.fsys.data.model.FileSystemItem;

/**
 *
 * @author Mara
 */
public class Path {

    public static final String DRIVE_SEPARATOR = "://";
    public static final String DIRECTORY_SEPARATOR = "/";

    public static String combine(String path1, String path2) {
        if (!path1.endsWith(DIRECTORY_SEPARATOR)) {
            path1 += DIRECTORY_SEPARATOR;
        }
        path1 += path2;
        if (!path1.endsWith(DIRECTORY_SEPARATOR)) {
            path1 += DIRECTORY_SEPARATOR;
        }
        return path1;
    }

    public static String compose(BaseDAO bdao, Drive d) throws DataStorageException {
        return d.getName() + DRIVE_SEPARATOR;
    }

    public static String compose(BaseDAO bdao, Directory d) throws DataStorageException {
        return compose(bdao, d, null);
    }

    public static String compose(BaseDAO bdao, Directory d, User current) throws DataStorageException {
        Drive dr = bdao.createDAO(DriveDAO.class).findByFolder(d);
        if (dr != null && current != null && current.equals(dr.getOwner())) {
            return compose(bdao, dr);
        }
        if (d.getParent() != null) {
            return compose(bdao, d.getParent(), current) + d.getName() + DIRECTORY_SEPARATOR;
        } else {
            dr = bdao.createDAO(DriveDAO.class).findByFolder(d);
            return compose(bdao, dr);
        }
    }

    public static String compose(BaseDAO bdao, File f, User current) throws DataStorageException {
        return compose(bdao, f.getParent(), current) + DIRECTORY_SEPARATOR + f.getName();
    }

    public static ResolveResult resolve(BaseDAO bdao, String url, User current) throws DataStorageException {
        int driveIndex = url.indexOf(DRIVE_SEPARATOR);
        if (driveIndex == -1) {
            return null;
        } else {
            String driveName = url.substring(0, driveIndex);
            Drive drive = bdao.createDAO(DriveDAO.class).find(driveName);
            if(drive == null)
                return null;

            Directory parent = drive.getDirectory();

            if (!driveName.equals(url)) {
                String[] folders = url.substring(driveIndex + DRIVE_SEPARATOR.length()).split(DIRECTORY_SEPARATOR);
                for (String item : folders) {
                    if (item.length() != 0) {
                        parent = bdao.createDAO(FolderDAO.class).find(item, parent);
                    }
                }
            }
            //TODO: Resolve file url!!!
            return new ResolveResult(parent, true, false);
        }
    }

    public static String real(BaseDAO bdao, Drive d) throws DataStorageException {
        if (d.getPhysicalPath() != null) {
            return d.getPhysicalPath() + DIRECTORY_SEPARATOR;
        } else {
            return real(bdao, d.getDirectory());
        }
    }

    public static String real(BaseDAO bdao, FileSystemItem item) throws DataStorageException {
        if(item instanceof Directory)
            return real(bdao, (Directory) item);
        else
            return real(bdao, (File) item);
    }

    public static String real(BaseDAO bdao, Directory d) throws DataStorageException {
        if (d.getParent() != null) {
            return real(bdao, d.getParent()) + d.getName() + DIRECTORY_SEPARATOR;
        } else {
            Drive dr = bdao.createDAO(DriveDAO.class).findByFolder(d);
            return real(bdao, dr);
        }
    }

    public static String real(BaseDAO bdao, File f) throws DataStorageException {
        return real(bdao, f.getParent()) + DIRECTORY_SEPARATOR + f.getName();
    }
}

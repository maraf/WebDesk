/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.os.fsys.util;

import com.neptuo.data.DataStorage;
import com.neptuo.data.DataStorageException;
import com.neptuo.os.core.data.dao.AccessTypeDAO;
import com.neptuo.os.core.data.dao.KnownAccessTypes;
import com.neptuo.os.core.data.dao.UserDAO;
import com.neptuo.os.core.data.dao.UserRoleDAO;
import com.neptuo.os.core.data.model.AccessType;
import com.neptuo.os.fsys.data.model.Directory;
import com.neptuo.os.fsys.data.model.File;
import com.neptuo.os.fsys.data.model.FileSystemPermission;
import com.neptuo.os.core.data.model.User;
import com.neptuo.os.core.data.model.UserRole;
import com.neptuo.os.fsys.data.dao.DriveDAO;
import com.neptuo.os.fsys.data.dao.FileDAO;
import com.neptuo.os.fsys.data.dao.FolderDAO;
import com.neptuo.os.fsys.data.model.Drive;
import java.io.IOException;
import java.util.Date;
import javax.activation.MimetypesFileTypeMap;

/**
 *
 * @author Mara
 */
public class Importer {
//    public static final String SYSTEM_PATH = "D:/Temp/FileBrowser";
//    public static final String SEPARATOR = java.io.File.separator;

    protected DataStorage dataStorage;
    protected AccessTypeDAO accessDAO;
    protected UserRoleDAO roleDAO;
    protected UserDAO userDAO;
    protected FolderDAO folderDAO;
    protected FileDAO fileDAO;
    protected DriveDAO driveDAO;
    protected AccessType read;
    protected AccessType write;
    protected AccessType delete;
    protected AccessType manage;
    protected UserRole admins;
    protected UserRole everyone;
    protected User admin;

    public Importer(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    public DataStorage getDs() {
        return dataStorage;
    }

    public void setDataStorage(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    /**
     * Import existing fs structure under passed directory.
     *
     * @param path
     */
    public void doImport(String path, User owner, Drive parent) throws ImporterException, IOException {
        try {
            if(dataStorage == null) {
                throw new ImporterException("DataStorage can't be null!");
            }
            accessDAO = new AccessTypeDAO();
            accessDAO.setDataStorage(dataStorage);
            roleDAO = new UserRoleDAO();
            roleDAO.setDataStorage(dataStorage);
            userDAO = new UserDAO();
            userDAO.setDataStorage(dataStorage);
            folderDAO = new FolderDAO();
            folderDAO.setDataStorage(dataStorage);
            fileDAO = new FileDAO();
            fileDAO.setDataStorage(dataStorage);
            driveDAO = new DriveDAO();
            driveDAO.setDataStorage(dataStorage);


            admins = roleDAO.findAdmins();
            everyone = roleDAO.findEveryone();
            admin = userDAO.findAdmin();

            //Import
            java.io.File root = new java.io.File(path + Path.DIRECTORY_SEPARATOR);
            if (!root.isDirectory()) {
                throw new ImporterException("Selected path is not existing directory!");
            } else {
                Directory parentFolder = parent.getDirectory();

                doPartImport(root, parentFolder, owner);
            }
        } catch (DataStorageException ex) {
            throw new ImporterException(ex);
        }
    }

    public void doPartImport(java.io.File parent, Directory parentd, User owner) throws DataStorageException, ImporterException, IOException {
        for (java.io.File f : parent.listFiles()) {
            if (f.isFile()) {
                createFile(parentd, f.getName(), new MimetypesFileTypeMap().getContentType(f), owner);
            } else if (f.isDirectory()) {
                Directory d = createDirectory(parentd, f.getName());
                doPartImport(f, d, owner);
            }
        }
    }

    private Directory createDirectory(Directory parent, String name) throws DataStorageException {
        Directory d = new Directory();
        d.setName(name);
        d.setCreated(new Date());
        d.setModified(new Date());
        d.setParent(parent);

        return folderDAO.create(parent, admin, true);
    }

    private File createFile(Directory parent, String name, String type, User owner) throws DataStorageException, IOException {
        File f = new File();
        f.setName(name);
        f.setParent(parent);
        f.setType(type);

        return fileDAO.createEmpty(f, owner);
    }
}

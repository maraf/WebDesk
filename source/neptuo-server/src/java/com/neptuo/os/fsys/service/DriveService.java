/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.fsys.service;

import com.neptuo.os.core.data.dao.UserDAO;
import com.neptuo.os.core.data.model.User;
import com.neptuo.data.DataStorageException;
import com.neptuo.service.annotation.ServiceClass;
import com.neptuo.service.annotation.ServiceMethod;
import com.neptuo.service.io.Serializer;
import com.neptuo.os.fsys.data.dao.DriveDAO;
import com.neptuo.os.fsys.data.model.Drive;
import com.neptuo.service.HttpException;
import com.neptuo.service.annotation.RequestInput;
import com.neptuo.service.result.CollectionResult;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mara
 */
@ServiceClass(url="/fsys/drives")
public class DriveService {

    @ServiceMethod
    public CollectionResult<Drive> getUserDrives(Serializer s, User current, DriveDAO dao) throws DataStorageException, HttpException {
        return new CollectionResult<Drive>("drives", dao.findUserDrives(current));
    }

    @ServiceMethod(name="create")
    public CollectionResult<Drive> createUserDrive(Serializer s, User current, DriveDAO dao, @RequestInput("drives") List<Drive> drives) throws DataStorageException, HttpException {
        List<Drive> newDrives = new ArrayList<Drive>();
        for(Drive d : drives) {
            newDrives.add(dao.createUserDrive(d.getName(), d.getLabel(), d.getDirectory().getId(), current));
        }
        return new CollectionResult<Drive>("drives", newDrives);
    }

    @ServiceMethod(name="update")
    public CollectionResult<Drive> updateUserDrive(Serializer s, User current, DriveDAO dao, @RequestInput("drives") List<Drive> drives) throws DataStorageException, HttpException {
        List<Drive> newDrives = new ArrayList<Drive>();
        for(Drive d : drives) {
            newDrives.add(dao.updateUserDrive(d.getId(), d.getName(), d.getLabel(), d.getDirectory().getId(), current));
        }
        return new CollectionResult<Drive>("drives", newDrives);
    }

    @ServiceMethod(name="delete")
    public void deleteUserDrive(Serializer s, User current, DriveDAO dao, @RequestInput("drives") List<Drive> drives) throws DataStorageException, HttpException {
        for(Drive d : drives) {
            dao.deleteUserDrive(d.getId(), current);
        }
    }

    @ServiceMethod(name="deleteCustom", require={"admins"})
    public void deleteCustomUserDrive(Serializer s, User current, DriveDAO dao, @RequestInput("drives") List<Drive> drives) throws DataStorageException, HttpException {
        for(Drive d : drives) {
            dao.deleteUserDrive(d.getId(), dao.createDAO(UserDAO.class).find(d.getOwner().getId()));
        }
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.os.fsys.service;

import com.neptuo.os.core.data.dao.UserDAO;
import com.neptuo.os.core.data.model.User;
import com.neptuo.service.annotation.ServiceClass;
import com.neptuo.service.annotation.ServiceMethod;
import com.neptuo.data.DataStorage;
import com.neptuo.data.DataStorageException;
import com.neptuo.service.HttpException;
import java.io.IOException;

/**
 *
 * @author Mara
 */
@ServiceClass(url = "/fsys/import")
public class ImportService {

    @ServiceMethod(require = {"admins"})
    public void doImport(DataStorage storage, User current, UserDAO dao) throws DataStorageException, HttpException, IOException {
//        try {
//            current = dao.merge(current);
//            Importer importer = new Importer(storage);
//            importer.doImport(Importer.SYSTEM_PATH, current);
//        } catch (ImporterException ex) {
//            throw new HttpInternalServerErrorException();
//        }
    }
}

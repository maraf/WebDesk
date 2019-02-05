/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.fsys.service;

import com.neptuo.os.core.service.CoreParamsProvider;
import com.neptuo.os.fsys.data.dao.DriveDAO;
import com.neptuo.os.fsys.data.dao.FileDAO;
import com.neptuo.os.fsys.data.dao.FolderDAO;
import com.neptuo.service.MethodInfo;
import com.neptuo.service.ParamsProviderResult;
import com.neptuo.service.ProviderMethodType;
import com.neptuo.service.annotation.ParamsProvider;
import com.neptuo.service.annotation.ProviderMethod;

/**
 *
 * @author Mara
 */
@ParamsProvider
public class FsysParamsProvider extends CoreParamsProvider {

    @Override
    @ProviderMethod(ProviderMethodType.PROVIDE)
    public ParamsProviderResult provide(Class clazz, MethodInfo method, int index) {
        this.method = method;

        Object result = null;
        boolean handled = false;

        if (clazz == DriveDAO.class) {
            result = getDriveDAO();
            handled = true;
        } else if (clazz == FolderDAO.class) {
            result = getFolderDAO();
            handled = true;
        } else if (clazz == FileDAO.class) {
            result = getFileDAO();
            handled = true;
        }

        if (handled) {
            return new ParamsProviderResult(result);
        } else {
            return new ParamsProviderResult();
        }
    }

    public DriveDAO getDriveDAO() {
        DriveDAO dao = new DriveDAO();
        dao.setDataStorage(getDataStorage());
        return dao;
    }

    public FolderDAO getFolderDAO() {
        FolderDAO dao = new FolderDAO();
        dao.setDataStorage(getDataStorage());
        return dao;
    }

    public FileDAO getFileDAO() {
        FileDAO dao = new FileDAO();
        dao.setDataStorage(getDataStorage());
        return dao;
    }
}

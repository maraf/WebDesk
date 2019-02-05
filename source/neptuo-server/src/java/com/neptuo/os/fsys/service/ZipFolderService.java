/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.fsys.service;

import com.neptuo.data.DataStorageException;
import com.neptuo.os.fsys.data.dao.FolderDAO;
import com.neptuo.os.fsys.data.model.Directory;
import com.neptuo.os.fsys.util.Files;
import com.neptuo.service.HttpException;
import com.neptuo.service.HttpMethodType;
import com.neptuo.service.HttpNotAcceptableException;
import com.neptuo.service.annotation.HttpParam;
import com.neptuo.service.annotation.ServiceClass;
import com.neptuo.service.annotation.ServiceMethod;
import com.neptuo.service.result.HttpRawResult;
import com.neptuo.service.result.RawResult;
import com.neptuo.service.result.Result;
import java.io.IOException;
import java.util.zip.ZipException;

/**
 *
 * @author Mara
 */
@ServiceClass(url="/fsys/zip-folder")
public class ZipFolderService {

    @ServiceMethod(httpMethod={HttpMethodType.POST, HttpMethodType.GET})
    public Result zipFolder(FolderDAO dao, @HttpParam("publicId") String folderPublicId) throws DataStorageException, HttpException, IOException {
        if (folderPublicId == null)
            throw new HttpNotAcceptableException("Missing directory id");

        try {
            Directory d = dao.find(folderPublicId);
            byte[] content = dao.zip(folderPublicId);
            return new HttpRawResult(content, "application/x-zip", content.length, "Content-Transfer-Encoding:binary", "Content-Disposition:attachment; filename=\"" + d.getName() + ".zip\"");
        } catch (ZipException e) {
            String message = "Target folder doesn't contain any file.";
            return new HttpRawResult(Files.stringToByteArray(message), "text/plain", message.length());
        }

    }
}

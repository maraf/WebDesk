/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.fsys.service;

import com.neptuo.os.core.data.model.User;
import com.neptuo.data.DataStorageException;
import com.neptuo.os.fsys.data.dao.FileDAO;
import com.neptuo.os.fsys.data.dao.FolderDAO;
import com.neptuo.os.fsys.data.model.Directory;
import com.neptuo.os.fsys.data.model.File;
import com.neptuo.service.HttpException;
import com.neptuo.service.HttpMethodType;
import com.neptuo.service.HttpNotAcceptableException;
import com.neptuo.service.annotation.HttpHeader;
import com.neptuo.service.annotation.HttpParam;
import com.neptuo.service.annotation.RequestInput;
import com.neptuo.service.annotation.ServiceClass;
import com.neptuo.service.annotation.ServiceMethod;
import com.neptuo.service.result.HttpRawResult;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author Mara
 */
@ServiceClass(url="/fsys/file-content")
public class FileContentService {

    @ServiceMethod(httpMethod={HttpMethodType.POST, HttpMethodType.GET})
    public HttpRawResult getRawFileContent(@HttpParam("publicId") String filePublicId, FileDAO dao) throws DataStorageException, HttpException, IOException {
        if(filePublicId == null)
            throw new HttpNotAcceptableException();

        File f = dao.find(filePublicId);
        if(f == null)
            throw new HttpNotAcceptableException();
            
        byte[] content = dao.getContent(filePublicId);

        return new HttpRawResult(content, f.getType(), content.length, "Content-Transfer-Encoding:binary", "Content-Disposition:attachment; filename=\"" + f.getName() + "\"");
    }

    @ServiceMethod(name="setTextFileContent")
    public void setTextFileContent(User current, FileDAO dao, @RequestInput("files") List<FileContentItem> files) throws DataStorageException, HttpException, IOException {
        for (FileContentItem file : files) {
            File f = dao.find(file.getPublicId());
            if(f != null && f.getType().startsWith("text/")) {
                dao.setContent(f, current, file.getContent());
            }
        }
    }
}

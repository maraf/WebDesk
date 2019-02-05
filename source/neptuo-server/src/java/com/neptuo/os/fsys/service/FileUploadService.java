/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.fsys.service;

import com.neptuo.data.DataStorageException;
import com.neptuo.service.annotation.ServiceClass;
import com.neptuo.service.annotation.ServiceMethod;
import com.neptuo.data.UploadStorage;
import com.neptuo.os.core.util.PublicHelper;
import com.neptuo.os.fsys.util.Files;
import com.neptuo.service.HttpException;
import com.neptuo.service.HttpNotAcceptableException;
import com.neptuo.service.result.HttpRawResult;
import com.neptuo.service.io.Serializer;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 *
 * @author Mara
 */
@ServiceClass(url="/fsys/upload")
public class FileUploadService {
    private static final Logger log = Logger.getLogger(FileUploadService.class.getName());

    @ServiceMethod
    public HttpRawResult handleUpload(HttpServletRequest request, UploadStorage us, Serializer s) throws DataStorageException, HttpException, IOException {
        FileItem uploadItem = getFileItem(request);

        if (uploadItem == null) {
            throw new HttpNotAcceptableException("FileItem must be provided");
        }

        String uid = PublicHelper.random();
        us.add(uid, uploadItem);

//        s.writeKeyValue("key", uid);
        return new HttpRawResult(Files.stringToByteArray(uid), "text/plain", uid.length());
    }

    private FileItem getFileItem(HttpServletRequest request) {
        FileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);

        try {
            List items = upload.parseRequest(request);
            Iterator it = items.iterator();
            while (it.hasNext()) {
                FileItem item = (FileItem) it.next();
                if (!item.isFormField() && "uploadFormElement".equals(item.getFieldName())) {
                    return item;
                }
            }
        } catch (FileUploadException e) {
            log.log(Level.SEVERE, "Exception parsing upload request", e);
            return null;
        }

        return null;
    }
}

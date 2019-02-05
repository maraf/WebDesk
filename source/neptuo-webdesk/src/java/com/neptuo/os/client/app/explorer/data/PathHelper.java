/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.client.app.explorer.data;

import com.extjs.gxt.ui.client.widget.form.FileUploadField;

/**
 *
 * @author Mara
 */
public class PathHelper {
    public static final String DRIVE_SEPARATOR = "://";
    public static final String DIRECTORY_SEPARATOR = "/";

    public static String combine(String path1, String path2) {
        return combine(path1, path2, false);
    }

    public static String combine(String path1, String path2, boolean file) {
        return path1 + (path1.endsWith(DIRECTORY_SEPARATOR) ? "" : "/") + path2 + (file || path2.endsWith(DIRECTORY_SEPARATOR) ? "" : "/");
    }

    public static String parentPath(String path) {
        return (path.endsWith(DIRECTORY_SEPARATOR) ? path.substring(0, path.substring(0, path.length() - 1).lastIndexOf(DIRECTORY_SEPARATOR) + 1) : path.substring(0, path.lastIndexOf(DIRECTORY_SEPARATOR) + 1));
    }

    public static String drivePath(String path) {
        if(path.endsWith(DRIVE_SEPARATOR))
            return path;

        int index = path.indexOf(DRIVE_SEPARATOR);
        if(index != -1) {
            return path.substring(0, index + DRIVE_SEPARATOR.length());
        } else {
            return null;
        }
    }

    public static boolean isFile(String path) {
        return !path.equals(DIRECTORY_SEPARATOR) && !path.endsWith(DIRECTORY_SEPARATOR) && path.length() > 5 && path.contains(".");
    }

    public static boolean isFolder(String path) {
        return !isFile(path);
    }

    public static String getFolderPath(String path) {
        if(isFile(path)) {
            return parentPath(path);
        } else {
            return path;
        }
    }

    public static String getFileName(String path) {
        if(path.endsWith(DIRECTORY_SEPARATOR))
            return null;

        return path.substring(path.lastIndexOf(DIRECTORY_SEPARATOR) + 1, path.length());
    }

    public static String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."), fileName.length());
    }


    public static String getFileNameFromUploadField(FileUploadField fufFile) {
        String value = fufFile.getValue();
        if(value.indexOf("\\") != -1) {
            value = value.substring(value.lastIndexOf("\\") + 1, value.length());
        }
        return value;
    }
}

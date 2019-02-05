/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.os.fsys.util;

import com.neptuo.data.DataStorageException;
import com.neptuo.data.dao.BaseDAO;
import com.neptuo.os.core.util.PublicHelper;
import com.neptuo.os.fsys.data.model.Directory;
import com.neptuo.os.fsys.data.model.File;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 *
 * @author Mara
 */
public class Files {
    public static long size(String path) {
        return size(new java.io.File(path));
    }

    public static long size(java.io.File file) {
        return file.length();
    }

    public static long size(BaseDAO bdao, File file) throws DataStorageException {
        return size(Path.real(bdao, file));
    }

    public static byte[] read(String filePath) throws java.io.IOException {
        java.io.File file = new java.io.File(filePath);
        InputStream is = new FileInputStream(file);

        // Get the size of the file
        long length = file.length();

        if (length > Integer.MAX_VALUE) {
            // File is too large
        }

        // Create the byte array to hold the data
        byte[] bytes = new byte[(int) length];

        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }

        // Close the input stream and return bytes
        is.close();
        return bytes;
    }

    public static byte[] read(BaseDAO bdao, File file) throws DataStorageException, IOException {
        return read(Path.real(bdao, file));
    }

    public static void write(String filePath, byte[] content) throws java.io.IOException {
        OutputStream out = new FileOutputStream(filePath);
        out.write(content);
        out.close();
    }

    public static void write(BaseDAO bdao, File file, byte[] content) throws DataStorageException, java.io.IOException {
        write(Path.real(bdao, file), content);
    }

    public static void write(BaseDAO bdao, File file, String content) throws DataStorageException, java.io.IOException {
        write(Path.real(bdao, file), stringToByteArray(content));
    }

    public static java.io.File zipFolder(BaseDAO bdao, Directory folder) throws DataStorageException, java.io.IOException {
        return zipFolder(Path.real(bdao, folder));
    }

    public static java.io.File zipFolder(String folderPath) throws java.io.IOException {
        java.io.File inFolder = new java.io.File(folderPath);
        if (inFolder.isDirectory()) {
            String name = PublicHelper.random();
            java.io.File zipFile = java.io.File.createTempFile(name, ".zip");
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFile));
            addZipFolder(inFolder, out, "");
            out.close();
            out.flush();
            out.close();
            return zipFile;
        } else {
            throw new java.io.IOException("Not a directory");
        }
    }

    static void addZipFolder(java.io.File dirObj, ZipOutputStream out, String subdir) throws IOException {
        java.io.File[] files = dirObj.listFiles();
        byte[] tmpBuf = new byte[1024];

        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                String nextSubdir = (!"".equals(subdir)) ? subdir + java.io.File.separator + files[i].getName() : files[i].getName();
                addZipFolder(files[i], out, nextSubdir);
                continue;
            }
            FileInputStream in = new FileInputStream(files[i].getAbsolutePath());
            System.out.println(" Adding: " + files[i].getAbsolutePath());
            String path = "";
            if(!"".equals(subdir)) {
                path = subdir + java.io.File.separator;
            }
            out.putNextEntry(new ZipEntry(path + files[i].getName()));
            int len;
            while ((len = in.read(tmpBuf)) > 0) {
                out.write(tmpBuf, 0, len);
            }
            out.closeEntry();
            in.close();
        }
    }

    public static byte[] stringToByteArray(String content) {
        if (content == null || content.length() == 0) {
            return new byte[0];
        }
        byte[] array = new byte[content.length()];
        int i = 0;
        for (char c : content.toCharArray()) {
            array[i] = (byte) c;
            i++;
        }
        return array;
    }

    public static String extensionToMimeType(String path) {
        //TODO: Very primitive, do better
        String extension = path.substring(path.lastIndexOf(".") + 1);
        if("html".equals(extension) || "htm".equals(extension)) {
            return "text/html";
        }
        return "text/plain";
    }
}

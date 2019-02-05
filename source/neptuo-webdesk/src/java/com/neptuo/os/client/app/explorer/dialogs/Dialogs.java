/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.os.client.app.explorer.dialogs;

import com.neptuo.os.client.app.explorer.event.CreateEmptyFileCallback;
import com.neptuo.os.client.app.explorer.event.FileUploadCallback;
import com.neptuo.os.client.app.explorer.event.CreateFolderCallback;
import com.neptuo.os.client.data.model.FsysItem;
import com.neptuo.os.client.app.explorer.event.ExplorerItemSelected;
import com.neptuo.os.client.http.AsyncCallback;

/**
 *
 * @author Mara
 */
public class Dialogs {

    public static void createFolder(final CreateFolderCallback callback) {
        final CreateFolderWindow win = new CreateFolderWindow(callback);
        win.show();
    }

    public static void createEmptyFile(final CreateEmptyFileCallback callback) {
        final CreateEmptyFileWindow win = new CreateEmptyFileWindow(callback);
        win.show();
    }

    public static void uploadFile(FileUploadCallback callback) {
        final FileUploadWindow win = new FileUploadWindow(callback);
        win.show();
    }

    public static void selectFile(String fileName, ExplorerItemSelected callback) {
        final FileSelectWindow window = new FileSelectWindow(fileName, callback);
        window.show();
    }

    public static void selectFolder(String folderName, ExplorerItemSelected callback) {
        final FolderSelectWindow window = new FolderSelectWindow(folderName, callback);
        window.show();
    }

    public static void multiUploadFile(String folderPath, int folderId, AsyncCallback<FsysItem> callback) {
        final MultiUploadWindow window = new MultiUploadWindow(folderPath, folderId);
        if(callback != null) {
            window.addSavedListeners(callback);
        }
        window.show();
    }
}

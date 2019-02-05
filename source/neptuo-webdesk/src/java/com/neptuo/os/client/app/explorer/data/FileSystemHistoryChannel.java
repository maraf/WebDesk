/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.client.app.explorer.data;

import com.neptuo.os.client.data.HistoryChannel;

/**
 *
 * @author Mara
 */
public class FileSystemHistoryChannel extends HistoryChannel<FileSystemHistoryItem> {

    public void add(int folderId, String folderPath) {
        add(new FileSystemHistoryItem(folderId, folderPath));
    }

}

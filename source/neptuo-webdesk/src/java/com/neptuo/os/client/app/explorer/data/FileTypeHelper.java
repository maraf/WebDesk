/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.os.client.app.explorer.data;

import com.extjs.gxt.ui.client.widget.MessageBox;
import com.neptuo.os.client.app.explorer.Explorer;
import com.neptuo.os.client.data.model.FsysItem;
import com.neptuo.os.client.app.explorer.dialogs.DownloadFileWindow;
import com.neptuo.os.client.app.explorer.ui.panel.ExplorerPanel;
import com.neptuo.os.client.app.texteditor.TextEditor;
import com.neptuo.os.client.data.model.Drive;

/**
 *
 * @author Mara
 */
public class FileTypeHelper {

    public static String getIconPath(FsysItem item) {
        return getIconName(item);
    }

    public static String getIconPath(Drive item) {
        return getIconName(item);
    }

    public static String getGoIconPath(FsysItem item) {
        return "" + getIconName(item) + "_go";
    }

    private static String getIconName(Drive item) {
        return item.getIsSystem() ? "drive" : "drive_user";
    }

    private static String getIconName(FsysItem item) {
        String type = item.getType();
        if (type.equals("folder")) {
            return "folder";
        } else if (type.equals("text/html")) {
            return "html";
        } else if (type.equals("text/plain")) {
            return "note";
        } else if (type.startsWith("image")) {
            return "image";
        } else {
            return "page_white";
        }
    }

    public static void runProgram(ExplorerPanel panel, FsysItem item, boolean newWindow) {
        if (item.getType().equals("folder")) {
            if (newWindow) {
                Explorer explorer = new Explorer();
                explorer.run(new String[]{item.getPath()});
            } else {
                panel.setLocation(item);
            }
        } else {
            String type = item.getType();
            if (type.equals("text/html")) {
                //Run richwriter
                TextEditor editor = new TextEditor();
                editor.run(new Object[]{item, "html"});
            } else if (type.startsWith("text/")) {
                //Run textwriter
                TextEditor editor = new TextEditor();
                editor.run(new Object[]{item});
//            } else if (type.startsWith("image/")) {
                //-T-O-D-O-: Run image viewer
            } else {
                //Not a known program, open dialog to download a file
                DownloadFileWindow window = new DownloadFileWindow(item);
                window.show();
            }
        }
    }
}

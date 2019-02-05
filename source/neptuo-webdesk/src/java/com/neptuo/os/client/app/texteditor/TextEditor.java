/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.client.app.texteditor;

import com.neptuo.os.client.app.Application;
import com.neptuo.os.client.data.model.FsysItem;

/**
 *
 * @author Mara
 */
public class TextEditor implements Application {
    private MainWindow mainWindow;

    @Override
    public void run(Object[] args) {
        TextEditorMode mode = TextEditorMode.TEXT;
        if(args.length > 0 && args[0] instanceof FsysItem) {
            if(args.length > 1 && "html".equals(args[1])) {
                mode = TextEditorMode.HTML;
            }
            FsysItem item = (FsysItem) args[0];
            mainWindow = new MainWindow(item.getPublicId(), item.getPath(), mode);
        } else {
            mainWindow = new MainWindow();
        }
    }

    @Override
    public void terminate() {
        mainWindow.hide();
    }

}

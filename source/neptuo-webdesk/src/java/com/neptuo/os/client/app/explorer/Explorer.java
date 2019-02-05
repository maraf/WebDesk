/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.client.app.explorer;

import com.neptuo.os.client.app.explorer.ui.MainWindow;
import com.neptuo.os.client.app.Application;

/**
 *
 * @author Mara
 */
public class Explorer implements Application {

    private MainWindow mainWindow;

    @Override
    public void run(Object[] args) {
        if(args.length == 0) {
            mainWindow = new MainWindow();
        } else {
            mainWindow = new MainWindow((String) args[0]);
        }
        mainWindow.show();
    }

    @Override
    public void terminate() {
        mainWindow.hide();
    }

}

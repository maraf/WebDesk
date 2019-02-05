/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.client.app.usermanager;

import com.neptuo.os.client.app.Application;

/**
 *
 * @author Mara
 */
public class UserManager implements Application {

    private MainWindow mainWindow;

    @Override
    public void run(Object[] args) {
        mainWindow = new MainWindow();
        mainWindow.show();
    }

    @Override
    public void terminate() {
        mainWindow.hide();
    }

}

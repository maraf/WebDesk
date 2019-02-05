/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.client.ui;

/**
 *
 * @author Mara
 */
public abstract class DialogWindow extends Window {

    public DialogWindow() {
        setModal(true);
        setMinimizable(false);
        setMaximizable(false);
        setRefreshable(false);
    }

    @Override
    protected boolean getLoadWindowProperties() {
        return false;
    }

    @Override
    protected boolean registerTaskBarItem() {
        return false;
    }

}

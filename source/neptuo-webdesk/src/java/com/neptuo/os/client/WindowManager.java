/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.client;

import com.neptuo.os.client.ui.Window;

/**
 *
 * @author Mara
 */
public class WindowManager extends com.extjs.gxt.ui.client.widget.WindowManager {
    private Window active;

    public void setActive(Window w) {
        active = w;
    }

    public void deactivateActive(Window w) {
        if(active != null && active != w) {
            active.getTaskbarButton().deactivate();
            active = null;
        }
    }
}

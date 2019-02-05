/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.os.client.ui.event;

import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.google.gwt.event.dom.client.KeyCodes;

/**
 *
 * @author Mara
 */
public abstract class EnterKeyListener extends KeyListener {

    @Override
    public void componentKeyPress(ComponentEvent event) {
        if (event.getKeyCode() == KeyCodes.KEY_ENTER) {
            componentEnterPress(event);
        }
    }

    public abstract void componentEnterPress(ComponentEvent e);
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.client.app.explorer.ui.panel;

/**
 *
 * @author Mara
 */
public class ExplorerPanelFactory {

    public static ExplorerPanel create() {
        return new ExplorerPanelImpl();
    }
}

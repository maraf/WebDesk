/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.client.app.explorer.ui;

import com.neptuo.os.client.data.model.FsysItem;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.neptuo.os.client.Constants;
import com.neptuo.os.client.ui.DialogWindow;

/**
 *
 * @author Mara
 */
public class PermissionsWindow extends DialogWindow {

    private PermissionsLayoutContainer permissions;

    public PermissionsWindow(FsysItem item) {
        setLayout(new FitLayout());
        setWidth(330);
        setHeight(400);
        setIcon(Constants.icons16().folder_key());
        setHeading(Constants.explorer().tbiPermissions());

        permissions = new PermissionsLayoutContainer(item);
        add(permissions);
    }

    @Override
    public String getClassName() {
        return getClass().getName();
    }

}

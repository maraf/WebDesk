/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.os.client.app.explorer.ui;

import com.neptuo.os.client.data.model.FsysItem;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.neptuo.os.client.Constants;

/**
 *
 * @author Mara
 */
public class PermissionsTabItem extends TabItem {

    private PermissionsLayoutContainer permissions;

    public PermissionsTabItem(FsysItem item) {
        setLayout(new FitLayout());
        setText(Constants.explorer().tbiPermissions());

        permissions = new PermissionsLayoutContainer(item);
        add(permissions);
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.client.app.explorer.ui;

import com.neptuo.os.client.data.model.FsysItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.neptuo.os.client.Constants;
import com.neptuo.os.client.ui.DialogWindow;
import com.neptuo.os.client.ui.Window;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mara
 */
public class PropertiesWindow extends DialogWindow {
    private TabPanel tpnTabs;

    private List<Button> currentButtons = new ArrayList<Button>();

    public PropertiesWindow(FsysItem item) {
        setIcon(Constants.icons16().folder());
        setLayout(new FitLayout());
        setHeading(item.getName());
        setWidth(330);
        setHeight(400);
        tpnTabs = new TabPanel();

        if(item.getType().equals("folder")) {
            FolderDetailTabItem tab = new FolderDetailTabItem(item);
            tpnTabs.add(tab);
        } else {
            FileDetailTabItem tab = new FileDetailTabItem(item);
            tpnTabs.add(tab);
        }

        PermissionsTabItem perms = new PermissionsTabItem(item);
        tpnTabs.add(perms);

        add(tpnTabs);
    }

    @Override
    public String getClassName() {
        return getClass().getName();
    }

    @Override
    protected boolean registerTaskBarItem() {
        return false;
    }
}

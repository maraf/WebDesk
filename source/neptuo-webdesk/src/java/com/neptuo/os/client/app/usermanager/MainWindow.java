/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.client.app.usermanager;

import com.extjs.gxt.ui.client.event.IconButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.neptuo.os.client.Constants;
import com.neptuo.os.client.ui.Window;

/**
 *
 * @author Mara
 */
public class MainWindow extends Window {
    private TabPanel tbpTabs;
    private UsersTabItem tbiUsers;
    private UserRolesTabItem tbiRoles;

    public MainWindow() {
        setWidth(623);
        setHeight(600);
        setHeading(Constants.users().heading());
        setIcon(Constants.icons16().user());
        setLayout(new FitLayout());

        tbpTabs = new TabPanel();

        tbiUsers = new UsersTabItem(Constants.users().tbiUsers(), this);
        tbiRoles = new UserRolesTabItem(Constants.users().tbiRoles(), this);

        tbpTabs.add(tbiUsers);
        tbpTabs.add(tbiRoles);

        refresh.addSelectionListener(new SelectionListener<IconButtonEvent>() {

            @Override
            public void componentSelected(IconButtonEvent ce) {
                if(tbiUsers.equals(tbpTabs.getSelectedItem())) {
                    tbiUsers.loadData();
                } else if(tbiRoles.equals(tbpTabs.getSelectedItem())) {
                    tbiRoles.loadData();
                }
            }
        });

        add(tbpTabs);
    }

    @Override
    public String getClassName() {
        return getClass().getName();
    }
}

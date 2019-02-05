/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.client.app.explorer.ui;

import com.neptuo.os.client.app.explorer.ui.panel.ExplorerPanelImpl;
import com.neptuo.os.client.app.explorer.event.ExplorerPanelListener;
import com.extjs.gxt.ui.client.event.IconButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.neptuo.os.client.Constants;
import com.neptuo.os.client.app.explorer.ui.panel.ExplorerPanel;
import com.neptuo.os.client.app.explorer.ui.panel.ExplorerPanelFactory;
import com.neptuo.os.client.ui.Window;

/**
 *
 * @author Mara
 */
public class MainWindow extends Window {
    private ExplorerPanel panel;

    public MainWindow() {
        setHeading(Constants.explorer().heading());
        setIcon(Constants.icons16().folder());
        setLayout(new FitLayout());
        panel = ExplorerPanelFactory.create();
        setWidth(900);
        setHeight(600);
        add(panel.getComponent());

        refresh.addSelectionListener(new SelectionListener<IconButtonEvent>() {

            @Override
            public void componentSelected(IconButtonEvent ce) {
                panel.refresh();
            }
        });
    }

    public MainWindow(String loadPath) {
        this();
        panel.setLocation(loadPath);
    }

    @Override
    public String getClassName() {
        return getClass().getName();
    }

}

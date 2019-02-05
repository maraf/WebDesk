/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.os.client.ui;

import com.neptuo.os.client.ui.taskbar.Taskbar;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.user.client.Element;
import com.neptuo.os.client.log.Logger;
import com.neptuo.os.client.WindowManager;

/**
 *
 * @author Mara
 */
public class Workspace extends Viewport {

    private WindowManager manager;
    private MainMenu mainMenu;
    private Taskbar taskbar;
    private Desktop desktop;
    private Logger logger;

    public Workspace() {
        setLayout(new RowLayout());
        addStyleName("x-workspace");

        manager = new WindowManager();

        mainMenu = new MainMenu();
        taskbar = new Taskbar();
        desktop = new Desktop();
        logger = new Logger();
    }

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);

        this.add(mainMenu, new RowData(1, 30));
        this.add(desktop, new RowData(1, 1));
        this.add(taskbar, new RowData(1, 30));
    }

    public WindowManager getWindowManager() {
        return manager;
    }

    public MainMenu getMainMenu() {
        return mainMenu;
    }

    public Desktop getDesktop() {
        return desktop;
    }

    public Taskbar getTaskbar() {
        return taskbar;
    }

    public Logger getLogger() {
        return logger;
    }
}

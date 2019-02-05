/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.client.ui.taskbar;

import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.ToggleButton;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.menu.SeparatorMenuItem;
import com.google.gwt.user.client.Element;
import com.neptuo.os.client.Constants;
import com.neptuo.os.client.NeptuoRoot;
import com.neptuo.os.client.ui.Window;

/**
 *
 * @author Mara
 */
public class TaskbarButton extends ToggleButton {
    public static String STYLE_NAME = "x-taskbar-item";
    public static String STYLE_NAME_ACTIVE = "x-taskbar-item-active";
    public static String STYLE_NAME_LOADING = "x-loading";

    private boolean active;
    private Window window;
    private Element loading;

    private Menu mnuContext;
    private MenuItem mniClose;
    private MenuItem mniMaximize;
    private MenuItem mniMinimize;
    private MenuItem mniRefresh;

    public TaskbarButton(String text, Window w) {
        super(text);
        addStyleName(STYLE_NAME);
        setWidth(130);
        setHeight(25);
//        template = new Template(getButtonTemplate());

        if(w.getIcon() == null) {
            setIcon(Constants.icons16().application());
        } else {
            setIcon(w.getIcon());
        }
        showLoading();

        mnuContext = new Menu();
        setContextMenu(menu);

        mniRefresh = new MenuItem(Constants.webdesk().refresh(), new SelectionListener<MenuEvent>() {

            @Override
            public void componentSelected(MenuEvent ce) {
                if(window.isRefreshable()) {
                    window.callRefresh();
                }
            }

        });
        mnuContext.add(mniRefresh);

        mnuContext.add(new SeparatorMenuItem());

        mniMinimize = new MenuItem(Constants.webdesk().minimize(), new SelectionListener<MenuEvent>() {

            @Override
            public void componentSelected(MenuEvent ce) {
                if(window.isMinimizable()) {
                    window.minimize();
                }
            }

        });
        mnuContext.add(mniMinimize);

        mniClose = new MenuItem(Constants.webdesk().close(), new SelectionListener<MenuEvent>() {

            @Override
            public void componentSelected(MenuEvent ce) {
                if(window.isMaximizable()) {
                    window.maximize();
                }
            }

        });
        mnuContext.add(mniClose);

        active = false;
        window = w;
        NeptuoRoot.getCurrentWorkspace().getWindowManager().register(window);
    }

    public void activate() {
        if(!active) {
            active = true;
            toggle(true);
        }
    }

    public void deactivate() {
        if(active) {
            active = false;
            toggle(false);
        }
    }

    @Override
    public void setText(String text) {
        if (text != null && text.length() > 18) {
            text = text.substring(0, 18) + "...";
        }
        super.setText(text);
    }

    @Override
    protected void onClick(ComponentEvent ce) {
        super.onClick(ce);

        if(window.isMinimized()) {
            window.show();
            NeptuoRoot.getCurrentWorkspace().getWindowManager().bringToFront(window);
            return;
        }

        if(active) {
            deactivate();
            window.minimize();
            //NeptuoRoot.getCurrentWorkspace().getWindowManager().activatePrevious(window);
        } else {
            activate();
            NeptuoRoot.getCurrentWorkspace().getWindowManager().bringToFront(window);
            //NeptuoRoot.getCurrentWorkspace().getWindowManager().bringToFront(window);
        }
    }

    public void remove() {
        NeptuoRoot.getCurrentWorkspace().getWindowManager().unregister(window);
    }

    public void showLoading() {
        addStyleName(STYLE_NAME_LOADING);
    }

    public void hideLoading() {
        removeStyleName(STYLE_NAME_LOADING);
    }

    public Window getWindow() {
        return window;
    }

//    private native String getButtonTemplate() /*-{
//        return [
//        '<div class="tbi-cover">',
//            '<div class="tbi-content"><em unselectable="on"><button class="x-btn-text" type="{1}" style="height:28px;">{0}</button></em></div>',
//            '<div class="tbi-loading"></div>',
//        '</div>'
//        ].join("");
//    }-*/;
}

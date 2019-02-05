/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.client.ui;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.neptuo.os.client.ui.taskbar.TaskbarButton;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.ResizeEvent;
import com.extjs.gxt.ui.client.event.WindowEvent;
import com.extjs.gxt.ui.client.event.WindowListener;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ToolButton;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.neptuo.os.client.Constants;
import com.neptuo.os.client.NeptuoRoot;
import com.neptuo.os.client.data.model.Property;
import com.neptuo.os.client.http.AsyncCallback;
import com.neptuo.os.client.http.service.PropertyService;
import com.neptuo.os.client.http.ExceptionType;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mara
 */
public abstract class Window extends com.extjs.gxt.ui.client.widget.Window {
    public static final String PKEY_WIDTH = "window.width";
    public static final String PKEY_HEIGHT = "window.height";

    private boolean showWaiting;
    private boolean minimized;
    private boolean initialized;
    private TaskbarButton taskbarButton;
    private List<Listener<WindowEvent>> hideListeners = new ArrayList<Listener<WindowEvent>>();

    protected ToolButton refresh;
    protected boolean loadWindowProperties = true;
    protected List<Property> properties;

    public Window() {
        super();
        manager = NeptuoRoot.getCurrentWorkspace().getWindowManager();
        setParent(NeptuoRoot.getCurrentWorkspace().getDesktop());

        setMaximizable(true);
        setMinimizable(true);
        setContainer(NeptuoRoot.getCurrentWorkspace().getDesktop().getElement());
        addWindowListener(new WindowListenerImpl());

        if (taskbarButton == null) {
            if(registerTaskBarItem()) {
                taskbarButton = NeptuoRoot.getCurrentWorkspace().getTaskbar().register(getHeading(), this);
            } else {
                taskbarButton = new TaskbarButton(getHeading(), this);
            }
        }

        setIcon(Constants.icons16().application());

        minimized = false;
        initialized = false;

        refresh = new ToolButton("x-tool-refresh");
        getHeader().addTool(refresh);

        properties = new ArrayList<Property>();
        properties.add(new Property(getClassName(), PKEY_WIDTH, ""));
        properties.add(new Property(getClassName(), PKEY_HEIGHT, ""));
        properties.addAll(addPropertiesToLoad());
        if(getLoadWindowProperties()) {
            PropertyService.get(properties, new AsyncCallback<Property>() {

                @Override
                public void onSuccess(List<Property> properties) {
                    for(Property p : properties) {
//                        NeptuoRoot.getCurrentWorkspace().getLogger().log(getClassName(), "p.key: " + p.getKey() + ", p.value: " + p.getValue());
                        if(PKEY_WIDTH.equals(p.getKey())) {
                            setWidth(p.getValue());
                        } else if(PKEY_HEIGHT.equals(p.getKey())) {
                            setHeight(p.getValue());
                        }
                    }
                    loadWindowProperties = false;
                    doLayout(true);
                    show();
                }

                @Override
                public void onClientError(Throwable e) { }

                @Override
                public void onServerError(ExceptionType exceptionType) { }
            });
        }
    }
//    Removed because of IE and modal windows!!
//    @Override
//    public void onRender(Element parent, int pos) {
//        super.onRender(NeptuoRoot.getCurrentWorkspace().getDesktop().getElement(), pos);
//    }

    public void callRefresh() {
        if(isRefreshable()) {
            refresh.fireEvent(Events.Select);
        }
    }

    @Override
    public void show() {
        if (!getLoadWindowProperties()) {
            super.show();
            initialized = true;
//        NeptuoRoot.getCurrentWorkspace().getWindowManager().bringToFront(this);
        } else {
            showWaiting = true;
        }
    }

    @Override
    public void setIcon(AbstractImagePrototype icon) {
        super.setIcon(icon);
        getTaskbarButton().setIcon(icon);
    }

    @Override
    public void hide() {
        super.hide();
    }

    @Override
    public void hide(Button buttonPressed) {
        super.hide(buttonPressed);
        onWindowHide();
    }

    @Override
    protected void onEndResize(ResizeEvent re) {
        super.onEndResize(re);

        for (Property p : properties) {
            if (PKEY_WIDTH.equals(p.getKey())) {
                p.setValue(String.valueOf(getWidth()));
            } else if (PKEY_HEIGHT.equals(p.getKey())) {
                p.setValue(String.valueOf(getHeight()));
            }
        }
        PropertyService.set(properties);
    }

    public boolean isMinimized() {
        return minimized;
    }

    public TaskbarButton getTaskbarButton() {
        return taskbarButton;
    }

    private void onWindowHide() {
        if(isMinimized()) {
            return;
        }
        if(initialized && registerTaskBarItem()) {
            NeptuoRoot.getCurrentWorkspace().getTaskbar().removeItem(taskbarButton);
        }
    }

    private <E extends BaseEvent> void callListeners(List<Listener<E>> listeners, E e) {
        for(Listener<E> list : listeners) {
            list.handleEvent(e);
        }
    }

    private Window getThis() {
        return this;
    }

    protected boolean getLoadWindowProperties() {
        return loadWindowProperties;
    }

    protected List<Property> addPropertiesToLoad() {
        return new ArrayList<Property>();
    }

    protected void onPropertiesLoaded(List<Property> properties) { }

    protected boolean registerTaskBarItem() {
        return true;
    }

    public void setRefreshable(boolean value) {
        if(value) {
            getHeader().addTool(refresh);
        } else {
            getHeader().removeTool(refresh);
        }
    }

    public boolean isRefreshable() {
        return getHeader().getTools().contains(refresh);
    }

    @Override
    public void setHeading(String text) {
        super.setHeading(text);
        taskbarButton.setText(text);
    }

    public void addHideListener(Listener<WindowEvent> list) {
        hideListeners.add(list);
    }

    public void removeHideListener(Listener<WindowEvent> list) {
        hideListeners.remove(list);
    }

    public abstract String getClassName();

    class WindowListenerImpl extends WindowListener {

        @Override
        public void windowActivate(WindowEvent we) {
            if(getTaskbarButton() != null) {
                getTaskbarButton().activate();
            }
//            NeptuoRoot.getCurrentWorkspace().getWindowManager().deactivateActive(getThis());
//            NeptuoRoot.getCurrentWorkspace().getWindowManager().setActive(getThis());
        }

        @Override
        public void windowDeactivate(WindowEvent we) {
            getTaskbarButton().deactivate();
        }

        @Override
        public void windowHide(WindowEvent we) {
            callListeners(hideListeners, we);
        }

        @Override
        public void windowMinimize(WindowEvent we) {
            minimized = true;
            getTaskbarButton().deactivate();
            hide();
        }

        @Override
        public void windowShow(WindowEvent we) {
            minimized = false;
            if(getTaskbarButton() != null) {
                getTaskbarButton().activate();
            }
            //show();
        }
    }
}

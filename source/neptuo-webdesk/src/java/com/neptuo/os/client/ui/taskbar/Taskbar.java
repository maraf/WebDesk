/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.os.client.ui.taskbar;

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.core.Template;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.WindowEvent;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ToggleButton;
import com.extjs.gxt.ui.client.widget.layout.MarginData;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.google.gwt.user.client.Timer;
import com.neptuo.os.client.Constants;
import com.neptuo.os.client.NeptuoRoot;
import com.neptuo.os.client.app.system.FeedbackWindow;
import com.neptuo.os.client.app.system.LoggerWindow;
import com.neptuo.os.client.log.LoggerEvent;
import com.neptuo.os.client.ui.Window;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mara
 */
public class Taskbar extends LayoutContainer {

    public static int ICON_SIZE = 24;
    private int loggerMessagesCount = 0;
    protected List<TaskbarButton> items;
    private LayoutContainer leftContent;
    private LayoutContainer rightContent;
    protected Button btnDesktop;
    protected ToggleButton btnLogger;
    protected ToggleButton btnFeedback;
    protected ClockPanel clpClock;
    protected LoggerWindow lgwLogger;
    protected FeedbackWindow fbwFeedback;

    public Taskbar() {
        addStyleName("x-taskbar");
        setLayout(new RowLayout(Orientation.HORIZONTAL));

        leftContent = new LayoutContainer();
        add(leftContent, new RowData(1, 1, new Margins(0, 10, 0, 6)));

        rightContent = new LayoutContainer();
        add(rightContent, new RowData(-1, 1));

        items = new ArrayList<TaskbarButton>();
        btnDesktop = new Button("", Constants.icons16().application_home(), new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                for (TaskbarButton tbi : items) {
                    tbi.getWindow().minimize();
                }
            }
        });
        btnDesktop.setHeight(26);
        leftContent.add(btnDesktop);

        btnLogger = new ToggleButton("", new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                if (lgwLogger == null) {
                    lgwLogger = new LoggerWindow();
                    lgwLogger.addHideListener(new Listener<WindowEvent>() {

                        @Override
                        public void handleEvent(WindowEvent be) {
                            btnLogger.toggle(false);
                            loggerMessagesCount = 0;
                            setLoggerMessagesLabel();
                        }
                    });

                    NeptuoRoot.getCurrentWorkspace().getLogger().addMessageListener(new Listener<LoggerEvent>() {

                        @Override
                        public void handleEvent(LoggerEvent be) {
                            loggerMessagesCount++;
                            setLoggerMessagesLabel();
                        }
                    });
                }
                lgwLogger.refresh();
                lgwLogger.show();
            }
        });
        btnLogger.setHeight(25);
        btnLogger.addStyleName("x-taskbar-item");
        btnLogger.setToolTip(Constants.webdesk().logger());
        btnLogger.setIcon(Constants.icons16().application_view_list());
        rightContent.add(btnLogger);

        btnFeedback = new ToggleButton("", new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                if (fbwFeedback == null) {
                    fbwFeedback = new FeedbackWindow();
                    fbwFeedback.addHideListener(new Listener<WindowEvent>() {

                        @Override
                        public void handleEvent(WindowEvent be) {
                            btnFeedback.toggle(false);
                        }
                    });
                }
                fbwFeedback.show();
            }
        });
        btnFeedback.setHeight(25);
        btnFeedback.addStyleName("x-taskbar-item");
        btnFeedback.setToolTip(Constants.webdesk().feedback());
        btnFeedback.setIcon(Constants.icons16().anchor());
        rightContent.add(btnFeedback);

        clpClock = new ClockPanel();
        rightContent.add(clpClock, new MarginData(10));
    }

    private void setLoggerMessagesLabel() {
        btnLogger.setToolTip(Constants.webdesk().logger() + " (" + loggerMessagesCount + ")");
    }

    public void addItem(TaskbarButton tbi) {
        items.add(tbi);
        leftContent.add(new SeparatorToolItem());
        leftContent.add(tbi);
        layout(true);
    }

    public TaskbarButton register(String text, Window w) {
        TaskbarButton tbi = new TaskbarButton(text, w);
        addItem(tbi);
        return tbi;
    }

    public void removeItem(TaskbarButton tbi) {
        items.remove(tbi);
        tbi.remove();
        leftContent.remove(tbi);
        layout(true);
    }
}

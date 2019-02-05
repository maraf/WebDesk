/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.os.client.ui;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ToggleButton;
import com.extjs.gxt.ui.client.widget.layout.MarginData;
import com.google.gwt.user.client.Timer;
import com.neptuo.os.client.Constants;
import com.neptuo.os.client.NeptuoRoot;
import com.neptuo.os.client.app.drivemanager.DriveManager;
import com.neptuo.os.client.app.explorer.Explorer;
import com.neptuo.os.client.app.usermanager.UserManager;
import com.neptuo.os.client.data.model.User;
import com.neptuo.os.client.http.AsyncCallback;
import com.neptuo.os.client.http.ExceptionType;
import com.neptuo.os.client.http.service.LoginService;
import java.util.List;

/**
 *
 * @author Mara
 */
public class MainMenu extends LayoutContainer {

    private static final int SESSION_TIMEOUT = 30 * 60;
    private int timeoutCount = 0;
    private Timer timeoutTimer;
    private LayoutContainer leftContent;
    private LayoutContainer rightContent;
    private Button btnTimeout;

    public MainMenu() {
        addStyleName("x-main-menu");

        leftContent = new LayoutContainer();
        leftContent.addStyleName("x-main-menu-left");
        add(leftContent);

        rightContent = new LayoutContainer();
        rightContent.addStyleName("x-main-menu-right");
        add(rightContent);

        createPanel();
    }

    public void createPanel() {
        Button btnExplorer = new Button(Constants.explorer().heading(), Constants.icons16().folder(), new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                Explorer explorer = new Explorer();
                explorer.run(new String[0]);
            }
        });
        leftContent.add(btnExplorer);

        Button btnUsers = new Button(Constants.users().heading(), Constants.icons16().users(), new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                UserManager manager = new UserManager();
                manager.run(new String[0]);
            }
        });
        leftContent.add(btnUsers);

        Button btnDrives = new Button(Constants.drives().heading(), Constants.icons16().drives(), new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                DriveManager manager = new DriveManager();
                manager.run(new String[0]);
            }
        });
        leftContent.add(btnDrives);

        btnTimeout = new Button("", new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                btnTimeout.disable();
                LoginService.isLogged(new AsyncCallback<User>() {

                    @Override
                    public void onSuccess(List<User> objects) {
                        btnTimeout.enable();
                    }

                    @Override
                    public void onClientError(Throwable e) {
                        btnTimeout.enable();
                    }

                    @Override
                    public void onServerError(ExceptionType exceptionType) {
                        btnTimeout.enable();
                    }
                });
            }
        });
        btnTimeout.setToolTip(Constants.webdesk().timeoutInCaption());
        rightContent.add(btnTimeout);

        timeoutTick();
        timeoutTimer = new Timer() {

            @Override
            public void run() {
                timeoutTick();
            }
        };
        timeoutTimer.scheduleRepeating(1000);

        Button btnLogout = new Button(Constants.webdesk().logout() + ", " + NeptuoRoot.getUser().getUsername(), Constants.icons16().door_out(), new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                NeptuoRoot.logout();
            }
        });
        rightContent.add(btnLogout);
    }

    private void timeoutTick() {
        timeoutCount++;
        if ((SESSION_TIMEOUT - timeoutCount) > 0) {
            btnTimeout.setText(Constants.webdesk().timeoutIn() + " " + (SESSION_TIMEOUT - timeoutCount) + "s");
        } else {
            NeptuoRoot.showLoginScreen();
        }
        doLayout(true);
    }

    public void resetTimeout() {
        timeoutCount = 0;
        timeoutTick();
    }

    public void stopTimeout() {
        timeoutTimer.cancel();
    }
}

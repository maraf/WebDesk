/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.os.client;

import com.extjs.gxt.ui.client.event.FormEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.neptuo.os.client.conf.Configuration;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.IFrameElement;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.neptuo.os.client.data.Clipboard;
import com.neptuo.os.client.data.model.User;
import com.neptuo.os.client.http.ExceptionType;
import com.neptuo.os.client.http.RequestCallback;
import com.neptuo.os.client.http.RequestFactory;
import com.neptuo.os.client.http.SimpleCallback;
import com.neptuo.os.client.ui.Workspace;
import com.neptuo.os.client.ui.LoginScreen;
import com.neptuo.os.client.data.model.Property;
import com.neptuo.os.client.data.databus.DataEventBus;
import com.neptuo.os.client.data.model.Drive;
import com.neptuo.os.client.http.AbstractAsyncCallback;
import com.neptuo.os.client.http.AsyncCallback;
import com.neptuo.os.client.http.service.FileSystemService;
import com.neptuo.os.client.http.service.LoginService;
import com.neptuo.os.client.http.service.PropertyService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Mara
 */
public class NeptuoRoot {

    private static boolean initialized = false;
    private static List<Property> desktopProperties = new ArrayList<Property>();
    private static List<Drive> userDrives = new ArrayList<Drive>();
    private static Map<String, String> userProperties = new HashMap<String, String>();
    private static User currentUser;
    private static String authToken;
    private static DataEventBus dataBus;
    private static Clipboard clipboard;
    private static Configuration configuration;
    private static RootPanel contentRootPanel;
    private static Workspace currentWorkspace;
    private static LoginScreen loginScreen;
    private static IFrameElement downloadFrame;

    public static void initialize() {
        if (!initialized) {
            RootPanel.get("x-coreloading").setStyleName("x-hidden");
            downloadFrame = (IFrameElement) IFrameElement.as(RootPanel.get("__neptuo_download").getElement());
            contentRootPanel = RootPanel.get("x-content");
            dataBus = new DataEventBus();
            clipboard = new Clipboard();

            loginScreen = new LoginScreen();
            loginScreen.setVisible(false);
            contentRootPanel.add(loginScreen);

            LoginService.isLogged(createLoginCallback());
        }
    }

    public static void login(String username, String password, String domain, final RequestCallback callback) {
        loginScreen.setMessage(Constants.webdesk().loggingIn());
        LoginService.login(username, password, domain, createLoginCallback());

    }

    public static void logout() {
        cleanWorkspace();
        getCurrentWorkspace().getMainMenu().stopTimeout();
        currentWorkspace.hide();
        desktopProperties.clear();
        userDrives.clear();

        LoginService.logout(new RequestCallback() {

            @Override
            public void onSuccess(Request request, Response response) {
                loginScreen.show();
            }

            @Override
            public void onClientError(Request request, Throwable exception) {
                loginScreen.show();
            }

            @Override
            public void onServerError(ExceptionType exceptionType) {
                loginScreen.show();
            }
        });
        authToken = null;
    }

    public static void loadCurrentUserInfo() {
        LoginService.isLogged(new AsyncCallback<User>() {

            @Override
            public void onSuccess(List<User> objects) {
                currentUser = objects.get(0);
            }

            @Override
            public void onClientError(Throwable e) {
                getCurrentWorkspace().getLogger().log("Error loading current user info", e);
            }

            @Override
            public void onServerError(ExceptionType exceptionType) {
                getCurrentWorkspace().getLogger().log("Error loading current user info", exceptionType.getLocalizedMessage());
            }
        });
    }

    public static void loadUserDesktopSettings(final SimpleCallback callback) {
        if (desktopProperties.isEmpty()) {
            Property p;
            desktopProperties.add(new Property(NeptuoRoot.class.getName(), "desktop.backgroundimage", null));
            desktopProperties.add(new Property(NeptuoRoot.class.getName(), "desktop.backgroundstretch", null));
        }
        PropertyService.get(desktopProperties, new AsyncCallback<Property>() {

            @Override
            public void onSuccess(List<Property> properties) {
                for (Property p : properties) {
                    userProperties.put(p.getKey(), p.getValue());
                }
                callback.called();
            }

            @Override
            public void onClientError(Throwable e) {
                callback.called();
            }

            @Override
            public void onServerError(ExceptionType exceptionType) {
                callback.called();
            }
        });
    }

    public static void loadUserDrives() {
        FileSystemService.getDrives(new AsyncCallback<Drive>() {

            @Override
            public void onSuccess(List<Drive> objects) {
                userDrives.clear();
                userDrives.addAll(objects);
            }

            @Override
            public void onClientError(Throwable e) {
                Info.display("NeptuoRoot", "Error in comunication with server, please contact provider!");
            }

            @Override
            public void onServerError(ExceptionType exceptionType) {
                Info.display("NeptuoRoot", "Error in comunication with server, please contact provider!");
            }
        });
    }

    public static Workspace getCurrentWorkspace() {
        return currentWorkspace;
    }

    public static LoginScreen getLoginScreen() {
        return loginScreen;
    }

    public static void showLoginScreen() {
        authToken = null;
        if (NeptuoRoot.getCurrentWorkspace() != null) {
            getCurrentWorkspace().getMainMenu().stopTimeout();
            NeptuoRoot.getCurrentWorkspace().setVisible(false);
        }
        NeptuoRoot.getLoginScreen().setVisible(true);
    }

    public static void downloadFile(String url, Listener<FormEvent> listener) {
//        FormPanel fp = new FormPanel();
//        fp.setUrl(url);
//        fp.setAction(url);
//        if (listener != null) {
//            fp.addListener(Events.Submit, listener);
//        }
//        fp.hide();
//        RootPanel.get("x-temp").add(fp);
//        fp.submit();
        Window.open(url, url, null);
        listener.handleEvent(null);
    }

    public static User getUser() {
        return currentUser;
    }

    public static DataEventBus getDataBus() {
        return dataBus;
    }

    public static Clipboard getClipboard() {
        return clipboard;
    }

    public static List<Drive> getUserDrives() {
        return userDrives;
    }

    public static Drive getUserHomeDrive() {
        for (Drive item : userDrives) {
            if (item.getName().equals("Home")) {
                return item;
            }
        }
        return null;
    }

    public static String getProperty(String key) {
        return userProperties.get(key);
    }

    public static boolean isInitialized() {
        return initialized;
    }

    public static Configuration getConfiguration() {
        if (configuration == null) {
            configuration = GWT.create(Configuration.class);
        }
        return configuration;
    }

    public static String getAuthToken() {
        return authToken;
    }

    public static IFrameElement getDownloadFrame() {
        return downloadFrame;
    }

    private static AsyncCallback<User> createLoginCallback() {
        return new AbstractAsyncCallback<User>() {

            @Override
            public void onSuccess(List<User> objects) {
                if (getResponse().getStatusCode() == 200) {
                    authToken = getResponse().getHeader(RequestFactory.AUTH_HEADER_NAME);
                    loginScreen.setMessage(Constants.webdesk().loadingProperties());
                    currentUser = objects.get(0);
                    dataBus = new DataEventBus();
                    clipboard = new Clipboard();

                    FileSystemService.getDrives(new AsyncCallback<Drive>() {

                        @Override
                        public void onSuccess(List<Drive> objects) {
                            userDrives.addAll(objects);

                            loadUserDesktopSettings(new SimpleCallback() {

                                @Override
                                public void called() {
                                    loginScreen.hide();
                                    createWorkspace();
                                    currentWorkspace.show();
                                }
                            });
                        }

                        @Override
                        public void onClientError(Throwable e) {
                            Info.display("NeptuoRoot", "Error in comunication with server, please contact provider!");
                        }

                        @Override
                        public void onServerError(ExceptionType exceptionType) {
                            Info.display("NeptuoRoot", "Error in comunication with server, please contact provider!");
                        }
                    });
                } else {
                    loginScreen.show();
                }
                initialized = true;
            }

            @Override
            public void onClientError(Throwable e) {
                Info.display("NeptuoRoot", "Error in comunication with server, please contact provider!");
            }

            @Override
            public void onServerError(ExceptionType exceptionType) {
                Info.display("NeptuoRoot", "Error in comunication with server, please contact provider!");
            }
        };
    }

    private static void createWorkspace() {
        PropertyService.clear();
        userProperties.clear();
        if (currentWorkspace != null) {
            contentRootPanel.remove(currentWorkspace);
        }
        currentWorkspace = new Workspace();
        contentRootPanel.add(currentWorkspace);
    }

    private static void cleanWorkspace() {
        //TODO: Clear WORKSPACE
        ArrayList<com.extjs.gxt.ui.client.widget.Window> windows = new ArrayList<com.extjs.gxt.ui.client.widget.Window>();
        for(com.extjs.gxt.ui.client.widget.Window w : getCurrentWorkspace().getWindowManager().getWindows()) {
            windows.add(w);
        }
        for(com.extjs.gxt.ui.client.widget.Window w : windows) {
            w.minimize();
        }
    }

    public static class Utils {

        public static int parseInt(String value) {
            try {
                if (value.indexOf('.') != -1) {
                    value = value.substring(0, value.indexOf('.'));
                }
                return Integer.valueOf(value).intValue();
            } catch (Exception e) {
                MessageBox.alert("NeptuoRoot::parseInt", "Error parsing int value: " + value + ", message: " + e.getMessage(), null);
                return 0;
            }
        }
    }
}

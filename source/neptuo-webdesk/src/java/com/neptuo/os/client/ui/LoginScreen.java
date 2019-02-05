/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.os.client.ui;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.AbsoluteLayout;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.Response;
import com.google.gwt.safehtml.shared.OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml;
import com.google.gwt.user.client.ui.Anchor;
import com.neptuo.os.client.Constants;
import com.neptuo.os.client.NeptuoRoot;
import com.neptuo.os.client.http.ExceptionType;
import com.neptuo.os.client.http.RequestCallback;
import com.neptuo.os.client.ui.event.EnterKeyListener;
import com.neptuo.os.client.ui.form.validator.NotEmptyValidator;

/**
 *
 * @author Mara
 */
public class LoginScreen extends Viewport {

    private LayoutContainer lcnLangs;
    private Anchor anrCzech;
    private Anchor anrEnglish;

    private FormPanel fplForm;
    private ErrorLabel lblMessage;
    private TextField<String> tfdUsername;
    private TextField<String> tfdPassword;
    private TextField<String> tfdDomain;
    private Button btnLogin;
    private Button btnAutoLogin;

    public LoginScreen() {
        setLayout(new AbsoluteLayout());
        addStyleName("x-login-screen");

        lcnLangs = new LayoutContainer();
        lcnLangs.addStyleName("x-languages");
        add(lcnLangs);

        anrCzech = new Anchor(new OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml("<span>cs</span>"), getUrlWithParam("locale", "cs_CZ"));
        anrCzech.addStyleName("x-lang-cs");
        lcnLangs.add(anrCzech);

        anrEnglish = new Anchor(new OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml("<span>en</span>"), getUrlWithParam("locale", "en_US"));
        anrEnglish.addStyleName("x-lang-en");
        lcnLangs.add(anrEnglish);

        fplForm = new FormPanel();
        fplForm.setWidth(325);
        fplForm.addStyleName("x-login-form");
        fplForm.setHeading(Constants.webdesk().webdesktitle());

        lblMessage = new ErrorLabel();
        lblMessage.setClassicMode(true);
        fplForm.add(lblMessage);

        KeyListener submitListener = new EnterKeyListener() {

            @Override
            public void componentEnterPress(ComponentEvent e) {
                btnLogin.fireEvent(Events.Select);
            }
        };

        tfdUsername = new TextField<String>();
        tfdUsername.setId("username");
        tfdUsername.setFieldLabel(Constants.webdesk().username());
        tfdUsername.addKeyListener(submitListener);
        fplForm.add(tfdUsername);

        tfdPassword = new TextField<String>();
        tfdPassword.setId("password");
        tfdPassword.setPassword(true);
        tfdPassword.setFieldLabel(Constants.webdesk().password());
        tfdPassword.addKeyListener(submitListener);
        fplForm.add(tfdPassword);

        tfdDomain = new TextField<String>();
        tfdDomain.setId("domain");
        tfdDomain.setFieldLabel(Constants.webdesk().domain());
        tfdDomain.setValue("localhost");
        tfdDomain.addKeyListener(submitListener);
        fplForm.add(tfdDomain);

        btnAutoLogin = new Button("AutoLogin", Constants.icons16().user_go(), new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                login("admin", "admin", "localhost");
            }
        });
//        fplForm.addButton(btnAutoLogin);

        btnLogin = new Button(Constants.webdesk().login(), Constants.icons16().user_go(), new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                if (tfdUsername.getValue() != null && tfdPassword.getValue() != null && tfdDomain.getValue() != null) {
                    login(tfdUsername.getValue(), tfdPassword.getValue(), tfdDomain.getValue());
                }
            }
        });
        fplForm.addButton(btnLogin);

        this.add(fplForm);
    }

    @Override
    protected void onWindowResize(int width, int height) {
        super.onWindowResize(width, height);

        fplForm.setPosition((width - fplForm.getWidth()) / 2, (height - fplForm.getHeight()) / 2);
    }

    @Override
    public void show() {
        super.show();

        setFormEnabled(true);
        resetMessage();
        tfdUsername.setValue("");
        tfdUsername.focus();
        tfdPassword.setValue("");
        fplForm.setPosition((this.getWidth() - fplForm.getWidth()) / 2, (this.getHeight() - fplForm.getHeight()) / 2);
    }

    public void setMessage(String message) {
        fplForm.setHeading(Constants.webdesk().webdesktitle() + " :: " + message);
    }

    public void resetMessage() {
        fplForm.setHeading(Constants.webdesk().webdesktitle());
    }

    private void setFormEnabled(boolean state) {
        tfdDomain.setEnabled(state);
        tfdUsername.setEnabled(state);
        tfdPassword.setEnabled(state);
        btnLogin.setEnabled(state);
        btnAutoLogin.setEnabled(state);
    }

    private void login(String username, String password, String domain) {
        lblMessage.setVisible(false);
        setFormEnabled(false);

        NeptuoRoot.login(username, password, domain, new RequestCallback() {

            @Override
            public void onClientError(Request request, Throwable exception) {
                lblMessage.setText(Constants.webdesk().errorSendingRequest());
                setFormEnabled(true);
                resetMessage();
            }

            @Override
            public void onServerError(ExceptionType exceptionType) {
                lblMessage.setText(exceptionType.getLocalizedMessage());
                setFormEnabled(true);
                resetMessage();
            }

            @Override
            public void onSuccess(Request request, Response response) {
                if (response.getStatusCode() != 202) {
                    lblMessage.setText(Constants.webdesk().loginNoUser());
                }
                setFormEnabled(true);
                resetMessage();
            }
        });
    }

    private String getUrlWithParam(String param, String value) {
        String path = com.google.gwt.user.client.Window.Location.getPath();
        String queryString = com.google.gwt.user.client.Window.Location.getQueryString();
        
        if (com.google.gwt.user.client.Window.Location.getParameter(param) != null) {
            String oldValue = com.google.gwt.user.client.Window.Location.getParameter(param);
            return path + queryString.replace(param + "=" + oldValue, param + "=" + value);
        } else {
            if (queryString.length() == 0) {
                return path + "?" + param + "=" + value;
            } else {
                return path + queryString + "&" + param + "=" + value;
            }
        }
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.os.client.app.system;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.Response;
import com.neptuo.os.client.Constants;
import com.neptuo.os.client.data.model.Feedback;
import com.neptuo.os.client.http.ExceptionType;
import com.neptuo.os.client.http.RequestCallback;
import com.neptuo.os.client.http.service.FeedbackService;
import com.neptuo.os.client.ui.DialogWindow;
import com.neptuo.os.client.ui.form.HtmlEditor;

/**
 *
 * @author Mara
 */
public class FeedbackWindow extends DialogWindow {
    private FormPanel fpnForm;
    private TextField<String> tfdEmail;
    private TextField<String> tfdSubject;
    private HtmlEditor hteContent;

    private Button btnSend;
    private Button btnCancel;

    public FeedbackWindow() {
        setPlain(true);
        setIcon(Constants.icons16().application_home());
        setSize(700, 500);
        setHeading(Constants.webdesk().feedback());
        setLayout(new FitLayout());

        fpnForm = new FormPanel();
        fpnForm.setBorders(false);
        fpnForm.setBodyBorder(false);
        fpnForm.setLabelWidth(55);
        fpnForm.setPadding(5);
        fpnForm.setHeaderVisible(false);
        add(fpnForm);

        tfdEmail = new TextField<String>();
        tfdEmail.setFieldLabel(Constants.webdesk().email());
        tfdEmail.setAllowBlank(false);
        tfdEmail.setRegex("^[a-zA-Z0-9.-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$");
        fpnForm.add(tfdEmail, new FormData("100%"));

        tfdSubject = new TextField<String>();
        tfdSubject.setFieldLabel(Constants.webdesk().subject());
        tfdSubject.setAllowBlank(false);
        tfdSubject.setMinLength(5);
        fpnForm.add(tfdSubject, new FormData("100%"));

        hteContent = new HtmlEditor();
        hteContent.setHideLabel(true);
        fpnForm.add(hteContent, new FormData("100% -53"));

        btnSend = new Button(Constants.webdesk().send(), Constants.icons16().email_go(), new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                hteContent.syncValue();
                mask(Constants.explorer().mask());
                FeedbackService.send(new Feedback(tfdEmail.getValue(), tfdSubject.getValue(), hteContent.getValue()), new RequestCallback() {

                    @Override
                    public void onSuccess(Request request, Response response) {
                        MessageBox.info(Constants.webdesk().feedback(), Constants.webdesk().feedback_sent(), new Listener<MessageBoxEvent>() {

                            @Override
                            public void handleEvent(MessageBoxEvent be) {
                                hide();
                            }
                        });
                    }

                    @Override
                    public void onClientError(Request request, Throwable exception) {
                        MessageBox.alert(Constants.webdesk().feedback(), exception.getLocalizedMessage(), null);
                        unmask();
                    }

                    @Override
                    public void onServerError(ExceptionType exceptionType) {
                        MessageBox.alert(Constants.webdesk().feedback(), exceptionType.getLocalizedMessage(), null);
                        unmask();
                    }
                });
            }
        });
        addButton(btnSend);

        btnCancel = new Button(Constants.webdesk().cancel(), Constants.icons16().email_delete(), new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                hide();
            }
        });
        addButton(btnCancel);
    }

    @Override
    public String getClassName() {
        return getClass().getName();
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.os.client.app.explorer.dialogs;

import com.neptuo.os.client.app.explorer.event.FileUploadCallback;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FormEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FileUploadField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Encoding;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Method;
import com.neptuo.os.client.Constants;
import com.neptuo.os.client.http.service.ServiceUrls;
import com.neptuo.os.client.ui.Window;

/**
 *
 * @author Mara
 */
public class FileUploadWindow extends Window {
    public static final String FIELD_NAME = "uploadFormElement";

    protected FormPanel fpnPanel;
    protected FileUploadField fufUpload;

    protected Button btnOk;
    protected Button btnCancel;

    public FileUploadWindow(final FileUploadCallback callback) {
        setWidth(350);
        setHeading(Constants.explorer().winFileUploadWindow());
        setIcon(Constants.icons16().page_white_add());
        setModal(true);
        setRefreshable(false);
        setMinimizable(false);
        setMaximizable(false);

        fpnPanel = new FormPanel();
        fpnPanel.setBorders(false);
        fpnPanel.setBodyBorder(false);
        fpnPanel.setHeaderVisible(false);
        fpnPanel.setAction(ServiceUrls.FileSystem.Upload.toAbsolute());
        fpnPanel.setEncoding(Encoding.MULTIPART);
        fpnPanel.setMethod(Method.POST);
        fpnPanel.addListener(Events.Submit, new Listener<FormEvent>() {

            @Override
            public void handleEvent(FormEvent be) {
                String key = be.getResultHtml();
                key = key.replace("<pre>", "").replace("</pre>", "");
                callback.onFileUploaded(key);
                hide();
            }
        });

        fufUpload = new FileUploadField();
        fufUpload.setAllowBlank(false);
        fufUpload.setFieldLabel(Constants.explorer().file());
        fufUpload.setName(FIELD_NAME);
        fpnPanel.add(fufUpload);

        btnOk = new Button(Constants.webdesk().ok(), new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                if (!fpnPanel.isValid()) {
                    return;
                } else {
                    fpnPanel.mask(Constants.explorer().mask());
                    fpnPanel.submit();
                }
            }
        });
        fpnPanel.addButton(btnOk);

        btnCancel = new Button(Constants.webdesk().cancel(), new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                hide();
            }
        });
        fpnPanel.addButton(btnCancel);

        add(fpnPanel);
        getTaskbarButton().hideLoading();
    }

    public Button getBtnCancel() {
        return btnCancel;
    }

    public Button getBtnOk() {
        return btnOk;
    }

    @Override
    public String getClassName() {
        return getClass().getName();
    }

    @Override
    protected boolean getLoadWindowProperties() {
        return false;
    }

    @Override
    protected boolean registerTaskBarItem() {
        return false;
    }

    public static FormPanel createFormPanel(final FileUploadCallback callback) {
        FormPanel panel = new FormPanel();
        panel.setBorders(false);
        panel.setBodyBorder(false);
        panel.setHeaderVisible(false);
        panel.setAction(ServiceUrls.FileSystem.Upload.toAbsolute());
        panel.setEncoding(Encoding.MULTIPART);
        panel.setMethod(Method.POST);
        panel.addListener(Events.Submit, new Listener<FormEvent>() {

            @Override
            public void handleEvent(FormEvent be) {
                String xml = be.getResultHtml();
                callback.onFileUploaded(xml);
            }
        });

        FileUploadField upload = new FileUploadField();
        upload.setAllowBlank(false);
        upload.setFieldLabel(Constants.explorer().file());
        upload.setName(FIELD_NAME);
        panel.add(upload);

        return panel;
    }
}

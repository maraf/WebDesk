/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.client.app.explorer.dialogs;

import com.neptuo.os.client.app.explorer.event.CreateFolderCallback;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.neptuo.os.client.Constants;
import com.neptuo.os.client.ui.DialogWindow;
import com.neptuo.os.client.ui.event.EnterKeyListener;

/**
 *
 * @author Mara
 */
public class CreateFolderWindow extends DialogWindow {
    protected TextField<String> tfdName;
    protected Button btnOk;
    protected Button btnCancel;

    public CreateFolderWindow(final CreateFolderCallback callback) {
        setWidth(400);
        setHeight(112);
        setLayout(new RowLayout(Orientation.VERTICAL));
        setIcon(Constants.icons16().folder_add());
        setHeading(Constants.explorer().winCreateFolderWindow());

        tfdName = new TextField<String>();
        tfdName.setFieldLabel(Constants.explorer().tfdName());
        tfdName.addKeyListener(new EnterKeyListener() {

            @Override
            public void componentEnterPress(ComponentEvent e) {
                btnOk.fireEvent(Events.Select);
            }
        });
        add(tfdName, new RowData(1, -1, new Margins(10)));

        btnOk = new Button(Constants.webdesk().ok(), new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                callback.onOkButtonClicked(tfdName.getValue());
                hide();
            }
        });
        addButton(btnOk);
        btnCancel = new Button(Constants.webdesk().cancel(), new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                hide();
            }
        });
        addButton(btnCancel);
        getTaskbarButton().hideLoading();

        tfdName.focus();
    }

    public Button getBtnCancel() {
        return btnCancel;
    }

    public Button getBtnOk() {
        return btnOk;
    }

    public TextField<String> getTfdName() {
        return tfdName;
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
}

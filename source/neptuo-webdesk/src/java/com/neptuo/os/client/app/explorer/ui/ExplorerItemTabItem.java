/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.client.app.explorer.ui;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.neptuo.os.client.data.model.FsysItem;
import com.extjs.gxt.ui.client.binding.FormBinding;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ButtonBar;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.neptuo.os.client.Constants;

/**
 *
 * @author Mara
 */
public class ExplorerItemTabItem extends TabItem {
    protected FsysItem model;

    protected FormPanel fpnPanel;
    protected FormBinding fbdPanel;
    protected TextField<String> tfdId;
    protected TextField<String> tfdName;
    protected TextField<String> tfdCreated;
    protected TextField<String> tfdModified;
    protected TextField<String> tfdLocation;
    protected TextField<String> tfdPublicId;

    protected ButtonBar bbrButtons;
    protected Button btnOk;
    protected Button btnCancel;

    public ExplorerItemTabItem(FsysItem item) {
        model = item;
        setText(Constants.explorer().tbiFileDetail());
        setBorders(false);
        setLayout(new RowLayout());

        fpnPanel = new FormPanel();
        fpnPanel.setHeaderVisible(false);
        fpnPanel.setBorders(false);
        fpnPanel.setBodyBorder(false);
        add(fpnPanel, new RowData(1, 1));

        tfdId = new TextField<String>();
        tfdId.setName("id");
        tfdId.setWidth(50);
        tfdId.setReadOnly(true);
        tfdId.setFieldLabel(Constants.explorer().tfdId());
        fpnPanel.add(tfdId);

        tfdName = new TextField<String>();
        tfdName.setName("name");
        tfdName.setFieldLabel(Constants.explorer().tfdName());
        fpnPanel.add(tfdName);

        tfdCreated = new TextField<String>();
        tfdCreated.setName("created");
        tfdCreated.setFieldLabel(Constants.explorer().tfdCreated());
        tfdCreated.setReadOnly(true);
        fpnPanel.add(tfdCreated);

        tfdModified = new TextField<String>();
        tfdModified.setName("modified");
        tfdModified.setFieldLabel(Constants.explorer().tfdModified());
        tfdModified.setReadOnly(true);
        fpnPanel.add(tfdModified);

        tfdLocation = new TextField<String>();
        tfdLocation.setName("path");
        tfdLocation.setFieldLabel(Constants.explorer().tfdPath());
        tfdLocation.setReadOnly(true);
        fpnPanel.add(tfdLocation);

        tfdPublicId = new TextField<String>();
        tfdPublicId.setName("publicId");
        tfdPublicId.setFieldLabel(Constants.explorer().tfdPublicId());
        tfdPublicId.setReadOnly(true);
        fpnPanel.add(tfdPublicId);

        bbrButtons = new ButtonBar();
        bbrButtons.setAlignment(HorizontalAlignment.RIGHT);
        add(bbrButtons, new RowData(1, 30));

        btnOk = new Button(Constants.webdesk().ok());
        bbrButtons.add(btnOk);
    }

    protected ExplorerItemTabItem getThis() {
        return this;
    }
}

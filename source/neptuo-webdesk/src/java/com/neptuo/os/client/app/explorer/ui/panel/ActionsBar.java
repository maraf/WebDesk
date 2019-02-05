/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.os.client.app.explorer.ui.panel;

import com.extjs.gxt.ui.client.Style.*;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.*;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.*;
import com.extjs.gxt.ui.client.widget.menu.*;
import com.neptuo.os.client.Constants;

/**
 *
 * @author Mara
 */
public class ActionsBar extends LayoutContainer {

    protected ButtonBar bbrActions;
    protected Menu mnuCreate;
    protected Button btnCreate;
    protected Button btnDownload;
    protected Button btnCopy;
    protected Menu mnuCopy;
    protected MenuItem mniCut;
    protected MenuItem mniCopy;
    protected MenuItem mniPaste;
    protected Button btnProperties;
    protected Button btnDelete;
    protected ButtonBar bbrFilter;
    protected ToggleButton tgbFoldersOnly;
    protected TextField<String> tfdFileNameFilter;
    protected TextField<String> tfdFileExtensionFilter;
    protected MenuItem mniCreateFolder;
    protected MenuItem mniCreateEmptyFile;
    protected MenuItem mniFileUpload;
    protected MenuItem mniFileUploader;

    public ActionsBar() {
        setLayout(new RowLayout(Orientation.HORIZONTAL));
        setHeight(26);
        addStyleName("x-transparent");
        setBorders(false);

        bbrActions = new ButtonBar();
        add(bbrActions, new RowData(1, 1));

        mnuCreate = new Menu();

        btnCreate = new Button(Constants.explorer().btnCreate(), Constants.icons16().bullet_add());
        btnCreate.setMenu(mnuCreate);
        btnCreate.disable();
        bbrActions.add(btnCreate);

        mniCreateFolder = new MenuItem(Constants.explorer().btnCreateFolder(), Constants.icons16().folder_add());
        mnuCreate.add(mniCreateFolder);

        mniCreateEmptyFile = new MenuItem(Constants.explorer().btnCreateEmptyFile(), Constants.icons16().page_white_add());
        mnuCreate.add(mniCreateEmptyFile);

        mniFileUpload = new MenuItem(Constants.explorer().btnFileUpload(), Constants.icons16().page_white_get());
        mnuCreate.add(mniFileUpload);

        mniFileUploader = new MenuItem(Constants.explorer().btnFileUploader(), Constants.icons16().page_white_get());
        mnuCreate.add(mniFileUploader);

        btnDownload = new Button(Constants.explorer().btnDownload(), Constants.icons16().folder_go());
        btnDownload.disable();
        bbrActions.add(btnDownload);

        mnuCopy = new Menu();

        btnCopy = new Button(Constants.explorer().btnCopy());
        btnCopy.setMenu(mnuCopy);
        btnCopy.disable();
        bbrActions.add(btnCopy);

        mniCut = new MenuItem(Constants.explorer().mniCut());
        mniCut.disable();
        mnuCopy.add(mniCut);

        mniCopy = new MenuItem(Constants.explorer().mniCopy());
        mniCopy.disable();
        mnuCopy.add(mniCopy);

        mniPaste = new MenuItem(Constants.explorer().mniPaste());
        mniPaste.disable();
        mnuCopy.add(mniPaste);

        btnProperties = new Button(Constants.explorer().btnProperties(), Constants.icons16().folder_edit());
        btnProperties.disable();
        bbrActions.add(btnProperties);

        btnDelete = new Button(Constants.explorer().btnDelete(), Constants.icons16().folder_delete());
        btnDelete.disable();
        bbrActions.add(btnDelete);

        bbrFilter = new ButtonBar();
        bbrFilter.setAlignment(HorizontalAlignment.RIGHT);
        bbrFilter.setWidth(300);
        add(bbrFilter, new RowData(-1, 1));

        tgbFoldersOnly = new ToggleButton(Constants.explorer().chxFoldersOnly());
        bbrFilter.add(tgbFoldersOnly);

        tfdFileNameFilter = new TextField<String>();
        tfdFileNameFilter.setEmptyText(Constants.explorer().tfdFileNameFilterEmptyText());
        bbrFilter.add(tfdFileNameFilter);

        tfdFileExtensionFilter = new TextField<String>();
        tfdFileExtensionFilter.setWidth(50);
        tfdFileExtensionFilter.setEmptyText(Constants.explorer().tfdFileExtensionFilterEmptyText());
        bbrFilter.add(tfdFileExtensionFilter);
    }

    public void setItemButtonsEnabled(boolean enabled) {
        btnDownload.setEnabled(enabled);
        btnProperties.setEnabled(enabled);
        btnDelete.setEnabled(enabled);
        mniCut.setEnabled(enabled);
        mniCopy.setEnabled(enabled);
    }

    public void setItemButtonsIconToFile() {
        btnDownload.setIcon(Constants.icons16().page_white_go());
        btnCopy.setIcon(Constants.icons16().page_white_copy());
        btnProperties.setIcon(Constants.icons16().page_white_edit());
        btnDelete.setIcon(Constants.icons16().page_white_delete());
    }

    public void setItemButtonsIconToFolder() {
        btnDownload.setIcon(Constants.icons16().folder_go());
        btnCopy.setIcon(Constants.icons16().folders());
        btnProperties.setIcon(Constants.icons16().folder_edit());
        btnDelete.setIcon(Constants.icons16().folder_delete());
    }

    public Button getCreateButton() {
        return btnCreate;
    }

    public Button getDeleteButton() {
        return btnDelete;
    }

    public Button getDownloadButton() {
        return btnDownload;
    }

    public Button getPropertiesButton() {
        return btnProperties;
    }

    public Menu getCreateMenu() {
        return mnuCreate;
    }

    public TextField<String> getFileExtensionFilterTextField() {
        return tfdFileExtensionFilter;
    }

    public TextField<String> getFileNameFilterTextField() {
        return tfdFileNameFilter;
    }

    public ToggleButton getFoldersOnlyToggleButton() {
        return tgbFoldersOnly;
    }

    public MenuItem getCreateEmptyFileMenuItem() {
        return mniCreateEmptyFile;
    }

    public MenuItem getCreateFolderMenuItem() {
        return mniCreateFolder;
    }

    public MenuItem getFileUploadMenuItem() {
        return mniFileUpload;
    }

    public MenuItem getFileUploaderMenuItem() {
        return mniFileUploader;
    }

    public Button getCopyButton() {
        return btnCopy;
    }

    public MenuItem getCopyMenuItem() {
        return mniCopy;
    }

    public MenuItem getCutMenuItem() {
        return mniCut;
    }

    public MenuItem getPasteMenuItem() {
        return mniPaste;
    }
}

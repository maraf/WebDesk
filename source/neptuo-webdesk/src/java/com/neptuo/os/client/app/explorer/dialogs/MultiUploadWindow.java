/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.os.client.app.explorer.dialogs;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FormEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ToggleButton;
import com.extjs.gxt.ui.client.widget.form.FileUploadField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Encoding;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Method;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.neptuo.os.client.Constants;
import com.neptuo.os.client.app.explorer.data.PathHelper;
import com.neptuo.os.client.data.model.FsysItem;
import com.neptuo.os.client.data.model.UploadItem;
import com.neptuo.os.client.http.AsyncCallback;
import com.neptuo.os.client.http.ExceptionType;
import com.neptuo.os.client.http.service.FileSystemService;
import com.neptuo.os.client.http.service.ServiceUrls;
import com.neptuo.os.client.ui.DialogWindow;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mara
 */
public class MultiUploadWindow extends DialogWindow {
    private List<AsyncCallback<FsysItem>> onSavedListeners;

    private String parentFolderPath;
    private int parentFolderId;
    private ListStore<UploadItem> lstUploads;
    private Grid<UploadItem> grdUploads;
    private FormPanel fpnForm;
    private FileUploadField fufFile;
    private ToolBar tlbActions;
    private Button btnAdd;
    private Button btnRemove;
    private Button btnRemoveAll;
    private ToggleButton tgbRemoveAfterSave;
    private Button btnSave;

    public MultiUploadWindow(String parentFolderPath, int parentFolderId) {
        this.parentFolderPath = parentFolderPath;
        this.parentFolderId = parentFolderId;
        this.onSavedListeners = new ArrayList<AsyncCallback<FsysItem>>();

        setWidth(600);
        setHeight(400);
        setLayout(new RowLayout());
        setHeading(Constants.explorer().uploads() + " - " + parentFolderPath);
        setIcon(Constants.icons16().page_white_go());

        lstUploads = new ListStore<UploadItem>();

        List<ColumnConfig> columns = new ArrayList<ColumnConfig>();

        ColumnConfig nameColumn = new ColumnConfig("name", Constants.explorer().file(), 200);
        TextField<String> nameTextField = new TextField<String>();
        nameTextField.setAllowBlank(false);
        nameColumn.setEditor(new CellEditor(nameTextField));

        columns.add(nameColumn);
        columns.add(new ColumnConfig("status", Constants.explorer().status(), 70));

        ColumnModel columnModel = new ColumnModel(columns);

        grdUploads = new EditorGrid<UploadItem>(lstUploads, columnModel);
        grdUploads.setAutoExpandColumn("name");
        grdUploads.setBorders(false);
        grdUploads.setStripeRows(true);
        grdUploads.getView().setForceFit(true);
        grdUploads.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        add(grdUploads, new RowData(1, 1));

        tlbActions = new ToolBar();
        add(tlbActions, new RowData(1, -1));

        fpnForm = new FormPanel();
        fpnForm.addStyleName("x-upload-hidden");
        fpnForm.setAction(ServiceUrls.FileSystem.Upload.toAbsolute());
        fpnForm.setEncoding(Encoding.MULTIPART);
        fpnForm.setMethod(Method.POST);
        fpnForm.addListener(Events.Submit, new Listener<FormEvent>() {

            @Override
            public void handleEvent(FormEvent be) {
                String key = be.getResultHtml();
                key = key.toLowerCase().replace("<pre>", "").replace("</pre>", "").replace("<pre style=\"word-wrap: break-word; white-space: pre-wrap;\">", "");
                lstUploads.add(new UploadItem(key, PathHelper.getFileNameFromUploadField(fufFile), Constants.explorer().status_uploaded(), UploadItem.STATUS_UPLOADED));
                getBody().unmask();
            }
        });
        add(fpnForm);

        fufFile = new FileUploadField();
        fufFile.setName(FileUploadWindow.FIELD_NAME);
        fufFile.setFireChangeEventOnSetValue(true);
        fufFile.addListener(Events.Change, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                fpnForm.submit();
                getBody().mask(Constants.explorer().mask());
            }
        });
        fpnForm.add(fufFile);

        btnAdd = new Button(Constants.explorer().btnAddFile(), Constants.icons16().page_white_add(), new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                fufFile.getFileInput().click();
            }
        });
        tlbActions.add(btnAdd);

        btnRemove = new Button(Constants.explorer().btnRemoveFile(), Constants.icons16().page_white_delete(), new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                if (grdUploads.getSelectionModel().getSelectedItem() != null) {
                    lstUploads.remove(grdUploads.getSelectionModel().getSelectedItem());
                }
            }
        });
        tlbActions.add(btnRemove);

        btnRemoveAll = new Button(Constants.explorer().btnRemoveAllFiles(), Constants.icons16().page_white_delete(), new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                lstUploads.removeAll();
            }
        });
        tlbActions.add(btnRemoveAll);

        tlbActions.add(new SeparatorToolItem());

        tgbRemoveAfterSave = new ToggleButton(Constants.explorer().tgbRemoveAfterSave());
        tgbRemoveAfterSave.toggle();
        tlbActions.add(tgbRemoveAfterSave);

        tlbActions.add(new FillToolItem());

        btnSave = new Button(Constants.explorer().btnSaveFiles(), Constants.icons16().page_white_go(), new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                for (UploadItem item : lstUploads.findModels("statusCode", UploadItem.STATUS_UPLOADED)) {
                    new CreateFileWorker(item);
                }
            }
        });
        tlbActions.add(btnSave);
    }

    public void addSavedListeners(AsyncCallback<FsysItem> callback) {
        onSavedListeners.add(callback);
    }

    @Override
    public String getClassName() {
        return getClass().getName();
    }

    public void setParentFolderId(int parentFolderId) {
        this.parentFolderId = parentFolderId;
    }

    public void setParentFolderPath(String parentFolderPath) {
        this.parentFolderPath = parentFolderPath;
    }

    public int getParentFolderId() {
        return parentFolderId;
    }

    public String getParentFolderPath() {
        return parentFolderPath;
    }

    class CreateFileWorker {
        private UploadItem upload;

        public CreateFileWorker(UploadItem item) {
            this.upload = item;
            upload.setStatus(Constants.explorer().status_saving());
            upload.setStatusCode(UploadItem.STATUS_SAVING);
            lstUploads.update(upload);
            FileSystemService.createFile(getParentFolderId(), item.getName(), item.getKey(), new AsyncCallback<FsysItem>() {

                @Override
                public void onSuccess(List<FsysItem> objects) {
                    upload.setStatus(Constants.explorer().status_saved());
                    upload.setStatusCode(UploadItem.STATUS_SAVED);
                    lstUploads.update(upload);
                    if(tgbRemoveAfterSave.isPressed()) {
                        lstUploads.remove(upload);
                    }
                    for(AsyncCallback<FsysItem> list : onSavedListeners) {
                        list.onSuccess(objects);
                    }
                }

                @Override
                public void onClientError(Throwable e) {
                    upload.setStatus(e.getMessage());
                    upload.setStatusCode(UploadItem.STATUS_UPLOADED);
                    lstUploads.update(upload);
                    for(AsyncCallback<FsysItem> list : onSavedListeners) {
                        list.onClientError(e);
                    }
                }

                @Override
                public void onServerError(ExceptionType exceptionType) {
                    upload.setStatus(exceptionType.getLocalizedMessage());
                    upload.setStatusCode(UploadItem.STATUS_UPLOADED);
                    lstUploads.update(upload);
                    for(AsyncCallback<FsysItem> list : onSavedListeners) {
                        list.onServerError(exceptionType);
                    }
                }
            });
        }
    }
}

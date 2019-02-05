/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.client.app.drivemanager;

import com.extjs.gxt.ui.client.binding.FormBinding;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.neptuo.os.client.Constants;
import com.neptuo.os.client.app.explorer.data.ItemType;
import com.neptuo.os.client.data.model.FsysItem;
import com.neptuo.os.client.app.explorer.ui.form.ExplorerSelectField;
import com.neptuo.os.client.data.model.Drive;
import com.neptuo.os.client.http.service.FileSystemService;
import com.neptuo.os.client.http.AsyncCallback;
import com.neptuo.os.client.http.ExceptionType;
import com.neptuo.os.client.ui.Window;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mara
 */
public class EditWindow extends Window {
    private Drive item;
    private List<AsyncCallback<Drive>> createdListeners = new ArrayList<AsyncCallback<Drive>>();
    private List<AsyncCallback<Drive>> updatedListeners = new ArrayList<AsyncCallback<Drive>>();

    private FormPanel fpnDetail;
    private FormBinding fbdDetail;

    private TextField<String> tfdName;
    private TextField<String> tfdLabel;
    private ExplorerSelectField esfFolderId;

    private Button btnSave;
    private Button btnCancel;

    public EditWindow(Drive item, AsyncCallback<Drive> created, AsyncCallback<Drive> updated) {
        this(item);
        
        if(created != null)
            addCreatedListener(created);

        if(updated != null)
            addUpdatedListener(updated);
    }

    public EditWindow(final Drive item) {
        this.item = item;

        setWidth(340);
        setHeight(220);
        setRefreshable(false);
        setHeading(Constants.drives().singleHeading() + (item.getName() != null ? " : " + item.getName() : ""));
        setIcon(Constants.icons16().drive());

        fpnDetail = new FormPanel();
        fpnDetail.setHeaderVisible(false);
        fpnDetail.setBorders(false);
        fpnDetail.setBodyBorder(false);

        tfdName = new TextField<String>();
        tfdName.setFieldLabel(Constants.webdesk().name());
        tfdName.setAllowBlank(false);
        tfdName.setName("name");
        fpnDetail.add(tfdName);

        tfdLabel = new TextField<String>();
        tfdLabel.setFieldLabel(Constants.drives().label());
        tfdLabel.setName("label");
        fpnDetail.add(tfdLabel);
        
        esfFolderId = new ExplorerSelectField();
        esfFolderId.setFieldLabel(Constants.drives().folder());
        esfFolderId.setSelectType(ItemType.FOLDER);
        fpnDetail.add(esfFolderId);
        
        fbdDetail = new FormBinding(fpnDetail, true);
        fbdDetail.bind(item);
        add(fpnDetail, new RowData(1, 1));

        btnSave = new Button(Constants.webdesk().save(), new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                if(fpnDetail.isValid()) {
                    AsyncCallback<Drive> callback = new AsyncCallback<Drive>() {

                        @Override
                        public void onSuccess(List<Drive> objects) {
                            hide();
                        }

                        @Override
                        public void onClientError(Throwable e) {
                            mask(e.getMessage());
                        }

                        @Override
                        public void onServerError(ExceptionType exceptionType) {
                            mask(exceptionType.getLocalizedMessage());
                        }
                    };
                    if (item.getId() == null) {
                        FileSystemService.createDrive(new Drive(tfdName.getValue(), tfdLabel.getValue(), esfFolderId.getSelectedItem().getId()), callback);
                    } else {
                        FileSystemService.updateDrive(new Drive(item.getId(), tfdName.getValue(), tfdLabel.getValue(), esfFolderId.getSelectedItem().getId()), callback);
                    }
                }
            }
        });
        addButton(btnSave);

        btnCancel = new Button(Constants.webdesk().cancel(), new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                hide();
            }
        });
        addButton(btnCancel);

        if (item.getFolderId() != null) {
//            mask(Constants.explorer().mask());
            FileSystemService.getFolderItemsInfo(item.getFolderId(), new AsyncCallback<FsysItem>() {

                @Override
                public void onSuccess(List<FsysItem> objects) {
                    esfFolderId.setSelectedItem(objects.get(0));
//                    unmask();
                }

                @Override
                public void onClientError(Throwable e) {
                    mask(e.getMessage());
                }

                @Override
                public void onServerError(ExceptionType exceptionType) {
                    mask(exceptionType.getLocalizedMessage());
                }
            });
        }
    }

    public void addSaveSelectionListener(SelectionListener<ButtonEvent> listener) {
        btnSave.addSelectionListener(listener);
    }

    public void removeSaveSelectionListener(SelectionListener<ButtonEvent> listener) {
        btnSave.removeSelectionListener(listener);
    }

    public void addCancelSelectionListener(SelectionListener<ButtonEvent> listener) {
        btnCancel.addSelectionListener(listener);
    }

    public void removeCancelSelectionListener(SelectionListener<ButtonEvent> listener) {
        btnCancel.removeSelectionListener(listener);
    }

    public void addCreatedListener(AsyncCallback<Drive> callback) {
        createdListeners.add(callback);
    }

    public void removeCreatedListener(AsyncCallback<Drive> callback) {
        createdListeners.remove(callback);
    }

    public void addUpdatedListener(AsyncCallback<Drive> callback) {
        updatedListeners.add(callback);
    }

    public void removeUpdatedListener(AsyncCallback<Drive> callback) {
        updatedListeners.remove(callback);
    }

    @Override
    public String getClassName() {
        return getClass().getName();
    }
}

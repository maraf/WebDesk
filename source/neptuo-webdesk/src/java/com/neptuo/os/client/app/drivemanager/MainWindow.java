/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.client.app.drivemanager;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.IconButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ButtonBar;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.Response;
import com.neptuo.os.client.Constants;
import com.neptuo.os.client.NeptuoRoot;
import com.neptuo.os.client.data.model.FsysItem;
import com.neptuo.os.client.app.explorer.ui.PermissionsWindow;
import com.neptuo.os.client.data.ListStore;
import com.neptuo.os.client.data.databus.DataEvents;
import com.neptuo.os.client.data.model.Drive;
import com.neptuo.os.client.http.service.FileSystemService;
import com.neptuo.os.client.http.AsyncCallback;
import com.neptuo.os.client.http.ComponentRequestAdapter;
import com.neptuo.os.client.http.ExceptionType;
import com.neptuo.os.client.ui.Window;
import com.neptuo.os.client.ui.grid.NumberColumnConfig;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mara
 */
public class MainWindow extends Window {
    private ButtonBar bbrActions;
    private Button btnCreate;
    private Button btnEdit;
    private Button btnEditPermissions;
    private Button btnDelete;

    private ListStore<Drive> lstItems;
    private Grid<Drive> grdItems;
    
    public MainWindow() {
        setWidth(700);
        setHeight(400);
        setHeading(Constants.drives().heading());
        setIcon(Constants.icons16().drives());
        setLayout(new RowLayout());

        refresh.addSelectionListener(new SelectionListener<IconButtonEvent>() {

            @Override
            public void componentSelected(IconButtonEvent ce) {
                loadData();
            }
        });

        bbrActions = new ButtonBar();
        add(bbrActions, new RowData(1, -1, new Margins(4, 0, 4, 0)));

        btnCreate = new Button(Constants.drives().btnCreate(), Constants.icons16().drive_add(), new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                EditWindow window = new EditWindow(new Drive());
                window.addCreatedListener(new AsyncCallback<Drive>() {

                    @Override
                    public void onSuccess(List<Drive> objects) {
                        lstItems.add(objects);
                    }

                    @Override
                    public void onClientError(Throwable e) {
                        MessageBox.alert(Constants.drives().singleHeading(), e.getMessage(), null);
                    }

                    @Override
                    public void onServerError(ExceptionType exceptionType) {
                        MessageBox.alert(Constants.drives().singleHeading(), exceptionType.getLocalizedMessage(), null);
                    }
                });
            }
        });
        bbrActions.add(btnCreate);

        btnEdit = new Button(Constants.drives().btnEdit(), Constants.icons16().drive_edit(), new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                EditWindow window = new EditWindow(grdItems.getSelectionModel().getSelectedItem(), new AsyncCallback<Drive>() {

                    @Override
                    public void onSuccess(List<Drive> objects) {
                        lstItems.add(objects);
                    }

                    @Override
                    public void onClientError(Throwable e) {
                        MessageBox.alert(Constants.drives().singleHeading(), e.getMessage(), null);
                    }

                    @Override
                    public void onServerError(ExceptionType exceptionType) {
                        MessageBox.alert(Constants.drives().singleHeading(), exceptionType.getLocalizedMessage(), null);
                    }
                }, null);
            }
        });
        btnEdit.disable();
        bbrActions.add(btnEdit);

        btnEditPermissions = new Button(Constants.drives().btnEditPermissions(), Constants.icons16().drive_key(), new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                FileSystemService.getFolderItemsInfo(grdItems.getSelectionModel().getSelectedItem().getFolderId(), new AsyncCallback<FsysItem>() {

                    @Override
                    public void onSuccess(List<FsysItem> objects) {
                        if (objects.size() > 0) {
                            PermissionsWindow win = new PermissionsWindow(objects.get(0));
                            win.show();
                        }
                    }

                    @Override
                    public void onClientError(Throwable e) {
                        MessageBox.alert(Constants.drives().singleHeading(), e.getMessage(), null);
                    }

                    @Override
                    public void onServerError(ExceptionType exceptionType) {
                        MessageBox.alert(Constants.drives().singleHeading(), exceptionType.getLocalizedMessage(), null);
                    }
                });
            }
        });
        btnEditPermissions.disable();
        bbrActions.add(btnEditPermissions);

        btnDelete = new Button(Constants.drives().btnDelete(), Constants.icons16().drive_delete(), new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                FileSystemService.deleteDrive(grdItems.getSelectionModel().getSelectedItem().getId(), new ComponentRequestAdapter(getThis()) {

                    @Override
                    public void onSuccess(Request request, Response response) {
                        super.onSuccess(request, response);
                        lstItems.remove(grdItems.getSelectionModel().getSelectedItem());
                    }
                });
            }
        });
        btnDelete.disable();
        bbrActions.add(btnDelete);

        lstItems = new ListStore<Drive>(DataEvents.Drives, NeptuoRoot.getUser().getId());

        List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
        columns.add(new NumberColumnConfig("id", Constants.webdesk().id(), 50));
        columns.add(new ColumnConfig("name", Constants.webdesk().name(), 200));
        columns.add(new ColumnConfig("label", Constants.drives().label(), 200));
        columns.add(new NumberColumnConfig("folderId", Constants.drives().folderId(), 50));
        columns.add(new ColumnConfig("isSystem", Constants.drives().isSystem(), 80));
        columns.add(new ColumnConfig("url", Constants.drives().url(), 100));

        grdItems = new Grid<Drive>(lstItems, new ColumnModel(columns));
        grdItems.setAutoExpandColumn("name");
        grdItems.setBorders(false);
        grdItems.setStripeRows(true);
        grdItems.getView().setForceFit(true);
        grdItems.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        grdItems.getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<Drive>() {

            @Override
            public void selectionChanged(SelectionChangedEvent<Drive> se) {
                Drive selected = grdItems.getSelectionModel().getSelectedItem();
                if (selected != null && !selected.getIsSystem() && !selected.getName().equals("Home")) {
                    btnEdit.enable();
                    btnDelete.enable();
                    btnEditPermissions.enable();
                } else if (grdItems.getSelectionModel().getSelectedItem() != null) {
                    btnEdit.disable();
                    btnEditPermissions.enable();
                    btnDelete.disable();
                } else {
                    btnEdit.disable();
                    btnEditPermissions.disable();
                    btnDelete.disable();
                }
            }
        });
        add(grdItems, new RowData(1, 1));

        loadData();
    }

    private void loadData() {
        lstItems.removeAll();
//        mask(Constants.explorer().mask());
        FileSystemService.getDrives(new AsyncCallback<Drive>() {

            @Override
            public void onSuccess(List<Drive> objects) {
                lstItems.removeAll();
                lstItems.add(objects);
                unmask();
            }

            @Override
            public void onClientError(Throwable e) {
                mask(Constants.drives().singleHeading(), e.getMessage());
            }

            @Override
            public void onServerError(ExceptionType exceptionType) {
                mask(Constants.drives().singleHeading(), exceptionType.getLocalizedMessage());
            }
        });
    }

    @Override
    public String getClassName() {
        return getClass().getName();
    }

    public MainWindow getThis() {
        return this;
    }
}

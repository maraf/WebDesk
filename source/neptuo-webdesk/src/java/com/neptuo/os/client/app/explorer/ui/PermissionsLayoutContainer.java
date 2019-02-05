/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.client.app.explorer.ui;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.neptuo.os.client.data.model.Permission;
import com.neptuo.os.client.app.explorer.data.ItemType;
import com.neptuo.os.client.data.model.FsysItem;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ButtonBar;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.neptuo.os.client.Constants;
import com.neptuo.os.client.data.model.Identity;
import com.neptuo.os.client.http.AsyncCallback;
import com.neptuo.os.client.http.ComponentRequestAdapter;
import com.neptuo.os.client.http.ExceptionType;
import com.neptuo.os.client.http.service.FileSystemService;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mara
 */
public class PermissionsLayoutContainer extends LayoutContainer {

    private FsysItem model;
    private ItemType modelType;
    private ListStore<Permission> lstPermissions;
    private Grid<Permission> grdPermissions;
    private ButtonBar bbrActions;
    private Button btnRemove;
    private Button btnAdd;

    public PermissionsLayoutContainer(FsysItem item) {
        model = item;
        modelType = item.getType().equals("folder") ? ItemType.FOLDER : ItemType.FILE;
        setLayout(new RowLayout());

        lstPermissions = new ListStore<Permission>();

        List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
        ColumnConfig typeColumn = new ColumnConfig("type", Constants.webdesk().type(), 30);
        typeColumn.setRenderer(new GridCellRenderer<Permission>() {

            @Override
            public Object render(Permission model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<Permission> store, Grid<Permission> grid) {
                return model.getIdentityType().equals("user") ? Constants.icons16().user().getHTML() : Constants.icons16().users().getHTML();
            }
        });
        columns.add(typeColumn);
        columns.add(new ColumnConfig("identityName", Constants.explorer().ccgIdentity(), 100));
        columns.add(new ColumnConfig("typeName", Constants.explorer().ccgType(), 100));

        grdPermissions = new Grid<Permission>(lstPermissions, new ColumnModel(columns));
        grdPermissions.setBorders(false);
        grdPermissions.setStripeRows(true);
        grdPermissions.getView().setForceFit(true);
        grdPermissions.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        grdPermissions.getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<Permission>() {

            @Override
            public void selectionChanged(SelectionChangedEvent<Permission> se) {
                btnRemove.setEnabled(se.getSelectedItem() != null);
            }
        });
        add(grdPermissions, new RowData(1, 1, new Margins(0, 0, 4, 0)));

        bbrActions = new ButtonBar();
        bbrActions.setAlignment(HorizontalAlignment.RIGHT);
        add(bbrActions, new RowData(1, 30));

        btnAdd = new Button(Constants.explorer().btnAdd(), new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                Window w = new NewPermissionWindow(model, lstPermissions);
                w.show();
            }
        });
        bbrActions.add(btnAdd);

        btnRemove = new Button(Constants.explorer().btnRemove(), new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                final Permission perm = grdPermissions.getSelectionModel().getSelectedItem();
                if (perm != null) {
                    mask(Constants.explorer().mask());
                    FileSystemService.removePermission(perm.getTargetId(), perm.getTypeId(), perm.getIdentityId(), modelType, new ComponentRequestAdapter(getThis()));
                    lstPermissions.remove(perm);
                }
            }
        });
        btnRemove.setEnabled(false);
        bbrActions.add(btnRemove);

        mask(Constants.explorer().mask());
        loadData();
    }

    private void loadData() {
        FileSystemService.getPermissions(model.getId(), modelType, new AsyncCallback<Permission>() {

            @Override
            public void onSuccess(List<Permission> objects) {
                lstPermissions.removeAll();
                lstPermissions.add(objects);
                unmask();
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

    private LayoutContainer getThis() {
        return this;
    }
}

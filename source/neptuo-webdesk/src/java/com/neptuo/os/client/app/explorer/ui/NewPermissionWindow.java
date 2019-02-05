/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.os.client.app.explorer.ui;

import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.neptuo.os.client.data.model.Permission;
import com.neptuo.os.client.data.model.Identity;
import com.neptuo.os.client.data.model.FsysItem;
import com.neptuo.os.client.http.service.FileSystemService;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.neptuo.os.client.Constants;
import com.neptuo.os.client.http.AsyncCallback;
import com.neptuo.os.client.http.ExceptionType;
import com.neptuo.os.client.data.model.AccessType;
import com.neptuo.os.client.http.service.AccessTypeService;
import com.neptuo.os.client.http.service.UserService;
import com.neptuo.os.client.ui.DialogWindow;
import com.neptuo.os.client.ui.event.EnterKeyListener;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mara
 */
public class NewPermissionWindow extends DialogWindow {

    private ListStore<Permission> permssions;
    private FsysItem model;
    private LayoutContainer lcnTop;
    private TextField<String> tfdLookup;
    private Button btnLookup;
    private ListStore<AccessType> lstTypes;
    private ComboBox<AccessType> cbxTypes;
    private ListStore<Identity> lstIdentities;
    private Grid<Identity> grdIdentities;
    private Button btnAdd;

    public NewPermissionWindow(FsysItem item, ListStore<Permission> perms) {
        this.model = item;
        this.permssions = perms;

        setIcon(Constants.icons16().folder());
        setHeading(Constants.explorer().winNewPermission() + " " + model.getName());
        setWidth(500);
        setHeight(300);
        setLayout(new RowLayout());

        Margins rightMargin = new Margins(0, 4, 0, 0);

        lcnTop = new LayoutContainer(new RowLayout(Orientation.HORIZONTAL));
        add(lcnTop, new RowData(1, 30, new Margins(4)));

        tfdLookup = new TextField<String>();
        tfdLookup.setEmptyText(Constants.explorer().tfdLookupEmptyText());
        tfdLookup.addKeyListener(new EnterKeyListener() {

            @Override
            public void componentEnterPress(ComponentEvent e) {
                doLookup();
            }
        });
        lcnTop.add(tfdLookup, new RowData(1, 1, rightMargin));

        btnLookup = new Button(Constants.explorer().btnLookup(), new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                doLookup();
            }
        });
        lcnTop.add(btnLookup, new RowData(-1, 1, rightMargin));

        lstTypes = new ListStore<AccessType>();

        cbxTypes = new ComboBox<AccessType>();
        cbxTypes.setStore(lstTypes);
        cbxTypes.setAllowBlank(false);
        cbxTypes.setEditable(false);
        cbxTypes.setDisplayField("name");
        cbxTypes.setTriggerAction(TriggerAction.ALL);
        lcnTop.add(cbxTypes, new RowData(-1, 1));

        lstIdentities = new ListStore<Identity>();

        List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
        ColumnConfig typeColumn = new ColumnConfig("type", Constants.webdesk().type(), 30);
        typeColumn.setRenderer(new GridCellRenderer<Identity>() {

            @Override
            public Object render(Identity model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<Identity> store, Grid<Identity> grid) {
                return model.getType().equals("user") ? Constants.icons16().user().getHTML() : Constants.icons16().users().getHTML();
            }
        });
        columns.add(typeColumn);
        columns.add(new ColumnConfig("name", Constants.webdesk().name(), 100));
        columns.add(new ColumnConfig("surname", Constants.webdesk().surname(), 100));
        columns.add(new ColumnConfig("username", Constants.webdesk().username(), 100));

        grdIdentities = new Grid<Identity>(lstIdentities, new ColumnModel(columns));
        grdIdentities.setBorders(false);
        grdIdentities.setStripeRows(true);
        grdIdentities.getView().setForceFit(true);
        grdIdentities.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        grdIdentities.getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<Identity>() {

            @Override
            public void selectionChanged(SelectionChangedEvent<Identity> se) {
                btnAdd.setEnabled(se.getSelectedItem() != null);
            }
        });
        add(grdIdentities, new RowData(1, 1));

        btnAdd = new Button(Constants.explorer().btnAdd(), new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                Identity identity = grdIdentities.getSelectionModel().getSelectedItem();
                if (cbxTypes.getSelection().size() == 1) {
                    AccessType type = cbxTypes.getSelection().get(0);
                    if (identity != null && type != null) {
                        FileSystemService.addPermission(model.getId(), type.getId(), identity.getId(), FileSystemService.getItemType(model), new AsyncCallback<Permission>() {

                            @Override
                            public void onSuccess(List<Permission> objects) {
                                permssions.add(objects);
                                unmask();
                                hide();
                            }

                            @Override
                            public void onClientError(Throwable e) {
                                grdIdentities.mask(e.getMessage());
                            }

                            @Override
                            public void onServerError(ExceptionType exceptionType) {
                                grdIdentities.mask(exceptionType.getLocalizedMessage());
                            }
                        });
                    }
                }
            }
        });
        btnAdd.setEnabled(false);
        addButton(btnAdd);

        loadTypes();
    }

    private void loadTypes() {
        AccessTypeService.get("fsys", new AsyncCallback<AccessType>() {

            @Override
            public void onSuccess(List<AccessType> objects) {
                lstTypes.removeAll();
                lstTypes.add(objects);
            }

            @Override
            public void onClientError(Throwable e) {
                getBody().mask(e.getMessage());
            }

            @Override
            public void onServerError(ExceptionType exceptionType) {
                getBody().mask(exceptionType.getLocalizedMessage());
            }
        });
    }

    private void doLookup() {
        if (tfdLookup.getValue() == null) {
            return;
        }

        grdIdentities.mask(Constants.explorer().mask());
        UserService.lookupIdentity(tfdLookup.getValue(), new AsyncCallback<Identity>() {

            @Override
            public void onSuccess(List<Identity> objects) {
                lstIdentities.removeAll();
                lstIdentities.add(objects);
                grdIdentities.unmask();
            }

            @Override
            public void onClientError(Throwable e) {
                grdIdentities.mask(e.getMessage());
            }

            @Override
            public void onServerError(ExceptionType exceptionType) {
                grdIdentities.mask(exceptionType.getLocalizedMessage());
            }
        });
    }

    @Override
    public String getClassName() {
        return getClass().getName();
    }
}

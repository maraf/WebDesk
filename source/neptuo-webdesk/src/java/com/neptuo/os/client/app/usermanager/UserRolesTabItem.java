/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.os.client.app.usermanager;

import com.neptuo.os.client.data.model.UserRole;
import com.neptuo.os.client.http.service.UserService;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.binding.FormBinding;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.HiddenField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.Response;
import com.neptuo.os.client.Constants;
import com.neptuo.os.client.NeptuoRoot;
import com.neptuo.os.client.data.ListStore;
import com.neptuo.os.client.data.databus.DataEvents;
import com.neptuo.os.client.http.AsyncCallback;
import com.neptuo.os.client.http.ExceptionType;
import com.neptuo.os.client.http.RequestCallback;
import com.neptuo.os.client.ui.ErrorLabel;
import com.neptuo.os.client.ui.Window;
import com.neptuo.os.client.ui.form.validator.NotEmptyValidator;
import com.neptuo.os.client.ui.grid.NumberColumnConfig;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mara
 */
public class UserRolesTabItem extends TabItem {

    private Window containerWindow;
    private Grid<UserRole> grdUserRoles;
    private ListStore<UserRole> lstUserRoles;
    private ListStore<UserRole> lstParentRoles;
    private FormPanel fpnUserRoles;
    private FormBinding fbdUserRoleDetail;
    private FormPanel fpnUserRoleDetail;
    private ToolBar tlbUserRoles;
    private HiddenField<String> tfdId;
    private TextField<String> tfdName;
    private ComboBox<UserRole> cbxParent;
    private Button btnAddUserRole;
    private Button btnDeleteUserRole;
    private Button btnSaveUserRole;
    private Label lblError;

    UserRolesTabItem(String tbiRoles, Window window) {
        containerWindow = window;
        setLayout(new RowLayout());
        setIcon(Constants.icons16().group());
        setText(Constants.users().tbiRoles());

        tlbUserRoles = new ToolBar();
        btnAddUserRole = new Button(Constants.users().btnAddUserRole(), Constants.icons16().group_add(), new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                fbdUserRoleDetail.unbind();
                grdUserRoles.getSelectionModel().deselectAll();
                tfdName.focus();
            }
        });
        tlbUserRoles.add(btnAddUserRole);
        tlbUserRoles.add(new SeparatorToolItem());
        btnDeleteUserRole = new Button(Constants.users().btnDeleteUserRole(), Constants.icons16().group_delete(), new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                if (grdUserRoles.getSelectionModel().getSelectedItem() != null) {
                    mask(Constants.explorer().mask());
                    UserService.deleteUserRole(grdUserRoles.getSelectionModel().getSelectedItem().getId(), new RequestCallback() {

                        @Override
                        public void onSuccess(Request request, Response response) {
                            lstUserRoles.remove(grdUserRoles.getSelectionModel().getSelectedItem());
                            btnDeleteUserRole.disable();
                            unmask();
                        }

                        @Override
                        public void onClientError(Request request, Throwable exception) {
                            lblError.setText(exception.getMessage());
                            btnDeleteUserRole.disable();
                            unmask();
                        }

                        @Override
                        public void onServerError(ExceptionType exceptionType) {
                            btnDeleteUserRole.disable();
                            lblError.setText(exceptionType.getLocalizedMessage());
                            unmask();
                        }
                    });
                }
            }
        });
        btnDeleteUserRole.disable();
        tlbUserRoles.add(btnDeleteUserRole);
        add(tlbUserRoles, new RowData(1, 30));

        lblError = new ErrorLabel();
        add(lblError, new RowData(1, 30, new Margins(4)));

        fpnUserRoles = new FormPanel();
        fpnUserRoles.setHeading(Constants.users().cpnUserRoles());
        fpnUserRoles.setLayout(new FitLayout());
        add(fpnUserRoles, new RowData(1, 1, new Margins(4)));

        lstUserRoles = new ListStore<UserRole>(DataEvents.UserRoles);

        List<ColumnConfig> userRolesColumns = new ArrayList<ColumnConfig>();
        userRolesColumns.add(new NumberColumnConfig("id", Constants.webdesk().id(), 30));
        userRolesColumns.add(new ColumnConfig("name", Constants.users().roleName(), 100));

        ColumnModel usersColumnModel = new ColumnModel(userRolesColumns);
        grdUserRoles = new Grid<UserRole>(lstUserRoles, usersColumnModel);
        grdUserRoles.setAutoExpandColumn("name");
        grdUserRoles.setBorders(false);
        grdUserRoles.setStripeRows(true);
        grdUserRoles.getView().setForceFit(true);
        grdUserRoles.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        grdUserRoles.getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<UserRole>() {

            @Override
            public void selectionChanged(SelectionChangedEvent<UserRole> se) {
                if (se.getSelection().size() > 0) {
                    UserRole model = se.getSelection().get(0);
                    for(UserRole r : lstParentRoles.getModels()) {
                        if(r.getId() == model.getParentId()) {
                            model.set("parent", r);
                        }
                    }
                    fbdUserRoleDetail.bind(model);
                    fpnUserRoleDetail.setHeading(Constants.users().fpnUserRoleDetailEdit());
                    btnDeleteUserRole.enable();
                    tfdName.focus();
                } else {
                    btnDeleteUserRole.disable();
                    fbdUserRoleDetail.unbind();
                    fpnUserRoleDetail.setHeading(Constants.users().fpnUserRoleDetailCreate());
                }
            }
        });
        fpnUserRoles.add(grdUserRoles);

        fpnUserRoleDetail = new FormPanel();
        fpnUserRoleDetail.setHeading(Constants.users().fpnUserRoleDetailCreate());
        add(fpnUserRoleDetail, new RowData(1, 150, new Margins(4)));

        tfdId = new HiddenField<String>();
        tfdId.setFieldLabel(Constants.webdesk().id());
        tfdId.setName("id");
        fpnUserRoleDetail.add(tfdId);

        tfdName = new TextField<String>();
        tfdName.setFieldLabel(Constants.users().roleName());
        tfdName.setValidator(new NotEmptyValidator(Constants.users().nameIsRequired()));
        tfdName.setName("name");
        tfdName.setMinLength(4);
        fpnUserRoleDetail.add(tfdName);

        lstParentRoles = new ListStore<UserRole>(DataEvents.UserRoles);

        cbxParent = new ComboBox<UserRole>();
        cbxParent.setFieldLabel(Constants.users().parentRole());
        cbxParent.setName("parent");
        cbxParent.setDisplayField("name");
        cbxParent.setStore(lstParentRoles);
        cbxParent.setForceSelection(true);
        cbxParent.setEditable(false);
        cbxParent.setTriggerAction(TriggerAction.ALL);
        fpnUserRoleDetail.add(cbxParent);

        btnSaveUserRole = new Button(Constants.users().btnSaveUserRole(), Constants.icons16().bullet_disk(), new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                if(!fpnUserRoleDetail.isValid())
                    return;

                UserRole parent = null;
                if (cbxParent.getSelection().size() == 1) {
                    parent = cbxParent.getSelection().get(0);
                }
                if (parent != null) {
                    mask(Constants.explorer().mask());
                    UserRole r = new UserRole();
                    if (tfdId.getValue() != null) {
                        r.setId(NeptuoRoot.Utils.parseInt(tfdId.getValue()));
                    }
                    r.setName(tfdName.getValue());
                    r.setParentId(parent.getId());
                    UserService.saveUserRole(r, new AsyncCallback<UserRole>() {

                        @Override
                        public void onSuccess(List<UserRole> objects) {
                            if (tfdId.getValue() == null) {
                                lstUserRoles.add(objects);
                                lstParentRoles.add(objects);
                            }
                            fpnUserRoleDetail.clear();
                            unmask();
                        }

                        @Override
                        public void onClientError(Throwable e) {
                            lblError.setText(e.getMessage());
                            unmask();
                        }

                        @Override
                        public void onServerError(ExceptionType exceptionType) {
                            lblError.setText(exceptionType.getLocalizedMessage());
                            unmask();
                        }
                    });
                } else {
                    lblError.setText(Constants.users().parentCantBeNull());
                }
            }
        });
        fpnUserRoleDetail.addButton(btnSaveUserRole);

        fbdUserRoleDetail = new FormBinding(fpnUserRoleDetail, true);

        loadData();
    }

    public final void loadData() {
//        lstUserRoles.removeAll();
//        lstParentRoles.removeAll();
        UserService.getUserRoles(new AsyncCallback<UserRole>() {

            @Override
            public void onSuccess(List<UserRole> roles) {
//                lstUserRoles.add(roles);
//                lstParentRoles.add(roles);
            }

            @Override
            public void onClientError(Throwable exception) {
                mask(exception.getMessage());
            }

            @Override
            public void onServerError(ExceptionType exceptionType) {
                mask(exceptionType.getLocalizedMessage());
                containerWindow.getTaskbarButton().hideLoading();
            }
        });
    }
}

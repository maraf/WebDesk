/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.os.client.app.usermanager;

import com.neptuo.os.client.data.model.User;
import com.neptuo.os.client.data.model.UserRole;
import com.neptuo.os.client.http.service.UserService;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.binding.FormBinding;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.HiddenField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.AccordionLayout;
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
import com.neptuo.os.client.http.ExceptionType;
import com.neptuo.os.client.http.RequestAdapter;
import com.neptuo.os.client.ui.Window;
import com.neptuo.os.client.ui.grid.DateColumnConfig;
import com.neptuo.os.client.ui.grid.NumberColumnConfig;
import com.neptuo.os.client.data.model.UserInRole;
import com.neptuo.os.client.http.AsyncCallback;
import com.neptuo.os.client.http.RequestCallback;
import com.neptuo.os.client.ui.ErrorLabel;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mara
 */
public class UsersTabItem extends TabItem {

    private Window containerWindow;
    private Grid<User> grdUsers;
    private ListStore<User> lstUsers;
    private ContentPanel cpnUsers;
    private ContentPanel cpnUserDetail;
    private FormBinding fbdUserDetail;
    private FormPanel fpnUserDetail;
    private ToolBar tlbUsers;
    private HiddenField<String> tfdId;
    private TextField<String> tfdName;
    private TextField<String> tfdSurname;
    private TextField<String> tfdUsername;
    private TextField<String> tfdPassword;
    private TextField<String> tfdPasswordAgain;
    private CheckBox cbxEnabled;
    private Button btnAddUser;
    private Button btnDeleteUser;
    private Button btnSaveUser;
    private Grid<UserRole> grdUserRoles;
    private ListStore<UserRole> lstUserRoles;
    private CheckBoxSelectionModel<UserRole> csmSelection;
    private ContentPanel cpnUserRoles;
    private Button btnSaveRoles;
    private List<UserRole> currentSelected;
    private Label lblError;

    public UsersTabItem(String text, Window window) {
        containerWindow = window;
        setLayout(new RowLayout());
        setIcon(Constants.icons16().user());
        setText(Constants.users().tbiUsers());

        tlbUsers = new ToolBar();
        add(tlbUsers, new RowData(1, -1));

        btnAddUser = new Button(Constants.users().btnAddUser(), Constants.icons16().user_add(), new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                fbdUserDetail.unbind();
                grdUsers.getSelectionModel().deselectAll();
                grdUserRoles.getSelectionModel().deselectAll();
                fpnUserDetail.expand();
                tfdName.focus();
            }
        });
        tlbUsers.add(btnAddUser);

        tlbUsers.add(new SeparatorToolItem());

        btnDeleteUser = new Button(Constants.users().btnDeleteUser(), Constants.icons16().user_delete(), new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                if (grdUsers.getSelectionModel().getSelectedItem() != null) {
                    mask(Constants.explorer().mask());
                    UserService.deleteUser(grdUsers.getSelectionModel().getSelectedItem().getId(), new RequestCallback() {

                        @Override
                        public void onSuccess(Request request, Response response) {
                            unmask();
                            loadData();
                        }

                        @Override
                        public void onServerError(ExceptionType exceptionType) {
                            unmask();
                            lblError.setText(exceptionType.getLocalizedMessage());
                        }

                        @Override
                        public void onClientError(Request request, Throwable exception) {
                            unmask();
                            lblError.setText(exception.getMessage());
                        }
                    });
                }
            }
        });
        btnDeleteUser.disable();
        tlbUsers.add(btnDeleteUser);

        lblError = new ErrorLabel();
        lblError.hide();
        add(lblError, new RowData(1, 30, new Margins(4)));

        cpnUsers = new ContentPanel();
        cpnUsers.setCollapsible(true);
        cpnUsers.setHeading(Constants.users().cpnUsers());
        cpnUsers.setLayout(new FitLayout());
        add(cpnUsers, new RowData(1, 200, new Margins(4)));

        lstUsers = new ListStore<User>(DataEvents.Users);

        List<ColumnConfig> usersColumns = new ArrayList<ColumnConfig>();
        usersColumns.add(new NumberColumnConfig("id", Constants.webdesk().id(), 30));
        usersColumns.add(new ColumnConfig("username", Constants.webdesk().username(), 150));
        usersColumns.add(new ColumnConfig("name", Constants.webdesk().name(), 100));
        usersColumns.add(new ColumnConfig("surname", Constants.webdesk().surname(), 100));
        usersColumns.add(new DateColumnConfig("created", Constants.webdesk().created(), 150));
        usersColumns.add(new ColumnConfig("enabled", Constants.webdesk().enabled(), 100));

        ColumnModel usersColumnModel = new ColumnModel(usersColumns);
        grdUsers = new Grid<User>(lstUsers, usersColumnModel);
        grdUsers.setBorders(false);
        grdUsers.setStripeRows(true);
        grdUsers.getView().setForceFit(true);
        grdUsers.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        grdUsers.getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<User>() {

            @Override
            public void selectionChanged(SelectionChangedEvent<User> se) {
                if (se.getSelection().size() > 0) {
                    fbdUserDetail.bind(se.getSelection().get(0));
                    fpnUserDetail.setHeading(Constants.users().fpnUserDetailEdit());
                    tfdName.focus();
                    btnDeleteUser.enable();
                    loadUserRoles(grdUsers.getSelectionModel().getSelectedItem().getId());
                } else {
                    btnDeleteUser.disable();
                    fbdUserDetail.unbind();
                    fpnUserDetail.setHeading(Constants.users().fpnUserDetailCreate());
                }
                csmSelection.deselectAll();
            }
        });
        cpnUsers.add(grdUsers);

        cpnUserDetail = new ContentPanel(new FitLayout());
        cpnUserDetail.setHeading(Constants.users().cpnUserDetail());
        cpnUserDetail.setAnimCollapse(false);
        cpnUserDetail.setLayout(new AccordionLayout());
        add(cpnUserDetail, new RowData(1, 1, new Margins(4)));

        fpnUserDetail = new FormPanel();
        fpnUserDetail.setBorders(false);
        fpnUserDetail.setBodyBorder(false);
        fpnUserDetail.setHeading(Constants.users().fpnUserDetailCreate());
        cpnUserDetail.add(fpnUserDetail);

        tfdId = new HiddenField<String>();
        tfdId.setName("id");
        tfdName = new TextField<String>();
        tfdName.setFieldLabel(Constants.webdesk().name());
        tfdName.setName("name");
        tfdName.setMinLength(2);
        tfdSurname = new TextField<String>();
        tfdSurname.setFieldLabel(Constants.webdesk().surname());
        tfdSurname.setName("surname");
        tfdSurname.setMinLength(2);
        tfdUsername = new TextField<String>();
        tfdUsername.setFieldLabel(Constants.webdesk().username());
        tfdUsername.setName("username");
        tfdUsername.setMinLength(4);
        tfdPassword = new TextField<String>();
        tfdPassword.setFieldLabel(Constants.webdesk().password());
        tfdPassword.setPassword(true);
        tfdPassword.setMinLength(4);
        tfdPasswordAgain = new TextField<String>();
        tfdPasswordAgain.setFieldLabel(Constants.webdesk().passwordAgain());
        tfdPasswordAgain.setPassword(true);
        tfdPasswordAgain.setMinLength(4);
        cbxEnabled = new CheckBox();
        cbxEnabled.setFieldLabel(Constants.webdesk().enabled());
        cbxEnabled.setName("enabled");

        btnSaveUser = new Button(Constants.users().btnSaveUser(), Constants.icons16().bullet_disk(), new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                lblError.hide();

                if(!fpnUserDetail.isValid())
                    return;

                if ((tfdId.getValue() != null && tfdPassword.getValue() == null) || (tfdPassword.getValue() != null && tfdPassword.getValue().equals(tfdPasswordAgain.getValue()))) {
                    User u = new User();
                    if (tfdId.getValue() != null && !"".equals(tfdId.getValue())) {
                        u.setId(NeptuoRoot.Utils.parseInt(tfdId.getValue()));
                    }
                    u.setName(tfdName.getValue());
                    u.setSurname(tfdSurname.getValue());
                    u.setUsername(tfdUsername.getValue());
                    u.setPassword(tfdPassword.getValue());
                    u.setEnabled(cbxEnabled.getValue());
                    mask(Constants.explorer().mask());

                    UserService.saveUser(u, new AsyncCallback<User>() {

                        @Override
                        public void onSuccess(List<User> objects) {
                            unmask();
                            fpnUserDetail.clear();
                        }

                        @Override
                        public void onClientError(Throwable e) {
                            unmask();
                            lblError.setText(e.getMessage());
                        }

                        @Override
                        public void onServerError(ExceptionType exceptionType) {
                            unmask();
                            lblError.setText(exceptionType.getLocalizedMessage());
                        }
                    });
                } else {
                    lblError.setText(Constants.users().passwordsMustMatch());
                }
            }
        });

        fpnUserDetail.add(tfdId);
        fpnUserDetail.add(tfdName);
        fpnUserDetail.add(tfdUsername);
        fpnUserDetail.add(tfdSurname);
        fpnUserDetail.add(tfdPassword);
        fpnUserDetail.add(tfdPasswordAgain);
        fpnUserDetail.add(cbxEnabled);
        fpnUserDetail.addButton(btnSaveUser);

        fbdUserDetail = new FormBinding(fpnUserDetail, true);

        cpnUserRoles = new ContentPanel();
        cpnUserRoles.setBorders(false);
        cpnUserRoles.setBodyBorder(false);
        cpnUserRoles.setHeading(Constants.users().cpnUserRolesOfUser());
        cpnUserDetail.add(cpnUserRoles);

        csmSelection = new CheckBoxSelectionModel<UserRole>();

        List<ColumnConfig> rolesColumnConfigs = new ArrayList<ColumnConfig>();
        rolesColumnConfigs.add(csmSelection.getColumn());
        rolesColumnConfigs.add(new NumberColumnConfig("id", Constants.webdesk().id(), 30));
        rolesColumnConfigs.add(new ColumnConfig("name", Constants.webdesk().name(), 100));
        ColumnModel rolesColumnModel = new ColumnModel(rolesColumnConfigs);

        lstUserRoles = new ListStore<UserRole>(DataEvents.UserRoles);

        grdUserRoles = new Grid<UserRole>(lstUserRoles, rolesColumnModel);
        grdUserRoles.setSelectionModel(csmSelection);
        grdUserRoles.setBorders(false);
        grdUserRoles.setStripeRows(true);
        grdUserRoles.getView().setForceFit(true);
        cpnUserRoles.add(grdUserRoles);

        btnSaveRoles = new Button(Constants.webdesk().save(), Constants.icons16().bullet_disk(), new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                List<UserInRole> list = new ArrayList<UserInRole>();
                for (UserRole r1 : currentSelected) {
                    boolean selected = false;
                    for (UserRole r2 : csmSelection.getSelectedItems()) {
                        if (r1.getId() == r2.getId()) {
                            selected = true;
                        }
                    }
                    if (!selected) {
                        list.add(new UserInRole(grdUsers.getSelectionModel().getSelectedItem().getId(), r1.getId()));
                    }
                }
                UserService.removeUserFromRole(list, new RequestAdapter());

                list = new ArrayList<UserInRole>();
                for (UserRole r1 : csmSelection.getSelectedItems()) {
                    boolean selected = false;
                    for (UserRole r2 : currentSelected) {
                        if (r1.getId() == r2.getId()) {
                            selected = true;
                        }
                    }
                    if (!selected) {
                        list.add(new UserInRole(grdUsers.getSelectionModel().getSelectedItem().getId(), r1.getId()));
                    }
                }
                
                UserService.addUserToRole(list, new RequestAdapter() {

                    @Override
                    public void onSuccess(Request request, Response response) {
                        loadUserRoles(grdUsers.getSelectionModel().getSelectedItem().getId());
                        unmask();
                    }
                });
                mask(Constants.explorer().mask());
            }
        });
        cpnUserRoles.addButton(btnSaveRoles);

        loadData();
    }

    public final void loadData() {
//        lstUsers.removeAll();
//        lstUserRoles.removeAll();
        UserService.getUsers(new AsyncCallback<User>() {

            @Override
            public void onSuccess(List<User> users) {
//                lstUsers.add(users);
            }

            @Override
            public void onClientError(Throwable exception) {
                mask(exception.getLocalizedMessage());
            }

            @Override
            public void onServerError(ExceptionType exceptionType) {
                mask(exceptionType.getLocalizedMessage());
            }
        });

        UserService.getUserRoles(new AsyncCallback<UserRole>() {

            @Override
            public void onClientError(Throwable exception) {
                mask(exception.getMessage());
            }

            @Override
            public void onServerError(ExceptionType exceptionType) {
                mask(exceptionType.getLocalizedMessage());
            }

            @Override
            public void onSuccess(List<UserRole> objects) {
//                lstUserRoles.add(objects);
            }
        });
    }

    public void loadUserRoles(int userId) {
        containerWindow.getTaskbarButton().showLoading();
        csmSelection.deselectAll();
        UserService.getUserRoles(userId, new AsyncCallback<UserRole>() {

            @Override
            public void onClientError(Throwable exception) {
                mask(exception.getMessage());
            }

            @Override
            public void onServerError(ExceptionType exceptionType) {
                lblError.setText(exceptionType.getLocalizedMessage());
            }

            @Override
            public void onSuccess(List<UserRole> objects) {
                currentSelected = objects;
                for (UserRole r1 : objects) {
                    for (UserRole r2 : lstUserRoles.getModels()) {
                        if (r1.getId() == r2.getId()) {
                            csmSelection.select(r2, true);
                        }
                    }
                }
            }
        });
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.client.app.usermanager.locale;

/**
 * Use the following code to "instantiate" this interface:
 * {@code UserManagerConstants i18n = GWT.create(UserManagerConstants.class);}
 * See this URL for more examples:
 * http://code.google.com/intl/en-EN/webtoolkit/doc/latest/DevGuideI18nConstants.html
 *
 * @author Mara
 */
public interface UserManagerConstants extends com.google.gwt.i18n.client.Constants {

    String heading();
    String tbiUsers();
    String tbiRoles();
    String cpnUsers();
    String fpnUserDetailEdit();
    String fpnUserDetailCreate();
    String btnDeleteUser();
    String btnAddUser();
    String btnSaveUser();
    String cpnUserRolesOfUser();

    String cpnUserRoles();
    String fpnUserRoleDetailEdit();
    String fpnUserRoleDetailCreate();
    String btnDeleteUserRole();
    String btnAddUserRole();
    String btnSaveUserRole();
    String parentRole();
    String roleName();
    String cpnPermissions();
    String btnLookup();
    String btnAddPermission();
    String btnDeletePermission();
    String tfdIdentity();
    String identity();
    String accessType();

    String passwordsMustMatch();
    String parentCantBeNull();
    String nameIsRequired();
    String nameMustHaveAtLeastLength();
    String lengthMin4();
    String lengthMin2();

    String cpnUserDetail();

}

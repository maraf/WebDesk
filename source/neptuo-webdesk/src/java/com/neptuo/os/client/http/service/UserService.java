package com.neptuo.os.client.http.service;

import com.extjs.gxt.ui.client.data.ModelType;
import com.neptuo.os.client.data.model.Identity;
import com.neptuo.os.client.data.BaseModelData;
import com.neptuo.os.client.data.model.User;
import com.neptuo.os.client.data.model.UserRole;
import com.neptuo.os.client.http.RequestCallback;
import com.neptuo.os.client.data.databus.DataEventType;
import com.neptuo.os.client.data.databus.DataEvents;
import com.neptuo.os.client.data.model.UserInRole;
import com.neptuo.os.client.http.AsyncCallback;
import com.neptuo.os.client.http.ServiceRequestBuilder;
import java.util.List;

/**
 *
 * @author Mara
 */
public class UserService {

    public static final String USER_ONLY = "USER_ONLY";
    public static final String USER_MANAGEABLE = "USER_MANAGEABLE";

    public static void getUsers(final AsyncCallback<User> callback) {
        ServiceRequestBuilder.factory(User.class)
                .setCallback(callback)
                .setDataEvent(DataEvents.Users, DataEventType.REPLACE)
                .setMethod(ServiceMethods.Core.Users.Get)
                .toRequestSent();
    }

    public static void saveUser(User u, AsyncCallback<User> callback) {
        ServiceRequestBuilder.factory(User.class)
                .setCallback(callback)
                .setDataEvent(DataEvents.Users, DataEventType.ADD)
                .setMethod(ServiceMethods.Core.Users.Save)
                .setData(u)
                .toRequestSent();
    }

    public static void deleteUser(int userId, RequestCallback callback) {
        ServiceRequestBuilder.factory(User.class)
                .setCallback(callback)
                .setDataEvent(DataEvents.Users, DataEventType.REMOVE)
                .setMethod(ServiceMethods.Core.Users.Delete)
                .setData(new User(userId))
                .toRequestSent();
    }

    public static void getUserRoles(int userId, final AsyncCallback<UserRole> callback) {
        BaseModelData bmd = new BaseModelData();
        bmd.getModelType().setRoot("userRoles");
        bmd.getModelType().addField("userId");
        bmd.set("userId", userId);

        ServiceRequestBuilder.factory(UserRole.class)
                .setCallback(callback)
                .setDataEvent(DataEvents.UserRoles, DataEventType.REPLACE, userId)
                .setMethod(ServiceMethods.Core.UserRoles.GetUserRoles)
                .setModelType(bmd.getModelType())
                .setData(bmd)
                .toRequestSent();
    }

    public static void getUserRoles(final AsyncCallback<UserRole> callback) {
        BaseModelData bmd = new BaseModelData();
        bmd.getModelType().setRoot("userRoles");
        bmd.getModelType().addField("type");
        bmd.set("type", USER_MANAGEABLE);

        ServiceRequestBuilder.factory(UserRole.class)
                .setCallback(callback)
                .setDataEvent(DataEvents.UserRoles, DataEventType.REPLACE)
                .setMethod(ServiceMethods.Core.UserRoles.GetManageable)
                .setModelType(bmd.getModelType())
                .setData(bmd)
                .toRequestSent();
    }

    public static void deleteUserRole(int id, RequestCallback callback) {
        ServiceRequestBuilder.factory(UserRole.class)
                .setCallback(callback)
                .setDataEvent(DataEvents.UserRoles, DataEventType.REMOVE)
                .setMethod(ServiceMethods.Core.UserRoles.Delete)
                .setData(new UserRole(id))
                .toRequestSent();
    }

    public static void saveUserRole(UserRole role, final AsyncCallback<UserRole> callback) {
        ServiceRequestBuilder.factory(UserRole.class)
                .setCallback(callback)
                .setDataEvent(DataEvents.UserRoles, DataEventType.ADD)
                .setMethod(ServiceMethods.Core.UserRoles.Save)
                .setData(role)
                .toRequestSent();
    }

    public static void addUserToRole(int userId, int roleId, RequestCallback callback) {
        ServiceRequestBuilder.factory()
                .setCallback(callback)
                .setDataEvent(DataEvents.UserInUserRoles, DataEventType.ADD)
                .setMethod(ServiceMethods.Core.UserRoles.AddUserToRole)
                .setData(new UserInRole(userId, roleId))
                .toRequestSent();
    }

    public static void addUserToRole(List<UserInRole> usersInRoles, RequestCallback callback) {
        if (usersInRoles.size() > 0) {
            ServiceRequestBuilder.factory()
                    .setCallback(callback)
                    .setDataEvent(DataEvents.UserInUserRoles, DataEventType.ADD)
                    .setMethod(ServiceMethods.Core.UserRoles.AddUserToRole)
                    .setData(usersInRoles)
                    .toRequestSent();
        }
    }

    public static void removeUserFromRole(int userId, int roleId, RequestCallback callback) {
        ServiceRequestBuilder.factory()
                .setCallback(callback)
                .setDataEvent(DataEvents.UserInUserRoles, DataEventType.REMOVE)
                .setMethod(ServiceMethods.Core.UserRoles.RemoveUserFromRole)
                .setData(new UserInRole(userId, roleId))
                .toRequestSent();
    }

    public static void removeUserFromRole(List<UserInRole> usersInRoles, RequestCallback callback) {
        if (usersInRoles.size() > 0) {
            ServiceRequestBuilder.factory()
                    .setCallback(callback)
                    .setDataEvent(DataEvents.UserInUserRoles, DataEventType.REMOVE)
                    .setMethod(ServiceMethods.Core.UserRoles.RemoveUserFromRole)
                    .setData(usersInRoles)
                    .toRequestSent();
        }
    }

    public static void lookupIdentity(String name, final AsyncCallback<Identity> callback) {
        Identity identity = new Identity();
        identity.setName(name);

        ModelType type = new ModelType();
        type.setRecordName(identity.getModelType().getRecordName());
        type.setRoot("identityLookup");
        type.addField("name");

        ServiceRequestBuilder.factory(Identity.class)
                .setCallback(callback)
                .setData(identity)
                .setModelType(type)
                .setMethod(ServiceMethods.Core.IdentityLookup.Lookup)
                .toRequestSent();
    }
}
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.client.data;

import com.neptuo.os.client.data.model.FsysItem;
import com.neptuo.os.client.data.model.Identity;
import com.neptuo.os.client.data.model.Permission;
import com.neptuo.os.client.data.model.User;
import com.neptuo.os.client.data.model.UserRole;
import com.neptuo.os.client.data.model.AccessType;
import com.neptuo.os.client.data.model.Property;
import com.neptuo.os.client.data.model.Drive;
import com.neptuo.os.client.data.model.Feedback;
import com.neptuo.os.client.http.ExceptionType;

/**
 *
 * @author Mara
 */
public class ObjectFactory {

    public static <T> T create(Class<T> type) {
        if(type == AccessType.class) {
            return (T) new AccessType();
        } else if(type == Identity.class) {
            return (T) new Identity();
        } else if(type == Permission.class) {
            return (T) new Permission();
        } else if(type == Property.class) {
            return (T) new Property();
        } else if(type == User.class) {
            return (T) new User();
        } else if(type == UserRole.class) {
            return (T) new UserRole();
        } else if(type == FsysItem.class) {
            return (T) new FsysItem();
        } else if(type == Drive.class) {
            return (T) new Drive();
        } else if(type == Feedback.class) {
            return (T) new Feedback();
        } else if(type == ExceptionType.class) {
            return (T) new ExceptionType();
        } else if(type == BaseModelData.class) {
            return (T) new BaseModelData();
        } else if(type == BaseTreeModelData.class) {
            return (T) new BaseTreeModelData();
        }
        return null;
    }
}

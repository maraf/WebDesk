/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.data;

/**
 *
 * @author Mara
 */
public interface UserSessionContext {

    public UserSession getSession(String token, boolean create);

    public void invalidate(String token);
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.data.session;

import com.neptuo.data.UserSession;
import com.neptuo.data.UserSessionContext;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Mara
 */
public class UserSessionContextImpl implements UserSessionContext {
    private Map<String, UserSessionImpl> sessions = new HashMap<String, UserSessionImpl>();

    @Override
    public UserSession getSession(String token, boolean create) {
        if(sessions.containsKey(token)) {
            return sessions.get(token);
        } else if(create) {
            UserSessionImpl ussi = new UserSessionImpl(token, this);
            sessions.put(token, ussi);
            return ussi;
        } else {
            return null;
        }
    }

    @Override
    public void invalidate(String token) {
        sessions.remove(token);
    }
}

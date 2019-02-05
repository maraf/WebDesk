/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.data.session;

import com.neptuo.data.UserSession;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Mara
 */
public class UserSessionImpl implements UserSession {
    private String token;
    private Map<String, Object> items = new HashMap<String, Object>();
    private UserSessionContextImpl context = null;

    public UserSessionImpl(String token, UserSessionContextImpl context) {
        this.token = token;
        this.context = context;
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public Object get(String name) {
        return items.get(name);
    }

    @Override
    public void put(String name, Object value) {
        items.put(name, value);
    }

    @Override
    public void remove(String name) {
        items.remove(name);
    }

    @Override
    public void clear() {
        items.clear();
    }

    @Override
    public Iterable<String> getKeys() {
        return items.keySet();
    }

    @Override
    public void invalidate() {
        clear();
        context.invalidate(token);
    }

}

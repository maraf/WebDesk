/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.data;

/**
 *
 * @author Mara
 */
public interface UserSession {

    public String getToken();

    public Object get(String name);

    public void put(String name, Object value);

    public void remove(String name);

    public void clear();

    public Iterable<String> getKeys();

    public void invalidate();
}

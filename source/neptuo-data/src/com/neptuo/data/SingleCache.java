/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.data;

import java.util.Map;
import javax.persistence.Query;

/**
 *
 * @author Mara
 */
public interface SingleCache {

    public Object insert(Object key, Object item);

    public Object get(Object key);

    public Map<Object, Object> get();

    public void remove(Object key);

    public int size();
    
    public boolean has(Object key);

    public Object has(Object key, Query value);
}

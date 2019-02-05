/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.data.jpa;

import com.neptuo.data.SingleCache;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import javax.persistence.Query;

/**
 *
 * @author Mara
 */
public class JpaSingleCache implements SingleCache {
    private static final Logger log = Logger.getLogger(JpaSingleCache.class.getName());

    private Map<Object, Object> cache = new HashMap<Object, Object>();

    @Override
    public Object insert(Object key, Object item) {
        cache.put(key, item);
        return item;
    }

    @Override
    public Object get(Object key) {
        return cache.get(key);
    }

    @Override
    public Map<Object, Object> get() {
        return cache;
    }

    @Override
    public void remove(Object key) {
        cache.remove(key);
    }

    @Override
    public int size() {
        return cache.size();
    }

    @Override
    public boolean has(Object key) {
        return cache.containsKey(key);
    }

    @Override
    public Object has(Object key, Query value) {
        if(has(key)) {
            return get(key);
        }
        return insert(key, value.getSingleResult());
    }
}

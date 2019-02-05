/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.service.io;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Mara
 */
public class Options {
    private Set<String> options = new HashSet<String>();

    public Options() {}

    public Options(String key) {
        add(key);
    }

    public Options(String key1, String key2) {
        add(key1);
        add(key2);
    }

    public boolean get(String key) {
        return options.contains(key) ? true : false;
    }

    public void add(String key) {
        options.add(key);
    }

    public void delete(String key) {
        options.remove(key);
    }
}

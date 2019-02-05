/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.service.io;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Mara
 */
public class TypeCache {
    private String typeName;
    private Map<String, Method> methods = new HashMap<String, Method>();

    public TypeCache(String typeName) {
        this.typeName = typeName;
    }

    public void addMethod(String name, Method method) {
        methods.put(name, method);
    }

    public Map<String, Method> getMethods() {
        return methods;
    }

    public String getTypeName() {
        return typeName;
    }
}

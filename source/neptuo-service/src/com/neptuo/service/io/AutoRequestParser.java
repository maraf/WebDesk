/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.service.io;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Mara
 */
public class AutoRequestParser implements RequestParser {

    private AutoDeserializerItem[] items;
    private String currentName;
    private Method currentMethod;
    private AutoDeserializerItem currentItem;
    private String elementContent = null;

    public AutoRequestParser(AutoDeserializerItem[] items) {
        this.items = items;
    }

    @Override
    public void startElement(String name) throws Exception {
        if (currentItem != null) {
            if (currentItem.getItemClassDeserializable() != null && name.equals(currentItem.getItemClassDeserializable().name())) {
                currentItem.setItem(currentItem.getItemClass().newInstance());
                return;
            }
            if (currentItem.getItem() != null && currentItem.getItemMethodInfo().containsKey(name)) {
                currentMethod = currentItem.getItemMethodInfo().get(name);
                return;
            }
        } else {
            for (AutoDeserializerItem item : items) {
                if (name.equals(item.getRequestInput().value()) && item.getCollectionClass() != null) {
                    item.setCollection((Collection) (item.getCollectionClass().isInterface() ? ArrayList.class.newInstance() : item.getCollectionClass().newInstance()));
                    currentItem = item;
                    return;
                }
                if (item.getCollectionClass() == null && name.equals(item.getRequestInput().value()) && !item.getItemMethodInfo().isEmpty()) {
                    item.setItem(item.getItemClass().newInstance());
                    currentItem = item;
                    return;
                }
            }
            currentName = name;
        }
    }

    @Override
    public void content(String content) throws Exception {
        if(elementContent == null) {
            elementContent = new String();
        }
        elementContent += content;
    }

    @Override
    public void endElement(String name) throws Exception {
        if (elementContent != null) {
            if (currentItem != null) {
                if (currentMethod != null && currentItem.getItem() != null) {
                    Class[] params = currentMethod.getParameterTypes();
                    if (params.length == 1) {
                        Class param = params[0];
                        Object value = convertValue(param, elementContent);
                        currentMethod.invoke(currentItem.getItem(), value);
                    }
                }
            } else {
                for (AutoDeserializerItem item : items) {
                    if (currentName.equals(item.getRequestInput().value())) {
                        Object value = convertValue(item.getItemClass(), elementContent);
                        if (value != null) {
                            item.setItem(value);
                        }
                    }
                }
            }
            elementContent = null;
        }

        if (currentItem != null) {
            if (currentItem.getItem() != null && currentItem.getItemMethodInfo().containsKey(name)) {
                currentMethod = null;
                return;
            }
            if (currentItem.getItemClassDeserializable() != null && name.equals(currentItem.getItemClassDeserializable().name())) {
                if (currentItem.getCollection() != null) {
                    currentItem.getCollection().add(currentItem.getItem());
                    currentItem.setItem(null);
                }
                return;
            }
            if (name.equals(currentItem.getRequestInput().value())) {
                currentItem = null;
                return;
            }
        } else {
            currentName = null;
        }

        for (AutoDeserializerItem item : items) {
            if (name.equals(item.getRequestInput().value())) {
                currentItem = null;
                break;
            }
            if (item.getItemClassDeserializable() != null && name.equals(item.getItemClassDeserializable().name())) {
                if (item.getCollection() != null) {
                    item.getCollection().add(item.getItem());
                    item.setItem(null);
                }
            } else if (item.getItemMethodInfo().containsKey(name)) {
                currentMethod = null;
            }
        }
    }

    private Object convertValue(Class param, String content) {
        Object value = null;
        if (param.equals(int.class) || param.equals(Integer.class)) {
            value = Integer.valueOf(content);
        } else if (param.equals(long.class) || param.equals(Long.class)) {
            value = Long.valueOf(content);
        } else if (param.equals(double.class) || param.equals(Double.class)) {
            value = Double.valueOf(content);
        } else if (param.equals(String.class)) {
            value = content;
        } else if (param.equals(boolean.class) || param.equals(Boolean.class)) {
            value = Boolean.parseBoolean(content);
        }
        return value;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.client.data;

/**
 *
 * @author Mara
 */
public class StringHelper {

    public static String getDefaultValue(String value) {
        if(value == null)
            return null;

        value = value.trim();
        if("".equals(value))
            return null;

        return value;
    }
}

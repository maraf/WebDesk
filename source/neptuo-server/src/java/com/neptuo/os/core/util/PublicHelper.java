/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.core.util;

import java.util.UUID;

/**
 *
 * @author Mara
 */
public class PublicHelper {

    public static String random() {
        return UUID.randomUUID().toString();
    }
}

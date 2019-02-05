/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.os.client.util;

import com.google.gwt.i18n.client.DateTimeFormat;
import java.util.Date;

/**
 *
 * @author Mara
 */
public class DateHelper {

    public static Date parse(String date) {
        DateTimeFormat formatter = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss.S");
        return formatter.parse(date);
    }
}

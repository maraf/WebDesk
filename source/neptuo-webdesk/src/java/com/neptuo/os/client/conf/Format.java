/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.client.conf;

import com.google.gwt.i18n.client.DateTimeFormat;
import java.util.Date;

/**
 *
 * @author Mara
 */
public class Format extends com.extjs.gxt.ui.client.util.Format {

    public static DateTimeFormat formatDate() {
        return DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss");
    }

    public static String formatDate(Date date) {
        return formatDate().format(date);
    }

    public static String formatSize(Integer value) {
        int count = 0;
        while (value > 1024) {
            value = value / 1024;
            count++;
        }
        String[] exts = {"B", "KB", "MB", "GB"};
        return value + " " + exts[count];
    }
}

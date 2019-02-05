/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.client.ui.grid;

import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.google.gwt.i18n.client.DateTimeFormat;

/**
 *
 * @author Mara
 */
public class DateColumnConfig extends ColumnConfig {

    public DateColumnConfig(String id, String name, int width) {
        super(id, name, width);
        setDateTimeFormat(DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss"));
    }

    public DateColumnConfig(String id, int width) {
        super(id, width);
    }

    public DateColumnConfig() {
        super();
    }
}

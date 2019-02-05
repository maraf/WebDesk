/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.client.ui.grid;

import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.google.gwt.i18n.client.NumberFormat;

/**
 *
 * @author Mara
 */
public class NumberColumnConfig extends ColumnConfig {

    public NumberColumnConfig(String id, String name, int width) {
        super(id, name, width);
        setNumberFormat(NumberFormat.getDecimalFormat());
    }

    public NumberColumnConfig(String id, int width) {
        super(id, width);
        setNumberFormat(NumberFormat.getDecimalFormat());
    }

    public NumberColumnConfig() {
        super();
        setNumberFormat(NumberFormat.getDecimalFormat());
    }

}

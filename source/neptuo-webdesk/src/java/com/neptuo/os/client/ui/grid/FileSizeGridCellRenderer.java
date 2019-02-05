/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.os.client.ui.grid;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.neptuo.os.client.conf.Format;
import com.neptuo.os.client.data.model.FsysItem;

/**
 *
 * @author Mara
 */
public class FileSizeGridCellRenderer implements GridCellRenderer<FsysItem> {

    @Override
    public Object render(FsysItem model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<FsysItem> store, Grid<FsysItem> grid) {
        if (!model.getType().equals("folder")) {
            return Format.formatSize(Integer.valueOf(model.getSize()).intValue());
        } else {
            return "";
        }
    }
}

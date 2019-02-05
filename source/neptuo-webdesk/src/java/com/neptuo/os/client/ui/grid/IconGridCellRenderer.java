/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.os.client.ui.grid;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.neptuo.os.client.NeptuoRoot;
import com.neptuo.os.client.app.explorer.data.FileTypeHelper;
import com.neptuo.os.client.data.BaseModelData;
import com.neptuo.os.client.data.model.Drive;
import com.neptuo.os.client.data.model.FsysItem;

/**
 *
 * @author Mara
 */
public class IconGridCellRenderer<M extends BaseModelData> implements GridCellRenderer<M> {

    @Override
    public Object render(M model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<M> store, Grid<M> grid) {
        String iconName;
        if(model instanceof Drive) {
            iconName = FileTypeHelper.getIconPath((Drive) model);
        } else {
            iconName = FileTypeHelper.getIconPath((FsysItem) model);
        }
        return "<img class='x-tree3-node-icon' src='" + NeptuoRoot.getConfiguration().icons16Path() + iconName + ".png' /><span class='x-tree3-node-text'>" + model.get(property) + "</span>";
    }
}

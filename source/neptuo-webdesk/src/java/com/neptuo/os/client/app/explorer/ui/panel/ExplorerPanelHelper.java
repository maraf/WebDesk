/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.os.client.app.explorer.ui.panel;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.neptuo.os.client.Constants;
import com.neptuo.os.client.app.explorer.data.PathHelper;
import com.neptuo.os.client.data.model.Drive;
import com.neptuo.os.client.data.model.FsysItem;
import com.neptuo.os.client.ui.grid.DateColumnConfig;
import com.neptuo.os.client.ui.grid.FileSizeGridCellRenderer;
import com.neptuo.os.client.ui.grid.IconGridCellRenderer;
import com.neptuo.os.client.ui.grid.NumberColumnConfig;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mara
 */
public class ExplorerPanelHelper {

    public static BorderLayoutData createBorderWest() {
        BorderLayoutData westData = new BorderLayoutData(LayoutRegion.WEST, 150);
        westData.setSplit(true);
        westData.setMaxSize(300);
//        westData.setCollapsible(false);
        westData.setCollapsible(true);
        westData.setFloatable(true);
        westData.setMargins(new Margins(0, 5, 0, 0));

        return westData;
    }

    public static BorderLayoutData createBorderCenter() {
        BorderLayoutData centerData = new BorderLayoutData(LayoutRegion.CENTER);
        centerData.setMargins(new Margins(0));
        
        return centerData;
    }

    public static ColumnModel createMainGridColumns() {
        List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
        ColumnConfig ccgId = new NumberColumnConfig("id", Constants.webdesk().id(), 30);
        ccgId.setHidden(true);
        columns.add(ccgId);

        ColumnConfig ccgName = new ColumnConfig("name", Constants.explorer().name(), 200);
        ccgName.setRenderer(new IconGridCellRenderer());
        columns.add(ccgName);

        ColumnConfig ccgSize = new ColumnConfig("size", Constants.explorer().size(), 50);
        ccgSize.setRenderer(new FileSizeGridCellRenderer());
        columns.add(ccgSize);
        columns.add(new ColumnConfig("type", Constants.explorer().type(), 50));
        columns.add(new DateColumnConfig("created", Constants.explorer().created(), 80));

        ColumnConfig ccgOwner = new ColumnConfig("ownerDisplayName", Constants.explorer().owner(), 100);
        ccgOwner.setHidden(true);
        columns.add(ccgOwner);

        ColumnConfig ccgPath = new ColumnConfig("path", Constants.explorer().path(), 100);
        ccgPath.setHidden(true);
        columns.add(ccgPath);

        ColumnConfig ccgModified = new DateColumnConfig("modified", Constants.explorer().modified(), 100);
        ccgModified.setHidden(true);
        columns.add(ccgModified);

        ColumnModel columnModel = new ColumnModel(columns);
        return columnModel;
    }

    public static void applyItemsSetupDrives(String path, MainContentPanel mcpContent, NavigateBar nbrNavigate) {
        String drivePath = PathHelper.drivePath(path);
        for (Drive item : mcpContent.getDrivesListStore().getModels()) {
            if((item.getName() + PathHelper.DRIVE_SEPARATOR).equals(drivePath)) {
                mcpContent.getDrivesGrid().getSelectionModel().select(item, false);
            }
        }
    }

    public static void applyItemsSetupNextSelect(MainContentPanel mcpContent, List<FsysItem> items, String nextFileName) {
        FsysItem nextSelected = null;
        for (FsysItem item : items) {
            if (nextFileName != null && item.getName().equals(nextFileName)) {
                nextSelected = item;
            }
        }
        if (nextSelected != null) {
            mcpContent.getContentGrid().getSelectionModel().select(nextSelected, false);
        }
    }

    public static void applyItemsSetupButtons(MainContentPanel mcpContent, ActionsBar abrActions) {
        if (mcpContent.getContentListStore().getSourceFolderId() != null) {
            abrActions.getCreateButton().enable();
            abrActions.getCopyButton().enable();
        } else {
            abrActions.getCreateButton().disable();
            abrActions.getCopyButton().disable();
        }
    }
}

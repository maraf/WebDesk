/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.client.app.explorer.ui.panel;

import com.neptuo.os.client.app.explorer.data.FileSystemStoreStorter;
import com.extjs.gxt.ui.client.Style.*;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.grid.*;
import com.extjs.gxt.ui.client.widget.layout.*;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.neptuo.os.client.Constants;
import com.neptuo.os.client.NeptuoRoot;
import com.neptuo.os.client.data.*;
import com.neptuo.os.client.data.databus.DataEvents;
import com.neptuo.os.client.data.model.*;
import com.neptuo.os.client.ui.grid.IconGridCellRenderer;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mara
 */
public class MainContentPanel extends ContentPanel {

    protected ContentPanel cpnContent;
    protected Grid<FsysItem> grdContent;
    protected FileSystemListStore lstItems;
    protected ContentPanel cpnDrives;
    protected Grid<Drive> grdDrives;
    protected ListStore<Drive> lstDrives;

    public MainContentPanel() {
        addStyleName("x-explorer-main");
        setLayout(new BorderLayout());
        setBorders(false);
        setBodyBorder(false);
        setHeaderVisible(false);

        cpnDrives = new ContentPanel();
        cpnDrives.addStyleName("x-drives-panel");
        cpnDrives.setBorders(false);
        cpnDrives.setHeading(Constants.drives().heading());
        cpnDrives.setLayout(new FitLayout());
        add(cpnDrives, ExplorerPanelHelper.createBorderWest());

        List<ColumnConfig> drivesColumns = new ArrayList<ColumnConfig>();
        ColumnConfig ccgDriveName = new ColumnConfig("name", Constants.drives().heading(), 100);
        ccgDriveName.setRenderer(new IconGridCellRenderer());
        drivesColumns.add(ccgDriveName);

        lstDrives = new ListStore<Drive>(DataEvents.Drives, NeptuoRoot.getUser().getId());

        grdDrives = new Grid<Drive>(lstDrives, new ColumnModel(drivesColumns));
        grdDrives.setAutoExpandColumn("name");
        grdDrives.setHideHeaders(true);
        grdDrives.setBorders(false);
        grdDrives.setStripeRows(true);
        grdDrives.getView().setForceFit(true);
        grdDrives.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        cpnDrives.add(grdDrives);

        cpnContent = new ContentPanel(new FitLayout());
        cpnContent.setHeaderVisible(false);
        add(cpnContent, ExplorerPanelHelper.createBorderCenter());

        lstItems = new FileSystemListStore();
        lstItems.setStoreSorter(new FileSystemStoreStorter());
        lstItems.setDefaultSort("name", SortDir.ASC);

        grdContent = new Grid<FsysItem>(lstItems, ExplorerPanelHelper.createMainGridColumns());
        grdContent.setAutoExpandColumn("name");
        grdContent.setBorders(false);
        grdContent.setStripeRows(true);
        grdContent.getView().setForceFit(true);
        grdContent.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        cpnContent.add(grdContent, ExplorerPanelHelper.createBorderCenter());
    }

    public ContentPanel getContentPanel() {
        return cpnContent;
    }

    public ContentPanel getDrivesPanel() {
        return cpnDrives;
    }

    public Grid<FsysItem> getContentGrid() {
        return grdContent;
    }

    public FileSystemListStore getContentListStore() {
        return lstItems;
    }

    public Grid<Drive> getDrivesGrid() {
        return grdDrives;
    }

    public ListStore<Drive> getDrivesListStore() {
        return lstDrives;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.client.app.explorer.ui.panel;

import com.extjs.gxt.ui.client.widget.Component;
import com.neptuo.os.client.app.explorer.event.ExplorerPanelListener;
import com.neptuo.os.client.data.model.FsysItem;
import java.util.List;

/**
 *
 * @author Mara
 */
public interface ExplorerPanel {

    public void setNavigateVisible(boolean visible);

    public void setActionsVisible(boolean visible);

    public void setDrivesVisible(boolean visible);

    public void setDetailsVisible(boolean visible);

    public void setLocation(String path);

    public void setLocation(FsysItem item);

    public void refresh();

    public FsysItem getSelectedItem();

    public List<FsysItem> getSelectedItems();

    public void setFoldersOnly(boolean foldersOnly);

    public boolean getFoldersOnly();

    public void setFileNameFilter(String fileName);

    public String getFileNameFilter();

    public void setFileExtensionFilter(String fileExtension);

    public String getFileExtensionFilter();

    public void addDataStartLoading(ExplorerPanelListener listener);

    public void addDataLoaded(ExplorerPanelListener listener);

    public void addItemSelected(ExplorerPanelListener listener);

    public Component getComponent();
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.os.client.data;

import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.StoreFilter;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.neptuo.os.client.app.explorer.data.ExplorerFilterParams;
import com.neptuo.os.client.app.explorer.data.ItemType;
import com.neptuo.os.client.app.explorer.data.PathHelper;
import com.neptuo.os.client.data.databus.DataEventType;
import com.neptuo.os.client.data.databus.DataEvents;
import com.neptuo.os.client.data.model.FsysItem;
import com.neptuo.os.client.http.service.FileSystemService;
import java.util.List;

/**
 *
 * @author Mara
 */
public class FileSystemListStore extends ListStore<FsysItem> {

    private Integer sourceFolderId;
    private String sourceFolderPath;
    private boolean foldersOnly;
    private String filterName;
    private String filterExtension;

    public FileSystemListStore() {
        super(DataEvents.FsysItems);

//        filter("name", ".");
        addFilter(new StoreFilter<FsysItem>() {

            @Override
            public boolean select(Store<FsysItem> store, FsysItem parent, FsysItem item, String property) {
                return !item.getName().equals(".") && !item.getName().equals("..");
            }
        });
    }

    @Override
    protected void onDataChanged(DataEventType type, Object source, List<FsysItem> objects) {
        ExplorerFilterParams params = (ExplorerFilterParams) source;
        FsysItem dot = null;
        FsysItem doubleDot = null;

        if (!type.equals(DataEventType.REMOVE)) {
            for (FsysItem item : objects) {
                if (item.getName().equals(".") && ((sourceFolderId != null && item.getId().equals(sourceFolderId)) || (sourceFolderPath != null && item.getPath().equals(sourceFolderPath)))) {
                    dot = item;
                } else if (item.getName().equals("..")) {
                    doubleDot = item;
                }
                if (dot != null && doubleDot != null) {
                    break;
                }
            }
        }

        if (dot != null && type.equals(DataEventType.REPLACE)) {
            sourceFolderId = dot.getId();
            sourceFolderPath = dot.getPath();
            if (params.equals(foldersOnly, filterName, filterExtension)) {
                removeAll();
            }

            add(dot);
            if (doubleDot != null) {
                add(doubleDot);
            }

            for (FsysItem item : objects) {
                if (checkConditions(item)) {
                    add(item);
                }
            }
            applyFilters("tmp_property_1234");
        } else {
            for (FsysItem item : objects) {
                switch (type) {
                    case ADD:
                        if (checkConditions(item)) {
                            add(item);
                        }
                        break;
                    case UPDATE:
                        updateChanged(item);
                        break;
                    case REMOVE:
                        removeById(item.getId());
                        break;
                }
            }
        }
    }

    protected boolean checkConditions(FsysItem item) {
        if (sourceFolderId.equals(item.getParentId())) {
            ItemType type = FileSystemService.getItemType(item);
            if (foldersOnly && !type.equals(ItemType.FOLDER)) {
                return false;
            }
            if (filterName != null && type.equals(ItemType.FILE) && !item.getName().startsWith(filterName)) {
                return false;
            }
            if (filterExtension != null && type.equals(ItemType.FILE) && !item.getName().endsWith(filterExtension)) {
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    protected void updateChanged(FsysItem model) {
        if (sourceFolderId != null) {
            if (!sourceFolderId.equals(model.getParentId())) {
                remove(model);
            } else if (sourceFolderId.equals(model.getParentId())) {
                add(model);
            }
        } else {
            update(model);
        }
    }

    protected void removeById(Integer id) {
        FsysItem item = findModel("id", id);
        if (item != null) {
            remove(item);
        }
    }

    public Integer getSourceFolderId() {
        return sourceFolderId;
    }

    public void setSourceFolderId(Integer sourceFolderId) {
        this.sourceFolderId = sourceFolderId;
    }

    public String getSourceFolderPath() {
        return sourceFolderPath;
    }

    public void setSourceFolderPath(String sourceFolderPath) {
        this.sourceFolderPath = sourceFolderPath;
    }

    public String getFilterExtension() {
        return filterExtension;
    }

    public void setFilterExtension(String filterExtension) {
        this.filterExtension = filterExtension;
    }

    public String getFilterName() {
        return filterName;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    public boolean isFoldersOnly() {
        return foldersOnly;
    }

    public void setFoldersOnly(boolean foldersOnly) {
        this.foldersOnly = foldersOnly;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.client.app.explorer.data;

import com.neptuo.os.client.data.BaseModelData;
import com.neptuo.os.client.data.DataType;

/**
 *
 * @author Mara
 */
public class ExplorerFilterParams extends BaseModelData {

    public ExplorerFilterParams() {
        propMap.put("foldersOnly", DataType.BOOLEAN);

        modelType.setRecordName("explorerFilterParams");
        modelType.addField("foldersOnly");
        modelType.addField("filterName");
        modelType.addField("filterExtension");
    }

    public ExplorerFilterParams(boolean foldersOnly, String filterName, String filterExtension) {
        this();
        setFoldersOnly(foldersOnly);
        setFilterName(filterName);
        setFilterExtension(filterExtension);
    }

    public void setFoldersOnly(boolean foldersOnly) {
        set("foldersOnly", foldersOnly);
    }

    public Boolean getFoldersOnly() {
        return get("foldersOnly");
    }

    public void setFilterName(String filterName) {
        set("filterName", filterName);
    }

    public String getFilterName() {
        return get("filterName");
    }

    public void setFilterExtension(String filterExtension) {
        set("filterExtension", filterExtension);
    }

    public String getFilterExtension() {
        return get("filterExtension");
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null || !(obj instanceof ExplorerFilterParams))
            return false;

        ExplorerFilterParams other = (ExplorerFilterParams) obj;
        return equals(getFoldersOnly(), getFilterName(), getFilterExtension());
    }

    public boolean equals(boolean foldersOnly, String filterName, String filterExtension) {
        return foldersOnly == getFoldersOnly()
                && (filterName == null ? getFilterName() == null : filterName.equals(getFilterName()))
                && (filterExtension == null ? getFilterExtension() == null : filterExtension.equals(getFilterExtension()));
    }
}

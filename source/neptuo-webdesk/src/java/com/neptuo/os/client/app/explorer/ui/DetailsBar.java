/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.os.client.app.explorer.ui;

import com.extjs.gxt.ui.client.core.XTemplate;
import com.extjs.gxt.ui.client.util.Util;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.neptuo.os.client.Constants;
import com.neptuo.os.client.app.explorer.data.ItemType;
import com.neptuo.os.client.conf.Format;
import com.neptuo.os.client.data.model.FsysItem;
import com.neptuo.os.client.http.service.FileSystemService;

/**
 *
 * @author Mara
 */
public class DetailsBar extends LayoutContainer {

    XTemplate fileTemplate = XTemplate.create(getFileTemplate());
    XTemplate folderTemplate = XTemplate.create(getFolderTemplate());

    public DetailsBar() {
        setHeight(58);
        addStyleName("x-explorer-details");
        setBorders(true);
    }

    public void setItem(FsysItem item) {
        item.set("formatedCreated", Format.formatDate(item.getCreated()));
        item.set("formatedModified", Format.formatDate(item.getModified()));
        if(FileSystemService.getItemType(item).equals(ItemType.FOLDER)) {
            folderTemplate.overwrite(getElement(), Util.getJsObject(item));
        } else {
            item.set("formatedSize", Format.fileSize(item.getSize()));
            fileTemplate.overwrite(getElement(), Util.getJsObject(item));
        }
    }

    protected String getFileTemplate() {
        return ""
        + "<div>"
            + "<table>"
                + "<tr>"
                    + "<td><strong>" + Constants.webdesk().id() + ":</strong> {id}</td>"
                    + "<td><strong>" + Constants.explorer().path() + ":</strong> {path}</td>"
                    + "<td><strong>" + Constants.explorer().size() + ":</strong> {formatedSize}</td>"
                + "</tr>"
                + "<tr>"
                    + "<td><strong>" + Constants.explorer().type() + ":</strong> {type}</td>"
                    + "<td><strong>" + Constants.explorer().tfdPublicId() + ":</strong> {publicId}</td>"
                    + "<td><strong>" + Constants.explorer().created() + ":</strong> {formatedCreated}</td>"
                + "</tr>"
                + "<tr>"
                    + "<td><strong>" + Constants.explorer().owner() + ":</strong> {ownerDisplayName}</td>"
                    + "<td><strong>" + Constants.explorer().cbxIsPublic() + ":</strong> {isPublic}</td>"
                    + "<td><strong>" + Constants.explorer().modified() + ":</strong> {formatedModified}</td>"
                + "</tr>"
            + "</table>"
        + "</div>";
    }

    protected String getFolderTemplate() {
        return ""
        + "<div>"
            + "<table>"
                + "<tr>"
                    + "<td><strong>" + Constants.webdesk().id() + ":</strong> {id}</td>"
                    + "<td colspan='2'><strong>" + Constants.explorer().path() + ":</strong> {path}</td>"
                + "</tr>"
                + "<tr>"
                    + "<td><strong>" + Constants.explorer().type() + ":</strong> {type}</td>"
                    + "<td><strong>" + Constants.explorer().tfdPublicId() + ":</strong> {publicId}</td>"
                    + "<td><strong>" + Constants.explorer().created() + ":</strong> {formatedCreated}</td>"
                + "</tr>"
                + "<tr>"
                    + "<td colspan='2'><strong>" + Constants.explorer().cbxIsPublic() + ":</strong> {isPublic}</td>"
                    + "<td><strong>" + Constants.explorer().modified() + ":</strong> {formatedModified}</td>"
                + "</tr>"
            + "</table>"
        + "</div>";
    }
}

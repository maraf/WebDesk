/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.client.data.model;

/**
 *
 * @author Mara
 */
public class FolderCopyMoveItem extends CopyMoveItem {

    public FolderCopyMoveItem() {
        super();

        modelType.setRoot("folders");
        modelType.setRecordName("folder");
        modelType.addField("folderId");
    }

    public FolderCopyMoveItem(int folderId, int targetId) {
        this();
        set("folderId", folderId);
        set("targetId", targetId);
    }

}

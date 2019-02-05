/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.client.data.model;

/**
 *
 * @author Mara
 */
public class FileCopyMoveItem extends CopyMoveItem {

    public FileCopyMoveItem() {
        super();

        modelType.setRoot("files");
        modelType.setRecordName("file");
        modelType.addField("fileId");
    }

    public FileCopyMoveItem(int fileId, int targetId) {
        this();
        set("fileId", fileId);
        set("targetId", targetId);
    }

}

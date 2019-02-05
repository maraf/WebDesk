/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.fsys.service;

import com.neptuo.service.io.annotation.Deserializable;

/**
 *
 * @author Mara
 */
@Deserializable(name="folder")
public class FolderCopyMoveItem extends CopyMoveItemBase {
    private Long folderId;

    public Long getFolderId() {
        return folderId;
    }

    @Deserializable(name="folderId")
    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }
}

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
public class CopyMoveItemBase {
    private Long targetId;

    public Long getTargetId() {
        return targetId;
    }

    @Deserializable(name="targetId")
    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }
}

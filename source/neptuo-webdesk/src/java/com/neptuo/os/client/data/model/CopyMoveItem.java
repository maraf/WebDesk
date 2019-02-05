/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.client.data.model;

import com.neptuo.os.client.data.BaseModelData;

/**
 *
 * @author Mara
 */
public class CopyMoveItem extends BaseModelData {

    public CopyMoveItem() {
        modelType.addField("targetId");
    }

}

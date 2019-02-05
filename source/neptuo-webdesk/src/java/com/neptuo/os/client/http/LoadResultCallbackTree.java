/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.os.client.http;

import com.neptuo.os.client.data.BaseTreeModelData;

/**
 *
 * @author Mara
 */
public interface LoadResultCallbackTree<T extends BaseTreeModelData> {

    public void onItem(T item);
}

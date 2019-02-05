/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.client.http;

import com.neptuo.os.client.data.BaseModelData;

/**
 *
 * @author Mara
 */
public interface LoadResultCallback<T extends BaseModelData> {

    public void onItem(T item);
}

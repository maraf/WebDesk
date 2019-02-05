/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.client.http.service;

import com.neptuo.os.client.data.BaseModelData;

/**
 *
 * @author Mara
 */
public abstract class BaseGenericService<T extends BaseModelData> extends BaseService {

    public abstract T factory();
}

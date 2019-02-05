/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.client.data.databus;

import java.util.List;

/**
 *
 * @author Mara
 */
public interface DataListener<T> {

    public void changed(DataEventType type, Object source, List<T> objects);
}

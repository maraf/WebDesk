/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.client.data;

import com.extjs.gxt.ui.client.event.BaseEvent;

/**
 *
 * @author Mara
 */
public class ClipboardEvent<E extends ClipboardData> extends BaseEvent {
    private E data;

    public ClipboardEvent(E data) {
        super(data);
        this.data = data;
    }

    public E getData() {
        return data;
    }
}

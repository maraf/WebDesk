/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.client.data;

import com.extjs.gxt.ui.client.event.Listener;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mara
 */
public class Clipboard {
    private List<Listener<ClipboardEvent>> listeners = new ArrayList<Listener<ClipboardEvent>>();
    private ClipboardData data;

    public ClipboardData getCurrentData() {
        return data;
    }

    public <E> E getCurrentData(Class<E> clazz) {
        if(data.getClass().equals(clazz)) {
            return (E) data;
        }
        return null;
    }

    public <E extends ClipboardData> void setCurrentData(E data) {
        this.data = data;
        callListeners(data);
    }

    public void addListener(Listener<ClipboardEvent> listener) {
        listeners.add(listener);
    }

    public void removeListener(Listener<ClipboardEvent> listener) {
        listeners.remove(listener);
    }

    protected void callListeners(ClipboardData data) {
        for(Listener<ClipboardEvent> list : listeners) {
            list.handleEvent(new ClipboardEvent(data));
        }
    }
}

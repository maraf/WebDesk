/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.client.data.databus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Mara
 */
public class DataEventBus {
    private Map<DataEvent, List<DataListener>> listeners = new HashMap<DataEvent, List<DataListener>>();

    public void addListener(DataEvent event, DataListener listener) {
        if(!listeners.containsKey(event)) {
            listeners.put(event, new ArrayList<DataListener>());
        }
        listeners.get(event).add(listener);
    }

    public void removeListener(DataEvent event, DataListener listener) {
        if(listeners.containsKey(event)) {
            listeners.get(event).remove(listener);
        }
    }

    public <T> void fireEvent(DataEvent event, DataEventType type, Object source, List<T> objects) {
        if(listeners.containsKey(event)) {
            for(DataListener<T> list : listeners.get(event)) {
                list.changed(type, source, objects);
            }
        }
    }

    public <T> void fireEvent(DataEvent event, DataEventType type, Object source, T object) {
        List<T> list = new ArrayList<T>();
        list.add(object);
        fireEvent(event, type, source, list);
    }
}

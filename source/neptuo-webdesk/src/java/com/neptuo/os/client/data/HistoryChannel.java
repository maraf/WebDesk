/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.os.client.data;

import com.extjs.gxt.ui.client.widget.MessageBox;
import com.neptuo.os.client.NeptuoRoot;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mara
 */
public class HistoryChannel<E> {

    private List<E> items = new ArrayList<E>();
    private int pointer = -1;

    public HistoryChannel() {
    }

    public boolean hasNext() {
        return pointer < (items.size() - 1);
    }

    public boolean hasPrev() {
        return pointer > 0;
    }

    public E next() {
        if (hasNext()) {
            pointer++;
            NeptuoRoot.getCurrentWorkspace().getLogger().log("HistoryChannel", "Getting: " + items.get(pointer) + " from " + pointer);
            return items.get(pointer);
        }
        return null;
    }

    public E prev() {
        if (hasPrev()) {
            pointer--;
            NeptuoRoot.getCurrentWorkspace().getLogger().log("HistoryChannel", "Getting: " + items.get(pointer) + " from " + pointer);
            return items.get(pointer);
        }
        return null;
    }

    public void add(E item) {
        if (!items.isEmpty()) {
            E pre = items.get(pointer);
            //MessageBox.info("HistoryChannel", "Item: " + item + ", pre: " + pre, null);
            if (pre.equals(item)) {
                return;
            }
        }

        if (hasNext()) {
            items = (List<E>) items.subList(0, pointer + 1);
        }
        pointer++;
        NeptuoRoot.getCurrentWorkspace().getLogger().log("HistoryChannel", "Adding: " + item + " to " + pointer);
        items.add(item);
    }
    
    public void clear() {
        pointer = -1;
        items.clear();
    }
}

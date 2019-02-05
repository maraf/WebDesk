/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.os.client.data;

import com.neptuo.os.client.NeptuoRoot;
import com.neptuo.os.client.data.databus.DataEvent;
import com.neptuo.os.client.data.databus.DataEventType;
import com.neptuo.os.client.data.databus.DataListener;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mara
 */
public class ListStore<M extends BaseModelData> extends com.extjs.gxt.ui.client.store.ListStore<M> {
    private Object source;
    private DataEvent dataEvent;

    public ListStore(DataEvent event) {
        this(event, null);
    }

    public ListStore(DataEvent dataEvent, Object source) {
        this.source = source;
        this.dataEvent = dataEvent;

        NeptuoRoot.getDataBus().addListener(dataEvent, new DataListener<M>() {

            @Override
            public void changed(DataEventType type, Object source, List<M> objects) {
                onDataChanged(type, source, objects);
            }
        });
    }

    protected void onDataChanged(DataEventType type, Object source, List<M> objects) {
        if ((getSource() == null && source == null) || (getSource() != null && source != null && getSource().equals(source))) {
            switch (type) {
                case ADD:
                    onAddDataEventType(objects);
                    break;
                case REMOVE:
                    onRemoveDataEventType(objects);
                    break;
                case UPDATE:
                    onUpdateDataEventType(objects);
                    break;
                case REPLACE:
                    onReplaceDataEventType(objects);
                    break;

            }
        }
    }

    protected void onAddDataEventType(List<M> objects) {
        add(objects);
    }

    protected void onRemoveDataEventType(List<M> objects) {
        removeAll(objects);
    }

    protected void onUpdateDataEventType(List<M> objects) {
        updateAll(objects);
    }

    protected void onReplaceDataEventType(List<M> objects) {
        removeAll();
        add(objects);
    }

    public Object getSource() {
        return source;
    }

    public void setSource(Object source) {
        this.source = source;
    }

    public DataEvent getDataEvent() {
        return dataEvent;
    }

    public void setDataEvent(DataEvent dataEvent) {
        this.dataEvent = dataEvent;
    }

    @Override
    public void add(List<? extends M> model) {
        List<M> toAdd = new ArrayList<M>();
        for (M m : model) {
            if (contains(m)) {
                update(m);
            } else {
                toAdd.add(m);
            }
        }
        super.add(toAdd);
    }

    @Override
    public void add(M model) {
        if(contains(model)) {
            update(model);
        } else {
            super.add(model);
        }
    }

    public void updateAll(List<M> objects) {
        for (M bmd : objects) {
            update(bmd);
        }
    }

    public void removeAll(List<M> objects) {
        for (M bmd : objects) {
            remove(bmd);
        }
    }

    public M findModel(String property, Object value, boolean ignoreFilters) {
        if(ignoreFilters && filtersEnabled) {
            List<M> xFiltered = all;
            all = snapshot;
            M model = findModel(property, value);
            all = xFiltered;
            return model;
        } else {
            return findModel(property, value);
        }
    }
}

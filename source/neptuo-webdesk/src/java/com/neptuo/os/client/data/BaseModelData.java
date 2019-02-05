/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.os.client.data;

import com.extjs.gxt.ui.client.data.ModelType;
import com.neptuo.os.client.util.DateHelper;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Mara
 */
public class BaseModelData extends com.extjs.gxt.ui.client.data.BaseModelData {
    protected final ModelType modelType = new ModelType();
    protected final Map<String, DataType> propMap = new HashMap<String, DataType>();

    public ModelType getModelType() {
        return modelType;
    }

    protected Map<String, DataType> getFieldTypeMappings() {
        return propMap;
    }

    @Override
    public <X> X set(String property, X value) {
        if (getFieldTypeMappings().containsKey(property) && value instanceof String) {
            DataType target = getFieldTypeMappings().get(property);
            String casted = (String) value;
            if (casted != null) {
                switch (target) {
                    case INT:
                        return (X) super.set(property, Integer.valueOf(casted));
                    case LONG:
                        return (X) super.set(property, Long.valueOf(casted));
                    case DOUBLE:
                        return (X) super.set(property, Double.valueOf(casted));
                    case BOOLEAN:
                        return (X) super.set(property, Boolean.valueOf(casted));
                    case DATE:
                        return (X) super.set(property, DateHelper.parse(casted));
                    default:
                        return super.set(property, value);
                }
            } else {
                return super.set(property, value);
            }
        } else {
            return super.set(property, value);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null || !(obj instanceof BaseModelData))
            return false;

        BaseModelData other = (BaseModelData) obj;
        Object otherId = other.get("id");
        Object thisId = get("id");

        if(otherId == null || thisId == null)
            return false;
        return otherId.equals(thisId);
    }
}

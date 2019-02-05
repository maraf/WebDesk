/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.os.client.data;

import com.extjs.gxt.ui.client.data.ModelType;
import com.neptuo.os.client.conf.Format;
import com.neptuo.os.client.http.service.BaseService;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mara
 */
public class XmlWriter {

    private String result = "";

    public XmlWriter el(String name) {
        if (name != null) {
            result += "<" + name + ">";
        }
        return this;
    }

    public <E> XmlWriter el(String name, E value) {
        if (name != null && value != null) {
            result += "<" + name + ">" + Format.htmlEncode(value.toString()) + "</" + name + ">";
        }
        return this;
    }

    public XmlWriter endEl(String name) {
        if (name != null) {
            result += "</" + name + ">";
        }
        return this;
    }

    public String toXml() {
        return result;
    }

    public static XmlWriter factory() {
        return new XmlWriter();
    }

    public static <T extends BaseModelData> XmlWriter write(Class<T> type, T model) {
        List<T> models = new ArrayList<T>();
        models.add(model);
        return write(type, models);
    }

    public static <T extends BaseModelData> XmlWriter write(ModelType modelType, T model) {
        List<T> models = new ArrayList<T>();
        models.add(model);
        return write(modelType, models);
    }

    public static <T extends BaseModelData> XmlWriter write(Class<T> type, List<T> models) {
        ModelType modelType = BaseService.getModelType(type);
        return write(modelType, models);
    }

    public static <T extends BaseModelData> XmlWriter write(ModelType modelType, List<T> models) {
        XmlWriter writer = factory();

        writer.el(modelType.getRoot());
        for (T model : models) {
            writer.el(modelType.getRecordName());
            for (int i = 0; i < modelType.getFieldCount(); i++) {
                writer.el(modelType.getField(i).getName(), model.get(modelType.getField(i).getName()));
            }
            writer.endEl(modelType.getRecordName());
        }
        writer.endEl(modelType.getRoot());

        return writer;
    }
}

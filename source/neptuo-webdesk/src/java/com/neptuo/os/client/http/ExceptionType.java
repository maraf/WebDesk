/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.client.http;

import com.neptuo.os.client.data.BaseModelData;
import com.neptuo.os.client.data.DataType;

/**
 *
 * @author Mara
 */
public class ExceptionType extends BaseModelData {
    public ExceptionType() {
        propMap.put("errorCode", DataType.INT);

        modelType.setRoot("neptuo-os");
        modelType.setRecordName("exception");
        modelType.addField("className");
        modelType.addField("errorMessage");
        modelType.addField("errorCode");
    }

    public ExceptionType(String className, String message, String localizedMessage, int code) {
        setClassName(className);
        setMessage(message);
        setLocalizedMessage(localizedMessage);
        setCode(code);
    }

    public String getClassName() {
        return get("className");
    }

    public void setClassName(String className) {
        set("className", className);
    }

    public String getMessage() {
        return get("errorMessage");
    }

    public void setMessage(String message) {
        set("errorMessage", message);
    }

    public String getLocalizedMessage() {
        return get("localizedMessage");
    }

    public void setLocalizedMessage(String localizedMessage) {
        set("localizedMessage", localizedMessage);
    }

    public int getCode() {
        return (Integer) get("errorCode");
    }

    public void setCode(int code) {
        set("errorCode", code);
    }
}

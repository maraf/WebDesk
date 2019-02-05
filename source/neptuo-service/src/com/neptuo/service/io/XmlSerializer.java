/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.service.io;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Mara
 */
public class XmlSerializer implements Serializer {
    private String result = "";

    public static final String END_ELEMENT_KEY = "XmlEndElement";
    public static final String KEY_VALUE_AS_ATTRIBUTE = "XmlKeyValueAsAttribute";

    @Override
    public void writeKeyValue(String key, String value, Options options) {
        if(options.get(KEY_VALUE_AS_ATTRIBUTE)) {
            result += " " + key + "=\"" + value + "\" ";
        } else {
            result += "<" + key + ">" + value + "</" + key + ">";
        }
        if(options.get(END_ELEMENT_KEY)) {
            result += ">";
        }
    }

    @Override
    public void writeElementStart(String name, Options options) {
        result += "<" + name;
        if(!options.get(END_ELEMENT_KEY)) {
            result += ">";
        }
    }

    @Override
    public void writeElementEnd(String name, Options options) {
        result += "</" + name + ">";
    }

    @Override
    public void writeArrayStart() {
        return;
    }

    @Override
    public void writeArrayEnd() {
        return;
    }

    @Override
    public String getResult() {
        result = String.format("<neptuo-os generated=\"%s\">%s</neptuo-os>", String.valueOf(new Date().getTime()), result);
        return result;
    }

    @Override
    public void writeKeyValue(String key, String value) {
        writeKeyValue(key, value, new Options());
    }

    @Override
    public void writeKeyValue(String key, int value) {
        writeKeyValue(key, String.valueOf(value), new Options());
    }

    @Override
    public void writeKeyValue(String key, long value) {
        writeKeyValue(key, String.valueOf(value), new Options());
    }

    @Override
    public void writeKeyValue(String key, double value) {
        writeKeyValue(key, String.valueOf(value), new Options());
    }

    @Override
    public void writeKeyValue(String key, Date value) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
        writeKeyValue(key, format.format(value), new Options());
    }

    @Override
    public void writeKeyValue(String key, boolean value) {
        writeKeyValue(key, String.valueOf(value), new Options());
    }

}

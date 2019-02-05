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
public class JsonSerializer implements Serializer {
    private Type lastType = Type.NONE;
    private String result = "";

    public static final String ESCAPE_ELEMENT_KEY = "JsonEscapeElement";
    public static final String ESCAPE_SEPARATOR_KEY = "JsonEscapeSeparator";
    public static final String ESCAPE_VALUE_KEY = "JsonEscapeValue";

    @Override
    public void writeKeyValue(String key, String value, Options options) {
        if(lastType.equals(Type.EL_START)) {
            result += " { ";
        }
        if(options.get(ESCAPE_VALUE_KEY)) {
            result += "\"" + key + "\" : \"" + value + "\",";
        } else {
            result += "\"" + key + "\" : " + value + ",";
        }
        lastType = Type.KEY_VALUE;
    }

    @Override
    public void writeElementStart(String name, Options options) {
        if(options.get(ESCAPE_ELEMENT_KEY)) {
            result += "{ ";
        } else {
            result += "{ \"" + name + "\" : ";
        }
        lastType = Type.EL_START;
    }

    @Override
    public void writeElementEnd(String name, Options options) {
        if(result.endsWith(",")) {
            result = result.substring(0, result.length() - 1);
        }
        if(lastType.equals(Type.KEY_VALUE)) {
            result += " } ";
        }
        result += " } ";
        if(!options.get(ESCAPE_SEPARATOR_KEY)) {
            result += ",";
        }
        lastType = Type.EL_END;
    }

    @Override
    public void writeArrayStart() {
        result += " [ ";
        lastType = Type.ARR_START;
    }

    @Override
    public void writeArrayEnd() {
        if(result.endsWith(",")) {
            result = result.substring(0, result.length() - 1);
        }
        result += " ] ";
        lastType = Type.ARR_END;
    }

    @Override
    public String getResult() {
        if (result.endsWith(",")) {
            result = result.substring(0, result.length() - 1);
        }
//        if (!"".equals(result)) {
//            result = "{ \"neptuo-os\" : { \"generated\" : " + String.valueOf(new Date().getTime()) + ", \"data\" : " + result + " } }";
//        }
        return result;
    }

    @Override
    public void writeKeyValue(String key, String value) {
        writeKeyValue(key, value, new Options(ESCAPE_VALUE_KEY));
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
        writeKeyValue(key, format.format(value), new Options(ESCAPE_VALUE_KEY));
    }

    @Override
    public void writeKeyValue(String key, boolean value) {
        writeKeyValue(key, String.valueOf(value), new Options());
    }
}

enum Type {
    NONE, KEY_VALUE, EL_START, EL_END, ARR_START, ARR_END
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.service.io;

import java.util.Date;

/**
 *
 * @author Mara
 */
public interface Serializer {

    public void writeKeyValue(String key, String value);
    
    public void writeKeyValue(String key, int value);

    public void writeKeyValue(String key, long value);

    public void writeKeyValue(String key, double value);

    public void writeKeyValue(String key, Date value);

    public void writeKeyValue(String key, boolean value);

    public void writeKeyValue(String key, String value, Options options);

    public void writeElementStart(String name, Options options);

    public void writeElementEnd(String name, Options options);

    public void writeArrayStart();

    public void writeArrayEnd();

    public String getResult();
}

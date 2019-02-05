/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.service.result;

import com.neptuo.service.io.Serializer;

/**
 *
 * @author Mara
 */
public class SerializerResult implements Result {
    private Serializer serializer;

    public SerializerResult(Serializer serializer) {
        this.serializer = serializer;
    }

    @Override
    public String getResult() {
        return serializer.getResult();
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.service.io;

import java.io.InputStream;

/**
 *
 * @author Mara
 */
public class AutoDeserializer {

    private Deserializer deserializer;
    private InputStream input;
    private AutoDeserializerItem[] items;

    public AutoDeserializer(Deserializer deserializer, InputStream input, AutoDeserializerItem ... items) {
        this.deserializer = deserializer;
        this.input = input;
        this.items = items;
    }

    /**
     * Load objects from input stream and return instances
     * @return Load objects from input stream and return instances
     * @throws Exception
     */
    public AutoDeserializerItem[] deserialize() throws Exception {
        if (deserializer != null && input != null && items != null && items.length > 0) {
            AutoDeserializerWorker w = new AutoDeserializerWorker(input, items);
            return w.run(deserializer);
        }
        return null;
    }

    public static AutoDeserializer factory(Deserializer deserializer, InputStream input, AutoDeserializerItem ... items) {
        return new AutoDeserializer(deserializer, input, items);
    }
}


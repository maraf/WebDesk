/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.service.io;

import com.neptuo.service.io.annotation.Deserializable;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Mara
 */
public class AutoDeserializerWorker {
    private static Map<String, DeserializableClass> cache = new HashMap<String, DeserializableClass>();
    
    protected InputStream input;
    protected AutoDeserializerItem[] items;

    public AutoDeserializerWorker(InputStream input, final AutoDeserializerItem... items) throws Exception {
        this.input = input;
        this.items = items;

        for (AutoDeserializerItem item : items) {

            if (item.getItemClass() == null) {
                throw new Exception("Error getting parameter type!");
            }

            if (!cache.containsKey(item.getItemClass().getName())) {
                Deserializable classDeserializable = (Deserializable) item.getItemClass().getAnnotation(Deserializable.class);
                Map<String, Method> methodDeserializables = new HashMap<String, Method>();
                for (Method m : item.getItemClass().getMethods()) {
                    for (Annotation a : m.getAnnotations()) {
                        if (a instanceof Deserializable) {
                            Deserializable d = (Deserializable) a;
                            methodDeserializables.put(d.name(), m);
                        }
                    }
                }
                DeserializableClass dc = new DeserializableClass();
                dc.setClazz(item.getItemClass());
                dc.setClassDeserializable(classDeserializable);
                dc.setMethodDeserializables(methodDeserializables);
                cache.put(item.getItemClass().getName(), dc);

            }
            DeserializableClass dc = cache.get(item.getItemClass().getName());
            item.setItemClassDeserializable(dc.getClassDeserializable());
            item.setItemMethodInfo(dc.getMethodDeserializables());
        }
    }

    public AutoDeserializerItem[] run(Deserializer deserializer) throws Exception {
        AutoRequestParser parser = new AutoRequestParser(items);
        deserializer.setRequestParser(parser);
        deserializer.parse(input);
        return items;
    }

    class DeserializableClass {

        protected Class clazz;
        protected Deserializable classDeserializable;
        protected Map<String, Method> methodDeserializables;

        public Class getClazz() {
            return clazz;
        }

        public void setClazz(Class clazz) {
            this.clazz = clazz;
        }

        public Map<String, Method> getMethodDeserializables() {
            return methodDeserializables;
        }

        public void setMethodDeserializables(Map<String, Method> methodDeserializables) {
            this.methodDeserializables = methodDeserializables;
        }

        public Deserializable getClassDeserializable() {
            return classDeserializable;
        }

        public void setClassDeserializable(Deserializable classDeserializable) {
            this.classDeserializable = classDeserializable;
        }
    }
}

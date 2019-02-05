/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.service.io;

import com.neptuo.service.io.annotation.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mara
 */
public class AutoSerializer {

    private static final Map<Class, TypeCache> typeCache = new HashMap<Class, TypeCache>();
    private String rootName;
    private String recordName;
    private Collection listData;
    private Object objectData;
    private Serializer serializer;
    private Class clazz;

    public AutoSerializer() {
    }

    public AutoSerializer(String rootName, String recordName, Collection listData, Serializer serializer) {
        this.rootName = rootName;
        this.recordName = recordName;
        this.listData = listData;
        this.serializer = serializer;
    }

    public AutoSerializer(String rootName, Collection listData, Serializer serializer) {
        this.rootName = rootName;
        this.listData = listData;
        this.serializer = serializer;
    }

    public AutoSerializer(String recordName, Object objectData, Serializer serializer) {
        this.recordName = recordName;
        this.objectData = objectData;
        this.serializer = serializer;
    }

    public AutoSerializer(Object objectData, Serializer serializer) {
        this.objectData = objectData;
        this.serializer = serializer;
    }

    /**
     * Serialize objectData or listData to serializer
     */
    public void serialize() {
        if (objectData != null) {
            serialize(objectData);
        } else if (listData != null) {
            serialize(listData);
        } else {
            //TODO: Throw exception
        }
    }

    protected void serialize(Collection data) {
        Options o1 = new Options();
        o1.add(JsonSerializer.ESCAPE_SEPARATOR_KEY);

        serializer.writeElementStart(rootName, o1);
        serializer.writeArrayStart();
        for (Object object : listData) {
            serialize(object);
        }
        serializer.writeArrayEnd();
        serializer.writeElementEnd(rootName, o1);
    }

    protected void serialize(Object data) {
        if (clazz == null || !clazz.equals(data.getClass())) {
            clazz = data.getClass();
        }
        TypeCache cache = findCache(clazz);
        if (cache != null) {
            String usedRecordName = recordName == null ? cache.getTypeName() : recordName;
            serializer.writeElementStart(usedRecordName, new Options());

            for (String name : cache.getMethods().keySet()) {
                try {
                    serializeMethod(name, cache.getMethods().get(name), data);
                } catch (IllegalAccessException ex) {
                    //TODO: Handle exception!
                    Logger.getLogger(AutoSerializer.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalArgumentException ex) {
                    //TODO: Handle exception!
                    Logger.getLogger(AutoSerializer.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvocationTargetException ex) {
                    //TODO: Handle exception!
                    Logger.getLogger(AutoSerializer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            serializer.writeElementEnd(usedRecordName, new Options());
        }
    }

    protected void serializeMethod(String name, Method method, Object data) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Class<?> returnType = method.getReturnType();
        Object result = method.invoke(data);
        if (result != null) {
            if (returnType == String.class) {
                serializer.writeKeyValue(name, (String) result, new Options(JsonSerializer.ESCAPE_VALUE_KEY));
            } else if (returnType == Integer.class || returnType == int.class) {
                serializer.writeKeyValue(name, (Integer) result);
            } else if (returnType == Long.class || returnType == long.class) {
                serializer.writeKeyValue(name, (Long) result);
            } else if (returnType == Double.class || returnType == double.class) {
                serializer.writeKeyValue(name, (Double) result);
            } else if (returnType == Boolean.class || returnType == boolean.class) {
                serializer.writeKeyValue(name, (Boolean) result);
            } else if (returnType == Date.class) {
                serializer.writeKeyValue(name, (Date) result);
            } else if (Collection.class.isAssignableFrom(returnType)) {
                Collection collection = (Collection) result;
                if (collection != null) {
                    serialize(collection);
                }
            } else {
                serializer.writeKeyValue(name, result.toString());
            }
        } else {
            //TODO: How to handle null? Ignore it ..
//                        opt.add(JsonSerializer.ESCAPE_VALUE_KEY);
////                                s.writeKeyValue(sa.name(), "x:null", opt);
//                        s.writeKeyValue(sa.name(), "", opt);
        }
    }

    protected TypeCache findCache(Class clazz) {
        if (typeCache.containsKey(clazz)) {
            return typeCache.get(clazz);
        } else {
            TypeCache cache = null;
            Serializable seria = findClassSerializable(clazz);
            if (seria != null) {
                synchronized (typeCache) {
                    cache = new TypeCache(seria.name());
                    for (Method m : clazz.getMethods()) {
                        Serializable sa = m.getAnnotation(Serializable.class);
                        if (sa != null) {
                            cache.addMethod(sa.name(), m);
                        }
                    }
                    typeCache.put(clazz, cache);
                }
                return cache;
            } else {
                return null;
            }
        }
    }

    protected Serializable findClassSerializable(Class clazz) {
        Serializable sc = (Serializable) clazz.getAnnotation(Serializable.class);
        if (sc == null) {
            Class superClazz = clazz;
            do {
                superClazz = superClazz.getSuperclass();
                if (superClazz != null) {
                    sc = (Serializable) superClazz.getAnnotation(Serializable.class);
                } else {
                    break;
                }
            } while (sc == null);
        }
        return sc;
    }

    public Class getClazz() {
        return clazz;
    }

    public AutoSerializer setClazz(Class clazz) {
        this.clazz = clazz;
        return this;
    }

    public Collection getListData() {
        return listData;
    }

    public AutoSerializer setListData(Collection listData) {
        this.listData = listData;
        return this;
    }

    public Object getObjectData() {
        return objectData;
    }

    public AutoSerializer setObjectData(Object objectData) {
        this.objectData = objectData;
        return this;
    }

    public String getRecordName() {
        return recordName;
    }

    public AutoSerializer setRecordName(String recordName) {
        this.recordName = recordName;
        return this;
    }

    public String getRootName() {
        return rootName;
    }

    public AutoSerializer setRootName(String rootName) {
        this.rootName = rootName;
        return this;
    }

    public Serializer getSerializer() {
        return serializer;
    }

    public AutoSerializer setSerializer(Serializer serializer) {
        this.serializer = serializer;
        return this;
    }

    public static AutoSerializer factory() {
        return new AutoSerializer();
    }

    public static AutoSerializer factory(String rootName, String recordName, Collection listData, Serializer serializer) {
        return new AutoSerializer(rootName, recordName, listData, serializer);
    }

    public static AutoSerializer factory(String rootName, Collection listData, Serializer serializer) {
        return new AutoSerializer(rootName, listData, serializer);
    }

    public static AutoSerializer factory(String recordName, Object objectData, Serializer serializer) {
        return new AutoSerializer(recordName, objectData, serializer);
    }

    public static AutoSerializer factory(Object objectData, Serializer serializer) {
        return new AutoSerializer(objectData, serializer);
    }
}

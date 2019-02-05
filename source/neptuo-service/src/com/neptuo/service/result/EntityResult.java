/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.service.result;

import com.neptuo.service.io.AutoSerializer;
import com.neptuo.service.io.Serializer;

/**
 *
 * @author Mara
 */
public class EntityResult<E> implements SerializerRequiredResult {

    private Serializer serializer;
    private E entity;

    public EntityResult(E entity) {
        this.entity = entity;
    }

    @Override
    public void setSerializer(Serializer serializer) {
        this.serializer = serializer;
    }

    @Override
    public String getResult() {
        AutoSerializer.factory(entity, serializer).serialize();
        return serializer.getResult();
    }

    public E getEntity() {
        return entity;
    }
}

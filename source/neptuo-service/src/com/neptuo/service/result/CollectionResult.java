/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.service.result;

import com.neptuo.service.io.AutoSerializer;
import com.neptuo.service.io.Serializer;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Mara
 */
public class CollectionResult<E> implements SerializerRequiredResult {
    private Serializer serializer;
    private String rootName;
    private String recordName;
    private Collection<E> entities;

    public CollectionResult(String rootName, String recordName, Collection<E> entity) {
        this.rootName = rootName;
        this.recordName = recordName;
        this.entities = entity;
    }

    public CollectionResult(String rootName, Collection<E> entity) {
        this.rootName = rootName;
        this.entities = entity;
    }

    @Override
    public void setSerializer(Serializer serializer) {
        this.serializer = serializer;
    }

    @Override
    public String getResult() {
        AutoSerializer.factory(rootName, recordName, entities, serializer).serialize();
        return serializer.getResult();
    }

    public Collection<E> getEntities() {
        return entities;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.service.result;

import com.neptuo.service.io.AutoSerializer;
import com.neptuo.service.io.Serializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Mara
 */
public class MultiCollectionResult implements SerializerRequiredResult {
    private Serializer serializer;
    private String rootName;
    private String recordName;
    private Collection[] entities;

    public MultiCollectionResult(String rootName, String recordName, Collection ... entities) {
        this.rootName = rootName;
        this.recordName = recordName;
        this.entities = entities;
    }

    public MultiCollectionResult(String rootName, Collection ... entities) {
        this.rootName = rootName;
        this.entities = entities;
    }

    public void setSerializer(Serializer serializer) {
        this.serializer = serializer;
    }

    public String getResult() {
        if(entities.length > 0) {
            List list = new ArrayList();
            for(Collection col : entities) {
                list.addAll(col);
            }
            AutoSerializer.factory(rootName, recordName, list, serializer).serialize();
            return serializer.getResult();
        } else {
            return "";
        }
    }

}

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
public interface SerializerRequiredResult extends Result {

    public void setSerializer(Serializer serializer);
}

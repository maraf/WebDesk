/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.service;

import com.neptuo.service.result.*;
import com.neptuo.service.io.Serializer;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Mara
 */
public class ResultSerializer {
    private String rawResult;

    public ResultSerializer(Object result, Serializer serializer, HttpServletResponse response) {
        if (result != null) {
            if (Result.class.isAssignableFrom(result.getClass())) {
                if(SerializerRequiredResult.class.isAssignableFrom(result.getClass())) {
                    ((SerializerRequiredResult) result).setSerializer(serializer);
                }
                if(HttpRawResult.class.isAssignableFrom(result.getClass())) {
                    HttpRawResult w = (HttpRawResult) result;
                    response.setContentType(w.getContentType());
                    response.setContentLength(w.getLength());
                    for(String h : w.getHeaders()) {
                        String[] header = h.split(":", 2);
                        if(header.length == 2) {
                            response.setHeader(header[0], header[1]);
                        }
                    }
                }
                rawResult = ((Result) result).getResult();
            } else {
                //TODO: Handle other types
                rawResult = "";
            }
        } else {
            rawResult = "";
        }
    }

    public String getRawResult() {
        return rawResult;
    }
}

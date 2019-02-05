/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.service.result;

/**
 *
 * @author Mara
 */
public class HttpRawResult extends RawResult {
    private String contentType;
    private int length;
    private final String[] headers;

    public HttpRawResult(byte[] result, String contentType, int length, String ... headers) {
        super(result);
        this.contentType = contentType;
        this.length = length;
        this.headers = headers;
    }

    public String getContentType() {
        return contentType;
    }

    public int getLength() {
        return length;
    }

    public String[] getHeaders() {
        return headers;
    }
}

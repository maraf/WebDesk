/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.data;

/**
 *
 * @author Mara
 */
public class QueryBuilderException extends DataStorageException {

    public QueryBuilderException(Throwable cause) {
        super(cause);
    }

    public QueryBuilderException(String message, Throwable cause) {
        super(message, cause);
    }

    public QueryBuilderException(String message) {
        super(message);
    }

    public QueryBuilderException() {
    }
}

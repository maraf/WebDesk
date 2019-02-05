/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.data;

/**
 *
 * @author Mara
 */
public class DataStorageException extends Exception {

    public DataStorageException(Throwable cause) {
        super(cause);
    }

    public DataStorageException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataStorageException(String message) {
        super(message);
    }

    public DataStorageException() {
    }
}

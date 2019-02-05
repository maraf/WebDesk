/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.data;

/**
 *
 * @author Mara
 */
public class SystemValueException extends DataStorageException {

    public SystemValueException() {
    }

    public SystemValueException(String message) {
        super(message);
    }

    public SystemValueException(String message, Throwable cause) {
        super(message, cause);
    }

    public SystemValueException(Throwable cause) {
        super(cause);
    }
}

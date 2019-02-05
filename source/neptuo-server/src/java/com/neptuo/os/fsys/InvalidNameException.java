/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.os.fsys;

import com.neptuo.os.core.data.BaseDataException;

/**
 *
 * @author Mara
 */
public class InvalidNameException extends BaseDataException {

    public InvalidNameException() {
    }

    public InvalidNameException(String message) {
        super(message);
    }

    public InvalidNameException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidNameException(Throwable cause) {
        super(cause);
    }

    @Override
    public int getErrorCode() {
        return 716;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.core.data;

import com.neptuo.data.DataStorageException;
import com.neptuo.service.io.annotation.Serializable;

/**
 *
 * @author Mara
 */
@Serializable(name="exception")
public class BaseDataException extends DataStorageException {

    public BaseDataException() {
    }

    public BaseDataException(String msg) {
        super(msg);
    }

    public BaseDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseDataException(Throwable cause) {
        super(cause);
    }

    public int getErrorCode() {
        return 600;
    }

    @Serializable(name="errorCode")
    public int getErrorCodeOverride() {
        return getErrorCode();
    }

    @Override
    @Serializable(name="errorMessage")
    public String getMessage() {
        return super.getMessage();
    }

    @Serializable(name="className")
    public String getClassName() {
        return getClass().getName();
    }
}

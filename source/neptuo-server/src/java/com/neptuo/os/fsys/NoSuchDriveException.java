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
public class NoSuchDriveException extends BaseDataException {

    public NoSuchDriveException() {
    }

    public NoSuchDriveException(String message) {
        super(message);
    }

    public NoSuchDriveException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchDriveException(Throwable cause) {
        super(cause);
    }

    @Override
    public int getErrorCode() {
        return 705;
    }

}

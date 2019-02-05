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
public class PermissionDeniedException extends BaseDataException {

    public PermissionDeniedException() {
    }

    public PermissionDeniedException(String message) {
        super(message);
    }

    public PermissionDeniedException(String message, Throwable cause) {
        super(message, cause);
    }

    public PermissionDeniedException(Throwable cause) {
        super(cause);
    }

    @Override
    public int getErrorCode() {
        return 704;
    }
}

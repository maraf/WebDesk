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
public class UnderlayingFileSystemException extends BaseDataException {

    public UnderlayingFileSystemException() {
    }

    public UnderlayingFileSystemException(String message) {
        super(message);
    }

    public UnderlayingFileSystemException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnderlayingFileSystemException(Throwable cause) {
        super(cause);
    }

    @Override
    public int getErrorCode() {
        return 703;
    }

}

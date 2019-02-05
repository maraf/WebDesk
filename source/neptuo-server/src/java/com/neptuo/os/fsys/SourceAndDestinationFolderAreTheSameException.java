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
public class SourceAndDestinationFolderAreTheSameException extends BaseDataException {

    public SourceAndDestinationFolderAreTheSameException() {
    }

    public SourceAndDestinationFolderAreTheSameException(String message) {
        super(message);
    }

    public SourceAndDestinationFolderAreTheSameException(String message, Throwable cause) {
        super(message, cause);
    }

    public SourceAndDestinationFolderAreTheSameException(Throwable cause) {
        super(cause);
    }

    @Override
    public int getErrorCode() {
        return 718;
    }
}

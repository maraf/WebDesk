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
public class NoSuchFolderException extends BaseDataException {

    public NoSuchFolderException() {
        super();
    }

    public NoSuchFolderException(String message) {
        super(message);
    }

    public NoSuchFolderException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchFolderException(Throwable cause) {
        super(cause);
    }

    @Override
    public int getErrorCode() {
        return 706;
    }

}

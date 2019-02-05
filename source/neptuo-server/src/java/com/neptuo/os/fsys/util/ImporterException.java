/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.fsys.util;

import com.neptuo.data.DataStorageException;

/**
 *
 * @author Mara
 */
public class ImporterException extends Exception {

    public ImporterException(Throwable cause) {
        super(cause);
    }

    public ImporterException(DataStorageException ex) {
        super(ex);
    }

    public ImporterException(String message, Throwable cause) {
        super(message, cause);
    }

    public ImporterException(String message) {
        super(message);
    }

    public ImporterException() {
    }
}

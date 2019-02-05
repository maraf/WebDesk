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
public class NullUploadException extends BaseDataException {

    public NullUploadException(String message) {
        super(message);
    }

    @Override
    public int getErrorCode() {
        return 702;
    }
}

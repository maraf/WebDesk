/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.core;

import com.neptuo.os.core.data.BaseDataException;

/**
 *
 * @author Mara
 */
public class TooShortPasswordException extends BaseDataException {

    public TooShortPasswordException() {
    }

    public TooShortPasswordException(String msg) {
        super(msg);
    }

    @Override
    public int getErrorCode() {
        return 710;
    }
}
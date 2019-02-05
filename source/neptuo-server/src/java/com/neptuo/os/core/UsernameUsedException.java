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
public class UsernameUsedException extends BaseDataException {

    public UsernameUsedException() {
    }

    public UsernameUsedException(String msg) {
        super(msg);
    }

    @Override
    public int getErrorCode() {
        return 708;
    }
}

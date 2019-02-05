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
public class NameMustBeUniqueException extends BaseDataException {

    @Override
    public int getErrorCode() {
        return 701;
    }

}

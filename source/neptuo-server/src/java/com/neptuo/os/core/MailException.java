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
public class MailException extends BaseDataException {

    public MailException() {
    }

    public MailException(String msg) {
        super(msg);
    }

    public MailException(Throwable cause) {
        super(cause);
    }

    public MailException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getErrorCode() {
        return 717;
    }
}

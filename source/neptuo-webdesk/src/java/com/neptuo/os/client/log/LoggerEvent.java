/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.client.log;

import com.extjs.gxt.ui.client.event.BaseEvent;

/**
 *
 * @author Mara
 */
public class LoggerEvent extends BaseEvent {
    private LogMessage message;

    public LoggerEvent(Logger source, LogMessage message) {
        super(source);
    }

    public LogMessage getMessage() {
        return message;
    }
}

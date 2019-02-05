/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.client.log;

import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.google.gwt.core.client.GWT;
import com.neptuo.os.client.http.ExceptionType;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mara
 */
public class Logger {
    private List<Listener<LoggerEvent>> messageListeners = new ArrayList<Listener<LoggerEvent>>();
    private List<LogMessage> messages = new ArrayList<LogMessage>();

    public void log(LogMessage message) {
        messages.add(message);
        for(Listener<LoggerEvent> list : messageListeners) {
            list.handleEvent(new LoggerEvent(this, message));
        }
    }

    public void addMessageListener(Listener<LoggerEvent> list) {
        messageListeners.add(list);
    }

    public void removeMessageListener(Listener<LoggerEvent> list) {
        messageListeners.remove(list);
    }

    public void log(String title, String content) {
        log(new LogMessage(LogMessage.Level.INFO, title, content));
    }

    public void log(LogMessage.Level level, String title, String content) {
        log(new LogMessage(level, title, content));
    }

    public void log(String title, Throwable e) {
        log(new LogMessage(LogMessage.Level.ERROR, title, e.getMessage(), e));
        GWT.log(title, e);
    }

    public void log(ExceptionType e) {
        log(new LogMessage(LogMessage.Level.ERROR, e.getLocalizedMessage(), e.getClassName()));
    }

    public List<LogMessage> getMessages() {
        return messages;
    }
}

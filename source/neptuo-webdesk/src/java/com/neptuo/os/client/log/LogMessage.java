/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.client.log;

import com.extjs.gxt.ui.client.data.BaseModelData;
import java.util.Date;

/**
 *
 * @author Mara
 */
public class LogMessage extends BaseModelData {

    public enum Level {
        INFO, WARNING, ERROR
    }

    public LogMessage(Level type, String title, String content) {
        set("type", type);
        set("title", title);
        set("content", content);
        set("date", new Date());
    }

    public LogMessage(Level type, String title, String content, Throwable e) {
        this(type, title, content);
        set("throwable", e);
    }

    public Level getType() {
        return (Level) get("type");
    }

}

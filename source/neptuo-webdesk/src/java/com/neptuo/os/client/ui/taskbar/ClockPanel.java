/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.os.client.ui.taskbar;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Timer;
import com.neptuo.os.client.ui.Label;
import java.util.Date;

/**
 *
 * @author Mara
 */
public class ClockPanel extends Label {
    private Timer timer;
    private DateTimeFormat timeFormat = DateTimeFormat.getFormat("HH:mm");

    public ClockPanel() {
        addStyleName("x-clock-panel");
        setWidth(35);

        setTime();
        timer = new Timer() {

            @Override
            public void run() {
                setTime();
            }
        };
        timer.scheduleRepeating(60 * 1000);
    }

    protected void setTime() {
        setText(timeFormat.format(new Date()));
    }
}

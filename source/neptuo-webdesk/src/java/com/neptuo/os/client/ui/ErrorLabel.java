/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.client.ui;

/**
 *
 * @author Mara
 */
public class ErrorLabel extends Label {
    private boolean classicMode = false;

    public ErrorLabel(String text) {
        super(text);
        addStyleName("x-error-label");
    }

    public ErrorLabel() {
        addStyleName("x-error-label");
    }

    @Override
    public void hide() {
        if (classicMode) {
            super.hide();
        } else {
            removeStyleName("x-error-label-active");
        }
    }

    @Override
    public void show() {
        if (classicMode) {
            super.show();
        } else {
            addStyleName("x-error-label-active");
        }
    }

    @Override
    public void setText(String text) {
        super.setText(text);
        if(!"".equals(text) && text != null) {
            show();
        } else {
            hide();
        }
    }

    public boolean getClassicMode() {
        return classicMode;
    }

    public void setClassicMode(boolean classicMode) {
        if (classicMode) {
            addStyleName("x-bottom-margined");
            addStyleName("x-error-label-active");
        } else {
            removeStyleName("x-bottom-margined");
            removeStyleName("x-error-label-active");
        }
        this.classicMode = classicMode;
    }
}

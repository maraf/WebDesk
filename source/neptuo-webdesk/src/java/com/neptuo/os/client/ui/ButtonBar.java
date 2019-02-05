/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.client.ui;

import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.BoxLayout.BoxLayoutPack;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout.HBoxLayoutAlign;
import com.extjs.gxt.ui.client.widget.layout.MarginData;
import com.google.gwt.user.client.ui.Widget;

/**
 *
 * @author Mara
 */
public class ButtonBar extends LayoutContainer {

    public ButtonBar() {
        HBoxLayout layout = new HBoxLayout();
        layout.setHBoxLayoutAlign(HBoxLayoutAlign.MIDDLE);
        layout.setPack(BoxLayoutPack.START);
        setLayout(layout);
    }

    @Override
    public boolean add(Widget widget) {
        return super.add(widget, new MarginData(0, 4, 0, 0));
    }
}

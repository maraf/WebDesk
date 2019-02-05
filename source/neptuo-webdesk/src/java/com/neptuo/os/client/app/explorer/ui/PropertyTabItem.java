/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.client.app.explorer.ui;

import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.button.Button;
import java.util.List;

/**
 *
 * @author Mara
 */
public abstract class PropertyTabItem extends TabItem {

    public abstract List<Button> getButtons();
}

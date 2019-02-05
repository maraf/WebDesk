/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.client.util;

import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.neptuo.os.client.NeptuoRoot;

/**
 *
 * @author Mara
 */
public class IconHelper {

    public static AbstractImagePrototype createIcon(String name) {
        return com.extjs.gxt.ui.client.util.IconHelper.createPath(NeptuoRoot.getConfiguration().icons16Path() + name + ".png");
    }

    public static AbstractImagePrototype createIcon(String name, int width, int height) {
        return com.extjs.gxt.ui.client.util.IconHelper.createPath(NeptuoRoot.getConfiguration().icons16Path() + name + ".png", width, height);
    }

    public static AbstractImagePrototype createIcon24(String name) {
        return com.extjs.gxt.ui.client.util.IconHelper.createPath(NeptuoRoot.getConfiguration().icons24Path() + name + ".png");
    }

    public static AbstractImagePrototype createIcon24(String name, int width, int height) {
        return com.extjs.gxt.ui.client.util.IconHelper.createPath(NeptuoRoot.getConfiguration().icons24Path() + name + ".png", width, height);
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.client.app.explorer.data;

import com.neptuo.os.client.data.model.FsysItem;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.neptuo.os.client.util.IconHelper;

/**
 *
 * @author Mara
 */
public class ModelIconProvider implements com.extjs.gxt.ui.client.data.ModelIconProvider<FsysItem> {

    @Override
    public AbstractImagePrototype getIcon(FsysItem model) {
        return IconHelper.createIcon(FileTypeHelper.getIconPath(model));
    }

}

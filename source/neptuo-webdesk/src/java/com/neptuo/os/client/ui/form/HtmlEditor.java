/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.client.ui.form;

import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.form.HtmlEditor.HtmlEditorImages;

/**
 *
 * @author Mara
 */
public class HtmlEditor extends com.extjs.gxt.ui.client.widget.form.HtmlEditor {

    public HtmlEditor() {
        
    }

    @Override
    public HtmlEditorImages getImages() {
        if(images == null) {
            HtmlEditorImages hei = new HtmlEditorImages();
            hei.setBold(IconHelper.createPath("resources/images/icons/custom/tb-bold.gif"));
            hei.setFontColor(IconHelper.createPath("resources/images/icons/custom/tb-font-color.gif"));
            images = hei;
        }
        return (HtmlEditorImages) images;
    }
}

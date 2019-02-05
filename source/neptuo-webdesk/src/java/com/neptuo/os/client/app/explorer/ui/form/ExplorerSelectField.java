/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.os.client.app.explorer.ui.form;

import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.core.XDOM;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.extjs.gxt.ui.client.util.BaseEventPreview;
import com.extjs.gxt.ui.client.util.Rectangle;
import com.extjs.gxt.ui.client.widget.ComponentHelper;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.TextField.TextFieldMessages;
import com.neptuo.os.client.data.model.FsysItem;
import com.neptuo.os.client.app.explorer.event.ExplorerItemSelected;
import com.neptuo.os.client.app.explorer.data.ItemType;
import com.neptuo.os.client.app.explorer.data.PathHelper;
import com.neptuo.os.client.app.explorer.dialogs.Dialogs;
import com.neptuo.os.client.http.service.FileSystemService;

/**
 *
 * @author Mara
 */
public class ExplorerSelectField extends TextField<String> {

    public class FileUploadFieldMessages extends TextFieldMessages {

        private String browseText = GXT.MESSAGES.uploadField_browseText();

        /**
         * Returns the browse text.
         *
         * @return the browse text
         */
        public String getBrowseText() {
            return browseText;
        }

        /**
         * Sets the browse text.
         *
         * @param browseText the browse text
         */
        public void setBrowseText(String browseText) {
            this.browseText = browseText;
        }
    }
    private Button button;
    private AbstractImagePrototype buttonIcon;
    private int buttonOffset = 3;
    private BaseEventPreview focusPreview;
    private FsysItem selectedItem;
    private String path;
    private ItemType selectType;

    /**
     * Creates a new file upload field.
     */
    public ExplorerSelectField() {
        focusPreview = new BaseEventPreview();
        focusPreview.setAutoHide(false);
        messages = new FileUploadFieldMessages();
        ensureVisibilityOnSizing = true;
        setWidth(150);
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
        if (selectType == null) {
            this.selectType = PathHelper.isFile(path) ? ItemType.FILE : ItemType.FOLDER;
        }
    }

    public FsysItem getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(FsysItem selectedItem) {
        this.selectedItem = selectedItem;
        this.path = selectedItem.getPath();
        setValue(path);
    }

    public ItemType getSelectType() {
        return selectType;
    }

    public void setSelectType(ItemType selectType) {
        this.selectType = selectType;
    }

    /**
     * Returns the button icon class.
     */
    public AbstractImagePrototype getButtonIconStyle() {
        return buttonIcon;
    }

    /**
     * Returns the button offset.
     */
    public int getButtonOffset() {
        return buttonOffset;
    }

    @Override
    public FileUploadFieldMessages getMessages() {
        return (FileUploadFieldMessages) messages;
    }

    @Override
    public void onComponentEvent(ComponentEvent ce) {
        super.onComponentEvent(ce);
        switch (ce.getEventTypeInt()) {
            case Event.ONKEYDOWN:
                if (ce.getKeyCode() != KeyCodes.KEY_TAB && GXT.isFocusManagerEnabled()) {
                    focus();
                }
                break;
        }
    }

    @Override
    public void reset() {
        super.reset();
    }

    /**
     * Sets the button icon class.
     *
     * @param buttonIconStyle the button icon style
     */
    public void setButtonIcon(AbstractImagePrototype buttonIconStyle) {
        this.buttonIcon = buttonIconStyle;
    }

    /**
     * Sets the number of pixels between the input element and the browser button
     * (defaults to 3).
     */
    public void setButtonOffset(int buttonOffset) {
        this.buttonOffset = buttonOffset;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
        if (button != null) {
            button.setEnabled(!readOnly);
        }
        setEnabled(!readOnly);
    }

    @Override
    protected void afterRender() {
        super.afterRender();
        el().removeStyleName(fieldStyle);
    }

    @Override
    protected void doAttachChildren() {
        super.doAttachChildren();
        ComponentHelper.doAttach(button);
    }

    @Override
    protected void doDetachChildren() {
        super.doDetachChildren();
        ComponentHelper.doDetach(button);
    }

    @Override
    protected El getFocusEl() {
        return input;
    }

    @Override
    protected El getInputEl() {
        return input;
    }

    @Override
    protected El getStyleEl() {
        return input;
    }

    @Override
    protected void onBlur(ComponentEvent ce) {
        Rectangle rec = button.el().getBounds();
        if (rec.contains(BaseEventPreview.getLastXY())) {
            ce.stopEvent();
            return;
        }
        super.onBlur(ce);
        focusPreview.remove();
    }

    @Override
    protected void onDetach() {
        super.onDetach();
        if (focusPreview != null) {
            focusPreview.remove();
        }
    }

    @Override
    protected void onFocus(ComponentEvent ce) {
        super.onFocus(ce);
        focusPreview.add();
    }

    @Override
    protected void onRender(Element target, int index) {
        El wrap = new El(DOM.createDiv());
        wrap.addStyleName("x-form-field-wrap");
        wrap.addStyleName("x-form-file-wrap");

        input = new El(DOM.createInputText());
        input.addStyleName(fieldStyle);
        input.addStyleName("x-form-file-text");
        input.setId(XDOM.getUniqueId());
        if (GXT.isAriaEnabled()) {
            input.setTitle("File upload field");
        }

        if (GXT.isIE && target.getTagName().equals("TD")) {
            input.setStyleAttribute("position", "static");
        }

        wrap.appendChild(input.dom);

        setElement(wrap.dom, target, index);

        button = new Button(getMessages().getBrowseText());
        button.getFocusSupport().setIgnore(true);
        button.addStyleName("x-form-file-btn");
        button.setIcon(buttonIcon);
        button.render(wrap.dom);
        button.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                if (path != null) {
                    if (ItemType.FILE.equals(selectType)) {
                        Dialogs.selectFile(path, new ExplorerItemSelected() {

                            @Override
                            public void itemSelected(FsysItem item) {
                                selectedItem = item;
                                setValue(item.getPath());
                            }
                        });
                    } else {
                        Dialogs.selectFolder(path, new ExplorerItemSelected() {

                            @Override
                            public void itemSelected(FsysItem item) {
                                selectedItem = item;
                                setValue(item.getPath());
                            }
                        });
                     }
                } else {
                    if (ItemType.FILE.equals(selectType)) {
                        Dialogs.selectFile(null, new ExplorerItemSelected() {

                            @Override
                            public void itemSelected(FsysItem item) {
                                selectedItem = item;
                                setValue(item.getPath());
                            }
                        });
                    } else {
                        Dialogs.selectFolder(null, new ExplorerItemSelected() {

                            @Override
                            public void itemSelected(FsysItem item) {
                                selectedItem = item;
                                setValue(item.getPath());
                            }
                        });
                    }
                }
            }
        });

        super.onRender(target, index);
        super.setReadOnly(true);
    }

    @Override
    protected void onResize(int width, int height) {
        super.onResize(width, height);
        input.setWidth(width - button.getWidth() - buttonOffset, true);
    }
}

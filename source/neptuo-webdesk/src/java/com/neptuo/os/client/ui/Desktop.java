/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.os.client.ui;

import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.neptuo.os.client.conf.Format;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.ListView;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.neptuo.os.client.NeptuoRoot;
import com.neptuo.os.client.app.explorer.data.FileTypeHelper;
import com.neptuo.os.client.app.explorer.data.FileSystemStoreStorter;
import com.neptuo.os.client.data.FileSystemListStore;
import com.neptuo.os.client.data.model.FsysItem;
import com.neptuo.os.client.http.AsyncCallback;
import com.neptuo.os.client.http.ExceptionType;
import com.neptuo.os.client.http.service.FileSystemService;
import java.util.List;

/**
 *
 * @author Mara
 */
public class Desktop extends LayoutContainer {
    private ListView<FsysItem> lvwContent;
    private FileSystemListStore lstContent;

    public Desktop() {
        addStyleName("x-desktop");
        setLayout(new FitLayout());

        lstContent = new FileSystemListStore();
        lstContent.setSourceFolderId(NeptuoRoot.getUserHomeDrive().getFolderId());
        lstContent.setStoreSorter(new FileSystemStoreStorter());
        lstContent.setDefaultSort("name", SortDir.ASC);

        lvwContent = new ListView<FsysItem>() {

            @Override
            protected FsysItem prepareData(FsysItem model) {
                String iconName = FileTypeHelper.getIconPath(model);
                model.set("icon", NeptuoRoot.getConfiguration().icons24Path() + iconName + ".png");
                model.set("short", Format.ellipse(model.getName(), 10));
                return model;
            }
        };
        lvwContent.addStyleName("x-transparent");
        lvwContent.setBorders(false);
        lvwContent.setTemplate(getTemplate());
        lvwContent.setStore(lstContent);
        lvwContent.setItemSelector("div.x-desktop-item");
        lvwContent.addListener(Events.DoubleClick, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                FileTypeHelper.runProgram(null, lvwContent.getSelectionModel().getSelectedItem(), true);
            }

        });
        add(lvwContent);

        FileSystemService.getFolderItems(NeptuoRoot.getUserHomeDrive().getFolderId(), false, null, null, new AsyncCallback<FsysItem>() {

            @Override
            public void onSuccess(List<FsysItem> objects) {
                
            }

            @Override
            public void onClientError(Throwable e) {
                MessageBox.alert("Desktop", e.getLocalizedMessage(), null);
            }

            @Override
            public void onServerError(ExceptionType exceptionType) {
                MessageBox.alert("Desktop", exceptionType.getLocalizedMessage(), null);
            }
        });
//        setLayout(new FitLayout());
//        Image image = new Image(Services.getFullUrl(Services.Urls.FSYS_FILECONTENT + Services.Urls.FSYS_FILECONTENT_FILEID_PARAM + "11"));
//        add(image);
    }

    private native String getTemplate() /*-{
    return ['<tpl for=".">',
    '<div class="x-desktop-item">',
    '<div class="x-desktop-item-icon"><img src="{icon}" title="{name}"></div>',
    '<span class="x-editable x-desktop-item-name">{short}</span></div>',
    '</tpl>',
    '<div class="x-clear"></div>'].join("");
    }-*/;
}

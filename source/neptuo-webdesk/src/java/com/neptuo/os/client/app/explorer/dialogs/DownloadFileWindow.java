/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.client.app.explorer.dialogs;

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.FormEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.neptuo.os.client.Constants;
import com.neptuo.os.client.NeptuoRoot;
import com.neptuo.os.client.data.model.FsysItem;
import com.neptuo.os.client.app.explorer.data.FileTypeHelper;
import com.neptuo.os.client.conf.Format;
import com.neptuo.os.client.http.service.ServiceUrls;
import com.neptuo.os.client.ui.DialogWindow;
import com.neptuo.os.client.ui.Label;
import com.neptuo.os.client.util.IconHelper;

/**
 *
 * @author Mara
 */
public class DownloadFileWindow extends DialogWindow {

    private Label lblMessage;
    private Label lblFileInfo;
    private Button btnDownload;
    private Button btnClose;

    public DownloadFileWindow(final FsysItem item) {
        setWidth(550);
        setHeight(200);
        setLayout(new RowLayout(Orientation.VERTICAL));
        setIcon(IconHelper.createIcon(FileTypeHelper.getIconPath(item)));
        setHeading(Constants.explorer().winDownloadFile());

        lblMessage = new Label(Constants.explorer().notAssociatedProgramMessage());
        lblMessage.addStyleName("x-block");
        lblMessage.addStyleName("x-margined");
        add(lblMessage);

        lblFileInfo = new Label(Constants.webdesk().name() + ": " + item.getName() + " (" + item.getPath() + ")<br />" + Constants.explorer().size() + ": " + Format.formatSize(item.getSize()));
        lblFileInfo.addStyleName("x-block");
        lblFileInfo.addStyleName("x-margined");
        add(lblFileInfo);

        btnDownload = new Button(Constants.explorer().btnDownload(), IconHelper.createIcon(FileTypeHelper.getGoIconPath(item)), new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                String url = ServiceUrls.FileSystem.FileContent.toAbsolute() + ServiceUrls.FileSystem.PUBLICID_PARAM + item.getPublicId();
                NeptuoRoot.downloadFile(url, new Listener<FormEvent>() {

                    @Override
                    public void handleEvent(FormEvent be) {
                        hide();
                    }
                });
            }
        });
        addButton(btnDownload);

        btnClose = new Button(Constants.webdesk().cancel(), new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                hide();
            }
        });
        addButton(btnClose);
    }

    @Override
    public String getClassName() {
        return getClass().getName();
    }
}

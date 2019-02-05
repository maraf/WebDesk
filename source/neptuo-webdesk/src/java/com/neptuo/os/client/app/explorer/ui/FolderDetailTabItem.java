/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.client.app.explorer.ui;

import com.extjs.gxt.ui.client.Style.ButtonScale;
import com.neptuo.os.client.data.model.FsysItem;
import com.neptuo.os.client.http.ExceptionType;
import com.neptuo.os.client.http.service.FileSystemService;
import com.extjs.gxt.ui.client.binding.FormBinding;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.neptuo.os.client.Constants;
import com.neptuo.os.client.http.AsyncCallback;
import java.util.List;

/**
 *
 * @author Mara
 */
public class FolderDetailTabItem extends ExplorerItemTabItem {
    protected String oldName;

    public FolderDetailTabItem(final FsysItem item) {
        super(item);
        
        oldName = item.getName();

        btnOk.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                if (!oldName.equals(model.getName())) {
                    mask(Constants.explorer().mask());
                    model.setPath(model.getPath().replace(oldName, model.getName()));
                    oldName = model.getName();
                    FileSystemService.renameFolder(model.getId(), model.getName(), new AsyncCallback<FsysItem>() {

                        @Override
                        public void onSuccess(List<FsysItem> objects) {
                            fbdPanel.bind(model);
                            unmask();
                        }

                        @Override
                        public void onClientError(Throwable e) {
                            MessageBox.alert(item.getName(), e.getLocalizedMessage(), null);
                            unmask();
                        }

                        @Override
                        public void onServerError(ExceptionType exceptionType) {
                            MessageBox.alert(item.getName(), exceptionType.getLocalizedMessage(), null);
                            unmask();
                        }
                    });
                }
            }
        });

        fbdPanel = new FormBinding(fpnPanel, true);
        fbdPanel.bind(model);
    }
}

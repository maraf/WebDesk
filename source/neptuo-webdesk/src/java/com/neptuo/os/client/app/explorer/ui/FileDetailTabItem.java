/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.client.app.explorer.ui;

import com.extjs.gxt.ui.client.Style.ButtonScale;
import com.neptuo.os.client.data.model.FsysItem;
import com.neptuo.os.client.http.service.FileSystemService;
import com.extjs.gxt.ui.client.binding.FormBinding;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.neptuo.os.client.Constants;
import com.neptuo.os.client.app.explorer.dialogs.Dialogs;
import com.neptuo.os.client.app.explorer.event.FileUploadCallback;
import com.neptuo.os.client.http.AsyncCallback;
import com.neptuo.os.client.http.ExceptionType;
import com.neptuo.os.client.http.RequestAdapter;
import java.util.List;

/**
 *
 * @author Mara
 */
public class FileDetailTabItem extends ExplorerItemTabItem {
    protected String oldName;
    protected boolean oldIsPublic;

    protected TextField<String> tfdType;

    protected Button btnUpload;

    public FileDetailTabItem(final FsysItem item) {
        super(item);

        oldName = model.getName();
        oldIsPublic = model.getIsPublic();

        tfdType = new TextField<String>();
        tfdType.setName("type");
        tfdType.setFieldLabel(Constants.explorer().tfdType());
        tfdType.setReadOnly(true);
        fpnPanel.add(tfdType);

        btnUpload = new Button(Constants.explorer().btnFileUpload(), new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                Dialogs.uploadFile(new FileUploadCallback() {

                    @Override
                    public void onFileUploaded(String key) {
                        model.setKey(key);
                        FileSystemService.newVersionFile(model.getId(), key, new RequestAdapter());
                    }
                });
            }
        });
        bbrButtons.add(btnUpload);

        btnOk.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                if (!oldName.equals(model.getName())) {
                    mask(Constants.explorer().mask());
                    oldName = model.getName();
                    model.setPath(model.getPath().replace(oldName, model.getName()));
                    oldIsPublic = model.getIsPublic();
                    FileSystemService.renameFile(model.getId(), model.getName(), new AsyncCallback<FsysItem>() {

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

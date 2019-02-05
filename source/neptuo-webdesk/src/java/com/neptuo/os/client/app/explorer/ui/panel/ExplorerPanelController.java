/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.os.client.app.explorer.ui.panel;

import com.neptuo.os.client.data.model.FsysItem;
import com.neptuo.os.client.http.service.FileSystemService;
import com.extjs.gxt.ui.client.Style.*;
import com.extjs.gxt.ui.client.event.*;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.form.*;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Encoding;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Method;
import com.extjs.gxt.ui.client.widget.layout.*;
import com.neptuo.os.client.NeptuoRoot;
import com.neptuo.os.client.app.explorer.data.FileSystemClipboardData;
import com.neptuo.os.client.app.explorer.data.PathHelper;
import com.neptuo.os.client.app.explorer.dialogs.*;
import com.neptuo.os.client.app.explorer.ui.DetailsBar;
import com.neptuo.os.client.data.ClipboardEvent;
import com.neptuo.os.client.http.*;
import com.neptuo.os.client.data.model.Drive;
import com.neptuo.os.client.http.service.ServiceUrls;
import com.neptuo.os.client.ui.event.EnterKeyListener;
import java.util.List;

/**
 *
 * @author Mara
 */
public abstract class ExplorerPanelController extends ExplorerPanelBase {

    protected NavigateBar nbrNavigate;
    protected ActionsBar abrActions;
    protected MainContentPanel mcpContent;
    protected DetailsBar dbrDetails;

    public ExplorerPanelController() {
        setLayout(new RowLayout(Orientation.VERTICAL));

        nbrNavigate = new NavigateBar();
        nbrNavigate.getBackButton().addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                btnBack_Click(ce);
            }
        });
        nbrNavigate.getForwardButton().addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                btnForward_Click(ce);
            }
        });
        nbrNavigate.getUpButton().addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                btnUp_Click(ce);
            }
        });
        nbrNavigate.getPathTextField().addKeyListener(new EnterKeyListener() {

            @Override
            public void componentEnterPress(ComponentEvent e) {
                tfdPath_EnterPressed(e);
            }
        });
        nbrNavigate.getGoButton().addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                btnGo_Click(ce);
            }
        });
        add(nbrNavigate, new RowData(1, -1, new Margins(4)));

        abrActions = new ActionsBar();
        abrActions.getCreateFolderMenuItem().addSelectionListener(new SelectionListener<MenuEvent>() {

            @Override
            public void componentSelected(MenuEvent ce) {
                if (mcpContent.getContentListStore().getSourceFolderId() != null) {
                    mniCreateFolder_Click(ce);
                }
            }
        });
        abrActions.getCreateEmptyFileMenuItem().addSelectionListener(new SelectionListener<MenuEvent>() {

            @Override
            public void componentSelected(MenuEvent ce) {
                if (mcpContent.getContentListStore().getSourceFolderId() != null) {
                    mniCreateEmptyFile_Click(ce);
                }
            }
        });
        abrActions.getCutMenuItem().addSelectionListener(new SelectionListener<MenuEvent>() {

            @Override
            public void componentSelected(MenuEvent ce) {
                mniCut_Click(ce);
            }
        });
        abrActions.getCopyMenuItem().addSelectionListener(new SelectionListener<MenuEvent>() {

            @Override
            public void componentSelected(MenuEvent ce) {
                mniCopy_Click(ce);
            }
        });
        abrActions.getPasteMenuItem().addSelectionListener(new SelectionListener<MenuEvent>() {

            @Override
            public void componentSelected(MenuEvent ce) {
                mniPaste_Click(ce);
            }
        });

        final FormPanel fpnForm = new FormPanel();
        fpnForm.addStyleName("x-upload-hidden");
        fpnForm.setAction(ServiceUrls.FileSystem.Upload.toAbsolute());
        fpnForm.setEncoding(Encoding.MULTIPART);
        fpnForm.setMethod(Method.POST);
        add(fpnForm);
        final FileUploadField fufFile = new FileUploadField();
        fufFile.setName(FileUploadWindow.FIELD_NAME);
        fufFile.setFireChangeEventOnSetValue(true);
        fufFile.addListener(Events.Change, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                fpnForm.submit();
            }
        });
        fpnForm.add(fufFile);
        fpnForm.addListener(Events.Submit, new Listener<FormEvent>() {

            @Override
            public void handleEvent(FormEvent be) {
                showMask();
                String key = be.getResultHtml();
                key = key.toLowerCase().replace("<pre>", "").replace("</pre>", "").replace("<pre style=\"word-wrap: break-word; white-space: pre-wrap;\">", "");
                FileSystemService.createFile(loadingFolderId, PathHelper.getFileNameFromUploadField(fufFile), key, new AsyncCallback<FsysItem>() {

                    @Override
                    public void onSuccess(List<FsysItem> objects) {
                        mniFileUpload_FileUploaded(objects);
                        hideMask();
                    }

                    @Override
                    public void onClientError(Throwable e) {
                        hideMask();
                        showErrorMessage(e);
                    }

                    @Override
                    public void onServerError(ExceptionType exceptionType) {
                        hideMask();
                        showErrorMessage(exceptionType);
                    }
                });
            }
        });
        abrActions.getFileUploadMenuItem().addSelectionListener(new SelectionListener<MenuEvent>() {

            @Override
            public void componentSelected(MenuEvent ce) {
                fufFile.getFileInput().click();
            }
        });
        abrActions.getFileUploaderMenuItem().addSelectionListener(new SelectionListener<MenuEvent>() {

            @Override
            public void componentSelected(MenuEvent ce) {
                mniFileUploader_Click(ce);
            }
        });
        abrActions.getDownloadButton().addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                if (mcpContent.getContentGrid().getSelectionModel().getSelectedItem() != null) {
                    btnDownload_Click(ce);
                }
            }
        });
        abrActions.getPropertiesButton().addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                if (mcpContent.getContentGrid().getSelectionModel().getSelectedItem() != null) {
                    btnProperties_Click(ce);
                }
            }
        });
        abrActions.getDeleteButton().addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                if (mcpContent.getContentGrid().getSelectionModel().getSelectedItem() != null) {
                    btnDelete_Click(ce);
                }
            }
        });
        abrActions.getFoldersOnlyToggleButton().addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                tgbFoldersOnly_Click(ce);
            }
        });
        abrActions.getFileNameFilterTextField().addKeyListener(new EnterKeyListener() {

            @Override
            public void componentEnterPress(ComponentEvent e) {
                tfdFileNameFilter_EnterPressed(e);
            }
        });
        abrActions.getFileExtensionFilterTextField().addKeyListener(new EnterKeyListener() {

            @Override
            public void componentEnterPress(ComponentEvent e) {
                tfdFileExtensionFilter_EnterPressed(e);
            }
        });
        add(abrActions, new RowData(1, -1, new Margins(4)));

        mcpContent = new MainContentPanel();
        mcpContent.getDrivesGrid().getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<Drive>() {

            @Override
            public void selectionChanged(SelectionChangedEvent<Drive> se) {
                if (se.getSelectedItem() != null && !manualSelect) {
                    grdDrives_SelectionChanged(se);
                }
            }
        });
        mcpContent.getDrivesGrid().addListener(Events.CellDoubleClick, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                GridEvent e = (GridEvent) be;
                grdDrives_DoubleClick(e);
            }
        });
        mcpContent.getContentGrid().getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<FsysItem>() {

            @Override
            public void selectionChanged(SelectionChangedEvent<FsysItem> se) {
                if (se.getSelectedItem() != null && !manualSelect) {
                    grdContent_SelectionChanged(se);
                    callListeners(itemSelected);
                }
            }
        });
        mcpContent.getContentGrid().addListener(Events.CellDoubleClick, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                GridEvent e = (GridEvent) be;
                grdContent_DoubleClick(e);
            }
        });
        add(mcpContent, new RowData(1, 1, new Margins(0, 4, 0, 4)));

        dbrDetails = new DetailsBar();
        add(dbrDetails, new RowData(1, -1, new Margins(4, 4, 0, 4)));


        NeptuoRoot.getClipboard().addListener(new Listener<ClipboardEvent>() {

            @Override
            public void handleEvent(ClipboardEvent be) {
                abrActions.getPasteMenuItem().setEnabled((be.getData() != null && be.getData() instanceof FileSystemClipboardData));
            }
        });
    }

    protected abstract void btnBack_Click(ButtonEvent event);

    protected abstract void btnForward_Click(ButtonEvent event);

    protected abstract void tfdPath_EnterPressed(ComponentEvent event);

    protected abstract void btnGo_Click(ButtonEvent event);

    protected abstract void btnUp_Click(ButtonEvent event);

    protected abstract void mniCreateFolder_Click(MenuEvent event);

    protected abstract void mniCreateEmptyFile_Click(MenuEvent event);

    protected abstract void mniFileUpload_FileUploaded(List<FsysItem> objects);

    protected abstract void mniFileUploader_Click(MenuEvent event);

    protected abstract void mniCut_Click(MenuEvent event);

    protected abstract void mniCopy_Click(MenuEvent event);

    protected abstract void mniPaste_Click(MenuEvent event);

    protected abstract void btnDownload_Click(ButtonEvent event);

    protected abstract void btnProperties_Click(ButtonEvent event);

    protected abstract void btnDelete_Click(ButtonEvent event);

    protected abstract void tgbFoldersOnly_Click(ButtonEvent event);

    protected abstract void tfdFileNameFilter_EnterPressed(ComponentEvent event);

    protected abstract void tfdFileExtensionFilter_EnterPressed(ComponentEvent event);

    protected abstract void grdDrives_SelectionChanged(SelectionChangedEvent<Drive> event);

    protected abstract void grdDrives_DoubleClick(GridEvent event);

    protected abstract void grdContent_SelectionChanged(SelectionChangedEvent<FsysItem> event);

    protected abstract void grdContent_DoubleClick(GridEvent event);
}

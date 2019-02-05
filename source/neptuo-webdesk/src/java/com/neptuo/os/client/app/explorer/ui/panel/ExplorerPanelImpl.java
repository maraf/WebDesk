/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.os.client.app.explorer.ui.panel;

import com.extjs.gxt.ui.client.Style.*;
import com.extjs.gxt.ui.client.event.*;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.google.gwt.http.client.*;
import com.neptuo.os.client.Constants;
import com.neptuo.os.client.NeptuoRoot;
import com.neptuo.os.client.app.explorer.data.*;
import com.neptuo.os.client.app.explorer.dialogs.*;
import com.neptuo.os.client.app.explorer.event.*;
import com.neptuo.os.client.app.explorer.ui.PropertiesWindow;
import com.neptuo.os.client.data.*;
import com.neptuo.os.client.data.model.*;
import com.neptuo.os.client.http.service.FileSystemService;
import com.neptuo.os.client.http.*;
import com.neptuo.os.client.http.RequestCallback;
import com.neptuo.os.client.http.service.ServiceUrls;
import java.util.List;

/**
 *
 * @author Mara
 */
public class ExplorerPanelImpl extends ExplorerPanelController implements ExplorerPanel {

    public ExplorerPanelImpl() {
        initialize();
    }

    protected void initialize() {
        showMask();
        FileSystemService.getDrives(new AsyncCallback<Drive>() {

            @Override
            public void onSuccess(List<Drive> objects) {
                hideMask();
            }

            @Override
            public void onClientError(Throwable e) {
                showErrorMessage(e);
            }

            @Override
            public void onServerError(ExceptionType exceptionType) {
                showErrorMessage(exceptionType);
            }
        });
    }

    @Override
    protected void applyItems(List<FsysItem> items) {
        manualSelect = true;
        ExplorerPanelHelper.applyItemsSetupNextSelect(mcpContent, items, nextFileName);
        nextFileName = null;

        FsysItem dot = mcpContent.getContentListStore().findModel("name", ".", true);
        loadingFolderId = dot.getId();
        loadingFolderPath = dot.getPath();

        nbrNavigate.getPathTextField().setValue(loadingFolderPath);
        mcpContent.getContentListStore().setSourceFolderId(loadingFolderId);
        mcpContent.getContentListStore().setSourceFolderPath(loadingFolderPath);

        if (addToHistory) {
            history.add(loadingFolderId, loadingFolderPath);
        } else {
            addToHistory = true;
        }

        abrActions.setItemButtonsIconToFolder();
        abrActions.setItemButtonsEnabled(false);

        nbrNavigate.setHistoryButtonsEnabled(history.hasPrev(), history.hasNext());
        nbrNavigate.getUpButton().setEnabled(mcpContent.getContentListStore().findModel("name", "..", true) != null);

        ExplorerPanelHelper.applyItemsSetupButtons(mcpContent, abrActions);

        ExplorerPanelHelper.applyItemsSetupDrives(loadingFolderPath, mcpContent, nbrNavigate);

        callListeners(dataLoaded);
        hideMask();
        manualSelect = false;
    }

    //Don't use this directly!! Use loadFolder(folderId) or loadFolder(path)
    protected void loadFolder(Integer folderId, String path) {
        loadingFolderId = folderId;
        loadingFolderPath = path;

        //Filter params
        boolean foldersOnly = abrActions.getFoldersOnlyToggleButton().isPressed();
        String filterName = StringHelper.getDefaultValue(abrActions.getFileNameFilterTextField().getValue());
        String filterExtension = StringHelper.getDefaultValue(abrActions.getFileExtensionFilterTextField().getValue());

        //Set it here to support DataEventBus and FileSystemListStore events
        mcpContent.getContentListStore().setSourceFolderId(loadingFolderId);
        mcpContent.getContentListStore().setSourceFolderPath(loadingFolderPath);
        mcpContent.getContentListStore().setFoldersOnly(foldersOnly);
        mcpContent.getContentListStore().setFilterName(filterName);
        mcpContent.getContentListStore().setFilterExtension(filterExtension);

        if (folderId != null) {
            FileSystemService.getFolderItems(folderId, foldersOnly, filterName, filterExtension, itemsCallback);
        } else if (path != null) {
            FileSystemService.getFolderItems(path, foldersOnly, filterName, filterExtension, itemsCallback);
        }

        callListeners(dataStartLoding);
        showMask();
    }

    protected void loadFolder(int folderId) {
        loadFolder(folderId, null);
    }

    protected void loadFolder(String path) {
        loadFolder(null, path);
    }

    @Override
    protected ExplorerPanelImpl getThis() {
        return this;
    }

    @Override
    public void setNavigateVisible(boolean visible) {
        nbrNavigate.setVisible(visible);
    }

    @Override
    public void setActionsVisible(boolean visible) {
        abrActions.setVisible(visible);
    }

    @Override
    public void setDrivesVisible(boolean visible) {
        mcpContent.getDrivesGrid().setVisible(visible);
    }

    @Override
    public void setDetailsVisible(boolean visible) {
        dbrDetails.setVisible(visible);
    }

    @Override
    public void setLocation(String path) {
        if (PathHelper.isFolder(path)) {
            addToHistory = !path.equals(loadingFolderPath);
            loadFolder(path);
        } else {
            nextFileName = PathHelper.getFileName(path);
            loadFolder(PathHelper.parentPath(path));
        }
    }

    @Override
    public void setLocation(FsysItem item) {
        loadFolder(item.getId());
    }

    @Override
    public void refresh() {
        addToHistory = false;
        if (loadingFolderId != null) {
            loadFolder(loadingFolderId);
        }
    }

    @Override
    public FsysItem getSelectedItem() {
        return mcpContent.getContentGrid().getSelectionModel().getSelectedItem();
    }

    @Override
    public List<FsysItem> getSelectedItems() {
        return mcpContent.getContentGrid().getSelectionModel().getSelectedItems();
    }

    @Override
    public void setFoldersOnly(boolean foldersOnly) {
        abrActions.getFoldersOnlyToggleButton().toggle(foldersOnly);
        abrActions.getFoldersOnlyToggleButton().disable();
    }

    @Override
    public boolean getFoldersOnly() {
        return abrActions.getFoldersOnlyToggleButton().isPressed();
    }

    @Override
    public void setFileNameFilter(String fileName) {
        abrActions.getFileNameFilterTextField().setValue(fileName);
        abrActions.getFileNameFilterTextField().disable();
    }

    @Override
    public String getFileNameFilter() {
        return abrActions.getFileNameFilterTextField().getValue();
    }

    @Override
    public void setFileExtensionFilter(String fileExtension) {
        abrActions.getFileExtensionFilterTextField().setValue(fileExtension);
        abrActions.getFileExtensionFilterTextField().disable();
    }

    @Override
    public String getFileExtensionFilter() {
        return abrActions.getFileExtensionFilterTextField().getValue();
    }

    @Override
    public Component getComponent() {
        return this;
    }

    @Override
    protected void btnBack_Click(ButtonEvent event) {
        addToHistory = false;
        if (history.hasPrev()) {
            loadFolder(history.prev().getFolderId());
        }
    }

    @Override
    protected void btnForward_Click(ButtonEvent event) {
        addToHistory = false;
        if (history.hasNext()) {
            loadFolder(history.next().getFolderId());
        }
    }

    @Override
    protected void tfdPath_EnterPressed(ComponentEvent event) {
        String path = nbrNavigate.getPath();
        if (path != null && !"".equals(path.trim())) {
            if (!path.endsWith(PathHelper.DIRECTORY_SEPARATOR)) {
                path += PathHelper.DIRECTORY_SEPARATOR;
            }
            setLocation(path);
        }
    }

    @Override
    protected void btnGo_Click(ButtonEvent event) {
        String path = nbrNavigate.getPath();
        if (path != null && !"".equals(path.trim())) {
            if (!path.endsWith(PathHelper.DIRECTORY_SEPARATOR)) {
                path += PathHelper.DIRECTORY_SEPARATOR;
            }
            setLocation(path);
        }
    }

    @Override
    protected void btnUp_Click(ButtonEvent event) {
        FsysItem parent = mcpContent.getContentListStore().findModel("name", "..", true);
        if (parent != null) {
            setLocation(parent);
        }
    }

    @Override
    protected void mniCreateFolder_Click(MenuEvent event) {
        if (loadingFolderId != null) {
            Dialogs.createFolder(new CreateFolderCallback() {

                @Override
                public void onOkButtonClicked(String name) {
                    if (name.length() > 0) {
                        showMask();
                        FileSystemService.createFolder(loadingFolderId, name, new AsyncCallback<FsysItem>() {

                            @Override
                            public void onSuccess(List<FsysItem> objects) {
                                hideMask();
                            }

                            @Override
                            public void onClientError(Throwable e) {
                                showErrorMessage(e);
                            }

                            @Override
                            public void onServerError(ExceptionType exceptionType) {
                                showErrorMessage(exceptionType);
                            }
                        });
                    }
                }
            });
        }
    }

    @Override
    protected void mniCreateEmptyFile_Click(MenuEvent event) {
        if (loadingFolderId != null) {
            Dialogs.createEmptyFile(new CreateEmptyFileCallback() {

                @Override
                public void onOkButtonClicked(String name) {
                    if (name.length() > 0) {
                        showMask();
                        FileSystemService.createEmptyFile(loadingFolderId, name, new AsyncCallback<FsysItem>() {

                            @Override
                            public void onSuccess(List<FsysItem> objects) {
                                hideMask();
                            }

                            @Override
                            public void onClientError(Throwable e) {
                                showErrorMessage(e);
                            }

                            @Override
                            public void onServerError(ExceptionType exceptionType) {
                                showErrorMessage(exceptionType);
                            }
                        });
                    }
                }
            });
        }
    }

    @Override
    protected void mniFileUpload_FileUploaded(List<FsysItem> objects) {
        hideMask();
    }

    @Override
    protected void mniFileUploader_Click(MenuEvent event) {
        if (mcpContent.getContentListStore().getSourceFolderId() != null) {
            Dialogs.multiUploadFile(nbrNavigate.getPathTextField().getValue(), mcpContent.getContentListStore().getSourceFolderId(), new AsyncCallback<FsysItem>() {

                @Override
                public void onSuccess(List<FsysItem> objects) {
                    hideMask();
                }

                @Override
                public void onClientError(Throwable e) {
                    showErrorMessage(e);
                }

                @Override
                public void onServerError(ExceptionType exceptionType) {
                    showErrorMessage(exceptionType);
                }
            });
        }
    }

    @Override
    protected void mniCut_Click(MenuEvent event) {
        if (getSelectedItem() != null) {
            NeptuoRoot.getClipboard().setCurrentData(new FileSystemClipboardData(getSelectedItem(), FileSystemClipboardEventType.CUT));
        }
    }

    @Override
    protected void mniCopy_Click(MenuEvent event) {
        if (getSelectedItem() != null) {
            NeptuoRoot.getClipboard().setCurrentData(new FileSystemClipboardData(getSelectedItem(), FileSystemClipboardEventType.COPY));
        }
    }

    @Override
    protected void mniPaste_Click(MenuEvent event) {
        FileSystemClipboardData data = NeptuoRoot.getClipboard().getCurrentData(FileSystemClipboardData.class);
        if (data != null && loadingFolderId != null) {
            AsyncCallback<FsysItem> callback = new AsyncCallback<FsysItem>() {

                @Override
                public void onSuccess(List<FsysItem> objects) {
                    unmask();
                }

                @Override
                public void onClientError(Throwable e) {
                    unmask();
                    showErrorMessage(e.getLocalizedMessage());
                }

                @Override
                public void onServerError(ExceptionType exceptionType) {
                    unmask();
                    showErrorMessage(exceptionType.getLocalizedMessage());
                }
            };

            boolean file = FileSystemService.getItemType(data.getData()).equals(ItemType.FILE);
            boolean move = data.getEventType().equals(FileSystemClipboardEventType.CUT);

            if (file) {
                if (move) {
                    FileSystemService.moveFile(data.getData().getId(), loadingFolderId, callback);
                } else {
                    FileSystemService.copyFile(data.getData().getId(), loadingFolderId, callback);
                }
            } else {
                if (move) {
                    FileSystemService.moveFolder(data.getData().getId(), loadingFolderId, callback);
                } else {
                    FileSystemService.copyFolder(data.getData().getId(), loadingFolderId, callback);
                }
            }
            NeptuoRoot.getClipboard().setCurrentData(null);
            showMask();
        }
    }

    @Override
    protected void btnDownload_Click(ButtonEvent event) {
        if (mcpContent.getContentGrid().getSelectionModel().getSelectedItem() != null) {
            String url = "";
            if (FileSystemService.getItemType(mcpContent.getContentGrid().getSelectionModel().getSelectedItem()).equals(ItemType.FOLDER)) {
                url = ServiceUrls.FileSystem.ZipFolder.toAbsolute();
            } else {
                url = ServiceUrls.FileSystem.FileContent.toAbsolute();
            }
            url += ServiceUrls.FileSystem.PUBLICID_PARAM + getSelectedItem().getPublicId();

            showMask();
            NeptuoRoot.downloadFile(url, new Listener<FormEvent>() {

                @Override
                public void handleEvent(FormEvent be) {
                    unmask();
                }
            });
        }
    }

    @Override
    protected void btnProperties_Click(ButtonEvent event) {
        PropertiesWindow win = new PropertiesWindow(mcpContent.getContentGrid().getSelectionModel().getSelectedItem());
        win.show();
    }

    @Override
    protected void btnDelete_Click(ButtonEvent event) {
        if (getSelectedItem() != null) {
            MessageBox.confirm(Constants.explorer().heading(), Constants.explorer().deleteConfirm() + " '" + getSelectedItem().getName() + "'?", new Listener<MessageBoxEvent>() {

                @Override
                public void handleEvent(MessageBoxEvent be) {
                    if (be.getButtonClicked().getText().equals(Constants.webdesk().yes())) {
                        showMask();
                        RequestCallback callback = new RequestCallback() {

                            @Override
                            public void onSuccess(Request request, Response response) {
                                hideMask();
                            }

                            @Override
                            public void onClientError(Request request, Throwable exception) {
                                showErrorMessage(exception);
                            }

                            @Override
                            public void onServerError(ExceptionType exceptionType) {
                                showErrorMessage(exceptionType);
                            }
                        };
                        FsysItem item = getSelectedItem();
                        if (item.getType().equals("folder")) {
                            FileSystemService.deleteFolder(item.getId(), callback);
                        } else {
                            FileSystemService.deleteFile(item.getId(), callback);
                        }
                    }
                }
            });
        }
    }

    @Override
    protected void tgbFoldersOnly_Click(ButtonEvent event) {
        refresh();
    }

    @Override
    protected void tfdFileNameFilter_EnterPressed(ComponentEvent event) {
        refresh();
    }

    @Override
    protected void tfdFileExtensionFilter_EnterPressed(ComponentEvent event) {
        refresh();
    }

    @Override
    protected void grdDrives_SelectionChanged(SelectionChangedEvent<Drive> event) {
        if (event.getSelectedItem() != null) {
            loadFolder(event.getSelectedItem().getFolderId());
        }
    }

    @Override
    protected void grdDrives_DoubleClick(GridEvent event) {
        Drive item = mcpContent.getDrivesListStore().getAt(event.getRowIndex());
        loadFolder(item.getFolderId());
    }

    @Override
    protected void grdContent_SelectionChanged(SelectionChangedEvent<FsysItem> event) {
        if (event.getSelectedItem() == null) {
            abrActions.setItemButtonsEnabled(false);
        } else {
            dbrDetails.setItem(event.getSelectedItem());
            abrActions.setItemButtonsEnabled(true);
            if (FileSystemService.getItemType(event.getSelectedItem()).equals(ItemType.FOLDER)) {
                abrActions.setItemButtonsIconToFolder();
            } else {
                abrActions.setItemButtonsIconToFile();
            }
        }
        callListeners(itemSelected);
    }

    @Override
    protected void grdContent_DoubleClick(GridEvent event) {
        FsysItem item = mcpContent.getContentListStore().getAt(event.getRowIndex());
        FileTypeHelper.runProgram(getThis(), item, false);
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.os.client.app.explorer.ui.panel;

import com.extjs.gxt.ui.client.widget.*;
import com.neptuo.os.client.Constants;
import com.neptuo.os.client.app.explorer.data.FileSystemHistoryChannel;
import com.neptuo.os.client.app.explorer.event.ExplorerPanelListener;
import com.neptuo.os.client.data.model.FsysItem;
import com.neptuo.os.client.http.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mara
 */
public abstract class ExplorerPanelBase extends LayoutContainer {
    protected String loadingFolderPath = null;
    protected Integer loadingFolderId = null;
    protected boolean addToHistory = true;
    protected boolean manualSelect = false;

    protected String nextFileName = null;
    protected FileSystemHistoryChannel history;
    protected AsyncCallback<FsysItem> itemsCallback;
    protected List<ExplorerPanelListener> dataStartLoding = new ArrayList<ExplorerPanelListener>();
    protected List<ExplorerPanelListener> dataLoaded = new ArrayList<ExplorerPanelListener>();
    protected List<ExplorerPanelListener> itemSelected = new ArrayList<ExplorerPanelListener>();

    public ExplorerPanelBase() {
        history = new FileSystemHistoryChannel();
        itemsCallback = new AsyncCallback<FsysItem>() {

            @Override
            public void onSuccess(List<FsysItem> objects) {
                applyItems(objects);
            }

            @Override
            public void onClientError(Throwable e) {
                showErrorMessage(e);
            }

            @Override
            public void onServerError(ExceptionType exceptionType) {
                showErrorMessage(exceptionType);
            }
        };
    }

    public void addDataStartLoading(ExplorerPanelListener listener) {
        dataStartLoding.add(listener);
    }

    public void addDataLoaded(ExplorerPanelListener listener) {
        dataLoaded.add(listener);
    }

    public void addItemSelected(ExplorerPanelListener listener) {
        itemSelected.add(listener);
    }

    protected void showMask() {
        showMask(Constants.explorer().mask());
    }

    protected void showMask(String content) {
        mask(content);
    }

    protected void showErrorMessage(Throwable exception) {
        hideMask();
        showErrorMessage(exception.getLocalizedMessage());
    }

    protected void showErrorMessage(ExceptionType type) {
        hideMask();
        showErrorMessage(type.getLocalizedMessage());
    }

    protected void showErrorMessage(String message) {
        hideMask();
        MessageBox.alert(Constants.explorer().heading(), message, null);
    }

    protected void hideMask() {
        unmask();
    }

    protected void callListeners(List<ExplorerPanelListener> listeners) {
        for (ExplorerPanelListener list : listeners) {
            list.onEvent(getThis());
        }
    }

    protected boolean isLoadingFromPath() {
        return loadingFolderId == null;
    }

    protected boolean isLoadingFromId() {
        return loadingFolderId != null;
    }

    protected abstract void applyItems(List<FsysItem> items);

    protected abstract  ExplorerPanelImpl getThis();
}

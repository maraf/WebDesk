/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.os.client.app.explorer.dialogs;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.IconButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.neptuo.os.client.Constants;
import com.neptuo.os.client.app.explorer.data.ItemType;
import com.neptuo.os.client.data.model.FsysItem;
import com.neptuo.os.client.app.explorer.event.ExplorerItemSelected;
import com.neptuo.os.client.app.explorer.event.ExplorerPanelListener;
import com.neptuo.os.client.app.explorer.ui.panel.ExplorerPanel;
import com.neptuo.os.client.app.explorer.ui.panel.ExplorerPanelFactory;
import com.neptuo.os.client.http.service.FileSystemService;
import com.neptuo.os.client.ui.DialogWindow;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mara
 */
public class FolderSelectWindow extends DialogWindow {

    private FsysItem currentItem;
    private List<ExplorerItemSelected> listeners;
    private ExplorerPanel panel;
    private Button btnSelect;
    private Button btnCancel;

    public FolderSelectWindow(String folderPath, ExplorerItemSelected listener) {
        listeners = new ArrayList<ExplorerItemSelected>();
        addItemSelectedListener(listener);

        setHeading(Constants.explorer().headingFolderSelect());
        setIcon(Constants.icons16().folder());
        setLayout(new FitLayout());
        setRefreshable(true);
        panel = ExplorerPanelFactory.create();
        if (folderPath != null) {
            panel.setLocation(folderPath);
        }
        panel.setDetailsVisible(false);
        panel.setFoldersOnly(true);
        panel.addItemSelected(new ExplorerPanelListener() {

            @Override
            public void onEvent(ExplorerPanel panel) {
                currentItem = panel.getSelectedItem();
            }
        });
        setWidth(900);
        setHeight(600);
        add(panel.getComponent());

        btnSelect = new Button(Constants.webdesk().select(), new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                if (currentItem != null && FileSystemService.getItemType(currentItem).equals(ItemType.FOLDER)) {
                    for (ExplorerItemSelected listener : listeners) {
                        listener.itemSelected(currentItem);
                    }
                    hide();
                }
            }
        });
        addButton(btnSelect);

        btnCancel = new Button(Constants.webdesk().cancel(), new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                hide();
            }
        });
        addButton(btnCancel);

        refresh.addSelectionListener(new SelectionListener<IconButtonEvent>() {

            @Override
            public void componentSelected(IconButtonEvent ce) {
                panel.refresh();
            }
        });
    }

    public void addItemSelectedListener(ExplorerItemSelected listener) {
        listeners.add(listener);
    }

    @Override
    public String getClassName() {
        return getClass().getName();
    }
}

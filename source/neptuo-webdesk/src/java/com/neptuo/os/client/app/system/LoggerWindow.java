/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.os.client.app.system;

import com.neptuo.os.client.log.LogMessage;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.event.IconButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.neptuo.os.client.Constants;
import com.neptuo.os.client.NeptuoRoot;
import com.neptuo.os.client.ui.DialogWindow;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Mara
 */
public class LoggerWindow extends DialogWindow {

    private Grid<LogMessage> grdMessages;
    private ListStore<LogMessage> lstMessages;
    private ColumnModel clmColumns;
    private LayoutContainer lcnDetail;
    private Text txtTitle;
    private Text txtContent;
    private Text txtDate;

    public LoggerWindow() {
        setWidth(800);
        setHeight(600);
        setHeading(Constants.webdesk().logger());
        setResizable(false);
        setLayout(new RowLayout());
        setRefreshable(true);
        setIcon(Constants.icons16().application_view_list());

        refresh.addSelectionListener(new SelectionListener<IconButtonEvent>() {

            @Override
            public void componentSelected(IconButtonEvent ce) {
                refresh();
            }
        });

        GridCellRenderer<LogMessage> typeRenderer = new GridCellRenderer<LogMessage>() {

            @Override
            public String render(LogMessage model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<LogMessage> store, Grid<LogMessage> grid) {
                AbstractImagePrototype icon = null;
                switch (model.getType()) {
                    case ERROR:
                        icon = Constants.icons16().delete();
                        break;
                    case WARNING:
                        icon = Constants.icons16().error();
                        break;
                    case INFO:
                        icon = Constants.icons16().information();
                        break;
                }
                return icon != null ? icon.getHTML() : "";
            }
        };

        List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
        ColumnConfig type = new ColumnConfig("type", Constants.webdesk().type(), 50);
        type.setRenderer(typeRenderer);
        columns.add(type);
        columns.add(new ColumnConfig("title", Constants.webdesk().message(), 300));
        ColumnConfig date = new ColumnConfig("date", Constants.webdesk().dateTime(), 200);
        date.setDateTimeFormat(DateTimeFormat.getFormat("HH:mm:ss dd.MM.yyyy"));
        columns.add(date);

        clmColumns = new ColumnModel(columns);

        lstMessages = new ListStore<LogMessage>();
        lstMessages.setDefaultSort("date", SortDir.DESC);

        grdMessages = new Grid<LogMessage>(lstMessages, clmColumns);
        grdMessages.setBorders(false);
        grdMessages.setStripeRows(true);
        grdMessages.getView().setForceFit(true);
        grdMessages.setAutoExpandColumn("title");
        grdMessages.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        grdMessages.getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<LogMessage>() {

            @Override
            public void selectionChanged(SelectionChangedEvent<LogMessage> se) {
                if (se.getSelection().size() > 0) {
                    txtTitle.setText((String) se.getSelectedItem().get("title"));
                    txtContent.setText((String) se.getSelectedItem().get("content"));
                    txtDate.setText(((Date) se.getSelectedItem().get("date")).toString());
                    lcnDetail.show();
                } else {
                    lcnDetail.hide();
                }
            }
        });
        add(grdMessages, new RowData(1, 0.5));

        txtTitle = new Text();
        txtContent = new Text();
        txtDate = new Text();

        lcnDetail = new LayoutContainer(new RowLayout());
        lcnDetail.setHeight("50%");
        lcnDetail.add(txtTitle, new RowData(1, -1, new Margins(4)));
        lcnDetail.add(txtContent, new RowData(1, 1, new Margins(0, 4, 0, 4)));
        lcnDetail.add(txtDate, new RowData(1, -1, new Margins(4)));
        add(lcnDetail, new RowData(1, 0.5));

        getTaskbarButton().hideLoading();
        lstMessages.add(NeptuoRoot.getCurrentWorkspace().getLogger().getMessages());
    }

    public ListStore<LogMessage> getMessagesStore() {
        return lstMessages;
    }

    public void refresh() {
        lstMessages.removeAll();
        lstMessages.add(NeptuoRoot.getCurrentWorkspace().getLogger().getMessages());
    }

    @Override
    public String getClassName() {
        return getClass().getName();
    }
}

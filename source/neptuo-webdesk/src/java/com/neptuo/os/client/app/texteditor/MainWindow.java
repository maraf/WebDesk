/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.os.client.app.texteditor;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ButtonBar;
import com.extjs.gxt.ui.client.widget.form.HtmlEditor;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.neptuo.os.client.Constants;
import com.neptuo.os.client.data.model.FsysItem;
import com.neptuo.os.client.app.explorer.event.ExplorerItemSelected;
import com.neptuo.os.client.http.service.FileSystemService;
import com.neptuo.os.client.app.explorer.dialogs.FileSelectWindow;
import com.neptuo.os.client.app.explorer.dialogs.Dialogs;
import com.neptuo.os.client.http.ComponentRequestAdapter;
import com.neptuo.os.client.http.ExceptionType;
import com.neptuo.os.client.http.StringCallback;
import com.neptuo.os.client.ui.Window;

/**
 *
 * @author Mara
 */
public class MainWindow extends Window {

    private TextEditorMode mode;
    private String publicId = null;
    private String fileName;
    private String fileContent;
    private ButtonBar bbrActions;
    private Button btnOpen;
    private Button btnSave;
    private Button btnSaveAs;
    private ContentPanel cpnContent;
    private TextArea teaContent;
    private HtmlEditor hteContent;

    private StringCallback fileContentCallback;

    public MainWindow() {
        this(null, null);
    }

    public MainWindow(final String publicId, String fileName) {
        this(publicId, fileName, TextEditorMode.TEXT);
    }

    public MainWindow(final String publicId, final String fileName, TextEditorMode mode) {
        this.publicId = publicId;
        this.fileName = fileName;
        this.mode = mode;

        fileContentCallback = new StringCallback() {

            @Override
            public void onSucces(String content) {
                fileContent = content;
                setContent(content);
                getBody().unmask();
            }

            @Override
            public void onClientError(Throwable e) {
                getBody().mask(e.getMessage());
            }

            @Override
            public void onServerError(ExceptionType exceptionType) {
                getBody().mask(exceptionType.getLocalizedMessage());
            }
        };

        setHeading(Constants.texteditor().heading() + (fileName != null ? " - " + fileName : ""));
        setIcon(Constants.icons16().note());
        setWidth(700);
        setHeight(500);
        setLayout(new RowLayout());
        setRefreshable(false);

        bbrActions = new ButtonBar();
        add(bbrActions, new RowData(1, 28, new Margins(0, 0, 4, 0)));

        btnOpen = new Button(Constants.texteditor().btnOpen(), new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                Dialogs.selectFile(getFileName(), new ExplorerItemSelected() {

                    @Override
                    public void itemSelected(FsysItem item) {
                        getBody().mask(Constants.explorer().mask());

                        setFileName(item.getPath());
                        setPublicId(item.getPublicId());
                        FileSystemService.getFileContent(item.getPublicId(), fileContentCallback);
                    }
                });
            }
        });
        bbrActions.add(btnOpen);

        btnSave = new Button(Constants.texteditor().btnSave(), new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                getBody().mask(Constants.explorer().mask());
                if (publicId != null) {
                    FileSystemService.setFileContent(getPublicId(), getContent(), new ComponentRequestAdapter(getBody()));
                } else {
                    btnSaveAs.fireEvent(Events.Select, ce);
                }
            }
        });
        bbrActions.add(btnSave);

        btnSaveAs = new Button(Constants.texteditor().btnSaveAs(), new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                Dialogs.selectFile(getFileName(), new ExplorerItemSelected() {

                    @Override
                    public void itemSelected(FsysItem item) {
                        getBody().mask(Constants.explorer().mask());
                        setFileName(item.getPath());
                        setPublicId(item.getPublicId());
                        FileSystemService.setFileContent(item.getPublicId(), getContent(), new ComponentRequestAdapter(getBody()));
                    }
                });
            }
        });
        bbrActions.add(btnSaveAs);

        cpnContent = new ContentPanel(new FitLayout());
        cpnContent.setHeaderVisible(false);
        cpnContent.setBorders(false);
        cpnContent.setBodyBorder(false);
        add(cpnContent, new RowData(1, 1));

        if (mode.equals(TextEditorMode.TEXT)) {
            teaContent = new TextArea();
            cpnContent.add(teaContent);
        } else {
            hteContent = new HtmlEditor();
            cpnContent.add(hteContent);
        }

        loadData();
    }

    public void setContent(String content) {
        if (mode.equals(TextEditorMode.TEXT)) {
            teaContent.setValue(content);
        } else {
            hteContent.setValue(content);
        }
    }

    public String getContent() {
        String result;
        if (mode.equals(TextEditorMode.TEXT)) {
            result = teaContent.getValue();
        } else {
            hteContent.syncValue();
            result = hteContent.getValue();
        }
        return result != null ? result : "";
    }

    protected void setFileName(String fileName) {
        setHeading(Constants.texteditor().heading() + (fileName != null ? " - " + fileName : ""));
        this.fileName = fileName;
    }

    protected String getFileName() {
        return fileName;
    }

    protected void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    protected String getPublicId() {
        return publicId;
    }

    @Override
    public String getClassName() {
        return getClass().getName();
    }

    protected final void loadData() {
        if (publicId != null) {
            FileSystemService.getFileContent(publicId, fileContentCallback);
        }
    }

    private MainWindow getThis() {
        return this;
    }
}

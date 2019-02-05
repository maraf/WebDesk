/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.client.app.explorer.ui.panel;

import com.extjs.gxt.ui.client.Style.IconAlign;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.*;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.*;
import com.neptuo.os.client.Constants;

/**
 *
 * @author Mara
 */
public class NavigateBar extends LayoutContainer {
    private ButtonBar bbrNavigate;
    private Button btnBack;
    private Button btnForward;
    private ButtonBar bbrGo;
    private Button btnGo;
    private Button btnLevelUp;
    private TextField<String> tfdPath;

    public NavigateBar() {
        setLayout(new RowLayout(Orientation.HORIZONTAL));
        setHeight(26);
        addStyleName("x-transparent");
        setBorders(false);

        bbrNavigate = new ButtonBar();
        bbrNavigate.setWidth(84);
        add(bbrNavigate, new RowData(-1, 1, new Margins(0, 4, 0, 0)));

        btnBack = new Button("", Constants.icons16().arrow_left());
        btnBack.setToolTip(Constants.explorer().btnBack());
        btnBack.setEnabled(false);
        bbrNavigate.add(btnBack);

        btnForward = new Button("", Constants.icons16().arrow_right());
        btnForward.setEnabled(false);
        btnForward.setToolTip(Constants.explorer().btnForward());
        bbrNavigate.add(btnForward);

        btnLevelUp = new Button("", Constants.icons16().arrow_up());
        btnLevelUp.disable();
        btnLevelUp.setToolTip(Constants.explorer().levelUp());
        bbrNavigate.add(btnLevelUp);

        tfdPath = new TextField<String>();
        tfdPath.setFieldLabel(Constants.explorer().tfdPath());
        tfdPath.setAutoWidth(true);
        add(tfdPath, new RowData(1, 1, new Margins(2, 0, 2, 0)));

        bbrGo = new ButtonBar();
        bbrGo.setWidth(28);
        add(bbrGo, new RowData(-1, 1, new Margins(0, 0, 0, 4)));

        btnGo = new Button("", Constants.icons16().bullet_go());
        btnGo.setToolTip(Constants.explorer().go());
        bbrGo.add(btnGo);
    }

    public void setHistoryButtonsEnabled(boolean enabled) {
        btnBack.setEnabled(enabled);
        btnForward.setEnabled(enabled);
    }

    public void setHistoryButtonsEnabled(boolean back, boolean forward) {
        btnBack.setEnabled(back);
        btnForward.setEnabled(forward);
    }

    public Button getBackButton() {
        return btnBack;
    }

    public Button getForwardButton() {
        return btnForward;
    }

    public Button getGoButton() {
        return btnGo;
    }

    public Button getUpButton() {
        return btnLevelUp;
    }

    public TextField<String> getPathTextField() {
        return tfdPath;
    }

    public String getPath() {
        return tfdPath.getValue();
    }

    public void setPath(String path) {
        tfdPath.setValue(path);
    }
}

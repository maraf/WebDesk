/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.client;

import com.neptuo.os.client.locale.WebdeskConstants;
import com.google.gwt.core.client.GWT;
import com.neptuo.os.client.app.drivemanager.locale.DriveManagerConstants;
import com.neptuo.os.client.app.explorer.locale.ExplorerConstants;
import com.neptuo.os.client.app.texteditor.locale.TextEditorConstants;
import com.neptuo.os.client.app.usermanager.locale.UserManagerConstants;
import com.neptuo.os.client.images.icons16.Icons16;
import com.neptuo.os.client.locale.Exceptions;

/**
 *
 * @author Mara
 */
public class Constants {
    private static WebdeskConstants webdesk;
    private static UserManagerConstants users;
    private static ExplorerConstants explorer;
    private static TextEditorConstants texteditor;
    private static DriveManagerConstants drives;
    private static Exceptions exceptions;
    private static Icons16 icons16;

    public static WebdeskConstants webdesk() {
        if(webdesk == null) {
            webdesk = GWT.create(WebdeskConstants.class);
        }
        return webdesk;
    }

    public static UserManagerConstants users() {
        if(users == null) {
            users = GWT.create(UserManagerConstants.class);
        }
        return users;
    }

    public static ExplorerConstants explorer() {
        if(explorer == null) {
            explorer = GWT.create(ExplorerConstants.class);
        }
        return explorer;
    }

    public static TextEditorConstants texteditor() {
        if(texteditor == null) {
            texteditor = GWT.create(TextEditorConstants.class);
        }
        return texteditor;
    }

    public static DriveManagerConstants drives() {
        if(drives == null) {
            drives = GWT.create(DriveManagerConstants.class);
        }
        return drives;
    }

    public static Exceptions exceptions() {
        if(exceptions == null) {
            exceptions = GWT.create(Exceptions.class);
        }
        return exceptions;
    }

    public static Icons16 icons16() {
        if (icons16 == null) {
            icons16 = GWT.create(Icons16.class);
        }
        return icons16;
    }
}

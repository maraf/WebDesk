/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.os.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.Response;
import com.neptuo.os.client.app.explorer.dialogs.MultiUploadWindow;
import com.neptuo.os.client.http.RequestAdapter;

/**
 * Main entry point.
 *
 * @author Mara
 */
public class DesktopModuleEntryPoint implements EntryPoint {
    
    @Override
    public void onModuleLoad() {
        NeptuoRoot.initialize();
//        NeptuoRoot.devInitialize("c42d37d8f42094d9d077278b378ae9200cb77b1d", new RequestAdapter() {
//
//            @Override
//            public void onSuccess(Request request, Response response) {
//
//            }
//        });

    }
}

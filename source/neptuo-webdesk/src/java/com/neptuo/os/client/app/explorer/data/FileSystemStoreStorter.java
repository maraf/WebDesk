/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.client.app.explorer.data;

import com.neptuo.os.client.data.model.FsysItem;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.StoreSorter;

/**
 *
 * @author Mara
 */
public class FileSystemStoreStorter extends StoreSorter<FsysItem> {

    @Override
    public int compare(Store<FsysItem> store, FsysItem m1, FsysItem m2, String property) {
        if(m1.getType().equals("folder") && m2.getType().equals("folder")) {
            return super.compare(store, m1, m2, property);
        } else if(m1.getType().equals("folder")) {
            return -1;
        } else if(m2.getType().equals("folder")) {
            return 1;
        } else {
            return super.compare(store, m1, m2, property);
        }
    }

}

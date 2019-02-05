/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.service.io;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author Mara
 */
public class StringInputStream extends InputStream {
    private int[] data;
    private int pointer;

    public StringInputStream(String data) {
        this.data = new int[data.length()];
        for(int i = 0; i < data.length(); i ++) {
            this.data[i] = data.charAt(i);
        }
        pointer = 0;
    }

    @Override
    public int read() throws IOException {
        if (pointer < data.length) {
            int i = data[pointer];
            pointer++;
            return i;
        } else {
            return -1;
        }
    }

}


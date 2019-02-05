/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.client.data.databus;

/**
 *
 * @author Mara
 */
public class DataEvent {
    private static int count = 0;

    private int id;

    public DataEvent() {
        id = count ++;
    }
}

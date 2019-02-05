/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.service.result;

/**
 *
 * @author Mara
 */
public class RawResult implements Result {
    private byte[] result;

    public RawResult(byte[] result) {
        this.result = result;
    }

    @Override
    public String getResult() {
        return result.toString();
    }

    public byte[] getRawResult() {
        return result;
    }

}

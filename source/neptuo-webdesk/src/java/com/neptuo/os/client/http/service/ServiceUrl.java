/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.client.http.service;

/**
 *
 * @author Mara
 */
public class ServiceUrl {
    private String relative;

    public ServiceUrl(String relative) {
        this.relative = relative;
    }

    public String getRelative() {
        return relative;
    }

    public String toAbsolute() {
        return ServiceUrls.toAbsolute(this);
    }
}

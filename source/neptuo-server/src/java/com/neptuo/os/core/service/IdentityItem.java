/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.os.core.service;

import com.neptuo.service.io.annotation.Deserializable;

/**
 *
 * @author Mara
 */
@Deserializable(name = "identity")
public class IdentityItem {

    private String name;

    public String getName() {
        return name;
    }

    @Deserializable(name = "name")
    public void setName(String name) {
        this.name = name;
    }
}

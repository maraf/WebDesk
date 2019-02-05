/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.client.conf;

/**
 *
 * @author Mara
 */
public interface Configuration extends com.google.gwt.i18n.client.Constants {
    public String serverApplicationPath();
    public String serverServicePath();
    public String resourcesPath();
    public String imagesPath();
    public String icons16Path();
    public String icons24Path();
    public int version_major();
    public int version_minor();
    public int version_build();
}

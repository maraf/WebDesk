/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.service.io;

/**
 *
 * @author Mara
 */
public interface RequestParser {

    public void startElement(String name) throws Exception;

    public void content(String content) throws Exception;

    public void endElement(String name) throws Exception;

}

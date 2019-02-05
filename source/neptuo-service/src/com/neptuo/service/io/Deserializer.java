/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.service.io;

import com.neptuo.service.HttpException;
import java.io.InputStream;

/**
 *
 * @author Mara
 */
public interface Deserializer {

    public void setRequestParser(RequestParser parser);

    public void parse(InputStream input) throws HttpException;
}

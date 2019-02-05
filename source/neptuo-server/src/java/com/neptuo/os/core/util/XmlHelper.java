/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.os.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author Mara
 */
public class XmlHelper {

    public static Document createDocument(String file) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setValidating(false);
            DocumentBuilder builder = dbf.newDocumentBuilder();
            return builder.parse(file);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(XmlHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(XmlHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XmlHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static Document createDocument(InputStream input) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setValidating(false);
            DocumentBuilder builder = dbf.newDocumentBuilder();
            return builder.parse(input);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(XmlHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(XmlHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XmlHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}

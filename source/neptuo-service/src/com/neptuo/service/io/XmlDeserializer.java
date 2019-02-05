/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.service.io;

import com.neptuo.service.HttpException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 *
 * @author Mara
 */
public class XmlDeserializer extends DefaultHandler implements Deserializer {

    private RequestParser parser;

    @Override
    public void parse(InputStream input) throws HttpException {
        try {
            XMLReader xr = XMLReaderFactory.createXMLReader();
            xr.setContentHandler(this);
            xr.parse(new InputSource(input));
        } catch (IOException ex) {
            //TODO: Handle exception
            Logger.getLogger(XmlDeserializer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            //TODO: Handle exception
            Logger.getLogger(XmlDeserializer.class.getName()).log(Level.SEVERE, null, ex);

            if(ex.getMessage().equals("HttpException")) {
                throw (HttpException) ex.getException();
            }
        }
    }

    @Override
    public void setRequestParser(RequestParser parser) {
        this.parser = parser;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        try {
            parser.startElement(localName);
            //Elements as attributes
            for (int i = 0; i < attributes.getLength(); i++) {
                parser.startElement(attributes.getLocalName(i));
                parser.content(attributes.getValue(i));
                parser.endElement(attributes.getLocalName(i));
            }
        } catch (Exception ex) {
            throw new SAXException("Exception", ex);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        try {
            parser.endElement(localName);
        } catch (Exception ex) {
            throw new SAXException("Exception", ex);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        try {
            String value = "";
            for (int i = start; i < start + length; i++) {
                value += ch[i];
            }
            parser.content(value);
        } catch (Exception ex) {
            throw new SAXException("Exception", ex);
        }
    }
}

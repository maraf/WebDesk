/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.os.core.util;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;

/**
 *
 * @author Mara
 */
public class PersistenceHelper {

    public static EntityManager lookupDefault() {
        try {
            EntityManager manager = (EntityManager) new InitialContext().lookup("java:comp/env/persistence/NeptuoOSDefaultEM");
            manager.joinTransaction();
            return manager;
        } catch (NamingException ex) {
            Logger.getLogger(PersistenceHelper.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}

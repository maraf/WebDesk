/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.os.client.http.service;

import com.neptuo.os.client.NeptuoRoot;

/**
 *
 * @author Mara
 */
public class ServiceUrls {

    public static class Core {
        public static final ServiceUrl AccessTypes = new ServiceUrl("/core/access-types");
        public static final ServiceUrl Feedback = new ServiceUrl("/core/feedback");
        public static final ServiceUrl IdentityLookup = new ServiceUrl("/core/identity-lookup");
        public static final ServiceUrl Login = new ServiceUrl("/core/login");
        public static final ServiceUrl Logout = new ServiceUrl("/core/logout");
        public static final ServiceUrl Properties = new ServiceUrl("/core/properties");
        public static final ServiceUrl Users = new ServiceUrl("/core/users");
        public static final ServiceUrl UserRoles = new ServiceUrl("/core/user-roles");
    }
    
    public static class FileSystem {
        public static final String PUBLICID_PARAM = "?publicId=";
        
        public static final ServiceUrl Drives = new ServiceUrl("/fsys/drives");
        public static final ServiceUrl Files = new ServiceUrl("/fsys/files");
        public static final ServiceUrl FileContent = new ServiceUrl("/fsys/file-content");
        public static final ServiceUrl Folders = new ServiceUrl("/fsys/folders");
        public static final ServiceUrl Upload = new ServiceUrl("/fsys/upload");
        public static final ServiceUrl ZipFolder = new ServiceUrl("/fsys/zip-folder");
    }

    public static String toAbsolute(ServiceUrl url) {
        return NeptuoRoot.getConfiguration().serverApplicationPath() + NeptuoRoot.getConfiguration().serverServicePath() + url.getRelative();
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.os.client.http.service;

/**
 *
 * @author Mara
 */
public class ServiceMethods {

    public static class Core {
        public static class AccessType {
            private static final ServiceUrl Service = ServiceUrls.Core.AccessTypes;
            public static final ServiceMethod Get = new ServiceMethod(null, Service);
        }

        public static class Feedback {
            private static final ServiceUrl Service = ServiceUrls.Core.Feedback;
            public static final ServiceMethod Send = new ServiceMethod(null, Service);
        }

        public static class IdentityLookup {
            private static final ServiceUrl Service = ServiceUrls.Core.IdentityLookup;
            public static final ServiceMethod Lookup = new ServiceMethod(null, Service);
        }

        public static class Login {
            private static final ServiceUrl Service = ServiceUrls.Core.Login;
            public static final ServiceMethod Login = new ServiceMethod(null, Service);
            public static final ServiceMethod IsLogged = new ServiceMethod("isLogged", Service);
        }

        public static class Logout {
            private static final ServiceUrl Service = ServiceUrls.Core.Logout;
            public static final ServiceMethod Logout = new ServiceMethod(null, Service);
        }

        public static class Property {
            private static final ServiceUrl Service = ServiceUrls.Core.Properties;
            public static final ServiceMethod Get = new ServiceMethod(null, Service);
            public static final ServiceMethod Set = new ServiceMethod("set", Service);
        }

        public static class Users {
            private static final ServiceUrl Service = ServiceUrls.Core.Users;
            public static final ServiceMethod Get = new ServiceMethod(null, Service);
            public static final ServiceMethod Save = new ServiceMethod("save", Service);
            public static final ServiceMethod Delete = new ServiceMethod("delete", Service);
        }

        public static class UserRoles {
            private static final ServiceUrl Service = ServiceUrls.Core.UserRoles;
            public static final ServiceMethod GetManageable = new ServiceMethod(null, Service);
            public static final ServiceMethod GetUserRoles = new ServiceMethod("getUserRoles", Service);
            public static final ServiceMethod Save = new ServiceMethod("save", Service);
            public static final ServiceMethod Delete = new ServiceMethod("delete", Service);
            public static final ServiceMethod AddUserToRole = new ServiceMethod("addUserToRole", Service);
            public static final ServiceMethod RemoveUserFromRole = new ServiceMethod("removeUserFormRole", Service);
        }
    }

    public static class FileSystem {
        public static class Drives {
            private static final ServiceUrl Service = ServiceUrls.FileSystem.Drives;
            public static final ServiceMethod Get = new ServiceMethod(null, Service);
            public static final ServiceMethod Create = new ServiceMethod("create", Service);
            public static final ServiceMethod Update = new ServiceMethod("update", Service);
            public static final ServiceMethod Delete = new ServiceMethod("delete", Service);
            public static final ServiceMethod DeleteCustom = new ServiceMethod("deleteCustom", Service);
        }

        public static class Files {
            private static final ServiceUrl Service = ServiceUrls.FileSystem.Files;
            public static final ServiceMethod Copy = new ServiceMethod("copy", Service);
            public static final ServiceMethod Create = new ServiceMethod("create", Service);
            public static final ServiceMethod CreateEmpty = new ServiceMethod("createEmpty", Service);
            public static final ServiceMethod Delete = new ServiceMethod("delete", Service);
            public static final ServiceMethod Rename = new ServiceMethod("rename", Service);
            public static final ServiceMethod Move = new ServiceMethod("move", Service);
            public static final ServiceMethod NewVersion = new ServiceMethod("newVersion", Service);
            public static final ServiceMethod SetIsPublic = new ServiceMethod("setIsPublic", Service);
            public static final ServiceMethod GetPermissions = new ServiceMethod("getPermissions", Service);
            public static final ServiceMethod AddPermissions = new ServiceMethod("addPermissions", Service);
            public static final ServiceMethod RemovePermissions = new ServiceMethod("removePermissions", Service);
        }

        public static class FileContent {
            private static final ServiceUrl Service = ServiceUrls.FileSystem.FileContent;
            public static final ServiceMethod Get = new ServiceMethod(null, Service);
            public static final ServiceMethod SetTextFileContent = new ServiceMethod("setTextFileContent", Service);
        }

        public static class Folders {
            private static final ServiceUrl Service = ServiceUrls.FileSystem.Folders;
            public static final ServiceMethod Copy = new ServiceMethod("copy", Service);
            public static final ServiceMethod Create = new ServiceMethod("create", Service);
            public static final ServiceMethod Delete = new ServiceMethod("delete", Service);
            public static final ServiceMethod Get = new ServiceMethod(null, Service);
            public static final ServiceMethod ItemsInfo = new ServiceMethod("itemsInfo", Service);
            public static final ServiceMethod Move = new ServiceMethod("move", Service);
            public static final ServiceMethod Rename = new ServiceMethod("rename", Service);
            public static final ServiceMethod SetIsPublic = new ServiceMethod("setIsPublic", Service);
            public static final ServiceMethod GetPermissions = new ServiceMethod("getPermissions", Service);
            public static final ServiceMethod AddPermissions = new ServiceMethod("addPermissions", Service);
            public static final ServiceMethod RemovePermissions = new ServiceMethod("removePermissions", Service);
        }

        public static class Upload {
            private static final ServiceUrl Service = ServiceUrls.FileSystem.Upload;
            public static final ServiceMethod Upload = new ServiceMethod(null, Service);
        }

        public static class ZipFolder {
            private static final ServiceUrl Service = ServiceUrls.FileSystem.ZipFolder;
            public static final ServiceMethod Zip = new ServiceMethod(null, Service);
        }
    }
}

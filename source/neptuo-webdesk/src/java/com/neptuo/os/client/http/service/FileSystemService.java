/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.os.client.http.service;

import com.neptuo.os.client.conf.Format;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.Response;
import com.neptuo.os.client.NeptuoRoot;
import com.neptuo.os.client.app.explorer.data.ExplorerFilterParams;
import com.neptuo.os.client.data.databus.DataEventType;
import com.neptuo.os.client.data.model.FsysItem;
import com.neptuo.os.client.app.explorer.data.ItemType;
import com.neptuo.os.client.data.model.Permission;
import com.neptuo.os.client.data.BaseModelData;
import com.neptuo.os.client.data.XmlWriter;
import com.neptuo.os.client.data.databus.DataEvents;
import com.neptuo.os.client.data.model.Drive;
import com.neptuo.os.client.data.model.FileCopyMoveItem;
import com.neptuo.os.client.data.model.FolderCopyMoveItem;
import com.neptuo.os.client.http.AsyncCallback;
import com.neptuo.os.client.http.ExceptionType;
import com.neptuo.os.client.http.RequestCallback;
import com.neptuo.os.client.http.RequestFactory;
import com.neptuo.os.client.http.ServiceRequestBuilder;
import com.neptuo.os.client.http.StringCallback;

/**
 *
 * @author Mara
 */
public class FileSystemService extends BaseService {

    public static void getDrives(AsyncCallback<Drive> callback) {
        ServiceRequestBuilder.factory(Drive.class)
                .setMethod(ServiceMethods.FileSystem.Drives.Get)
                .setCallback(callback)
                .setDataEvent(DataEvents.Drives, DataEventType.REPLACE, NeptuoRoot.getUser().getId())
                .toRequestSent();
    }

    public static void createDrive(Drive drive, AsyncCallback<Drive> callback) {
        ServiceRequestBuilder.factory(Drive.class)
                .setCallback(callback)
                .setDataEvent(DataEvents.Drives, DataEventType.ADD, NeptuoRoot.getUser().getId())
                .setData(drive)
                .setMethod(ServiceMethods.FileSystem.Drives.Create)
                .toRequestSent();
    }

    public static void updateDrive(Drive drive, AsyncCallback<Drive> callback) {
        ServiceRequestBuilder.factory(Drive.class)
                .setCallback(callback)
                .setDataEvent(DataEvents.Drives, DataEventType.UPDATE, NeptuoRoot.getUser().getId())
                .setData(drive)
                .setMethod(ServiceMethods.FileSystem.Drives.Update)
                .toRequestSent();
    }

    public static void deleteDrive(int driveId, RequestCallback callback) {
        ServiceRequestBuilder.factory(Drive.class)
                .setCallback(callback)
                .setDataEvent(DataEvents.Drives, DataEventType.REMOVE, NeptuoRoot.getUser().getId())
                .setData(new Drive(driveId))
                .setMethod(ServiceMethods.FileSystem.Drives.Delete)
                .toRequestSent();
    }




    public static void getFolderItems(int parentId, boolean foldersOnly, String nameFilter, String extension, AsyncCallback<FsysItem> callback) {
        //TODO: Not this way!
        XmlWriter writer = XmlWriter.factory()
                .el("folders").el("folder").el("id", parentId).endEl("folder").endEl("folders")
                .el("foldersOnly", foldersOnly ? true : null)
                .el("filterName", nameFilter)
                .el("filterExtension", extension);

        ServiceRequestBuilder.factory(FsysItem.class)
                .setCallback(callback)
                .setDataEvent(DataEvents.FsysItems, DataEventType.REPLACE, new ExplorerFilterParams(foldersOnly, nameFilter, extension))
                .setXmlWriter(writer)
                .setMethod(ServiceMethods.FileSystem.Folders.Get)
                .toRequestSent();
    }

    public static void getFolderItems(String path, boolean foldersOnly, String nameFilter, String extension, AsyncCallback<FsysItem> callback) {
        //TODO: Not this way!
        XmlWriter writer = XmlWriter.factory()
                .el("folders").el("folder").el("path", path).endEl("folder").endEl("folders")
                .el("foldersOnly", foldersOnly ? true : null)
                .el("filterName", nameFilter)
                .el("filterExtension", extension);

        ServiceRequestBuilder.factory(FsysItem.class)
                .setCallback(callback)
                .setDataEvent(DataEvents.FsysItems, DataEventType.REPLACE, new ExplorerFilterParams(foldersOnly, extension, extension))
                .setXmlWriter(writer)
                .setMethod(ServiceMethods.FileSystem.Folders.Get)
                .toRequestSent();
    }

    public static void getFolderItemsInfo(int folderId, AsyncCallback<FsysItem> callback) {
        BaseModelData bmd = new BaseModelData();
        bmd.getModelType().setRoot("folders");
        bmd.getModelType().setRecordName("folder");
        bmd.getModelType().addField("id");

        bmd.set("id", folderId);

        ServiceRequestBuilder.factory(FsysItem.class)
                .setCallback(callback)
                .setDataEvent(DataEvents.FsysItems, DataEventType.ADD)
                .setModelType(bmd.getModelType())
                .setData(bmd)
                .setMethod(ServiceMethods.FileSystem.Folders.ItemsInfo)
                .toRequestSent();
    }

    public static void getItemsInfo(String path, AsyncCallback<FsysItem> callback) {
        BaseModelData bmd = new BaseModelData();
        bmd.getModelType().setRoot("folders");
        bmd.getModelType().setRecordName("folder");
        bmd.getModelType().addField("path");

        bmd.set("path", path);

        ServiceRequestBuilder.factory(FsysItem.class)
                .setCallback(callback)
                .setDataEvent(DataEvents.FsysItems, DataEventType.ADD)
                .setModelType(bmd.getModelType())
                .setData(bmd)
                .setMethod(ServiceMethods.FileSystem.Folders.ItemsInfo)
                .toRequestSent();
    }

    public static void createFolder(int parentId, String name, AsyncCallback<FsysItem> callback) {
        FsysItem model = new FsysItem(null, name, parentId);
        model.getModelType().setRoot("folders");
        model.getModelType().setRecordName("folder");

        ServiceRequestBuilder.factory(FsysItem.class)
                .setCallback(callback)
                .setDataEvent(DataEvents.FsysItems, DataEventType.ADD)
                .setData(model)
                .setMethod(ServiceMethods.FileSystem.Folders.Create)
                .toRequestSent();
    }

    public static void renameFolder(int folderId, String name, AsyncCallback<FsysItem> callback) {
        FsysItem model = new FsysItem(folderId, name);
        model.getModelType().setRoot("folders");
        model.getModelType().setRecordName("folder");

        ServiceRequestBuilder.factory(FsysItem.class)
                .setCallback(callback)
                .setDataEvent(DataEvents.FsysItems, DataEventType.UPDATE)
                .setData(model)
                .setMethod(ServiceMethods.FileSystem.Folders.Rename)
                .toRequestSent();
    }

    public static void deleteFolder(int folderId, final RequestCallback callback) {
        FsysItem model = new FsysItem(folderId);
        model.getModelType().setRoot("folders");
        model.getModelType().setRecordName("folder");

        ServiceRequestBuilder.factory(FsysItem.class)
                .setCallback(callback)
                .setDataEvent(DataEvents.FsysItems, DataEventType.REMOVE)
                .setData(model)
                .setMethod(ServiceMethods.FileSystem.Folders.Delete)
                .toRequestSent();
    }

    public static void moveFolder(int folderId, int targetId, AsyncCallback<FsysItem> callback) {
        ServiceRequestBuilder.factory(FsysItem.class)
                .setCallback(callback)
                .setDataEvent(DataEvents.FsysItems, DataEventType.UPDATE)
                .setData(new FileCopyMoveItem(folderId, targetId))
                .setMethod(ServiceMethods.FileSystem.Folders.Move)
                .toRequestSent();
    }

    public static void copyFolder(int folderId, int targetId, AsyncCallback<FsysItem> callback) {
        ServiceRequestBuilder.factory(FsysItem.class)
                .setCallback(callback)
                .setDataEvent(DataEvents.FsysItems, DataEventType.ADD)
                .setData(new FolderCopyMoveItem(folderId, targetId))
                .setMethod(ServiceMethods.FileSystem.Folders.Copy)
                .toRequestSent();
    }




    public static void createEmptyFile(int parentId, String name, AsyncCallback<FsysItem> callback) {
        FsysItem model = new FsysItem(null, name, parentId);
        model.getModelType().setRoot("files");
        model.getModelType().setRecordName("file");

        ServiceRequestBuilder.factory(FsysItem.class)
                .setCallback(callback)
                .setDataEvent(DataEvents.FsysItems, DataEventType.ADD)
                .setData(model)
                .setMethod(ServiceMethods.FileSystem.Files.CreateEmpty)
                .toRequestSent();
    }

    public static void createFile(int parentId, String name, String key, AsyncCallback<FsysItem> callback) {
        FsysItem model = new FsysItem(null, name, parentId, key);
        model.getModelType().setRoot("files");
        model.getModelType().setRecordName("file");

        ServiceRequestBuilder.factory(FsysItem.class)
                .setCallback(callback)
                .setDataEvent(DataEvents.FsysItems, DataEventType.ADD)
                .setData(model)
                .setMethod(ServiceMethods.FileSystem.Files.Create)
                .toRequestSent();
    }

    public static void renameFile(int fileId, String name, AsyncCallback<FsysItem> callback) {
        FsysItem model = new FsysItem(fileId, name);
        model.getModelType().setRoot("files");
        model.getModelType().setRecordName("file");

        ServiceRequestBuilder.factory(FsysItem.class)
                .setCallback(callback)
                .setDataEvent(DataEvents.FsysItems, DataEventType.UPDATE)
                .setData(model)
                .setMethod(ServiceMethods.FileSystem.Files.Rename)
                .toRequestSent();
    }

    public static void newVersionFile(int fileId, String key, RequestCallback callback) {
        FsysItem model = new FsysItem(fileId, null, key);
        model.getModelType().setRoot("files");
        model.getModelType().setRecordName("file");

        ServiceRequestBuilder.factory(FsysItem.class)
                .setCallback(callback)
                .setDataEvent(DataEvents.FsysItems, DataEventType.UPDATE)
                .setData(model)
                .setMethod(ServiceMethods.FileSystem.Files.NewVersion)
                .toRequestSent();
    }

    public static void deleteFile(int fileId, RequestCallback callback) {
        FsysItem model = new FsysItem(fileId);
        model.getModelType().setRoot("files");
        model.getModelType().setRecordName("file");

        ServiceRequestBuilder.factory(FsysItem.class)
                .setCallback(callback)
                .setDataEvent(DataEvents.FsysItems, DataEventType.REMOVE)
                .setData(model)
                .setMethod(ServiceMethods.FileSystem.Files.Delete)
                .toRequestSent();
    }

    public static void getFileContent(String publicId, final StringCallback callback) {
        RequestFactory.send(ServiceUrls.FileSystem.FileContent.toAbsolute() + ServiceUrls.FileSystem.PUBLICID_PARAM + publicId, "", new RequestCallback() {

            @Override
            public void onSuccess(Request request, Response response) {
                callback.onSucces(Format.htmlDecode(response.getText()));
            }

            @Override
            public void onClientError(Request request, Throwable exception) {
                callback.onClientError(exception);
            }

            @Override
            public void onServerError(ExceptionType exceptionType) {
                callback.onServerError(exceptionType);
            }
        });
    }

    public static void setFileContent(String publicId, String content, RequestCallback callback) {
        FsysItem file = new FsysItem();
        file.getModelType().setRoot("files");
        file.getModelType().setRecordName("file");

        file.setPublicId(publicId);
        file.setContent(content);

        ServiceRequestBuilder.factory()
                .setCallback(callback)
                .setData(file)
                .setMethod(ServiceMethods.FileSystem.FileContent.SetTextFileContent)
                .toRequestSent();
    }

    public static void moveFile(int fileId, int targetId, AsyncCallback<FsysItem> callback) {
        ServiceRequestBuilder.factory(FsysItem.class)
                .setCallback(callback)
                .setDataEvent(DataEvents.FsysItems, DataEventType.UPDATE)
                .setData(new FileCopyMoveItem(fileId, targetId))
                .setMethod(ServiceMethods.FileSystem.Files.Move)
                .toRequestSent();
    }

    public static void copyFile(int fileId, int targetId, AsyncCallback<FsysItem> callback) {
        ServiceRequestBuilder.factory(FsysItem.class)
                .setCallback(callback)
                .setDataEvent(DataEvents.FsysItems, DataEventType.ADD)
                .setData(new FileCopyMoveItem(fileId, targetId))
                .setMethod(ServiceMethods.FileSystem.Files.Copy)
                .toRequestSent();
    }
    



    public static void getPermissions(int itemId, ItemType type, AsyncCallback<Permission> callback) {
        BaseModelData bmd = new BaseModelData();
        bmd.getModelType().setRoot(getItemTypeName(type) + "s");
        bmd.getModelType().setRecordName(getItemTypeName(type));
        bmd.getModelType().addField("id");
        bmd.set("id", itemId);

        ServiceRequestBuilder.factory(Permission.class)
                .setCallback(callback)
                .setDataEvent(DataEvents.FsysPermissions, DataEventType.REPLACE, itemId)
                .setModelType(bmd.getModelType())
                .setData(bmd)
                .setMethod((type.equals(ItemType.FILE)) ? ServiceMethods.FileSystem.Files.GetPermissions : ServiceMethods.FileSystem.Folders.GetPermissions)
                .toRequestSent();
    }

    public static void addPermission(int targetId, int typeId, int identityId, ItemType type, AsyncCallback<Permission> callback) {
        ServiceRequestBuilder.factory(Permission.class)
                .setCallback(callback)
                .setDataEvent(DataEvents.FsysPermissions, DataEventType.ADD, targetId)
                .setData(new Permission(targetId, typeId, identityId))
                .setMethod((type.equals(ItemType.FILE)) ? ServiceMethods.FileSystem.Files.AddPermissions : ServiceMethods.FileSystem.Folders.AddPermissions)
                .toRequestSent();
    }

    public static void removePermission(int targetId, int typeId, int identityId, ItemType type, RequestCallback callback) {
        ServiceRequestBuilder.factory(Permission.class)
                .setCallback(callback)
                .setDataEvent(DataEvents.FsysPermissions, DataEventType.ADD, targetId)
                .setData(new Permission(targetId, typeId, identityId))
                .setMethod((type.equals(ItemType.FILE)) ? ServiceMethods.FileSystem.Files.RemovePermissions : ServiceMethods.FileSystem.Folders.RemovePermissions)
                .toRequestSent();
    }



    
    private static String getItemTypeName(ItemType type) {
        return type.equals(ItemType.FILE) ? "file" : "folder";
    }

    public static ItemType getItemType(FsysItem item) {
        return item.getType().equals("folder") ? ItemType.FOLDER : ItemType.FILE;
    }
}

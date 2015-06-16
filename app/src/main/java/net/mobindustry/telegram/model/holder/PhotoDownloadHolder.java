package net.mobindustry.telegram.model.holder;

import org.drinkless.td.libcore.telegram.TdApi;

public class PhotoDownloadHolder {

    private static PhotoDownloadHolder instance;
    private TdApi.Photo photo;

    private int fileId;
    private int messageId;

    public static synchronized PhotoDownloadHolder getInstance() {
        if (instance == null) {
            instance = new PhotoDownloadHolder();
        }
        return instance;
    }

    public TdApi.Photo getPhoto() {
        return photo;
    }

    public void setPhoto(TdApi.Photo file) {
        this.photo = file;
    }

    public void setLoadFileId(int id) {
        fileId = id;
    }

    public void setMessageId(int msgId) {
        messageId = msgId;
    }

    public int getFileId() {
        return fileId;
    }

    public int getMessageId() {
        return messageId;
    }
}

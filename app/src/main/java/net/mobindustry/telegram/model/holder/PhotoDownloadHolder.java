package net.mobindustry.telegram.model.holder;

import net.mobindustry.telegram.ui.activity.ChatActivity;

import org.drinkless.td.libcore.telegram.TdApi;

public class PhotoDownloadHolder {

    private static PhotoDownloadHolder instance;
    private TdApi.Photo photo;
    private ChatActivity activity;

    private int fileId;
    private int messageId;

    private Object sync = new Object();

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

    public ChatActivity getActivity() {
        return activity;
    }

    public void setActivity(ChatActivity activity) {
        this.activity = activity;
    }

    public Object getSync() {
        return sync;
    }
}

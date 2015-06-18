package net.mobindustry.telegram.ui.activity;

public interface ClientRequest {

    void getUser(long id);
    void getChatHistory(long id, int messageId, int offset, int limit);
    void getContacts();

    void downloadFile(int fileId);

    void downloadFile(int fileId, int messageId);

    void sendMessage(long chatId, String message);

    void sendPhotoMessage(long chatId, String path);
}

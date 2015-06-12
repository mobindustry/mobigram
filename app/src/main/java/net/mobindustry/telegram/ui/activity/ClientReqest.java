package net.mobindustry.telegram.ui.activity;

public interface ClientReqest {

    void getUser(long id);
    void getChats(int offset, int limit);
    void getChatHistory(long id, int messageId, int offset, int limit);

    void sendMessage(long chatId, String message);
}

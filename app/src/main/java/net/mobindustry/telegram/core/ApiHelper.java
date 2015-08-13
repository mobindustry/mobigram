package net.mobindustry.telegram.core;

import android.os.AsyncTask;

import net.mobindustry.telegram.core.handlers.BaseHandler;
import net.mobindustry.telegram.core.handlers.ChatHistoryHandler;
import net.mobindustry.telegram.core.handlers.MessageHandler;
import net.mobindustry.telegram.core.handlers.OkHandler;

import org.drinkless.td.libcore.telegram.TdApi;

public class ApiHelper {

    public static void downloadFile(int id) {
        new ApiClient<>(new TdApi.DownloadFile(id), new OkHandler(), handler).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    public static void clearChatHistory(long chatId) {
        new ApiClient<>(new TdApi.DeleteChatHistory(chatId), new OkHandler(), handler).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    public static void createPrivateChat(long userId) {
        new ApiClient<>(new TdApi.CreatePrivateChat((int) userId), new OkHandler(), handler).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    public static void authReset() {
        new ApiClient<>(new TdApi.AuthReset(), new OkHandler(), handler).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    public static void sendDocumentMessage(long chatId, String path) {
        new ApiClient<>(new TdApi.SendMessage(chatId, new TdApi.InputMessageDocument(path)), new MessageHandler(), handler).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    public static void sendVideoMessage(long chatId, String path) {
        new ApiClient<>(new TdApi.SendMessage(chatId, new TdApi.InputMessageVideo(path)), new MessageHandler(), handler).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    public static void sendPhotoMessage(long chatId, String path) {
        new ApiClient<>(new TdApi.SendMessage(chatId, new TdApi.InputMessagePhoto(path)), new MessageHandler(), handler).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    public static void sendTextMessage(long chatId, String message) {
        new ApiClient<>(new TdApi.SendMessage(chatId, new TdApi.InputMessageText(message)), new MessageHandler(), handler).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    public static void sendStickerMessage(long chatId, String path) {
        new ApiClient<>(new TdApi.SendMessage(chatId, new TdApi.InputMessageSticker(path)), new MessageHandler(), handler).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);

    }

    public static void sendGeoPointMessage(long chatId, double longitude, double latitude) {
        new ApiClient<>(new TdApi.SendMessage(chatId, new TdApi.InputMessageGeoPoint(longitude, latitude)), new MessageHandler(), handler).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    public static void sendContactMessage(long chatId, String phoneNumber, String userFirstName, String userLastName) {
        new ApiClient<>(new TdApi.SendMessage(chatId, new TdApi.InputMessageContact(phoneNumber, userFirstName, userLastName)), new MessageHandler(), handler).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    public static void sendForwardMessage(long chatId, long shownChatId, int[] toForwardMessagesId) {
        new ApiClient<>(new TdApi.ForwardMessages(chatId, shownChatId, toForwardMessagesId), new ChatHistoryHandler(), handler).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    private static ApiClient.OnApiResultHandler handler = new ApiClient.OnApiResultHandler() {
        @Override
        public void onApiResult(BaseHandler output) {
        }
    };



}

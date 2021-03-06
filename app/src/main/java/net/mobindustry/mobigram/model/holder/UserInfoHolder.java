package net.mobindustry.mobigram.model.holder;

import android.os.AsyncTask;

import net.mobindustry.mobigram.core.ApiClient;
import net.mobindustry.mobigram.core.handlers.BaseHandler;
import net.mobindustry.mobigram.core.handlers.UserHandler;

import org.drinkless.td.libcore.telegram.TdApi;

import java.util.Map;
import java.util.TreeMap;

public class UserInfoHolder {

    private static UserInfoHolder instance;

    private static TdApi.User user;

    private static Map<Integer, TdApi.User> map = new TreeMap<>();

    public static synchronized UserInfoHolder getInstance() {
        if (instance == null) {
            instance = new UserInfoHolder();
        }
        return instance;
    }

    private UserInfoHolder() {
    }

    public static TdApi.User getUser() {
        return user;
    }

    public static void setUser(TdApi.User user1) {
        user = user1;
        map.put(user1.id, user1);
    }

    public static void addUsersToMap(TdApi.Chats chats) {
        for (int i = 0; i < chats.chats.length; i++) {
            if (chats.chats[i].type.getConstructor() != TdApi.GroupChatInfo.CONSTRUCTOR) {
                new ApiClient<>(new TdApi.GetUser((int) chats.chats[i].id), new UserHandler(), new ApiClient.OnApiResultHandler() {
                    @Override
                    public void onApiResult(BaseHandler output) {
                        if (output.getHandlerId() == UserHandler.HANDLER_ID) {
                            TdApi.User user = (TdApi.User) output.getResponse();
                            map.put(user.id, user);
                        }
                    }
                }).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
            }
        }
    }

    public static TdApi.User getUser(int id) {
        return map.get(id);
    }
}

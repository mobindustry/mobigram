package net.mobindustry.telegram.model.holder;

import android.os.AsyncTask;

import net.mobindustry.telegram.core.ApiClient;
import net.mobindustry.telegram.core.handlers.BaseHandler;
import net.mobindustry.telegram.core.handlers.UserHandler;

import org.drinkless.td.libcore.telegram.TdApi;


import java.util.Map;
import java.util.TreeMap;

public class UserInfoHolder {

    private static UserInfoHolder instance;

    private TdApi.User user;

    private static Map<Integer, TdApi.User> map = new TreeMap<>();

    public static synchronized UserInfoHolder getInstance() {
        if (instance == null) {
            instance = new UserInfoHolder();
        }
        return instance;
    }

    private UserInfoHolder() {
    }

    public TdApi.User getUser() {
        return user;
    }

    public void setUser(TdApi.User user) {
        this.user = user;
        map.put(user.id, user);
    }

    public static void addUsersToMap(TdApi.Chats chats) {
        for (int i = 0; i < chats.chats.length; i++) {
            if(chats.chats[i].type.getConstructor() != TdApi.GroupChatInfo.CONSTRUCTOR) {
                new ApiClient<>(new TdApi.GetUser((int) chats.chats[i].id), new UserHandler(), new ApiClient.OnApiResultHandler() {
                    @Override
                    public void onApiResult(BaseHandler output) {
                        if(output.getHandlerId() == UserHandler.HANDLER_ID){
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

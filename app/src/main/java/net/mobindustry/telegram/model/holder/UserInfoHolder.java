package net.mobindustry.telegram.model.holder;

import org.drinkless.td.libcore.telegram.TdApi;


import java.util.Map;
import java.util.TreeMap;

public class UserInfoHolder {

    private static UserInfoHolder instance;

    private TdApi.User user;

    private static Map<Integer, TdApi.User> map = new TreeMap<>(); //TODO add users and get

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
    }

    public static void addUser(TdApi.User user) {
        map.put(user.id, user);
    }
}

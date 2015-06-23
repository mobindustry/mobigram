package net.mobindustry.telegram.model.holder;

import org.drinkless.td.libcore.telegram.TdApi;

public class UserMeHolder {

    private static UserMeHolder instance;

    private TdApi.User user;

    public static synchronized UserMeHolder getInstance() {
        if (instance == null) {
            instance = new UserMeHolder();
        }
        return instance;
    }

    private UserMeHolder() {
    }

    public TdApi.User getUser() {
        return user;
    }

    public void setUser(TdApi.User user) {
        this.user = user;
    }
}

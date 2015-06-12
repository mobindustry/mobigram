package net.mobindustry.telegram.model.holder;

import org.drinkless.td.libcore.telegram.TdApi;

public class UserMeHolder {

    private static UserMeHolder instance;

    private TdApi.User userMe;

    public static synchronized UserMeHolder getInstance() {
        if (instance == null) {
            instance = new UserMeHolder();
        }
        return instance;
    }

    private UserMeHolder() {
    }

    public TdApi.User getUserMe() {
        return userMe;
    }

    public void setUserMe(TdApi.User userMe) {
        this.userMe = userMe;
    }
}

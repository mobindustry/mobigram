package net.mobindustry.telegram.utils;

import org.drinkless.td.libcore.telegram.TdApi;

public class HeaderInfoHolder {

    private static HeaderInfoHolder instance;

    private TdApi.User userMe;

    public static synchronized HeaderInfoHolder getInstance() {
        if (instance == null) {
            instance = new HeaderInfoHolder();
        }
        return instance;
    }

    private HeaderInfoHolder() {
    }

    public TdApi.User getUserMe() {
        return userMe;
    }

    public void setUserMe(TdApi.User userMe) {
        this.userMe = userMe;
    }
}

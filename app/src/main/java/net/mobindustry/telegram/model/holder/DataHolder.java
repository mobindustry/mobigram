package net.mobindustry.telegram.model.holder;

import android.content.Context;

public class DataHolder {

    private static boolean isLoggedIn = false;
    private static Context context;
    private static boolean active = false;

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        DataHolder.context = context;
    }

    public static boolean isLoggedIn() {
        return isLoggedIn;
    }

    public static void setIsLoggedIn(boolean isLoggedIn) {
        DataHolder.isLoggedIn = isLoggedIn;
    }

    public static void setActive(boolean actv) {
        active = actv;
    }

    public static boolean isActive() {
        return active;
    }
}

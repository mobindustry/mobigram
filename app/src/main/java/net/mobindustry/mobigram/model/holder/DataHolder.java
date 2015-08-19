package net.mobindustry.mobigram.model.holder;

import android.content.Context;

public class DataHolder {

    private static boolean isLoggedIn = false;
    private static Context context;
    private static boolean active = false;
    private static String cachePath;
    private static int countNoInternetToast = 5;

    private static Context themedContext;

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

    public static void setCachePath(String s) {
        cachePath = s;
    }

    public static String getCachePath() {
        return cachePath;
    }

    public static void setThemedContext(Context themedContext1) {
        themedContext = themedContext1;
    }

    public static Context getThemedContext() {
        return themedContext;
    }

    public static int getCountNoInternetToast() {
        return countNoInternetToast;
    }

    public static void setCountNoInternetToast() {
        countNoInternetToast++;
    }

    public static void clearCountNoInternetToast() {
        countNoInternetToast = 5;
    }
}

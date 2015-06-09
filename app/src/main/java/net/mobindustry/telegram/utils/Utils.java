package net.mobindustry.telegram.utils;

import org.drinkless.td.libcore.telegram.TdApi;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

public class Utils {

    public static SimpleDateFormat getDateFormat(String type) {
        return new SimpleDateFormat(type);
    }

    public static String getInitials(String firstName, String lastName) {
        if (firstName.isEmpty()) {
            return ":)";
        }
        if (lastName.isEmpty()) {
            char[] iconText = new char[2];
            firstName.getChars(0, 1, iconText, 0);
            firstName.getChars(1, 2, iconText, 1);
            return ("" + iconText[0] + iconText[1]).toUpperCase();
        } else {
            char[] iconText = new char[2];
            firstName.getChars(0, 1, iconText, 0);
            lastName.getChars(0, 1, iconText, 1);
            return ("" + iconText[0] + iconText[1]).toUpperCase();
        }
    }

    public static TdApi.Message[] reverseMessages(TdApi.Message[] messages) {
        List<TdApi.Message> list = Arrays.asList(messages);
        Collections.reverse(list);
        return (TdApi.Message[]) list.toArray();
    }
}

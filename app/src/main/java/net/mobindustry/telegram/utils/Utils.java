package net.mobindustry.telegram.utils;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class Utils {

    public static SimpleDateFormat getDateFormat(String type) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(type);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        dateFormat.setLenient(false);
        return dateFormat;
    }
}

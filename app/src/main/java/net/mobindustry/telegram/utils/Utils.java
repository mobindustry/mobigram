package net.mobindustry.telegram.utils;

import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.text.SimpleDateFormat;

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

    public static ShapeDrawable getShapeDrawable(int size, int color) {
        ShapeDrawable circle = new ShapeDrawable(new OvalShape());
        circle.setIntrinsicHeight(size);
        circle.setIntrinsicWidth(size);
        circle.getPaint().setColor(color);
        return circle;
    }

    public static int getSW(WindowManager manager) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(displaymetrics);
        int screenWidth = displaymetrics.widthPixels;
        int screenHeight = displaymetrics.heightPixels;

        if(screenWidth > screenHeight) {
            return screenHeight;
        } else {
            return screenWidth;
        }
    }
}

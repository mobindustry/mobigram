package net.mobindustry.telegram.utils;

import android.os.Environment;

import java.io.File;

public class Const {

    public final static int IN_MESSAGE = 0;
    public final static int IN_CONTENT_MESSAGE = 1;
    public final static int OUT_MESSAGE = 2;
    public final static int OUT_CONTENT_MESSAGE = 3;
    public final static String COUNTRY = "country";
    public static final int REQUEST_CODE_TAKE_PHOTO = 101;
    public static final int REQUEST_CODE_SELECT_IMAGE = 102;

    public static final int REQUEST_CODE_NEW_MESSAGE = 110;
    public static final int CROP_REQUEST_CODE = 111;

    public static final String PATH_TO_NETELEGRAM = Environment.getExternalStorageDirectory() + File.separator + "NeTelegram";

    public static final String DATE_TIME_PHOTO_PATTERN = "yyyyMMdd_HHmmss";
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String TIME_PATTERN = "HH:mm";

    public static final int NEW_MESSAGE_FRAGMENT = 200;
    public static final int FILE_CHOOSE_FRAGMENT = 201;



    public static final int CHAT_NOT_FOUND = 1001;
}

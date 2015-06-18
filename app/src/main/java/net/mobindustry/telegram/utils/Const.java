package net.mobindustry.telegram.utils;

import android.os.Environment;

import java.io.File;

public class Const {

    public final static int IN_MESSAGE = 0;
    public final static int IN_CONTENT_MESSAGE = 1;
    public final static int IN_STICKER = 2;
    public final static int OUT_MESSAGE = 3;
    public final static int OUT_CONTENT_MESSAGE = 4;
    public final static int OUT_STICKER = 5;

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
    public static final int MAP_FRAGMENT = 202;

    public static final int CHAT_NOT_FOUND = 1001;

    public static final int CHATS_HANDLER_ID = 0x1;
    public static final int CONTACTS_HANDLER_ID = 0x2;
    public static final int MESSAGE_HANDLER_ID = 0x3;
    public static final int STICKERS_HANDLER_ID = 0x4;
    public static final int CHAT_HISTORY_HANDLER_ID = 0x5;
    public static final int DOWNLOAD_FILE_HANDLER_ID = 0x6;

    public static final int LOG_HANDLER_ID = 0x10;

    public static final String CLIENT_ID_FOR_FOURSQUARE = "ABDMITGWDOMKIGHTWRU25XKIS2MJ3IPJQOEMZ5CVTRR0H05I";
    public static final String CLIENT_SECRET_FOR_FOURSQUARE = "XHS2CWCC5VD23MP1W2120RMUUYZJXFTCHK4A5KA3JAEZRFRG";
}

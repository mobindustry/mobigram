package net.mobindustry.telegram.utils;

import android.os.Environment;

import net.mobindustry.telegram.model.holder.DataHolder;

import java.io.File;

public class Const {

    public final static int IN_MESSAGE = 0;
    public final static int IN_CONTENT_MESSAGE = 1;
    public final static int IN_STICKER = 2;
    public final static int OUT_MESSAGE = 3;
    public final static int OUT_CONTENT_MESSAGE = 4;
    public final static int OUT_STICKER = 5;

    public static final int REQUEST_CODE_TAKE_PHOTO = 101;
    public static final int REQUEST_CODE_SELECT_IMAGE = 102;
    public static final int REQUEST_CODE_FORWARD_MESSAGE_TO_CHAT = 103;
    public static final int REQUEST_CODE_NEW_MESSAGE = 110;
    public static final int CROP_REQUEST_CODE = 111;

    public static final String PATH_TO_GALLERY = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + File.separator + "NeTelegram";
    public static final String PATH_TO_THUMBS_GALLERY = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "NeTelegram"
            + File.separator + "thumb" + File.separator + "gallery";

    public static final String DATE_TIME_PHOTO_PATTERN = "yyyyMMdd_HHmmss";
    public static final String TIME_PATTERN = "HH:mm";

    public static final int CONTACT_LIST_FRAGMENT = 200;
    public static final int FILE_CHOOSE_FRAGMENT = 201;
    public static final int MAP_FRAGMENT = 202;
    public static final int GALLERY_FRAGMENT = 203;
    public static final int SELECTED_MAP_FRAGMENT = 204;
    public static final int SELECTED_FOLDER_FRAGMENT = 205;
    public static final int SEND_FOLDER_FRAGMENT = 206;
    public static final int USER_INFO_FRAGMENT = 207;
    public static final int SELECT_CHAT = 208;


    public static final int CHAT_NOT_FOUND = 1001;

    public static final int BASE_HANDLER_ID = 700;
    public static final int AUTH_HANDLER_ID = 701;
    public static final int CHATS_HANDLER_ID = 702;
    public static final int CONTACTS_HANDLER_ID = 703;
    public static final int MESSAGE_HANDLER_ID = 704;
    public static final int STICKERS_HANDLER_ID = 705;
    public static final int CHAT_HISTORY_HANDLER_ID = 706;
    public static final int DOWNLOAD_FILE_HANDLER_ID = 707;
    public static final int USER_HANDLER_ID = 708;
    public static final int USER_FULL_HANDLER_ID = 709;
    public static final int OK_HANDLER_ID = 710;
    public static final int CHAT_HANDLER_ID = 711;
    public static final int GROUP_CHAT_FULL_HANDLER_ID = 712;

    public static final int STICKER_HANDLER_ID = 710;

    public static final int LIST_PRELOAD_POSITION = 30;

    public static final int CHATS_LIST_OFFSET = 0;
    public static final int CHATS_LIST_LIMIT = 200;

    public static final String CLIENT_ID_FOR_FOURSQUARE = "ABDMITGWDOMKIGHTWRU25XKIS2MJ3IPJQOEMZ5CVTRR0H05I";
    public static final String CLIENT_SECRET_FOR_FOURSQUARE = "XHS2CWCC5VD23MP1W2120RMUUYZJXFTCHK4A5KA3JAEZRFRG";
    public static final String URL_FOR_FOURSQUARE = "https://api.foursquare.com";
    public static final String API_KEY_GIF = "dc6zaTOxFJmzC";
    public static final String URL_GIF = "http://api.giphy.com";
    public static final String API_KEY_IMAGES = "c8d349e8bc5be538e22c275a9600de25";
    public static final String SECRET_IMAGES = "ca961b34e289d221";

    public static final String READ_INBOX_ACTION = "net.mobindustry.telegram.message_read_action";
    public static final String NEW_MESSAGE_ACTION_ID = "net.mobindustry.telegram.new_message_action_id";
    public static final String NEW_MESSAGE_ACTION = "net.mobindustry.telegram.new_message_action";

    public static final String IMAGE_LOADER_PATH_PREFIX = "file://";

}

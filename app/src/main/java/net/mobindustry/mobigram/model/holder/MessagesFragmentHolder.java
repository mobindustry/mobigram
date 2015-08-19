package net.mobindustry.mobigram.model.holder;

import android.content.Context;
import android.util.Log;

import net.mobindustry.mobigram.ui.emoji.DpCalculator;
import net.mobindustry.mobigram.ui.emoji.Emoji;
import net.mobindustry.mobigram.utils.Const;
import net.mobindustry.mobigram.utils.Utils;

import org.drinkless.td.libcore.telegram.TdApi;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class MessagesFragmentHolder {

    private static MessagesFragmentHolder instance;
    private static boolean isMapCalled = false;
    private static File mobiGramDirectory;
    private File tempPhotoFile;
    private File tempVideoFile;
    private static TdApi.Chat chat;
    private static TdApi.Chats chats;
    private boolean isEmojiCreated = false;
    private static HashMap<Long, Integer> topMessageMap = new HashMap<>();
    private Emoji emoji;
    private static TdApi.Stickers stickers;

    public void clearFiles() {
        tempPhotoFile = null;
        tempVideoFile = null;
        System.gc();
    }

    public static void addToMap(Long chatId, Integer topMessageId) {
        topMessageMap.put(chatId, topMessageId);
    }

    public static int getTopMessage(Long chatId) {
        if (topMessageMap.get(chatId) == null) {
            return 0;
        } else {
            return topMessageMap.get(chatId);
        }
    }

    public static synchronized MessagesFragmentHolder getInstance() {
        if (instance == null) {
            instance = new MessagesFragmentHolder();
        }
        if (mobiGramDirectory == null) {
            mobiGramDirectory = getExternalStoragePublicPictureDir();
        }
        return instance;
    }

    public void makeEmoji(Context context) {
        if (!isEmojiCreated) {
            Log.e("Log", "MakeEmoji");
            emoji = new Emoji(context, new DpCalculator(Utils.getDensity(context.getResources())));
            emoji.makeEmoji();
            isEmojiCreated = true;
        }
    }

    public Emoji getEmoji() {
        return emoji;
    }

    public static File getExternalStoragePublicPictureDir() {
        Boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        // Check if device has SD card I save photo on it, if no I save photo on internal memory
        if (isSDPresent) {
            String dir = Const.PATH_TO_SAVE_PHOTOS;
            File path = new File(dir);
            path.mkdirs();
            return path;
        } else {
            String dir = Const.PATH_TO_SAVE_PHOTOS;
            File path = new File(dir);
            path.mkdirs();
            return path;
        }
    }

    private File getOutputMediaFileVideo() {
        String dir = Const.PATH_TO_SAVE_VIDEO;
        File path = new File(dir);
        path.mkdirs();
        SimpleDateFormat dateFormat = new SimpleDateFormat(Const.DATE_TIME_PHOTO_PATTERN);
        String fileName = "MOV_" + dateFormat.format(new Date()) + ".mp4";
        return new File(path.getAbsolutePath(), fileName);
    }

    private File getOutputMediaFile() {
        String dir = Const.PATH_TO_SAVE_PHOTOS;
        File path = new File(dir);
        path.mkdirs();
        SimpleDateFormat dateFormat = new SimpleDateFormat(Const.DATE_TIME_PHOTO_PATTERN);
        String fileName = "IMG_" + dateFormat.format(new Date()) + ".jpg";
        return new File(path.getAbsolutePath(), fileName);
    }

    public File getTempVideoFile() {
        return tempVideoFile;
    }

    public File getTempPhotoFile() {
        return tempPhotoFile;
    }

    public File getNewTempPhotoFile() {
        tempPhotoFile = getOutputMediaFile();
        return tempPhotoFile;
    }

    public File getNewTempVideoFile() {
        tempVideoFile = getOutputMediaFileVideo();
        return tempVideoFile;
    }

    public static TdApi.Chat getChat() {
        return chat;
    }

    public static void setChat(TdApi.Chat chat1) {
        chat = chat1;
    }

    public static boolean isMapCalled() {
        return isMapCalled;
    }

    public static void mapClosed() {
        isMapCalled = false;
    }

    public static void mapCalled() {
        isMapCalled = true;
    }

    public static TdApi.Stickers getStickers() {
        return stickers;
    }

    public static void setStickers(TdApi.Stickers stickers1) {
        stickers = stickers1;
    }

    public static TdApi.Chats getChats() {
        return chats;
    }

    public static void setChats(TdApi.Chats chats1) {
        chats = chats1;
    }
}

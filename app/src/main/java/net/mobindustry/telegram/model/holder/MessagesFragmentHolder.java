package net.mobindustry.telegram.model.holder;

import android.content.Context;
import android.util.Log;

import net.mobindustry.telegram.utils.Const;
import net.mobindustry.telegram.utils.Utils;
import net.mobindustry.telegram.ui.emoji.DpCalculator;
import net.mobindustry.telegram.ui.emoji.Emoji;

import org.drinkless.td.libcore.telegram.TdApi;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class MessagesFragmentHolder {

    private static MessagesFragmentHolder instance;
    private static boolean isMapCalled = false;
    private static File neTelegramDirectory;
    private File tempPhotoFile;
    private TdApi.Chat chat;
    private boolean isEmojiCreated = false;

    private static HashMap<Long, Integer> topMessageMap = new HashMap<>();

    private Emoji emoji;

    private static TdApi.Stickers stickers;

    public static void addToMap(Long chatId, Integer topMessageId) {
        topMessageMap.put(chatId, topMessageId);
    }

    public static int getTopMessage(Long chatId) {
        if(topMessageMap.get(chatId) == null) {
            return 0;
        } else {
            return topMessageMap.get(chatId);
        }
    }

    public static synchronized MessagesFragmentHolder getInstance() {
        if (instance == null) {
            instance = new MessagesFragmentHolder();
        }
        if (neTelegramDirectory == null) {
            neTelegramDirectory = getExternalStoragePublicPictureDir();
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
            String dir = Const.PATH_TO_GALLERY;
            File path = new File(dir);
            path.mkdirs();
            return path;
        } else {
            String dir = Const.PATH_TO_GALLERY;
            File path = new File(dir);
            path.mkdirs();
            return path;
        }
    }

    private File getOutputMediaFile() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(Const.DATE_TIME_PHOTO_PATTERN);
        String fileName = "IMG_" + dateFormat.format(new Date()) + ".jpg";
        return new File(neTelegramDirectory, fileName);
    }

    public File getTempPhotoFile() {
        return tempPhotoFile;
    }

    public File getNewTempPhotoFile() {
        tempPhotoFile = getOutputMediaFile();
        return tempPhotoFile;
    }

    public TdApi.Chat getChat() {
        return chat;
    }

    public void setChat(TdApi.Chat chat) {
        this.chat = chat;
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
}

package net.mobindustry.telegram.model.holder;

import android.os.Environment;

import net.mobindustry.telegram.utils.Const;

import org.drinkless.td.libcore.telegram.TdApi;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MessagesFragmentHolder {

    private static MessagesFragmentHolder instance;

    private static File neTelegramDirectory;
    private File tempPhotoFile;
    private TdApi.Chat chat;

    public static synchronized MessagesFragmentHolder getInstance() {
        if (instance == null) {
            instance = new MessagesFragmentHolder();
        }
        if (neTelegramDirectory == null) {
            neTelegramDirectory = getExternalStoragePublicPictureDir();
        }
        return instance;
    }

    public static File getExternalStoragePublicPictureDir() {
        Boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        // Check if device has SD card I save photo on it, if no I save photo on internal memory
        if (isSDPresent) {
            String dir = Environment.getExternalStorageDirectory() + File.separator + "NeTelegram" + File.separator + "photo";
            File path = new File(dir);
            path.mkdirs();
            return path;
        } else {
            String dir = Environment.getExternalStorageDirectory() + File.separator + "NeTelegram" + File.separator + "photo";
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
}

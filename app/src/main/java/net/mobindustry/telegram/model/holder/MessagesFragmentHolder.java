package net.mobindustry.telegram.model.holder;

import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MessagesFragmentHolder {

    private static MessagesFragmentHolder instance;

    private static File neTelegramDirectory;

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

    public File getOutputMediaFile() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String fileName = "IMG_" + dateFormat.format(new Date()) + ".jpg";
        return new File(neTelegramDirectory, fileName);
    }

}

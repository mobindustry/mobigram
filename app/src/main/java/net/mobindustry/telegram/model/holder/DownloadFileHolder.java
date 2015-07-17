package net.mobindustry.telegram.model.holder;

import android.util.Log;

import org.drinkless.td.libcore.telegram.TdApi;

import java.util.Map;
import java.util.TreeMap;

public class DownloadFileHolder {

    private static Map<Integer, String> map = new TreeMap<>(); //TODO find a problem with showing downloaded images

    public static void addFile(TdApi.UpdateFile file) {
        map.put(file.fileId, file.path);
    }

    public static String getUpdatedFilePath(int id) {
        return map.get(id);
    }

    public static void clearList() {
        map.clear();
    }
}

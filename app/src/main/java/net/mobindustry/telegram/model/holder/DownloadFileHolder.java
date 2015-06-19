package net.mobindustry.telegram.model.holder;

import android.util.Log;

import org.drinkless.td.libcore.telegram.TdApi;

import java.util.ArrayList;
import java.util.List;

public class DownloadFileHolder {

    private static List<TdApi.UpdateFile> list = new ArrayList<>();

    public static void addFile(TdApi.UpdateFile file) {
        Log.i("Log", "DownloadFileHolder: Add file " + file.fileId);
        list.add(file);
    }

    public static String getUpdatedFilePath(int id) {
        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).fileId == id) {
                return list.get(i).path;
            }
        }
        return null;
    }

    public static void clearList() {
        Log.i("Log", "DownloadFileHolder: List clear.");
        list.clear();
    }
}

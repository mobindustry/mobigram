package net.mobindustry.telegram.model.holder;

import org.drinkless.td.libcore.telegram.TdApi;

import java.util.Map;
import java.util.TreeMap;

public class DownloadFileHolder {

    private static Map<Integer, TdApi.FileLocal> map = new TreeMap<>();

    public static void addFile(TdApi.UpdateFile file) {
        map.put(file.fileId, new TdApi.FileLocal(file.fileId, file.size, file.path));
    }

    public static String getUpdatedFilePath(int id) {
        if (map.get(id) != null) {
            return map.get(id).path;
        } else {
            return null;
        }
    }

    public static TdApi.FileLocal getUpdatedFile(int id) {
        return map.get(id);
    }

    public static void clear() {
        map.clear();
    }
}


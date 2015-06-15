package net.mobindustry.telegram.utils;

import android.support.annotation.Nullable;

import org.drinkless.td.libcore.telegram.TdApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PhotoUtils {
    //    s	box	100x100
    //    m	box	320x320
    //    x	box	800x800
    //    y	box	1280x1280
    //    w	box	2560x2560
    //    a	crop	160x160
    //    b	crop	320x320
    //    c	crop	640x640
    //    d	crop	1280x1280
    public static final List<String> boxes = Arrays.asList("s", "m", "x", "y", "w");
    public static final List<String> crops = Arrays.asList("a", "b", "c", "d");
    public static final List<String> all = new ArrayList<>();

    static {
        all.addAll(boxes);
        all.addAll(crops);
    }

    public static TdApi.File findSmallestBiggerThan(TdApi.Photo p, int width, int height) {
        TdApi.PhotoSize s = null;
        for (String type : all) {
            TdApi.PhotoSize t = getSizeByType(p, type);
            if (t == null) {
                continue;
            }
            if (t.width > width || t.height > height){
                s = t;
                break;
            }
        }
        if (s == null) {
            s = p.photos[p.photos.length-1];
        }
        return s.photo;
    }

    @Nullable
    private static TdApi.PhotoSize getSizeByType(TdApi.Photo p, String type){
        for (TdApi.PhotoSize photo : p.photos) {
            if (photo.type.equals(type)) {
                return photo;
            }

        }
        return null;
    }

    public static TdApi.PhotoSize findBiggestSize(TdApi.Photo photo) {
        for (int i = boxes.size()-1; i >=0 ; i--) {
            TdApi.PhotoSize s = getSizeByType(photo, boxes.get(i));
            if (s != null){
                return s;
            }
        }
        for (int i = crops.size()-1; i >=0 ; i--) {
            TdApi.PhotoSize s = getSizeByType(photo, crops.get(i));
            if (s != null){
                return s;
            }
        }
        throw new IllegalStateException("wtf");
    }

    public static float getPhotoRation(TdApi.Photo photo) {
        TdApi.PhotoSize size = findBiggestSize(photo);
        return (float)size.width / size.height;
    }
}

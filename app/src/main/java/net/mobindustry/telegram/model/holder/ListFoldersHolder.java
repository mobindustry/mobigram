package net.mobindustry.telegram.model.holder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ListFoldersHolder {
    private static List<File>list=new ArrayList<>();


    public static List<File> getList() {
        return list;
    }

    public static void setList(List<File> list) {
        ListFoldersHolder.list = list;
    }


}

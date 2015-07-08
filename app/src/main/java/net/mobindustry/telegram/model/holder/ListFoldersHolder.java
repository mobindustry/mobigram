package net.mobindustry.telegram.model.holder;

import net.mobindustry.telegram.utils.FileWithIndicator;
import net.mobindustry.telegram.utils.FolderCustomGallery;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ListFoldersHolder {
    private static List<FileWithIndicator>list=new ArrayList<>();
    private static int checkQuantity=0;
    private static List<FolderCustomGallery>listFolders;
    private static List<String>listForSending;

    public static List<String> getListForSending() {
        return listForSending;
    }

    public static void setListForSending(List<String> listForSending) {
        ListFoldersHolder.listForSending = listForSending;
    }

    public static List<FolderCustomGallery> getListFolders() {
        return listFolders;
    }

    public static void setListFolders(List<FolderCustomGallery> listFolders) {
        ListFoldersHolder.listFolders = listFolders;
    }

    public static int getCheckQuantity() {
        return checkQuantity;
    }

    public static void setCheckQuantity(int checkQuantity) {
        ListFoldersHolder.checkQuantity = checkQuantity;
    }

    public static List<FileWithIndicator> getList() {
        return list;
    }

    public static void setList(List<FileWithIndicator> list) {
        ListFoldersHolder.list = list;
    }
}

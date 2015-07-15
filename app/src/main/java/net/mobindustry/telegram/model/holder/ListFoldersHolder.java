package net.mobindustry.telegram.model.holder;

import net.mobindustry.telegram.utils.FileWithIndicator;
import net.mobindustry.telegram.utils.FolderCustomGallery;
import net.mobindustry.telegram.utils.GiphyObject;
import net.mobindustry.telegram.utils.MediaGallery;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ListFoldersHolder {
    private static List<FileWithIndicator>list=new ArrayList<>();
    private static int checkQuantity=0;
    private static List<FolderCustomGallery>listFolders;
    private static List<MediaGallery>listForSending;
    private static String nameHolder;
    private static int currentSelectedPhoto;
    private static long chatID;
    private static List<GiphyObject> giphyObjectList;
    private static List<String>listGif;

    public static List<String> getListGif() {
        return listGif;
    }

    public static void setListGif(List<String> listGif) {
        ListFoldersHolder.listGif = listGif;
    }

    public static List<GiphyObject> getGiphyObjectList() {
        return giphyObjectList;
    }

    public static void setGiphyObjectList(List<GiphyObject> giphyObjectList) {
        ListFoldersHolder.giphyObjectList = giphyObjectList;
    }

    public static long getChatID() {
        return chatID;
    }

    public static void setChatID(long chatID) {
        ListFoldersHolder.chatID = chatID;
    }

    public static int getCurrentSelectedPhoto() {
        return currentSelectedPhoto;
    }

    public static void setCurrentSelectedPhoto(int currentSelectedPhoto) {
        ListFoldersHolder.currentSelectedPhoto = currentSelectedPhoto;
    }

    public static String getNameHolder() {
        return nameHolder;
    }

    public static void setNameHolder(String nameHolder) {
        ListFoldersHolder.nameHolder = nameHolder;
    }

    public static List<MediaGallery> getListForSending() {
        return listForSending;
    }

    public static void setListForSending(List<MediaGallery> listForSending) {
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

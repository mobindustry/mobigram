package net.mobindustry.telegram.model.holder;

import net.mobindustry.telegram.model.flickr.PhotosFlickr;
import net.mobindustry.telegram.utils.FileWithIndicator;
import net.mobindustry.telegram.utils.FolderCustomGallery;
import net.mobindustry.telegram.utils.MediaGallery;

import java.util.ArrayList;
import java.util.List;

public class ListFoldersHolder {
    private static List<FileWithIndicator> list = new ArrayList<>();
    private static int checkQuantity = 0;
    private static List<FolderCustomGallery> listFolders;
    private static List<MediaGallery> listForSending;
    private static String nameHolder;
    private static int currentSelectedPhoto;
    private static long chatID;
    private static List<String> listGif;
    private static List<String> listImages;
    private static PhotosFlickr holderPhotosFlickr;

    public static PhotosFlickr getHolderPhotosFlickr() {
        return holderPhotosFlickr;
    }

    public static void setHolderPhotosFlickr(PhotosFlickr holderPhotosFlickr) {
        ListFoldersHolder.holderPhotosFlickr = holderPhotosFlickr;
    }

    public static List<String> getListImages() {
        return listImages;
    }

    public static void setListImages(List<String> listImages) {
        ListFoldersHolder.listImages = listImages;
    }

    public static List<String> getListGif() {
        return listGif;
    }

    public static void setListGif(List<String> listGif) {
        ListFoldersHolder.listGif = listGif;
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

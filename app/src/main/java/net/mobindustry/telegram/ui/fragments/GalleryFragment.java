package net.mobindustry.telegram.ui.fragments;

import android.content.ContentResolver;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.ui.adapters.GalleryAdapter;
import net.mobindustry.telegram.utils.FolderCustomGallery;
import net.mobindustry.telegram.utils.ImagesFromMediaStore;
import net.mobindustry.telegram.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class GalleryFragment extends Fragment {

    GridView gridList;
    GalleryAdapter galleryAdapter;
    List<ImagesFromMediaStore> listImagesMediaStore = new ArrayList<>();
    Set<String> listDirLink = new HashSet<>();
    List<FolderCustomGallery> listFolders = new ArrayList<>();
    List<String> listDirNames = new ArrayList<>();
    private String[] dirLink;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gallery_fragment, container, false);
        galleryAdapter = new GalleryAdapter(getActivity());
        gridList = (GridView) view.findViewById(R.id.gridList);
        gridList.setAdapter(galleryAdapter);
        adjustGridViewPort();
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AsyncMediaStore asyncMediaStore = new AsyncMediaStore();
        asyncMediaStore.execute();
    }

    private void adjustGridViewPort() {
        gridList.setNumColumns(GridView.AUTO_FIT);
        gridList.setNumColumns(2);
        gridList.setHorizontalSpacing(15);
    }

    private void adjustGridViewLand() {
        gridList.setNumColumns(GridView.AUTO_FIT);
        gridList.setNumColumns(3);
        gridList.setHorizontalSpacing(15);
    }

    private void getAllImages() {
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.SIZE, MediaStore.MediaColumns.DISPLAY_NAME,
                MediaStore.MediaColumns.TITLE, MediaStore.MediaColumns.MIME_TYPE, MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Images.ImageColumns.IS_PRIVATE};
        ContentResolver contentResolver = getActivity().getContentResolver();
        Cursor cursor = contentResolver.query(uri, projection, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                ImagesFromMediaStore images = new ImagesFromMediaStore();
                int idxData = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                images.setData(cursor.getString(idxData));
                int idxSize = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.SIZE);
                images.setSize(cursor.getString(idxSize));
                int idxDisplayName = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME);
                images.setDisplayName(cursor.getString(idxDisplayName));
                int idxTitle = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.TITLE);
                images.setTitle(cursor.getString(idxTitle));
                int idxMime = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.MIME_TYPE);
                images.setMimType(cursor.getString(idxMime));
                int idxBucketName = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME);
                images.setBucketDisplayName(cursor.getString(idxBucketName));
                int idxIsPrivate = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.IS_PRIVATE);
                images.setIsPrivate(cursor.getString(idxIsPrivate));
                listImagesMediaStore.add(images);
            }
            cursor.close();
        }

    }

    private List<File> getPhotosFromFolder(String path) {

        File dir = new File(path);
        File[] fileList = dir.listFiles();
        List<File> listPhotos = new ArrayList<>();
        List<File> list = new ArrayList<>(Arrays.asList(fileList));
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).toString().contains((".png")) || list.get(i).toString().contains(".jpg")) {
                listPhotos.add(list.get(i));
            }
        }
        return listPhotos;
    }


    private void completeListFolders() {
        for (int i = 0; i < listDirNames.size(); i++) {
            FolderCustomGallery folderCustomGallery = new FolderCustomGallery();
            folderCustomGallery.setName(listDirNames.get(i));
            folderCustomGallery.setPath(dirLink[i]);
            folderCustomGallery.setPhotosInFolder(getPhotosFromFolder(dirLink[i]));
            folderCustomGallery.setPhotosQuantity(String.valueOf(getPhotosFromFolder(dirLink[i]).size()));
            if (folderCustomGallery.getPhotosInFolder().isEmpty()) {
                continue;
            } else {
                folderCustomGallery.setFirstPhoto(getPhotosFromFolder(dirLink[i]).get(0).toString());
                listFolders.add(folderCustomGallery);
            }
        }
    }

    private void getFoldersPath() {
        for (int i = 0; i < listImagesMediaStore.size(); i++) {
            String dirLink = "";
            String uri = listImagesMediaStore.get(i).getData();
            if (uri != null) {
                String[] segments = uri.split("/");
                for (int j = 0; j < segments.length - 1; j++) {
                    dirLink = dirLink + segments[j] + "/";
                }
                listDirLink.add(dirLink);
            }
        }
        dirLink = new String[listDirLink.size()];
        listDirLink.toArray(dirLink);
    }

    private void getDirNames() {
        for (int i = 0; i < dirLink.length; i++) {
            String[] segments = dirLink[i].split("/");
            String nameDir = segments[segments.length - 1];
            listDirNames.add(nameDir);
        }
    }

    private class AsyncMediaStore extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {
            getAllImages();
            getFoldersPath();
            getDirNames();
            completeListFolders();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (Utils.isTablet(getActivity())) {
                adjustGridViewPort();
                galleryAdapter.clear();
                galleryAdapter.addAll(listFolders);
            }
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && !Utils.isTablet(getActivity())) {
                adjustGridViewLand();
                galleryAdapter.clear();
                galleryAdapter.addAll(listFolders);
            } else {
                adjustGridViewPort();
                galleryAdapter.clear();
                galleryAdapter.addAll(listFolders);
            }

        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (Utils.isTablet(getActivity())) {
            adjustGridViewPort();
            galleryAdapter.clear();
            galleryAdapter.addAll(listFolders);
        }
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && !Utils.isTablet(getActivity())) {
            adjustGridViewLand();
            galleryAdapter.clear();
            galleryAdapter.addAll(listFolders);
        } else {
            adjustGridViewPort();
            galleryAdapter.clear();
            galleryAdapter.addAll(listFolders);
        }


    }

}




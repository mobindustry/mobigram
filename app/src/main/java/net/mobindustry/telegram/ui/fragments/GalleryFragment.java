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
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.model.holder.ListFoldersHolder;
import net.mobindustry.telegram.ui.adapters.GalleryAdapter;
import net.mobindustry.telegram.utils.Const;
import net.mobindustry.telegram.utils.FileWithIndicator;
import net.mobindustry.telegram.utils.FolderCustomGallery;
import net.mobindustry.telegram.utils.ImagesFromMediaStore;
import net.mobindustry.telegram.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class GalleryFragment extends Fragment {

    private GridView gridList;
    private GalleryAdapter galleryAdapter;
    private List<ImagesFromMediaStore> listImagesMediaStore = new ArrayList<>();
    private Set<String> listDirLink = new HashSet<>();
    private List<FolderCustomGallery> listFolders = new ArrayList<>();
    private List<String> dirLinkFolders;
    private FrameLayout findGifs;
    private FrameLayout findImages;
    private FrameLayout buttonCancel;
    private FrameLayout buttonSend;
    private Map<Long, String> map;
    private Map<Long, String> mapForCustomThumbs;
    private ProgressBar progressBar;
    private TextView numberPhotos;
    private Toolbar toolbar;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gallery_fragment, container, false);
        galleryAdapter = new GalleryAdapter(getActivity());
        gridList = (GridView) view.findViewById(R.id.gridList);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar_gallery);
        findGifs = (FrameLayout) view.findViewById(R.id.findGifs);
        findImages = (FrameLayout) view.findViewById(R.id.findImages);
        buttonCancel = (FrameLayout) view.findViewById(R.id.buttonCancel);
        buttonSend = (FrameLayout) view.findViewById(R.id.buttonSend);
        progressBar = (ProgressBar) view.findViewById(R.id.gallery_progress_bar);
        numberPhotos=(TextView)view.findViewById(R.id.numberPhotos);
        gridList.setAdapter(galleryAdapter);
        adjustGridViewPort();
        return view;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (Utils.isTablet(getActivity())) {
            progressBar.setVisibility(View.GONE);
            adjustGridViewPort();
            galleryAdapter.clear();
            galleryAdapter.addAll(listFolders);
        }
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && !Utils.isTablet(getActivity())) {
            progressBar.setVisibility(View.GONE);
            adjustGridViewLand();
            galleryAdapter.clear();
            galleryAdapter.addAll(listFolders);
        } else {
            progressBar.setVisibility(View.GONE);
            adjustGridViewPort();
            galleryAdapter.clear();
            galleryAdapter.addAll(listFolders);
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (Utils.isTablet(getActivity())) {
            if (ListFoldersHolder.getCheckQuantity()!=0){
                numberPhotos.setVisibility(View.VISIBLE);
                int sdk = android.os.Build.VERSION.SDK_INT;
                if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    numberPhotos.setBackgroundDrawable(Utils.getShapeDrawable(40, getActivity().getResources().getColor(R.color.message_notify)));
                } else {
                    numberPhotos.setBackground(Utils.getShapeDrawable(40,  getActivity().getResources().getColor(R.color.message_notify)));
                }

                numberPhotos.setText(String.valueOf(ListFoldersHolder.getCheckQuantity()));
            } else {
                numberPhotos.setVisibility(View.GONE);
            }
        } else {
            if (ListFoldersHolder.getCheckQuantity()!=0){
                numberPhotos.setVisibility(View.VISIBLE);
                int sdk = android.os.Build.VERSION.SDK_INT;
                if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    numberPhotos.setBackgroundDrawable(Utils.getShapeDrawable(60, getActivity().getResources().getColor(R.color.message_notify)));
                } else {
                    numberPhotos.setBackground(Utils.getShapeDrawable(60,  getActivity().getResources().getColor(R.color.message_notify)));
                }

                numberPhotos.setText(String.valueOf(ListFoldersHolder.getCheckQuantity()));
            } else {
                numberPhotos.setVisibility(View.GONE);
            }

        }
        if (ListFoldersHolder.getListFolders() == null) {
            AsyncMediaStore asyncMediaStore = new AsyncMediaStore();
            asyncMediaStore.execute();
        } else {
            if (Utils.isTablet(getActivity())) {
                progressBar.setVisibility(View.GONE);
                adjustGridViewPort();
                galleryAdapter.clear();
                galleryAdapter.addAll(ListFoldersHolder.getListFolders());
            }
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && !Utils.isTablet(getActivity())) {
                progressBar.setVisibility(View.GONE);
                adjustGridViewLand();
                galleryAdapter.clear();
                galleryAdapter.addAll(ListFoldersHolder.getListFolders());
            } else {
                progressBar.setVisibility(View.GONE);
                adjustGridViewPort();
                galleryAdapter.clear();
                galleryAdapter.addAll(ListFoldersHolder.getListFolders());
            }
        }
        toolbar.setTitle(R.string.photos);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setTitleTextColor(getResources().getColor(R.color.background_activity));
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return true;
            }
        });

        findGifs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO find gifs
            }
        });

        findImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO find images
            }
        });

        gridList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListFoldersHolder.setList(ListFoldersHolder.getListFolders().get(position).getPhotosInFolder());
                ListFoldersHolder.setNameHolder(ListFoldersHolder.getListFolders().get(position).getName());
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                FolderFragment folderFragment = new FolderFragment();
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_in_right);
                fragmentTransaction.replace(R.id.transparent_content, folderFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Log", "Button send");
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Log", "Button cancel");
            }
        });

    }

    private void checkThumbsInFolder() {
        mapForCustomThumbs = new HashMap<Long, String>();
        String path = Const.PATH_TO_THUMBS_GALLERY;
        List<File> files = getListFiles(new File(path));
        if (files.size() > 0) {
            for (int i = 0; i < files.size(); i++) {
                Long idForMap = Long.valueOf(separateName(files.get(i).getAbsolutePath()));
                String pathForMap = files.get(i).getAbsolutePath();
                mapForCustomThumbs.put(idForMap, pathForMap);
            }
        }

    }

    private List<File> getListFiles(File parentDir) {
        ArrayList<File> inFiles = new ArrayList<File>();
        File[] files = parentDir.listFiles();
        for (File file : files) {
            if (file.getName().endsWith(".png")) {
                inFiles.add(file);
            }
        }
        return inFiles;
    }

    private String separateName(String path) {
        String filename = path.substring(path.lastIndexOf("/") + 1);
        int pos = filename.lastIndexOf(".");
        String name = filename.substring(0, pos);
        return name;
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

    public Map<Long, String> getThumbAll() {
        Uri uri = MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI;
        map = new HashMap<Long, String>();
        String[] projection = {MediaStore.Images.Thumbnails.DATA, MediaStore.Images.Thumbnails.IMAGE_ID};
        Cursor cursor = MediaStore.Images.Thumbnails.queryMiniThumbnails(getActivity().getContentResolver(), uri,
                MediaStore.Images.Thumbnails.MINI_KIND, projection);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA));
                File file = new File(path);
                if (file.canRead()) {
                    int idxId = cursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails.IMAGE_ID);
                    long id = cursor.getLong(idxId);
                    map.put(id, path);
                }
            }
            cursor.close();
        }
        return map;
    }

    private void getAllImages() {
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.SIZE, MediaStore.MediaColumns.DISPLAY_NAME,
                MediaStore.MediaColumns.TITLE, MediaStore.MediaColumns.MIME_TYPE, MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Images.ImageColumns.IS_PRIVATE, MediaStore.Images.Media._ID};
        //Todo
        ContentResolver contentResolver = getActivity().getContentResolver();
        Cursor cursor = contentResolver.query(uri, projection, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                ImagesFromMediaStore images = new ImagesFromMediaStore();
                int idxData = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                images.setData(cursor.getString(idxData));
                int idxId = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
                images.setId(cursor.getLong(idxId));
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
                if (images.getData() != null){
                        listImagesMediaStore.add(images);
                } else {
                    cursor.moveToNext();
                }

            }
            cursor.close();
        }

    }


    private List<FileWithIndicator> getPhotosFromFolder(List<ImagesFromMediaStore> list) {
        List<FileWithIndicator> listPhotos = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getData().contains((".png")) || list.get(i).getData().contains(".jpeg") || list.get(i).getData().contains(".jpg")) {
                FileWithIndicator fileWithIndicator = new FileWithIndicator();
                File file = new File(list.get(i).getData());
                if (mapForCustomThumbs.get(list.get(i).getId()) != null){
                    String thumb = mapForCustomThumbs.get(list.get(i).getId());
                    fileWithIndicator.setThumbPhoto(thumb);
                } else {
                    if (map.get(list.get(i).getId()) != null) {
                        if (file.canRead()) {
                            String thumb = map.get(list.get(i).getId());
                            fileWithIndicator.setThumbPhoto(thumb);
                        } else {
                            fileWithIndicator.setThumbPhoto("");
                        }
                    }

                }
                fileWithIndicator.setFile(file);
                fileWithIndicator.setCheck(false);
                listPhotos.add(fileWithIndicator);
            }
        }
        return listPhotos;
    }


    private void completeListFolders() {
        for (int i = 0; i < dirLinkFolders.size(); i++) {
            FolderCustomGallery folderCustomGallery = new FolderCustomGallery();
            folderCustomGallery.setName(getDirNames(dirLinkFolders.get(i)));
            folderCustomGallery.setPath(dirLinkFolders.get(i));
            List<ImagesFromMediaStore> fromMediaStoreList = getListImagesMediaStoreInFolder(dirLinkFolders.get(i));
            folderCustomGallery.setPhotosInFolder(getPhotosFromFolder(fromMediaStoreList));
            folderCustomGallery.setPhotosQuantity(String.valueOf(getPhotosFromFolder(fromMediaStoreList).size()));
            if (folderCustomGallery.getPhotosInFolder().size() == 0) {
                continue;
            } else {
                folderCustomGallery.setFirstPhoto(folderCustomGallery.getPhotosInFolder().get(0).getFile().getAbsolutePath());
                folderCustomGallery.setFirstThumb(folderCustomGallery.getPhotosInFolder().get(0).getThumbPhoto());
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
        String[] dirLink = new String[listDirLink.size()];
        listDirLink.toArray(dirLink);
        dirLinkFolders = new ArrayList<String>(Arrays.asList(dirLink));
    }


    private List<ImagesFromMediaStore> getListImagesMediaStoreInFolder(String folderPath) {
        List<ImagesFromMediaStore> listPhotosInFolder = new ArrayList<>();
        for (int i = 0; i < listImagesMediaStore.size(); i++) {
            if (listImagesMediaStore.get(i).getData().contains(folderPath)) {
                listPhotosInFolder.add(listImagesMediaStore.get(i));
            }
        }
        return listPhotosInFolder;
    }

    private String getDirNames(String path) {
        String[] segments = path.split("/");
        String nameDir = segments[segments.length - 1];
        return nameDir;
    }

    private class AsyncMediaStore extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {
            getAllImages();
            getThumbAll();
            checkThumbsInFolder();
            getFoldersPath();
            completeListFolders();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ListFoldersHolder.setListFolders(listFolders);
            if (Utils.isTablet(getActivity())) {
                progressBar.setVisibility(View.GONE);
                adjustGridViewPort();
                galleryAdapter.clear();
                galleryAdapter.addAll(listFolders);
            }
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && !Utils.isTablet(getActivity())) {
                progressBar.setVisibility(View.GONE);
                adjustGridViewLand();
                galleryAdapter.clear();
                galleryAdapter.addAll(listFolders);
            } else {
                progressBar.setVisibility(View.GONE);
                adjustGridViewPort();
                galleryAdapter.clear();
                galleryAdapter.addAll(listFolders);
            }

        }

    }
}




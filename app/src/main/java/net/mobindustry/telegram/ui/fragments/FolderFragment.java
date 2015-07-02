package net.mobindustry.telegram.ui.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.res.Configuration;
import android.graphics.drawable.LevelListDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.model.holder.ListFoldersHolder;
import net.mobindustry.telegram.ui.adapters.FolderAdapter;
import net.mobindustry.telegram.utils.FolderCustomGallery;
import net.mobindustry.telegram.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FolderFragment extends Fragment {

    private GridView gridView;
    private FolderAdapter folderAdapter;
    private List<File> listFolders = new ArrayList<>();
    private ListFoldersHolder listFoldersHolder;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.folder_fragment, container, false);
        listFolders=listFoldersHolder.getList();
        Log.e("LOG","SSSSSSSSSS "+listFoldersHolder.getList().size());
        folderAdapter = new FolderAdapter(getActivity());
        gridView = (GridView) view.findViewById(R.id.gridPhotos);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (Utils.isTablet(getActivity())) {
            adjustGridViewPort();
            folderAdapter.clear();
            folderAdapter.addAll(listFolders);
        }
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && !Utils.isTablet(getActivity())) {
            adjustGridViewLand();
            folderAdapter.clear();
            folderAdapter.addAll(listFolders);
        } else {
            adjustGridViewPort();
            folderAdapter.clear();
            folderAdapter.addAll(listFolders);
        }
        folderAdapter.clear();
        folderAdapter.addAll(listFolders);
        gridView.setAdapter(folderAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

    }

    private void adjustGridViewPort() {
        gridView.setNumColumns(GridView.AUTO_FIT);
        gridView.setNumColumns(2);
        gridView.setHorizontalSpacing(15);
    }

    private void adjustGridViewLand() {
        gridView.setNumColumns(GridView.AUTO_FIT);
        gridView.setNumColumns(3);
        gridView.setHorizontalSpacing(15);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (Utils.isTablet(getActivity())) {
            adjustGridViewPort();
            folderAdapter.clear();
            folderAdapter.addAll(listFolders);
        }
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && !Utils.isTablet(getActivity())) {
            adjustGridViewLand();
            folderAdapter.clear();
            folderAdapter.addAll(listFolders);
        } else {
            adjustGridViewPort();
            folderAdapter.clear();
            folderAdapter.addAll(listFolders);
        }


    }

}

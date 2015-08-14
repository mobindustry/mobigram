package net.mobindustry.telegram.ui.fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.model.holder.ListFoldersHolder;
import net.mobindustry.telegram.ui.activity.PhotoViewPagerActivity;
import net.mobindustry.telegram.ui.adapters.FolderAdapter;
import net.mobindustry.telegram.utils.Utils;

public class FolderFragment extends Fragment {

    private GridView gridList;
    private FolderAdapter folderAdapter;
    private TextView numberPhotos;
    private FrameLayout buttonSend;
    private FrameLayout buttonCancel;
    private Toolbar toolbar;
    private String nameHolder = "";
    private FragmentTransaction ft;
    private LinearLayout layoutButtons;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.folder_fragment, container, false);
        nameHolder = ListFoldersHolder.getNameHolder();
        buttonSend = (FrameLayout) view.findViewById(R.id.buttonSendFolder);
        buttonCancel = (FrameLayout) view.findViewById(R.id.buttonCancelFolder);
        gridList = (GridView) view.findViewById(R.id.gridPhotos);
        numberPhotos = (TextView) view.findViewById(R.id.numberPhotosAll);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar_folder);
        layoutButtons = (LinearLayout) view.findViewById(R.id.layoutButtonsFolder);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Utils.drawBackgroundForCheckedPhoto(numberPhotos, buttonSend, getActivity());
        folderAdapter = new FolderAdapter(getActivity(), new FolderAdapter.LoadPhotos() {
            @Override
            public void load() {
                Utils.drawBackgroundForCheckedPhoto(numberPhotos, buttonSend, getActivity());
            }
        });
        Utils.changeButtonsWhenRotate(layoutButtons, null, folderAdapter, getActivity(), gridList);
        folderAdapter.clear();
        folderAdapter.addAll(ListFoldersHolder.getList());
        gridList.setAdapter(folderAdapter);
        gridList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListFoldersHolder.setCurrentSelectedPhoto(position);
                Intent intent = new Intent(getActivity(), PhotoViewPagerActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.sendMessageFromGallery(getActivity());
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListFoldersHolder.setListForSending(null);
                getActivity().finish();
            }
        });
        toolbar.setTitle(nameHolder);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setTitleTextColor(getResources().getColor(R.color.background_activity));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GalleryFragment galleryFragment;
                galleryFragment = new GalleryFragment();
                ft = getFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_in_right);
                ft.replace(R.id.transparent_content, galleryFragment);
                ft.commit();
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Utils.changeButtonsWhenRotate(layoutButtons, null, folderAdapter, getActivity(), gridList);
    }
}

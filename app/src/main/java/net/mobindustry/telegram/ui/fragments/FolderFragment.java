package net.mobindustry.telegram.ui.fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import android.widget.TextView;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.model.holder.ListFoldersHolder;
import net.mobindustry.telegram.ui.activity.PhotoViewPagerActivity;
import net.mobindustry.telegram.ui.activity.TransparentActivity;
import net.mobindustry.telegram.ui.adapters.FolderAdapter;
import net.mobindustry.telegram.utils.Const;
import net.mobindustry.telegram.utils.FileWithIndicator;
import net.mobindustry.telegram.utils.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FolderFragment extends Fragment {

    private GridView gridView;
    private FolderAdapter folderAdapter;
    private TextView numberPhotos;
    private FrameLayout buttonSend;
    private FrameLayout buttonCancel;
    private Toolbar toolbar;
    private String nameHolder = "";
    private FragmentTransaction ft;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.folder_fragment, container, false);
        nameHolder = ListFoldersHolder.getNameHolder();
        buttonSend = (FrameLayout) view.findViewById(R.id.buttonSendFolder);
        buttonCancel = (FrameLayout) view.findViewById(R.id.buttonCancelFolder);
        gridView = (GridView) view.findViewById(R.id.gridPhotos);
        numberPhotos = (TextView) view.findViewById(R.id.numberPhotosAll);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar_folder);
        if (Utils.isTablet(getActivity())) {
            if (ListFoldersHolder.getCheckQuantity() != 0) {
                numberPhotos.setVisibility(View.VISIBLE);
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) numberPhotos.getLayoutParams();
                params.leftMargin = 65;
                numberPhotos.setLayoutParams(params);
                int sdk = android.os.Build.VERSION.SDK_INT;
                if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    numberPhotos.setBackgroundDrawable(Utils.getShapeDrawable(40, getActivity().getResources().getColor(R.color.message_notify)));
                } else {
                    numberPhotos.setBackground(Utils.getShapeDrawable(40, getActivity().getResources().getColor(R.color.message_notify)));
                }

                numberPhotos.setText(String.valueOf(ListFoldersHolder.getCheckQuantity()));
            } else {
                numberPhotos.setVisibility(View.GONE);
            }
        } else {
            if (ListFoldersHolder.getCheckQuantity() != 0) {
                numberPhotos.setVisibility(View.VISIBLE);
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) numberPhotos.getLayoutParams();
                params.leftMargin = 60;
                numberPhotos.setLayoutParams(params);
                int sdk = android.os.Build.VERSION.SDK_INT;
                if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    numberPhotos.setBackgroundDrawable(Utils.getShapeDrawable(60, getActivity().getResources().getColor(R.color.message_notify)));
                } else {
                    numberPhotos.setBackground(Utils.getShapeDrawable(60, getActivity().getResources().getColor(R.color.message_notify)));
                }

                numberPhotos.setText(String.valueOf(ListFoldersHolder.getCheckQuantity()));
            } else {
                numberPhotos.setVisibility(View.GONE);
            }

        }
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10000) {
            int choice = data.getIntExtra("choice", 0);
            if (choice == Const.SEND_FOLDER_FRAGMENT) {
                getActivity().finish();
            }

        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        folderAdapter = new FolderAdapter(getActivity(), new FolderAdapter.LoadPhotos() {
            @Override
            public void load() {
                if (Utils.isTablet(getActivity())) {
                    if (ListFoldersHolder.getCheckQuantity() != 0) {
                        Log.e("Log", "TABLET");
                        numberPhotos.setVisibility(View.VISIBLE);
                        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) numberPhotos.getLayoutParams();
                        params.leftMargin = 65;
                        numberPhotos.setLayoutParams(params);
                        int sdk = android.os.Build.VERSION.SDK_INT;
                        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                            numberPhotos.setBackgroundDrawable(Utils.getShapeDrawable(40, getActivity().getResources().getColor(R.color.message_notify)));
                        } else {
                            numberPhotos.setBackground(Utils.getShapeDrawable(40, getActivity().getResources().getColor(R.color.message_notify)));
                        }

                        numberPhotos.setText(String.valueOf(ListFoldersHolder.getCheckQuantity()));
                    } else {
                        numberPhotos.setVisibility(View.GONE);
                    }
                } else {
                    if (ListFoldersHolder.getCheckQuantity() != 0) {
                        numberPhotos.setVisibility(View.VISIBLE);
                        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) numberPhotos.getLayoutParams();
                        params.leftMargin = 60;
                        numberPhotos.setLayoutParams(params);
                        int sdk = android.os.Build.VERSION.SDK_INT;
                        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                            numberPhotos.setBackgroundDrawable(Utils.getShapeDrawable(60, getActivity().getResources().getColor(R.color.message_notify)));
                        } else {
                            numberPhotos.setBackground(Utils.getShapeDrawable(60, getActivity().getResources().getColor(R.color.message_notify)));
                        }

                        numberPhotos.setText(String.valueOf(ListFoldersHolder.getCheckQuantity()));
                    } else {
                        numberPhotos.setVisibility(View.GONE);
                    }

                }
            }

        });

        if (Utils.isTablet(getActivity())) {
            adjustGridViewPort();
            folderAdapter.clear();
            folderAdapter.addAll(ListFoldersHolder.getList());
        }
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && !Utils.isTablet(getActivity())) {
            adjustGridViewLand();
            folderAdapter.clear();
            folderAdapter.addAll(ListFoldersHolder.getList());
        } else {
            adjustGridViewPort();
            folderAdapter.clear();
            folderAdapter.addAll(ListFoldersHolder.getList());
        }
        folderAdapter.clear();
        folderAdapter.addAll(ListFoldersHolder.getList());
        gridView.setAdapter(folderAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListFoldersHolder.setCurrentSelectedPhoto(position);
                Intent intent = new Intent(getActivity(), PhotoViewPagerActivity.class);
                startActivityForResult(intent, 10000);
            }
        });

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ListFoldersHolder.getListForSending() != null) {
                    for (int i = 0; i < ListFoldersHolder.getListForSending().size(); i++) {
                        ((TransparentActivity) getActivity()).sendPhotoMessage(ListFoldersHolder.getChatID(), ListFoldersHolder.getListForSending().get(i));
                    }
                    ListFoldersHolder.setListForSending(null);
                    getActivity().finish();
                }
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
            folderAdapter.addAll(ListFoldersHolder.getList());
        }
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && !Utils.isTablet(getActivity())) {
            adjustGridViewLand();
            folderAdapter.clear();
            folderAdapter.addAll(ListFoldersHolder.getList());
        } else {
            adjustGridViewPort();
            folderAdapter.clear();
            folderAdapter.addAll(ListFoldersHolder.getList());
        }

    }


}

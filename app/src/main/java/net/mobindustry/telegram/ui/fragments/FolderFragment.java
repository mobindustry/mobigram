package net.mobindustry.telegram.ui.fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.TextView;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.model.holder.ListFoldersHolder;
import net.mobindustry.telegram.ui.adapters.FolderAdapter;
import net.mobindustry.telegram.utils.FileWithIndicator;
import net.mobindustry.telegram.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class FolderFragment extends Fragment {

    private GridView gridView;
    private FolderAdapter folderAdapter;
    private List<FileWithIndicator> listFolders = new ArrayList<>();
    private ListFoldersHolder listFoldersHolder;
    private TextView numberPhotos;
    private FrameLayout buttonSend;
    private FrameLayout buttonCancel;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.folder_fragment, container, false);
        listFolders=listFoldersHolder.getList();
        buttonSend=(FrameLayout)view.findViewById(R.id.buttonSendFolder);
        buttonCancel=(FrameLayout)view.findViewById(R.id.buttonCancelFolder);
        gridView = (GridView) view.findViewById(R.id.gridPhotos);
        numberPhotos=(TextView)view.findViewById(R.id.numberPhotosAll);
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
        return view;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        folderAdapter = new FolderAdapter(getActivity(), new FolderAdapter.LoadPhotos() {
            @Override
            public void load() {
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

            }
        });

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

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO give files list
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
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

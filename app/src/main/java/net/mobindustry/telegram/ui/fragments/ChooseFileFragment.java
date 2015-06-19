package net.mobindustry.telegram.ui.fragments;

import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.utils.Const;

import java.text.DecimalFormat;

public class ChooseFileFragment extends Fragment {

    private static double mByte;
    private LinearLayout internalStorage;
    private LinearLayout systemRoot;
    private LinearLayout neTelegramDirectory;
    private LinearLayout gallery;
    private TextView infoDeviceMemory;
    private TextView pathToNeTelegram;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_file_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.chooseFileToolbar);
        toolbar.setTitle("Select File");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setTitleTextColor(getResources().getColor(R.color.background_activity));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        internalStorage = (LinearLayout) getActivity().findViewById(R.id.internal_storage);
        systemRoot = (LinearLayout) getActivity().findViewById(R.id.root_storage);
        neTelegramDirectory = (LinearLayout) getActivity().findViewById(R.id.neTelegram_storage);
        gallery = (LinearLayout) getActivity().findViewById(R.id.gallery);
        infoDeviceMemory = (TextView) getActivity().findViewById(R.id.text_info_about_internal_storage_memory);
        pathToNeTelegram = (TextView) getActivity().findViewById(R.id.path_to_ne_telegram_directory);

        pathToNeTelegram.setText(Const.PATH_TO_GALLERY); //TODO set correct path!!!
        infoDeviceMemory.setText("Free " + formatFileSize(getAvailableMemory()) + " of " + formatFileSize(getMaxMemory()));
    }

    public long getAvailableMemory(){
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        long bytesAvailable = (long) stat.getBlockSize() * (long) stat.getAvailableBlocks();
        return bytesAvailable;
    }

    public long getMaxMemory(){
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        long totalSize = (long)stat.getBlockCount()*(long)stat.getBlockSize();
        return totalSize;
    }

    public static String formatFileSize(long size) {
        String hrSize = null;

        double b = size;
        mByte = 1024.0;
        double k = size / mByte;
        double m = ((size / mByte) / mByte);
        double g = (((size / mByte) / mByte) / mByte);
        double t = ((((size / mByte) / mByte) / mByte) / mByte);

        DecimalFormat dec = new DecimalFormat("0.00");

        if (t > 1) {
            hrSize = dec.format(t).concat(" TB");
        } else if (g > 1) {
            hrSize = dec.format(g).concat(" GB");
        } else if (m > 1) {
            hrSize = dec.format(m).concat(" MB");
        } else if (k > 1) {
            hrSize = dec.format(k).concat(" KB");
        } else {
            hrSize = dec.format(b).concat(" Bytes");
        }

        return hrSize;
    }


}

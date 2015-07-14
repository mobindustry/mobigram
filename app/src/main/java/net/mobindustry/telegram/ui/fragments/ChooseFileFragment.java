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
import net.mobindustry.telegram.utils.Utils;

import java.text.DecimalFormat;

public class ChooseFileFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.choose_file_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.chooseFileToolbar);
        toolbar.setTitle(R.string.select_file);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setTitleTextColor(getResources().getColor(R.color.background_activity));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        LinearLayout internalStorage = (LinearLayout) getActivity().findViewById(R.id.internal_storage);
        LinearLayout systemRoot = (LinearLayout) getActivity().findViewById(R.id.root_storage);
        LinearLayout neTelegramDirectory = (LinearLayout) getActivity().findViewById(R.id.neTelegram_storage);
        LinearLayout gallery = (LinearLayout) getActivity().findViewById(R.id.gallery);

        TextView infoDeviceMemory = (TextView) getActivity().findViewById(R.id.text_info_about_internal_storage_memory);
        TextView pathToNeTelegram = (TextView) getActivity().findViewById(R.id.path_to_ne_telegram_directory);

        pathToNeTelegram.setText(Const.PATH_TO_GALLERY);
        infoDeviceMemory.setText("Free " + Utils.formatFileSize(getAvailableMemory()) + " of " + Utils.formatFileSize(getMaxMemory()));
    }

    public long getAvailableMemory() {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        return stat.getBlockSizeLong() * stat.getAvailableBlocksLong();
    }

    public long getMaxMemory() {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        return stat.getBlockCountLong() * stat.getBlockSizeLong();
    }


}

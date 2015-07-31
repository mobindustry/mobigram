package net.mobindustry.telegram.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.utils.Const;
import net.mobindustry.telegram.utils.ImageLoaderHelper;

public class PageFragment extends Fragment {

    static public PageFragment newInstance(int page, String path) {
        PageFragment pageFragment = new PageFragment();
        Bundle arguments = new Bundle();
        arguments.putInt("number", page);
        arguments.putSerializable("path", path);
        pageFragment.setArguments(arguments);
        return pageFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_fragment, null);
        ImageView tvPage = (ImageView) view.findViewById(R.id.itemFromFragment);
        String path = (String) getArguments().getSerializable("path");
        ImageLoaderHelper.displayImageList(Const.IMAGE_LOADER_PATH_PREFIX + path, tvPage);
        return view;
    }

}

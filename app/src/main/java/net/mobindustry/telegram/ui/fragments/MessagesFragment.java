package net.mobindustry.telegram.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

public class MessagesFragment extends Fragment {

    public static MessagesFragment newInstance(int index) {
        MessagesFragment f = new MessagesFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        f.setArguments(args);

        return f;
    }

    public int getShownIndex() {
        return getArguments().getInt("index", 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        String[] arr = new String[20];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = ""+i;
        }

        ScrollView scroller = new ScrollView(getActivity());
        TextView text = new TextView(getActivity());
        int padding = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                4, getActivity().getResources().getDisplayMetrics());
        text.setPadding(padding, padding, padding, padding);
        scroller.addView(text);
        text.setText(arr[getShownIndex()]);
        return scroller;
    }
}

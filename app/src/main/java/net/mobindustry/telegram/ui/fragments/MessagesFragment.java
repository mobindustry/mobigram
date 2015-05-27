package net.mobindustry.telegram.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import net.mobindustry.telegram.R;

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

        View view = inflater.inflate(R.layout.message_fragment, container, false);

        String[] str = new String[20];
        for (int i = 0; i < str.length; i++) {
            str[i] = "" + i + i + i + i;
        }
        TextView textView = (TextView) view.findViewById(R.id.fragmentTextView);
        textView.setText(str[getShownIndex()]);
        return view;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.messageFragmentTollbar);
        toolbar.setTitle("Message");
        toolbar.setNavigationIcon(R.drawable.ic_close_white);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO detach fragment;
            }
        });
    }
}

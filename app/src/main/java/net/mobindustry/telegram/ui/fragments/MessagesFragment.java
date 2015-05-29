package net.mobindustry.telegram.ui.fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.ui.adapters.MessageAdapter;
import net.mobindustry.telegram.ui.model.NeTelegramMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MessagesFragment extends Fragment {

    private MessageAdapter adapter;
    private static List<NeTelegramMessage> messageList = new ArrayList<>();

    public static MessagesFragment newInstance(int index) {
        MessagesFragment f = new MessagesFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        f.setArguments(args);

        return f;
    }


    public void setList(List<NeTelegramMessage> messageList1){
        messageList.clear();
        messageList.addAll(messageList1);
    }
    public int getShownIndex() {
        return getArguments().getInt("index", 0);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.message_fragment, container, false);

        ListView messageListView = (ListView) view.findViewById(R.id.messageListView);
        adapter = new MessageAdapter(getActivity());

        messageListView.setAdapter(adapter);

        adapter.addAll(messageList);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.messageFragmentToolbar);
        if (toolbar != null) {
            toolbar.setTitle("Message");
            toolbar.inflateMenu(R.menu.message_menu);

            if (getResources().getConfiguration().orientation
                    == Configuration.ORIENTATION_PORTRAIT) {
                toolbar.setNavigationIcon(R.drawable.ic_back);
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getActivity().onBackPressed();
                    }
                });
            } else {
                toolbar.setNavigationIcon(R.drawable.ic_close_white);
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getFragmentManager().beginTransaction().remove(MessagesFragment.this).commit();
                    }
                });
            }
        }
    }
}

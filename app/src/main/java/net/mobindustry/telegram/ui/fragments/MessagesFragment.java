package net.mobindustry.telegram.ui.fragments;

import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.ui.activity.ChatActivity;
import net.mobindustry.telegram.ui.adapters.MessageAdapter;
import net.mobindustry.telegram.model.Contact;
import net.mobindustry.telegram.model.NeTelegramMessage;
import net.mobindustry.telegram.utils.Utils;

import org.drinkless.td.libcore.telegram.TdApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MessagesFragment extends Fragment {

    private MessageAdapter adapter;
    private static List<TdApi.Message> messageList = new ArrayList<>();

    private TextView icon;
    private TextView name;
    private TextView lastSeenText;

    private TdApi.User user;
    private TdApi.Chat chat;
    private ChatActivity activity;


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

    public void setChatHistory(TdApi.Messages messages) {

        adapter.addAll(Utils.reverseMessages(messages.messages));
    }


    public void setUser(TdApi.User user) {
        this.user = user;

        Log.e("Log", "User " + user.toString());
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.message_fragment, container, false);

        ListView messageListView = (ListView) view.findViewById(R.id.messageListView);
        adapter = new MessageAdapter(getActivity(), ((ChatActivity) getActivity()).getMyId());
        messageListView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ChatActivity activity = (ChatActivity) getActivity();

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.messageFragmentToolbar);
        if (toolbar != null) {

            icon = (TextView) getActivity().findViewById(R.id.toolbar_text_icon);
            name = (TextView) getActivity().findViewById(R.id.toolbar_text_name);
            lastSeenText = (TextView) getActivity().findViewById(R.id.toolbar_text_last_seen);

            ChatListFragment fragment = (ChatListFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.titles);
            chat = fragment.getChat();

            TdApi.PrivateChatInfo privateChatInfo= (TdApi.PrivateChatInfo) chat.type; //TODO verify;
            TdApi.User chatUser = privateChatInfo.user;
//            TdApi.UserStatusOffline status = (TdApi.UserStatusOffline) chatUser.status;

            name.setText(chatUser.firstName + " " + chatUser.lastName);
            lastSeenText.setText("lastSeen"); //TODO
            icon.setText(Utils.getInitials(chatUser.firstName, chatUser.lastName));
            icon.setBackground(getBackground());
            activity.getChatHistory(chat.id, chat.topMessage.id, -1, 30);

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

    public ShapeDrawable getBackground() {
        ShapeDrawable circle = new ShapeDrawable(new OvalShape());
        circle.setIntrinsicHeight(60);
        circle.setIntrinsicWidth(60);
        circle.getPaint().setColor(Color.rgb(100, 100, 100));

        return circle;
    }
}

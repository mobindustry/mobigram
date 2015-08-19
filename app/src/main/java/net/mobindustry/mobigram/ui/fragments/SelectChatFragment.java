package net.mobindustry.mobigram.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import net.mobindustry.mobigram.R;
import net.mobindustry.mobigram.core.ApiClient;
import net.mobindustry.mobigram.core.handlers.BaseHandler;
import net.mobindustry.mobigram.core.handlers.ChatsHandler;
import net.mobindustry.mobigram.ui.activity.TransparentActivity;
import net.mobindustry.mobigram.ui.adapters.ChatListAdapter;
import net.mobindustry.mobigram.utils.Const;

import org.drinkless.td.libcore.telegram.TdApi;

public class SelectChatFragment extends Fragment {

    private TdApi.Chats chats;
    private ChatListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.select_chat_fragment, container, false);
    }

    public void getChats() {
        new ApiClient<>(new TdApi.GetChats(Const.CHATS_LIST_OFFSET, Const.CHATS_LIST_LIMIT), new ChatsHandler(), new ApiClient.OnApiResultHandler() {
            @Override
            public void onApiResult(BaseHandler output) {
                if (output.getHandlerId() == ChatsHandler.HANDLER_ID) {
                    chats = (TdApi.Chats) output.getResponse();
                    adapter.addAll(chats.chats);
                    ((TransparentActivity) getActivity()).progressBarGone();
                }
            }
        }).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.select_chat_toolbar);
        toolbar.setTitle(getString(R.string.select_chat_fragment_title));
        toolbar.setTitleTextColor(getResources().getColor(R.color.background_activity));
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        adapter = new ChatListAdapter(getActivity());
        ListView listView = (ListView) getActivity().findViewById(R.id.chats);
        listView.setAdapter(adapter);

        getChats();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), MessagesFragment.class);
                intent.putExtra("id", adapter.getItem(position).id);
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();
            }
        });
    }
}

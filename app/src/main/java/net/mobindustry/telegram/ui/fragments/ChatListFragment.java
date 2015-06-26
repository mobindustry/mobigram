package net.mobindustry.telegram.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.core.ApiClient;
import net.mobindustry.telegram.core.handlers.BaseHandler;
import net.mobindustry.telegram.core.handlers.ChatsHandler;
import net.mobindustry.telegram.ui.activity.TransparentActivity;
import net.mobindustry.telegram.ui.adapters.ChatListAdapter;
import net.mobindustry.telegram.utils.Const;

import org.drinkless.td.libcore.telegram.TdApi;

public class ChatListFragment extends ListFragment implements ApiClient.OnApiResultHandler {

    boolean dualPane;
    int currentCheckPosition = 0;
    private ChatListAdapter adapter;
    private ProgressBar progressBar;
    private static TdApi.Chats chats;

    private long clickedId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        adapter = new ChatListAdapter(getActivity());
        return inflater.inflate(R.layout.chat_list_fragment, null);
    }

    public void setChatsList(final TdApi.Chats chats) {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
        Log.i("LOG", "chatsFragment setList");
        this.chats = chats;
        adapter.clear();
        adapter.addAll(chats.chats);
    }

    public void getChatsList(int offset, int limit) {
        new ApiClient<>(new TdApi.GetChats(offset, limit), new ChatsHandler(), this).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    @Override
    public void onActivityCreated(Bundle savedState) {
        super.onActivityCreated(savedState);

        progressBar = (ProgressBar) getActivity().findViewById(R.id.chat_list_progress_bar);

        getChatsList(0, 200); //TODO constant or logical get;

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.attachToListView(getListView());
        fab.setColorPressedResId(R.color.background_floating_button_pressed);
        fab.setColorNormalResId(R.color.background_floating_button);
        fab.setShadow(true);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TransparentActivity.class);
                intent.putExtra("choice", Const.NEW_MESSAGE_FRAGMENT);

                startActivityForResult(intent, Const.REQUEST_CODE_NEW_MESSAGE);
            }
        });
        setListAdapter(adapter);

        View detailsFrame = getActivity().findViewById(R.id.messages);
        dualPane = detailsFrame != null
                && detailsFrame.getVisibility() == View.VISIBLE;
        if (savedState != null) {
            currentCheckPosition = savedState.getInt("curChoice", 0);
        }
        if (dualPane) {
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        }
    }

    public TdApi.Chat getChat() {
        for (int i = 0; i < chats.chats.length; i++) {
            if (chats.chats[i].id == clickedId) {
                return chats.chats[i];
            }
        }
        return chats.chats[currentCheckPosition];
    }

    public int getChatPosition(long id) {
        for (int i = 0; i < chats.chats.length; i++) {
            if (chats.chats[i].id == id) {
                return i;
            }
        }
        return Const.CHAT_NOT_FOUND;
    }

    public void setAdapterFilter(String filter) {
        adapter.getFilter().filter(filter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("curChoice", currentCheckPosition);
    }

    @Override
    public void onListItemClick(ListView l, View v, int pos, long id) {
        TdApi.Chat selectedItem = adapter.getItem(pos);
        Log.e("Log", selectedItem.toString());
        clickedId = selectedItem.id;
        showMessages(pos);
    }

    void showMessages(int index) {
        currentCheckPosition = index;

        FragmentTransaction ft
                = getFragmentManager().beginTransaction();
        getListView().setItemChecked(index, true);
        MessagesFragment messagesFragment = (MessagesFragment)
                getFragmentManager().findFragmentById(R.id.messages);
        if (messagesFragment == null || messagesFragment.getShownIndex() != index) {
            messagesFragment = MessagesFragment.newInstance(index);
            //ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_in_right);
            ft.replace(R.id.messages, messagesFragment);
            ft.commit();

            LinearLayout layout = (LinearLayout) getActivity().findViewById(R.id.fragment_layout);
            layout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Const.REQUEST_CODE_NEW_MESSAGE && resultCode == Activity.RESULT_OK) {
            long resultId = data.getLongExtra("id", 0);
            clickedId = resultId;
            int position = getChatPosition(resultId);
            if (position == Const.CHAT_NOT_FOUND) {
                Toast.makeText(getActivity(), "You have no chat with this contact. " +
                        "Open a new chat with a contact in the development mode.", Toast.LENGTH_LONG).show();
                //TODO Start new chat;
            } else {
                showMessages(position);
            }
        }
    }

    @Override
    public void onApiResult(BaseHandler output) {
        if (output.getHandlerId() == ChatsHandler.HANDLER_ID) {
            setChatsList((TdApi.Chats) output.getResponse());
        }
    }

    public void update(long chatId, int unread, int lastRead) {

        int count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            TdApi.Chat chat = adapter.getItem(i);
            if(chat.id == chatId) {
                adapter.getItem(i).unreadCount = unread;
                adapter.getItem(i).lastReadInboxMessageId = lastRead;
            }
        }
    }
}

package net.mobindustry.telegram.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.melnykov.fab.FloatingActionButton;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.core.ApiClient;
import net.mobindustry.telegram.core.handlers.BaseHandler;
import net.mobindustry.telegram.core.handlers.ChatHandler;
import net.mobindustry.telegram.core.handlers.ChatsHandler;
import net.mobindustry.telegram.core.handlers.OkHandler;
import net.mobindustry.telegram.ui.activity.ChatActivity;
import net.mobindustry.telegram.ui.activity.TransparentActivity;
import net.mobindustry.telegram.ui.adapters.ChatListAdapter;
import net.mobindustry.telegram.utils.Const;

import org.drinkless.td.libcore.telegram.TdApi;

public class ChatListFragment extends ListFragment{

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

    @Override
    public void onActivityCreated(Bundle savedState) {
        super.onActivityCreated(savedState);

        progressBar = (ProgressBar) getActivity().findViewById(R.id.chat_list_progress_bar);

        getChatsList(Const.CHATS_LIST_OFFSET, Const.CHATS_LIST_LIMIT);

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.attachToListView(getListView());
        fab.setColorPressedResId(R.color.background_floating_button_pressed);
        fab.setColorNormalResId(R.color.background_floating_button);
        fab.setShadow(true);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TransparentActivity.class);
                intent.putExtra("choice", Const.CONTACT_LIST_FRAGMENT);
                intent.putExtra("destination", "chatList");
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

        long id = ((ChatActivity) getActivity()).getIntentChatId();
        if (id != 0) {
            clickedId = id;
            int position = getChatPosition(id);
            showMessages(position);
        }
    }

    public void setChatsList(final TdApi.Chats chats) {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
        this.chats = chats;
        adapter.clear();
        adapter.addAll(chats.chats);
    }

    public void getChatsList(int offset, int limit) {
        new ApiClient<>(new TdApi.GetChats(offset, limit), new ChatsHandler(), new ApiClient.OnApiResultHandler() {
            @Override
            public void onApiResult(BaseHandler output) {
                if (output == null) {
                    getChatsList(0, 200);
                } else {
                    if (output.getHandlerId() == ChatsHandler.HANDLER_ID) {
                        setChatsList((TdApi.Chats) output.getResponse());
                    }
                }
            }
        }).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
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
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("curChoice", currentCheckPosition);
    }

    @Override
    public void onListItemClick(ListView l, View v, int pos, long id) {
        TdApi.Chat selectedItem = adapter.getItem(pos);
        clickedId = selectedItem.id;
        showMessages(pos);
    }

    void showMessages(int index) {
        currentCheckPosition = index;

        FragmentTransaction ft
                = getFragmentManager().beginTransaction();
        getListView().setItemChecked(index, true);
        MessagesFragment messagesFragment = MessagesFragment.newInstance(index);
        //ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_in_right);
        ft.replace(R.id.messages, messagesFragment);
        ft.commit();

        LinearLayout layout = (LinearLayout) getActivity().findViewById(R.id.fragment_layout);
        layout.setVisibility(View.GONE);
    }

    public void openChat(long resultId) {
        clickedId = resultId;
        int position = getChatPosition(resultId);
        if (position == Const.CHAT_NOT_FOUND) {
            newPrivateChat(resultId);
        } else {
            showMessages(position);
        }
    }

    private void newPrivateChat(long userId) {
        new ApiClient<>(new TdApi.CreatePrivateChat((int) userId), new OkHandler(), new ApiClient.OnApiResultHandler() {
            @Override
            public void onApiResult(BaseHandler output) {
            }
        }).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        new ApiClient<>(new TdApi.GetChat(userId), new ChatHandler(), new ApiClient.OnApiResultHandler() {
            @Override
            public void onApiResult(BaseHandler output) {
                if(output.getHandlerId() == ChatHandler.HANDLER_ID) {
                    TdApi.Chat chat = (TdApi.Chat) output.getResponse();
                    clickedId = chat.id;
                    addChatToChatsArray(chat);
                    adapter.clear();
                    adapter.addAll(chats.chats);
                    showMessages(adapter.getPosition(chat));
                }
            }
        }).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    private void addChatToChatsArray(TdApi.Chat chat) {
        TdApi.Chat[] newChatArray = new TdApi.Chat[chats.chats.length + 1];
        newChatArray[0] = chat;
        System.arraycopy(chats.chats, 0, newChatArray, 1, chats.chats.length);
        chats = new TdApi.Chats(newChatArray);
    }

    public void updateChat(long chatId, int unread, int lastRead) {
        int count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            TdApi.Chat chat = adapter.getItem(i);
            if (chat.id == chatId) {
                adapter.getItem(i).unreadCount = unread;
                adapter.getItem(i).lastReadInboxMessageId = lastRead;
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Const.REQUEST_CODE_NEW_MESSAGE && resultCode == Activity.RESULT_OK) {
            long resultId = data.getLongExtra("id", 0);
            openChat(resultId);
        }
    }
}

package net.mobindustry.telegram.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.ui.adapters.ChatListAdapter;
import net.mobindustry.telegram.utils.Utils;

import org.drinkless.td.libcore.telegram.TdApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChatListFragment extends ListFragment {

    boolean dualPane;
    int currentCheckPosition = 0;
    private List<TdApi.Chat> list = new ArrayList<>();
    private ChatListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        adapter = new ChatListAdapter(getActivity());
        return inflater.inflate(R.layout.contact_list_fragment, null);
    }

    public void setChatsList(final TdApi.Chats chats) {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                list.addAll(Arrays.asList(chats.chats));
                Log.i("LOG", "chatsFragment setList");
                adapter.clear();
                adapter.addAll(chats.chats);//TODO check this!!!
            }
        });

    }

    @Override
    public void onActivityCreated(Bundle savedState) {
        super.onActivityCreated(savedState);

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.attachToListView(getListView());

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Open New Message Fragment", Toast.LENGTH_SHORT).show();
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
            //showDetails(currentCheckPosition);
        }
    }

    public TdApi.Chat getChat() {
        return list.get(currentCheckPosition);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("curChoice", currentCheckPosition);
    }

    @Override
    public void onListItemClick(ListView l, View v, int pos, long id) {
        showDetails(pos);
    }

    void showDetails(int index) {
        currentCheckPosition = index;

        if (dualPane) {
            getListView().setItemChecked(index, true);
            MessagesFragment messagesFragment = (MessagesFragment)
                    getFragmentManager().findFragmentById(R.id.messages);
            if (messagesFragment == null || messagesFragment.getShownIndex() != index) {
                messagesFragment = MessagesFragment.newInstance(index);

                FragmentTransaction ft
                        = getFragmentManager().beginTransaction();
                ft.replace(R.id.messages, messagesFragment);
                ft.setTransition(
                        FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }
        } else {
            getListView().setItemChecked(index, true);
            MessagesFragment messagesFragment = (MessagesFragment)
                    getFragmentManager().findFragmentById(R.id.messages);
            if (messagesFragment == null || messagesFragment.getShownIndex() != index) {
                messagesFragment = MessagesFragment.newInstance(index);

                FragmentTransaction ft
                        = getFragmentManager().beginTransaction();
                ft.replace(R.id.messages, messagesFragment);
                ft.setTransition(
                        FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }
        }

    }
}

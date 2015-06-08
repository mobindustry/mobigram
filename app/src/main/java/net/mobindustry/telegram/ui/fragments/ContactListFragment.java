package net.mobindustry.telegram.ui.fragments;

import android.content.Intent;
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
import net.mobindustry.telegram.ui.activity.MessagesActivity;
import net.mobindustry.telegram.ui.adapters.ContactsListAdapter;
import net.mobindustry.telegram.model.Contact;

import org.drinkless.td.libcore.telegram.TdApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ContactListFragment extends ListFragment {

    boolean dualPane;
    int currentCheckPosition = 0;
    private List<TdApi.Chat> list = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.contact_list_fragment, null);
    }

    public void setChatsList(TdApi.Chats chats) {
        list.addAll(Arrays.asList(chats.chats));
        Log.i("LOG", "contactsFragment setList");
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

        ContactsListAdapter adapter = new ContactsListAdapter(getActivity());
        setListAdapter(adapter);

        Log.i("LOG", "adapter.addAll");
        adapter.addAll(list);

        View detailsFrame = getActivity().findViewById(R.id.details);
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
            MessagesFragment details = (MessagesFragment)
                    getFragmentManager().findFragmentById(R.id.details);
            if (details == null || details.getShownIndex() != index) {
                details = MessagesFragment.newInstance(index);

                //details.setList(list.get(currentCheckPosition).getList());
                //details.setDataForToolbar(list.get(currentCheckPosition));

                FragmentTransaction ft
                        = getFragmentManager().beginTransaction();
                ft.replace(R.id.details, details);
                ft.setTransition(
                        FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }
        } else {
            Intent intent = new Intent();
            intent.setClass(getActivity(), MessagesActivity.class);
            intent.putExtra("index", index);
            //intent.putExtra("contact", list.get(index));
            startActivity(intent);
        }
    }
}

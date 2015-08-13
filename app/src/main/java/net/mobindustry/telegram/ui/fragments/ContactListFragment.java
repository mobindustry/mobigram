package net.mobindustry.telegram.ui.fragments;

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

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.core.ApiClient;
import net.mobindustry.telegram.core.handlers.BaseHandler;
import net.mobindustry.telegram.core.handlers.ContactsHandler;
import net.mobindustry.telegram.ui.adapters.ContactListAdapter;

import org.drinkless.td.libcore.telegram.TdApi;

public class ContactListFragment extends Fragment {

    private TdApi.Contacts contacts;
    private ContactListAdapter adapter;
    private String destination;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.contact_list_fragment, container, false);
    }

    public void getContacts() {
        new ApiClient<>(new TdApi.GetContacts(), new ContactsHandler(), new ApiClient.OnApiResultHandler() {
            @Override
            public void onApiResult(BaseHandler output) {
                if (output.getHandlerId() == ContactsHandler.HANDLER_ID) {
                    contacts = (TdApi.Contacts) output.getResponse();
                    adapter.addAll(contacts.users);
                }

            }
        }).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.newMessageToolbar);
        toolbar.setTitle(getString(R.string.new_message_fragment_title));
        toolbar.setTitleTextColor(getResources().getColor(R.color.background_activity));
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        adapter = new ContactListAdapter(getActivity());
        ListView listView = (ListView) getActivity().findViewById(R.id.contacts);
        listView.setAdapter(adapter);

        getContacts();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                if (destination.equals("chatList")) {
                    intent = new Intent(getActivity(), ChatListFragment.class);
                } else {
                    intent = new Intent(getActivity(), UserInfoFragment.class);
                }
                intent.putExtra("id", (long) adapter.getItem(position).id);
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();
            }
        });
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}

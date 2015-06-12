package net.mobindustry.telegram.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.ui.adapters.ContactListAdapter;
import net.mobindustry.telegram.model.holder.ContactListHolder;

import org.drinkless.td.libcore.telegram.TdApi;

public class NewMessageFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_message_fragment, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ContactListHolder holder = ContactListHolder.getInstance();

        Toolbar toolbar = (Toolbar) getView().findViewById(R.id.newMessageToolbar);
        toolbar.setTitle("New message");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        final ContactListAdapter adapter = new ContactListAdapter(getActivity());
        ListView listView = (ListView) getActivity().findViewById(R.id.contacts);
        listView.setAdapter(adapter);

        TdApi.Contacts contacts = holder.getContacts();
        adapter.addAll(contacts.users);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ChatListFragment.class);

                intent.putExtra("id", (long)adapter.getItem(position).id);
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();
            }
        });

    }
}

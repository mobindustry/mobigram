package net.mobindustry.telegram.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.ui.activity.MessagesActivity;
import net.mobindustry.telegram.ui.adapters.ContactsListAdapter;
import net.mobindustry.telegram.ui.model.Contact;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ContactListFragment extends ListFragment {

    boolean mDualPane;
    int mCurCheckPosition = 0;
    private List<Contact> list;

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

        String[] firstNames = new String[]{"sam", "Tommi", "Frontier", "Fedor", "Lex", "Pet", "Max", "sam", "Tommi", "Frontier", "Fedor", "Lex", "Pet", "Max", "sam", "Tommi", "Frontier", "Fedor", "Lex", "Pet", "Max"};
        String[] lastNames = new String[]{"Max", "sam", "Tommi", "frontier", "Fedor", "Lex", "Pet", "frontier", "Fedor", "Lex", "Pet", "frontier", "Fedor", "Lex", "Pet", "frontier", "Fedor", "Lex", "Pet", "frontier", "Fedor"};

        list = new ArrayList<>();
        for (int i = 0; i < firstNames.length; i++) {
            list.add(new Contact(firstNames[i], lastNames[i]));
        }

        adapter.addAll(list);

        View detailsFrame = getActivity().findViewById(R.id.details);
        mDualPane = detailsFrame != null
                && detailsFrame.getVisibility() == View.VISIBLE;

        if (savedState != null) {
            mCurCheckPosition = savedState.getInt("curChoice", 0);
        }

        if (mDualPane) {
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            //showDetails(mCurCheckPosition);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("curChoice", mCurCheckPosition);
    }

    @Override
    public void onListItemClick(ListView l, View v, int pos, long id) {
        showDetails(pos);
    }

    void showDetails(int index) {
        mCurCheckPosition = index;

        if (mDualPane) {
            getListView().setItemChecked(index, true);
            MessagesFragment details = (MessagesFragment)
                    getFragmentManager().findFragmentById(R.id.details);
            if (details == null || details.getShownIndex() != index) {
                details = MessagesFragment.newInstance(index);
                details.setList(list.get(mCurCheckPosition).getList());

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
            intent.putExtra("messages", (Serializable) list.get(index).getList());
            startActivity(intent);
        }
    }
}

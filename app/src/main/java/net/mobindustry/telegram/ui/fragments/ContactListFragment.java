package net.mobindustry.telegram.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.ui.activity.MessagesActivity;
import net.mobindustry.telegram.ui.adapters.ContactsListAdapter;
import net.mobindustry.telegram.ui.model.Contact;

import java.util.ArrayList;
import java.util.List;

public class ContactListFragment extends ListFragment {

    boolean mDualPane;
    int mCurCheckPosition = 0;

    @Override
    public void onActivityCreated(Bundle savedState) {
        super.onActivityCreated(savedState);

        ContactsListAdapter adapter = new ContactsListAdapter(getActivity());
        setListAdapter(adapter);

        List<Contact> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(new Contact("first " + i, "last " + i, "bla bla bla bla bla message " + i));
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
           // showDetails(mCurCheckPosition);
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
            startActivity(intent);
        }
    }


}

package net.mobindustry.telegram.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import net.mobindustry.telegram.R;

public class YourNameFragment extends Fragment {

    private EditText firstName;
    private EditText lastName;
    private TextView cancel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.your_name_fragment, container, false);

        firstName = (EditText) getActivity().findViewById(R.id.first_name);
        lastName = (EditText) getActivity().findViewById(R.id.last_name);
        cancel = (TextView) getActivity().findViewById(R.id.cancel_registration);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.yor_name_toolbar);
        toolbar.inflateMenu(R.menu.ok);
        toolbar.setTitle(getActivity().getString(R.string.your_name_fragment_title));
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                getActivity().setFirstLastName(firstName.getText().toString().trim(), lastName.getText().toString().trim());
                return true;
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO cancel registration;
            }
        });
    }
}

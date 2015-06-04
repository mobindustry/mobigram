package net.mobindustry.telegram.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.ui.activity.RegistrationActivity;

public class YourNameFragment extends Fragment {

    private EditText firstName;
    private EditText lastName;
    private TextView cancel;
    private RegistrationActivity activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.your_name_fragment, container, false);



        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = (RegistrationActivity) getActivity();
        firstName = (EditText) activity.findViewById(R.id.first_name);
        lastName = (EditText) activity.findViewById(R.id.last_name);
        cancel = (TextView) activity.findViewById(R.id.cancel_registration);



        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.yor_name_toolbar);
        toolbar.inflateMenu(R.menu.ok);
        toolbar.setTitle(getActivity().getString(R.string.your_name_fragment_title));
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                activity.setFirstLastName(firstName.getText().toString().trim(), lastName.getText().toString().trim());
                Log.e("Log", "SET NAME " + firstName.getText().toString());
                return true;
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("onClick");
            }
        });
    }
}

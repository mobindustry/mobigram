package net.mobindustry.mobigram.ui.fragments;

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

import net.mobindustry.mobigram.R;
import net.mobindustry.mobigram.model.holder.InfoRegistration;
import net.mobindustry.mobigram.ui.activity.RegistrationActivity;

public class YourNameFragment extends Fragment {

    private EditText firstName;
    private EditText lastName;
    private RegistrationActivity activity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.your_name_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InfoRegistration infoRegistration = InfoRegistration.getInstance();
        activity = (RegistrationActivity) getActivity();
        firstName = (EditText) activity.findViewById(R.id.first_name);
        lastName = (EditText) activity.findViewById(R.id.last_name);
        firstName.setText(infoRegistration.getFirstName());
        lastName.setText(infoRegistration.getLastName());
        TextView cancel = (TextView) activity.findViewById(R.id.cancel_registration);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.yor_name_toolbar);
        toolbar.inflateMenu(R.menu.ok);
        toolbar.setTitle(getActivity().getString(R.string.your_name_fragment_title));

        toolbar.setNavigationIcon(R.drawable.ic_back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                infoRegistration.setFirstName(firstName.getText().toString().trim());
                infoRegistration.setLastName(lastName.getText().toString().trim());
                activity.setFirstLastName(infoRegistration.getFirstName(), infoRegistration.getLastName());
                return true;
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RegistrationActivity) getActivity()).setAuthReset();
            }
        });
    }
}

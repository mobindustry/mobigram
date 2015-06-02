package net.mobindustry.telegram.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.internal.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.ui.activity.RegistrationActivity;
import net.mobindustry.telegram.utils.Const;
import net.mobindustry.telegram.utils.CountryObject;

public class RegistrationMainFragment extends Fragment {

    private TextView chooseCountry;
    private EditText code;
    private EditText phone;
    private ChooseCountryList chooseCountryList;
    private FragmentTransaction fragmentTransaction;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.registration_main_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        RegistrationActivity activity = (RegistrationActivity) getActivity();
        CountryObject countryObject = activity.getCountryObject();

        chooseCountry = (TextView) getActivity().findViewById(R.id.chooseCountry);
        code = (EditText) getActivity().findViewById(R.id.code);
        phone = (EditText) getActivity().findViewById(R.id.phone);

        if (countryObject != null) {

            Log.e("log", "Name " + countryObject.getCountryName());
            chooseCountry.setText(countryObject.getCountryName());
            code.setText(countryObject.getCountryCode());
        }
        chooseCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction.replace(R.id.fragmentContainer, chooseCountryList);
                fragmentTransaction.commit();
            }
        });


        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.your_phone);

        fragmentTransaction = getFragmentManager().beginTransaction();


        chooseCountryList = new ChooseCountryList();


    }
}

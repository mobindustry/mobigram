package net.mobindustry.telegram.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.ui.activity.RegistrationActivity;
import net.mobindustry.telegram.ui.adapters.CountriesListAdapter;
import net.mobindustry.telegram.utils.CountryObject;
import net.mobindustry.telegram.model.holder.InfoRegistration;
import net.mobindustry.telegram.utils.ListCountryObject;

import java.io.Serializable;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class ChooseCountryList extends Fragment implements Serializable {
    private StickyListHeadersListView list;
    private CountriesListAdapter countriesListAdapter;
    private FragmentTransaction fragmentTransaction;
    private RegistrationMainFragment registrationMainFragment;
    private ListCountryObject countries;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_country_fragment, container, false);
        list = (StickyListHeadersListView) view.findViewById(R.id.countriesList);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        InfoRegistration infoRegistration = InfoRegistration.getInstance();
        Log.e("LOG", "PHONE " + infoRegistration.getPhone());
        RegistrationActivity activity = (RegistrationActivity) getActivity();
        countries = activity.getListCountryObject();


        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.choose_country);
        toolbar.setTitleTextColor(getResources().getColor(R.color.background_activity));
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setClickable(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction = getFragmentManager().beginTransaction();
                registrationMainFragment = new RegistrationMainFragment();
                fragmentTransaction.replace(R.id.fragmentContainer, registrationMainFragment);
                getActivity().getSupportFragmentManager().popBackStack();
                fragmentTransaction.commit();
            }
        });


        countriesListAdapter = new CountriesListAdapter(getActivity(), countries);
        list.setAdapter(countriesListAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                fragmentTransaction = getFragmentManager().beginTransaction();

                registrationMainFragment = new RegistrationMainFragment();

                CountryObject countryObject = countries.getListCountries().get(position);
                if (getActivity() instanceof RegistrationActivity) {
                    RegistrationActivity activity = (RegistrationActivity) getActivity();
                    activity.setCountryObject(countryObject);
                }

                fragmentTransaction.replace(R.id.fragmentContainer, registrationMainFragment);
                getActivity().getSupportFragmentManager().popBackStack();
                fragmentTransaction.commit();
            }
        });
    }


}

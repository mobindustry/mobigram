package net.mobindustry.telegram.ui.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Filter;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.ui.activity.RegistrationActivity;
import net.mobindustry.telegram.ui.adapters.CountriesListAdapter;
import net.mobindustry.telegram.utils.CountryObject;
import net.mobindustry.telegram.model.holder.InfoRegistration;
import net.mobindustry.telegram.utils.ListCountryObject;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class ChooseCountryList extends Fragment implements Serializable {

    private StickyListHeadersListView list;
    private FragmentTransaction fragmentTransaction;
    private RegistrationMainFragment registrationMainFragment;
    private ListCountryObject countries;
    private InfoRegistration infoRegistration;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_country_fragment, container, false);
        list = (StickyListHeadersListView) view.findViewById(R.id.countriesList);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        infoRegistration = InfoRegistration.getInstance();
        countries = infoRegistration.getListCountryObject();
        final CountriesListAdapter countriesListAdapter = new CountriesListAdapter(getActivity(),countries);
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
        toolbar.inflateMenu(R.menu.search_country);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                SearchView sv = new SearchView(getActivity());
                EditText txtSearch = ((EditText)sv.findViewById(android.support.v7.appcompat.R.id.search_src_text));
                txtSearch.setTextColor(Color.WHITE);
                MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_IF_ROOM | MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
                MenuItemCompat.setActionView(item, sv);
                sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        Log.e("Log", "Submit " + query);
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        if (!newText.isEmpty()) {
                            Log.e("Log", "Change " + newText);
                            countriesListAdapter.getFilter().filter(newText);
                        } else {
                            countriesListAdapter.getFilter().filter("");
                        }
                        return true;
                    }
                });
                return false;
            }
        });
        list.setAdapter(countriesListAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("Log", "Yes ");
                fragmentTransaction = getFragmentManager().beginTransaction();
                registrationMainFragment = new RegistrationMainFragment();
                CountryObject countryObject =countries.getListTmp().get(position);
                infoRegistration.setCountryObject(null);
                infoRegistration.setCountryObject(countryObject);
                infoRegistration.setCodeCountryLetters(countryObject.getCountryStringCode());
                fragmentTransaction.replace(R.id.fragmentContainer, registrationMainFragment);
                fragmentTransaction.commit();
            }
        });
    }


}

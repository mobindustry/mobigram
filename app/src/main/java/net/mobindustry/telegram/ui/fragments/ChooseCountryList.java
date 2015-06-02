package net.mobindustry.telegram.ui.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.ui.adapters.CountriesListAdapter;
import net.mobindustry.telegram.utils.ListCountryObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.Writer;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class ChooseCountryList extends Fragment implements Serializable {
    StickyListHeadersListView list;
    private CountriesListAdapter countriesListAdapter;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_country_fragment, container, false);
        list = (StickyListHeadersListView) view.findViewById(R.id.countriesList);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.choose_country);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setClickable(true);
        toolbar.inflateMenu(R.menu.search_for_tool_bar);


        //Take file countries.txt from assets folder and parse it to String extFileFromAssets.
        String textFileFromAssets = null;

        InputStream is = null;
        try {
            is = getResources().getAssets().open("countries.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            textFileFromAssets = convertStreamToString(is);
        } catch (IOException e) {
            e.printStackTrace();
        }


        ListCountryObject countries = new ListCountryObject(textFileFromAssets);
        for (int i = 0; i < countries.getListCountries().size(); i++) {
            Log.e("log", " " + countries.getListCountries().get(i).getCountryCode()
                    + " " + countries.getListCountries().get(i).getCountryStringCode()
                    + " " + countries.getListCountries().get(i).getCountryName()
                    + " " + countries.getListCountries().get(i).getInitialLetter());

        }
        //Log.e("log", " " + countries.getListHeaderPositions().toString());
        Log.e("log", " " + countries.getRowsQuantity());
        countriesListAdapter = new CountriesListAdapter(getActivity(), countries);
        list.setAdapter(countriesListAdapter);



    }

    public static String convertStreamToString(InputStream is)
            throws IOException {
        Writer writer = new StringWriter();

        char[] buffer = new char[2048];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is,
                    "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } finally {
            is.close();
        }
        String text = writer.toString();
        return text;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search_for_tool_bar, menu);
    }


    @Override
    public void setHasOptionsMenu(boolean hasMenu) {
        super.setHasOptionsMenu(hasMenu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                SearchView sv = new SearchView(getActivity());
                MenuItemCompat.setShowAsAction(item,
                        MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW |
                                MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
                MenuItemCompat.setActionView(item, sv);
                sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        System.out.println(query);
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        System.out.println(newText);
                        return false;
                    }
                });
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}

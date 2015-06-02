package net.mobindustry.telegram.ui.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.AdapterView;
import android.widget.EditText;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.ui.adapters.CountriesListAdapter;
import net.mobindustry.telegram.utils.Const;
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
    private StickyListHeadersListView list;
    private CountriesListAdapter countriesListAdapter;
    private FragmentTransaction fragmentTransaction;
    private RegistrationMainFragment registrationMainFragment;


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
        countriesListAdapter = new CountriesListAdapter(getActivity(), countries);
        list.setAdapter(countriesListAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                fragmentTransaction = getFragmentManager().beginTransaction();

                registrationMainFragment = new RegistrationMainFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(Const.COUNTRY_WITH_CODE,);
                fragmentTransaction.replace(R.id.fragmentContainer, registrationMainFragment);
                fragmentTransaction.commit();
            }
        });
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


}

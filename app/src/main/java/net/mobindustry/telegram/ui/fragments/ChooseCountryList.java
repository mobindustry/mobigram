package net.mobindustry.telegram.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.applidium.headerlistview.HeaderListView;
import com.applidium.headerlistview.SectionAdapter;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.ui.adapters.ListAdapter;

public class ChooseCountryList extends Fragment {
    HeaderListView list;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_country_fragment, container, false);
        list = (HeaderListView) view.findViewById(R.id.countriesList);


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        list.setAdapter(new SectionAdapter() {
            @Override
            public int numberOfSections() {
                return 4;
            }

            @Override
            public int numberOfRows(int section) {
                return 35;
            }

            @Override
            public View getRowView(int section, int row, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = (TextView) getActivity().getLayoutInflater().inflate(getResources().getLayout(android.R.layout.simple_list_item_1), null);
                }
                ((TextView) convertView).setText("     Section " + section + " Row " + row);
                return convertView;
            }

            @Override
            public int getSectionHeaderViewTypeCount() {
                return 2;
            }

            @Override
            public int getSectionHeaderItemViewType(int section) {
                return section;
            }
            @Override
            public boolean hasSectionHeaderView(int section) {
                return true;
            }


            @Override
            public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {

                if (convertView == null) {
                    if (getSectionHeaderItemViewType(section) == 0) {
                        convertView = (TextView) getActivity().getLayoutInflater().inflate(getResources().getLayout(android.R.layout.simple_list_item_1), null);
                    } else {
                        convertView = getActivity().getLayoutInflater().inflate(getResources().getLayout(android.R.layout.simple_list_item_2), null);
                    }
                }

                if (getSectionHeaderItemViewType(section) == 0) {
                    ((TextView) convertView).setText(String.valueOf(section));
                } else {
                    ((TextView) convertView.findViewById(android.R.id.text1)).setText(String.valueOf(section));
                }
                convertView.setBackgroundColor(getResources().getColor(R.color.background_action_bar));


                return convertView;
            }

            @Override
            public Object getRowItem(int section, int row) {
                return null;
            }
        });


        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.choose_country);
        //((AppCompatActivity)getActivity()).getSupportActionBar().setLogo(R.drawable.ic_back);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);


    }
}

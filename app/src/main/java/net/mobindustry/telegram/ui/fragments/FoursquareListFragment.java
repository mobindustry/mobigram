package net.mobindustry.telegram.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import net.mobindustry.telegram.R;
import net.mobindustry.telegram.model.foursquare.FoursquareVenue;
import net.mobindustry.telegram.model.holder.FoursquareHolder;
import net.mobindustry.telegram.ui.adapters.FoursquareAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FoursquareListFragment extends Fragment implements Serializable {
    private FragmentTransaction ft;
    private Toolbar toolbar;
    private FoursquareAdapter foursquareAdapter;
    private ListView foursquareList;
    private List<FoursquareVenue> foursquareVenueList=new ArrayList<>();
    private double lng;
    private double lat;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        foursquareAdapter = new FoursquareAdapter(getActivity());
        foursquareAdapter.clear();
        FoursquareHolder foursquareHolder=FoursquareHolder.getInstance();
        foursquareVenueList =foursquareHolder.getFoursquareVenueList() ;
        Log.e("LOG", "LIST " + foursquareVenueList.size());
        Log.e("LOG", "ADAPTER " + foursquareAdapter);
        foursquareAdapter.addAll(foursquareVenueList);
        View view = inflater.inflate(R.layout.foursquare_list_fragment_layout, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        foursquareList = (ListView) getActivity().findViewById(R.id.foursquare_list);
        foursquareList.setAdapter(foursquareAdapter);
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar_foursquare_list);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setTitle(R.string.text_nearest_checkpoints);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationFragment locationFragment;
                locationFragment = new LocationFragment();
                ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.transparent_content, locationFragment);
                ft.commit();
            }
        });

        foursquareList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lat = foursquareVenueList.get(position).getFoursquareLocation().getLatitude();
                lng = foursquareVenueList.get(position).getFoursquareLocation().getLongitude();
                Toast.makeText(getActivity(),String.valueOf(lat)+" "+String.valueOf(lng),Toast.LENGTH_SHORT).show();
            }
        });


    }
}

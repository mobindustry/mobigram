package net.mobindustry.telegram.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

import net.mobindustry.telegram.R;

public class LocationFragment extends Fragment {

    private SupportMapFragment mapFragment;
    private GoogleMap map;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.location_fragment_layout,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map);
        Log.e("LOg","MAP "+mapFragment);
        map = mapFragment.getMap();
        if (map == null) {
            getActivity().finish();
            return;
        }
        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        init();
    }

    private void init() {
    }

}

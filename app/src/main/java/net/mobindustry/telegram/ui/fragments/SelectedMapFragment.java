package net.mobindustry.telegram.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.model.holder.InfoLocation;

public class SelectedMapFragment extends Fragment {

    private GoogleMap map;
    private Marker myMarker;

    private LatLng userLocation;
    private InfoLocation infoLocation;

    public void setUserLocation(double lng, double lat) {
        userLocation = new LatLng(lat, lng);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.selected_map_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        infoLocation = InfoLocation.getInstance();

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar_map);
        toolbar.inflateMenu(R.menu.map_menu);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setTitle(R.string.text_location);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.satellite_item_menu:
                        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                        init();
                        return true;
                    case R.id.hybrid_item_menu:
                        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                        init();
                        return true;
                    case R.id.map_item_menu:
                        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        init();
                        return true;
                }
                return false;
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                map = googleMap;
                map.setMyLocationEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
                init();
            }
        });
    }

    private void init() {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(userLocation)
                .zoom(14)
                .build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        map.animateCamera(cameraUpdate);
        if (myMarker == null) {
            infoLocation.setLng(new LatLng(userLocation.latitude, userLocation.longitude));
            myMarker = map.addMarker(new MarkerOptions()
                    .position(new LatLng(userLocation.latitude, userLocation.longitude))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        }
    }
}

package net.mobindustry.telegram.ui.fragments;

import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.melnykov.fab.FloatingActionButton;
import com.turbomanage.httpclient.HttpResponse;
import com.turbomanage.httpclient.ParameterMap;
import com.turbomanage.httpclient.android.AndroidHttpClient;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.core.ApiClient;
import net.mobindustry.telegram.core.handlers.BaseHandler;
import net.mobindustry.telegram.core.handlers.MessageHandler;
import net.mobindustry.telegram.model.foursquare.FoursquareObj;
import net.mobindustry.telegram.model.foursquare.FoursquareVenue;
import net.mobindustry.telegram.model.holder.FoursquareHolder;
import net.mobindustry.telegram.model.holder.InfoLocation;
import net.mobindustry.telegram.model.holder.MessagesFragmentHolder;
import net.mobindustry.telegram.ui.fragments.FoursquareListFragment;
import net.mobindustry.telegram.utils.Const;

import org.drinkless.td.libcore.telegram.TdApi;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SelectedMapFragment extends Fragment {

    private GoogleMap map;
    private Marker myMarker;

    private LatLng userLocation;
    private LocationManager service;
    private List<FoursquareVenue> foursquareVenueList;
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
        foursquareVenueList = new ArrayList<>();

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

        service = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);

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
                map.getUiSettings().setMyLocationButtonEnabled(false);
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

        if (map != null) {
            map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    map.clear();
                    myMarker = map.addMarker(new MarkerOptions()
                            .position(new LatLng(latLng.latitude, latLng.longitude))
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

                }
            });
        }
    }
}

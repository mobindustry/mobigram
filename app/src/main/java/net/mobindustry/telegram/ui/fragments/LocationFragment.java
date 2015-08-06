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
import net.mobindustry.telegram.utils.Const;

import org.drinkless.td.libcore.telegram.TdApi;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class LocationFragment extends Fragment implements ApiClient.OnApiResultHandler {

    private GoogleMap map;
    private Marker myMarker;
    private TextView textCurrentPosition;
    private LatLng userLocation;
    private LocationManager service;
    private List<FoursquareVenue> foursquareVenueList;
    private InfoLocation infoLocation;

    @Override
    public void onApiResult(BaseHandler output) {
    }

    public void sendGeoPointMessage(double lat, double lng) {
        getActivity().finish();
        long id = MessagesFragmentHolder.getChat().id;
        new ApiClient<>(new TdApi.SendMessage(id, new TdApi.InputMessageGeoPoint(lng, lat)),
                new MessageHandler(), this).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.location_fragment_layout, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        infoLocation = InfoLocation.getInstance();
        foursquareVenueList = new ArrayList<>();
        textCurrentPosition = (TextView) getActivity().findViewById(R.id.textCurrentPosition);

        FloatingActionButton buttonSendLocation = (FloatingActionButton) getActivity().findViewById(R.id.buttonSendLocation);
        buttonSendLocation.setColorPressedResId(R.color.button_send_location_pressed);
        buttonSendLocation.setColorNormalResId(R.color.button_send_location);
        buttonSendLocation.setShadow(true);
        buttonSendLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendGeoPointMessage(myMarker.getPosition().latitude, myMarker.getPosition().longitude);

            }
        });

        FloatingActionButton buttonFoursquare = (FloatingActionButton) getActivity().findViewById(R.id.buttonWatchLocationList);
        buttonFoursquare.setColorPressedResId(R.color.button_foursquare_pressed);
        buttonFoursquare.setColorNormalResId(R.color.button_foursquare);
        buttonFoursquare.setShadow(true);
        buttonFoursquare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyTaskForFoursquareList myTaskForFoursquareList = new MyTaskForFoursquareList();
                myTaskForFoursquareList.execute();
            }
        });

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
                        init(map.getMyLocation());
                        return true;
                    case R.id.hybrid_item_menu:
                        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                        init(map.getMyLocation());
                        return true;
                    case R.id.map_item_menu:
                        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        init(map.getMyLocation());
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

                Location location = getLastKnownLocation();
                if (location == null) {
                    map.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                        @Override
                        public void onMyLocationChange(Location location) {
                            init(location);
                            map.setOnMyLocationChangeListener(null);
                        }
                    });
                } else {
                    init(location);
                }
            }
        });

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab); //my custom button
        fab.setColorPressedResId(R.color.button_on_map_pressed);
        fab.setColorNormalResId(R.color.button_on_map);
        fab.setShadow(true);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userLocation != null) {
                    Location location = map.getMyLocation();
                    if (location != null) {
                        userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        infoLocation.setLng(userLocation);
                    }
                    map.clear();
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 14));
                    myMarker = map.addMarker(new MarkerOptions()
                            .position(userLocation)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                    textCurrentPosition.setText("lat " + String.valueOf(userLocation.latitude
                            + "\n" + "lng " + String.valueOf(userLocation.longitude)));
                }
            }
        });
    }

    private Location getLastKnownLocation() {
        List<String> providers = service.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = service.getLastKnownLocation(provider);

            if (l == null) {
                continue;
            } else {
                Log.e("Log", l.toString());
            }
            if (bestLocation == null
                    || l.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = l;
            }
        }
        if (bestLocation == null) {
            return null;
        }
        return bestLocation;
    }

    private void init(Location location) {
        userLocation = new LatLng(location.getLatitude(), location.getLongitude());

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
            textCurrentPosition.setText("lat " + String.valueOf(userLocation.latitude
                    + "\n" + "lng " + String.valueOf(userLocation.longitude)));
        }

        if (map != null) {
            map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    map.clear();
                    myMarker = map.addMarker(new MarkerOptions()
                            .position(new LatLng(latLng.latitude, latLng.longitude))
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                    textCurrentPosition.setText("lat " + String.valueOf(String.valueOf(latLng.latitude)
                            + "\n" + "lng " + String.valueOf(String.valueOf(latLng.longitude))));
                }
            });
        }
    }

    public class MyTaskForFoursquareList extends AsyncTask<Void, Void, List<FoursquareVenue>> implements Serializable {

        @Override
        protected List<FoursquareVenue> doInBackground(Void... params) {

            AndroidHttpClient httpClient = new AndroidHttpClient(Const.URL_FOR_FOURSQUARE);
            httpClient.setMaxRetries(5);
            ParameterMap param = httpClient.newParams()
                    .add("client_id", Const.CLIENT_ID_FOR_FOURSQUARE)
                    .add("client_secret", Const.CLIENT_SECRET_FOR_FOURSQUARE)
                    .add("v", "20140421")
                    .add("limit", "50")
                    .add("radius", "80000")
                    .add("ll", String.valueOf(infoLocation.getLng().latitude) + "," + String.valueOf(infoLocation.getLng().longitude));
            HttpResponse httpResponse = httpClient.get("/v2/venues/search", param);

            if (httpResponse.getBodyAsString() != null) {
                Type frsqObject = new TypeToken<FoursquareObj>() {
                }.getType();
                Gson gson = new Gson();
                FoursquareObj obj = gson.fromJson(httpResponse.getBodyAsString(), frsqObject);
                foursquareVenueList = obj.getResponse().getVenues();
                List<FoursquareVenue> list = foursquareVenueList;
                Log.e("LOG", "Quantity of object = " + list.size());
                return list;
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<FoursquareVenue> aVoid) {
            super.onPostExecute(aVoid);
            Log.e("Log", "POST");
            FoursquareHolder foursquareHolder = FoursquareHolder.getInstance();
            foursquareHolder.setFoursquareVenueList(aVoid);
            FoursquareListFragment foursquareListFragment;
            foursquareListFragment = new FoursquareListFragment();
            foursquareHolder.setFoursquareVenueList(foursquareVenueList);
            if (getActivity()!=null){
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.transparent_content, foursquareListFragment);
                fragmentTransaction.commit();
            }
        }
    }
}

package net.mobindustry.telegram.ui.fragments;

import android.os.AsyncTask;
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
import android.widget.ListView;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.core.ApiClient;
import net.mobindustry.telegram.core.handlers.BaseHandler;
import net.mobindustry.telegram.core.handlers.MessageHandler;
import net.mobindustry.telegram.model.foursquare.FoursquareVenue;
import net.mobindustry.telegram.model.holder.FoursquareHolder;
import net.mobindustry.telegram.model.holder.MessagesFragmentHolder;
import net.mobindustry.telegram.ui.adapters.FoursquareAdapter;

import org.drinkless.td.libcore.telegram.TdApi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FoursquareListFragment extends Fragment implements Serializable {

    private FragmentTransaction ft;
    private FoursquareAdapter foursquareAdapter;
    private List<FoursquareVenue> foursquareVenueList = new ArrayList<>();
    private double lng;
    private double lat;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        foursquareAdapter = new FoursquareAdapter(getActivity());
        foursquareAdapter.clear();
        FoursquareHolder foursquareHolder = FoursquareHolder.getInstance();
        foursquareVenueList = foursquareHolder.getFoursquareVenueList();
        Log.e("LOG", "LIST " + foursquareVenueList.size());
        Log.e("LOG", "ADAPTER " + foursquareAdapter);
        foursquareAdapter.addAll(foursquareVenueList);
        return inflater.inflate(R.layout.foursquare_list_fragment_layout, container, false);
    }

    public void sendGeoPointMessage(double lat, double lng) {
        getActivity().finish();
        long id = MessagesFragmentHolder.getChat().id;
        new ApiClient<>(new TdApi.SendMessage(id, new TdApi.InputMessageGeoPoint(lng, lat)),
                new MessageHandler(), new ApiClient.OnApiResultHandler() {
            @Override
            public void onApiResult(BaseHandler output) {
            }
        }).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ListView foursquareList = (ListView) getActivity().findViewById(R.id.foursquare_list);
        foursquareList.setAdapter(foursquareAdapter);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar_foursquare_list);
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
                sendGeoPointMessage(lat, lng);
            }
        });
    }
}

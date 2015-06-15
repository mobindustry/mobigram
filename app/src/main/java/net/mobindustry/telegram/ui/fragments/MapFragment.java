package net.mobindustry.telegram.ui.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jorgecastilloprz.pagedheadlistview.PagedHeadListView;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.ui.adapters.MapAdapter;

import java.util.List;

public class MapFragment extends Fragment {
    private PagedHeadListView pagedHeadListView;
    private Fragment locationFragment;
    private List<String> listLocations;
    private MapAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        pagedHeadListView = (PagedHeadListView) getActivity().findViewById(R.id.pagedHeadListView);
        locationFragment=new LocationFragment();

        pagedHeadListView.addFragmentToHeader(locationFragment);
        adapter = new MapAdapter(getActivity());
        pagedHeadListView.setAdapter(adapter);
    }
}

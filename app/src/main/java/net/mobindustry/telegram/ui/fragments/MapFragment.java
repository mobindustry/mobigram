package net.mobindustry.telegram.ui.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;
import net.mobindustry.telegram.R;
import java.io.Serializable;




public class MapFragment extends Fragment implements Serializable {
    private Toolbar toolbar;
    private LocationFragment locationFragment;
    private FragmentTransaction fragmentTransaction;
    private FrameLayout container;
    private TextView textCurrentPosition;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        locationFragment=new LocationFragment();
        View view = inflater.inflate(R.layout.map_fragment_layout, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        container=(FrameLayout)getActivity().findViewById(R.id.containerMap);
        textCurrentPosition=(TextView)getActivity().findViewById(R.id.textCurrentPosition);

        FloatingActionButton buttonSendLocation = (FloatingActionButton) getActivity().findViewById(R.id.buttonSendLocation);
        buttonSendLocation.setColorPressedResId(R.color.button_send_location_pressed);
        buttonSendLocation.setColorNormalResId(R.color.button_send_location);
        buttonSendLocation.setShadow(true);

        FloatingActionButton buttonFoursquare = (FloatingActionButton) getActivity().findViewById(R.id.buttonWatchLocationList);
        buttonFoursquare.setColorPressedResId(R.color.button_foursquare_pressed);
        buttonFoursquare.setColorNormalResId(R.color.button_foursquare);
        buttonFoursquare.setShadow(true);

        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar_map);
        toolbar.inflateMenu(R.menu.map_menu);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setTitle(R.string.text_location);

        fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.containerMap, locationFragment);
        fragmentTransaction.commit();

        //textCurrentPosition.setText();




    }

}

package net.mobindustry.telegram.model.holder;

import com.google.android.gms.maps.model.LatLng;

public class InfoLocation {

    private static InfoLocation instance;

    private LatLng lng = null;


    public static synchronized InfoLocation getInstance() {
        if (instance == null) {
            instance = new InfoLocation();
        }
        return instance;
    }

    private InfoLocation() {
    }

    public LatLng getLng() {
        return lng;
    }

    public void setLng(LatLng lng) {
        this.lng = lng;
    }
}

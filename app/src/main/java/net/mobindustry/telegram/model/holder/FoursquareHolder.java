package net.mobindustry.telegram.model.holder;

import net.mobindustry.telegram.model.foursquare.FoursquareVenue;

import java.io.Serializable;
import java.util.List;

public class FoursquareHolder implements Serializable {

    private static FoursquareHolder instance;

    private List<FoursquareVenue> foursquareVenueList;

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    private Object object = new Object();

    public static synchronized FoursquareHolder getInstance() {
        if (instance == null) {
            instance = new FoursquareHolder();
        }
        return instance;
    }

    public List<FoursquareVenue> getFoursquareVenueList() {
        return foursquareVenueList;
    }

    public void setFoursquareVenueList(List<FoursquareVenue> foursquareVenueList) {
        this.foursquareVenueList = foursquareVenueList;
    }
}

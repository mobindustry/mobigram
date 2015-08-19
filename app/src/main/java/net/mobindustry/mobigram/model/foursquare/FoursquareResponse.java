package net.mobindustry.mobigram.model.foursquare;

import java.io.Serializable;
import java.util.List;

public class FoursquareResponse implements Serializable {
    private List<FoursquareVenue> venues;

    public FoursquareResponse() {
    }

    public List<FoursquareVenue> getVenues() {
        return venues;
    }
}

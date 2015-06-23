package net.mobindustry.telegram.model.foursquare;

import java.io.Serializable;

public class FoursquareObj implements Serializable {
    private FoursquareResponse response;

    public FoursquareObj() {
    }

    public FoursquareResponse getResponse() {
        return response;
    }
}

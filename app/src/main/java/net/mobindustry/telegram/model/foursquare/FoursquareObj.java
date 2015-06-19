package net.mobindustry.telegram.model.foursquare;

import java.io.Serializable;
import java.util.List;

public class FoursquareObj implements Serializable {
    private FoursquareResponse response;

    public FoursquareObj() {
    }


    public FoursquareResponse getResponse() {
        return response;
    }
}

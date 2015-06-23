package net.mobindustry.telegram.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.model.foursquare.FoursquareVenue;
import net.mobindustry.telegram.utils.ImageLoaderHelper;
import net.mobindustry.telegram.utils.Utils;

import java.io.Serializable;

public class FoursquareAdapter extends ArrayAdapter<FoursquareVenue> implements Serializable {
    private LayoutInflater inflater;

    public FoursquareAdapter(Context context) {
        super(context, 0);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_foursquare_list, parent, false);
        }

        ImageView iconPlace = (ImageView) convertView.findViewById(R.id.iconPlace);
        TextView textPlaceName = (TextView) convertView.findViewById(R.id.textPlaceName);
        TextView textPlaceAddress = (TextView) convertView.findViewById(R.id.textPlaceAddress);

        int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            iconPlace.setBackgroundDrawable(Utils.getShapeDrawable(60, Color.GRAY));
        } else {
            iconPlace.setBackground(Utils.getShapeDrawable(60, Color.GRAY));
        }

        FoursquareVenue foursquareVenue = getItem(position);
        if (foursquareVenue != null) {
            Log.e("LOG", "CATEGORY SIZE " + foursquareVenue.getFoursquareCategories().size());
            if (foursquareVenue.getFoursquareCategories().size() > 0) {
                ImageLoaderHelper.displayImage(foursquareVenue.getFoursquareCategories().get(0).getFoursquareCategoryIcon().getIconUrl(), iconPlace);
            }
            textPlaceName.setText(foursquareVenue.getName());
            textPlaceAddress.setText(foursquareVenue.getFoursquareLocation().getAddress());
        }

        return convertView;
    }
}

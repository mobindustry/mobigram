package net.mobindustry.telegram.ui.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.model.foursquare.FoursquareVenue;

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
        TextView textPlaceAdress = (TextView) convertView.findViewById(R.id.textPlaceAddress);

        FoursquareVenue foursquareVenue = getItem(position);
        if (foursquareVenue != null) {
            Log.e("LOG", "CATEGOR SIZE " + foursquareVenue.getFoursquareCategories().size());
            if (foursquareVenue.getFoursquareCategories().size() > 0) {
                Glide.with(getContext()).load(foursquareVenue.getFoursquareCategories().get(0).getFoursquareCategoryIcon().getIconUrl()).into(iconPlace);
            }
            textPlaceName.setText(foursquareVenue.getName());
            textPlaceAdress.setText(foursquareVenue.getFoursquareLocation().getAddress());
        }

        return convertView;
    }
}

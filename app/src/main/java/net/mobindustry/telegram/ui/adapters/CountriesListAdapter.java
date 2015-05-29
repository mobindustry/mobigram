package net.mobindustry.telegram.ui.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import net.mobindustry.telegram.R;
import net.mobindustry.telegram.utils.ListCountryObject;
import java.io.Serializable;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by alytar on 28.05.15.
 */
public class CountriesListAdapter extends BaseAdapter implements StickyListHeadersAdapter, Serializable {

    private Activity activity;
    private ListCountryObject listCountryObject;
    private LayoutInflater inflater;

    public CountriesListAdapter(Activity activity, ListCountryObject listCountryObject) {
        this.activity = activity;
        this.listCountryObject = listCountryObject;
        inflater = LayoutInflater.from(activity);

    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolderCountriesList holder;
        if (convertView == null) {
            holder = new HeaderViewHolderCountriesList();
            convertView = inflater.inflate(R.layout.country_list_header_row, parent, false);
            holder.titleLetter = (TextView) convertView.findViewById(R.id.header);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolderCountriesList) convertView.getTag();
        }
        //set header text as first char in name
        holder.titleLetter.setText(listCountryObject.getListCountries().get(position).getInitialLetter());
        return convertView;
    }

    @Override
    public long getHeaderId(int i) {
        return listCountryObject.getListCountries().get(i).getInitialLetter().charAt(0);
    }

    @Override
    public int getCount() {
        return listCountryObject.getListCountries().size();
    }

    @Override
    public Object getItem(int position) {
        return listCountryObject.getListCountries().get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderCountriesList holder;

        if (convertView == null) {
            holder = new ViewHolderCountriesList();
            convertView = inflater.inflate(R.layout.country_list_row, parent, false);
            holder.country = (TextView) convertView.findViewById(R.id.country);
            holder.countryCode = (TextView) convertView.findViewById(R.id.code_country);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolderCountriesList) convertView.getTag();
        }

        holder.country.setText(listCountryObject.getListCountries().get(position).getCountryName());
        holder.countryCode.setText(listCountryObject.getListCountries().get(position).getCountryCode());

        return convertView;
    }
}

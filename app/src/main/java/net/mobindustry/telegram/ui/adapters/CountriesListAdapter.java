package net.mobindustry.telegram.ui.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.model.CountryObject;
import net.mobindustry.telegram.model.ListCountryObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

public class CountriesListAdapter extends BaseAdapter implements StickyListHeadersAdapter, Serializable, Filterable {

    private List<CountryObject> listCountryObjectListTmp;
    private List<CountryObject> listCountryObjectConst;
    private LayoutInflater inflater;
    private ListCountryObject countryObject;

    public CountriesListAdapter(Activity activity, ListCountryObject listCountryObject) {
        inflater = LayoutInflater.from(activity);
        this.countryObject = listCountryObject;
        listCountryObjectConst = listCountryObject.getListConst();
        listCountryObjectListTmp = listCountryObject.getListTmp();
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
        holder.titleLetter.setText(listCountryObjectListTmp.get(position).getInitialLetter());
        return convertView;
    }

    @Override
    public long getHeaderId(int i) {
        return listCountryObjectListTmp.get(i).getInitialLetter().charAt(0);
    }

    @Override
    public int getCount() {
        return listCountryObjectListTmp.size();
    }

    @Override
    public Object getItem(int position) {
        return listCountryObjectListTmp.get(position);
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

        holder.country.setText(listCountryObjectListTmp.get(position).getCountryName());
        holder.countryCode.setText(listCountryObjectListTmp.get(position).getCountryCode());

        return convertView;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                listCountryObjectListTmp = ((List<CountryObject>) results.values);
                countryObject.setListTmp(listCountryObjectListTmp);
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();
                List<CountryObject> list = new ArrayList<>();

                constraint = constraint.toString().toLowerCase();
                if (constraint.length() == 0) {
                    list.addAll(listCountryObjectConst);
                } else {
                    for (int i = 0; i < listCountryObjectConst.size(); i++) {
                        CountryObject country = listCountryObjectConst.get(i);
                        if (country.getCountryName().toLowerCase().startsWith(constraint.toString())) {
                            list.add(country);
                        }
                    }
                }
                results.count = list.size();
                results.values = list;
                return results;
            }
        };
        return filter;
    }

    public class ViewHolderCountriesList {
        TextView country;
        TextView countryCode;
    }

    public class HeaderViewHolderCountriesList {
        TextView titleLetter;
    }
}

package net.mobindustry.telegram.ui.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.applidium.headerlistview.SectionAdapter;

/**
 * Created by alytar on 27.05.15.
 */
public class ListAdapter extends SectionAdapter {
    @Override
    public int numberOfSections() {
        return 4;
    }

    @Override
    public int numberOfRows(int section) {
        return 10;
    }

    @Override
    public View getRowView(int section, int row, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public Object getRowItem(int section, int row) {
        return null;
    }
}

package com.example.djc.kanquimaniapark.MainActivity;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.djc.kanquimaniapark.Clases.SelectedAttraction;
import com.example.djc.kanquimaniapark.R;

import java.util.List;

/**
 * Created by dewyn on 11/22/2016.
 */

public class SelectedAttractionsAdapter extends BaseAdapter {

    private Context context;
    private List<SelectedAttraction> selected_attractions;

    public SelectedAttractionsAdapter(Context context, List<SelectedAttraction> selected_attractions) {
        this.context = context;
        this.selected_attractions = selected_attractions;
    }

    @Override
    public int getCount() {
        return selected_attractions.size();
    }

    @Override
    public Object getItem(int position) {
        return selected_attractions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(context, R.layout.selected_attractions_list_item, null);
        TextView client = (TextView)v.findViewById(R.id.attractions_list_item_name);
        TextView atr = (TextView)v.findViewById(R.id.attractions_list_item_attraction);

        client.setText(selected_attractions.get(position).getClient().get_nombre());
        atr.setText(String.valueOf(selected_attractions.get(position).getAtraccion().get_titulo()
                + " -> " + selected_attractions.get(position).getAtraccion().get_tiempo()));

        return v;
    }
}

package com.example.djc.kanquimaniapark.MainActivity;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.djc.kanquimaniapark.Clases.SelectedProduct;
import com.example.djc.kanquimaniapark.R;

import java.util.List;

/**
 * Created by dewyn on 11/22/2016.
 */

public class SelectedProductsAdapter extends BaseAdapter {

    private Context context;
    private List<SelectedProduct> selected_products;

    public SelectedProductsAdapter(Context context, List<SelectedProduct> selected_products) {
        this.context = context;
        this.selected_products = selected_products;
    }

    @Override
    public int getCount() {
        return selected_products.size();
    }

    @Override
    public Object getItem(int position) {
        return selected_products.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(context, R.layout.selected_product_list_item, null);
        TextView pName = (TextView)v.findViewById(R.id.hashmap_list_item_name);
        TextView pCount = (TextView)v.findViewById(R.id.hashmap_list_item_count);

        pName.setText(selected_products.get(position).get_producto().get_titulo());
        pCount.setText("x" + String.valueOf(selected_products.get(position).get_cantidad()));

        return v;
    }
}

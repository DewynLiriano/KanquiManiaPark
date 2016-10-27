package com.example.djc.kanquimaniapark.Admin.GestionProductos;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.djc.kanquimaniapark.Admin.ItemClickListener;
import com.example.djc.kanquimaniapark.R;

/**
 * Created by dewyn on 10/26/2016.
 */
public class ProductsVH extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView tPrecio, tTitulo;
    public ItemClickListener itemClickListener;

    ProductsVH(View v) {
        super(v);
        tTitulo = (TextView)v.findViewById(R.id.product_name);
        tPrecio = (TextView)v.findViewById(R.id.product_price);
        v.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        this.itemClickListener.OnItemClickListener(v, getLayoutPosition());
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
}

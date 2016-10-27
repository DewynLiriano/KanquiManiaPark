package com.example.djc.kanquimaniapark.Admin.GestionAtracciones;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.djc.kanquimaniapark.Admin.ItemClickListener;
import com.example.djc.kanquimaniapark.R;

/**
 * Created by dewyn on 10/26/2016.
 */
public class AtractionsVH extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView nombreET, precioET;
    public ItemClickListener itemClickListener;

    public AtractionsVH(View v) {
        super(v);
        nombreET = (TextView)v.findViewById(R.id.atraccion_nombre);
        precioET = (TextView)v.findViewById(R.id.atraccion_precio);
        v.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.OnItemClickListener(v, getLayoutPosition());
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
}

package com.example.djc.kanquimaniapark.Admin.GestionEmpleados;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.djc.kanquimaniapark.Helpers.ItemClickListener;
import com.example.djc.kanquimaniapark.R;

/**
 * Created by dewyn on 10/25/2016.
 */
public class EmpViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    TextView tv1, tv2;
    ItemClickListener itemClickListener;

    EmpViewHolder(View v){
        super(v);
        tv1 = (TextView)v.findViewById(R.id.list_name);
        tv2 = (TextView)v.findViewById(R.id.list_posicion);
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

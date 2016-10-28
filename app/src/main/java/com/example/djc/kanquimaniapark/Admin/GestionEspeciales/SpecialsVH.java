package com.example.djc.kanquimaniapark.Admin.GestionEspeciales;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.djc.kanquimaniapark.Admin.ItemClickListener;
import com.example.djc.kanquimaniapark.R;

/**
 * Created by dewyn on 10/27/2016.
 */
public class SpecialsVH extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView tvNombre, tvFechaInicio, tvFechaFin;
    public ItemClickListener itemClickListener;

    public SpecialsVH(View v) {
        super(v);
        tvNombre = (TextView)v.findViewById(R.id.especial_nombre_card);
        tvFechaInicio = (TextView)v.findViewById(R.id.fecha_inicio_especial);
        tvFechaFin = (TextView)v.findViewById(R.id.fecha_fin_especial);
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

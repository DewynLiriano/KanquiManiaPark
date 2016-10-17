package com.example.djc.kanquimaniapark.Admin.GestionEmpleados;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.djc.kanquimaniapark.R;

public class EmployeeRecyclerViewHolder extends RecyclerView.ViewHolder {

    TextView tv1,tv2;

    public EmployeeRecyclerViewHolder(View itemView) {
        super(itemView);

        tv1= (TextView) itemView.findViewById(R.id.list_name);
        tv2= (TextView) itemView.findViewById(R.id.list_posicion);
    }
}

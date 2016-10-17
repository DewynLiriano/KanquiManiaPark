package com.example.djc.kanquimaniapark.Admin.GestionEmpleados;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.djc.kanquimaniapark.R;

import java.util.List;

public class EmployeeRecyclerAdapter extends  RecyclerView.Adapter<EmployeeRecyclerViewHolder> {

    List<String> list;
    Context context;
    LayoutInflater inflater;

    public EmployeeRecyclerAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
        inflater =  LayoutInflater.from(context);
    }

    @Override
    public EmployeeRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.empleados_list, parent, false);
        return new EmployeeRecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(EmployeeRecyclerViewHolder holder, int position) {
        holder.tv1.setText(list.get(position));
        holder.tv1.setOnClickListener(clickListener);
    }

    View.OnClickListener clickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            EmployeeRecyclerViewHolder vholder = (EmployeeRecyclerViewHolder) v.getTag();
            int position = vholder.getAdapterPosition();
            Toast.makeText(context,"Pana numero " + position, Toast.LENGTH_LONG ).show();
        }
    };
    @Override
    public int getItemCount() {
        return list.size();
    }
}

package com.example.djc.kanquimaniapark.Admin.GestionEmpleados;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.djc.kanquimaniapark.Clases.Empleado;
import com.example.djc.kanquimaniapark.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EmployeeRecyclerAdapter extends  RecyclerView.Adapter<EmployeeRecyclerAdapter.EmplRVHolder> {

    private List<Empleado> list;
    private Context context;


    static class EmplRVHolder extends RecyclerView.ViewHolder {

        TextView tv1, tv2;
        EmplRVHolder(View v){
            super(v);
            tv1 = (TextView)itemView.findViewById(R.id.list_name);
            tv2 = (TextView)itemView.findViewById(R.id.list_posicion);
        }
    }


    public EmployeeRecyclerAdapter(Context context, List<Empleado> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public EmplRVHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.empleados_list, parent, false);
        return new EmplRVHolder(v);
    }

    @Override
    public void onBindViewHolder(EmplRVHolder holder, int position) {
        holder.tv1.setText(list.get(position).get_nombre());
        holder.tv2.setText(list.get(position).get_apellido());
        //holder.tv1.setOnClickListener(clickListener);
        //Toast.makeText(context, list.get(position).get_userName(), Toast.LENGTH_SHORT).show();
    }

    View.OnClickListener clickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            EmplRVHolder vholder = (EmplRVHolder) v.getTag();
            int position = vholder.getAdapterPosition();
            Toast.makeText(context,"Pana numero " + position, Toast.LENGTH_LONG ).show();
        }
    };

    @Override
    public int getItemCount() {
        return list.size();
    }
}

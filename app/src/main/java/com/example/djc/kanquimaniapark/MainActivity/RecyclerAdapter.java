package com.example.djc.kanquimaniapark.MainActivity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.djc.kanquimaniapark.R;

import java.util.List;

public class RecyclerAdapter extends  RecyclerView.Adapter<RecyclerViewHolder> {

    List<String> list;
    Context context;
    LayoutInflater inflater;

    public RecyclerAdapter (Context context, List<String> list) {
        this.context = context;
        this.list = list;
        inflater =  LayoutInflater.from(context);
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.client_list, parent, false);
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.tv1.setText(list.get(position));
        holder.imageView.setOnClickListener(clickListener);
        holder.imageView.setTag(holder);
    }

    View.OnClickListener clickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RecyclerViewHolder vholder = (RecyclerViewHolder) v.getTag();
            int position = vholder.getAdapterPosition();
            Toast.makeText(context,"Panita numero " + position, Toast.LENGTH_LONG ).show();
        }
    };
    @Override
    public int getItemCount() {
        return list.size();
    }
}

package com.example.djc.kanquimaniapark.MainActivity.ClientsList;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.djc.kanquimaniapark.Helpers.ItemClickListener;
import com.example.djc.kanquimaniapark.R;

public class ClientRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView tv1,tv2;
    ImageView imageView;
    ItemClickListener itemClickListener;

    public ClientRecyclerViewHolder(View itemView) {
        super(itemView);
        tv1= (TextView) itemView.findViewById(R.id.list_title);
        tv2= (TextView) itemView.findViewById(R.id.list_desc);
        imageView= (ImageView) itemView.findViewById(R.id.list_avatar);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        this.itemClickListener.OnItemClickListener(v, getLayoutPosition());
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
}

package com.example.djc.kanquimaniapark.MainActivity.ClientsList;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.djc.kanquimaniapark.Clases.Cliente;
import com.example.djc.kanquimaniapark.Helpers.ItemClickListener;
import com.example.djc.kanquimaniapark.R;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ClientRecyclerAdapter extends  RecyclerView.Adapter<ClientRecyclerViewHolder> implements Filterable{

    private static final String FOTOS_CLIENTES = "FOTOS_CLIENTES";
    List<Cliente> list;
    private Context context;
    private ClientsFilter filter;

    public ClientRecyclerAdapter(Context context, List<Cliente> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ClientRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_client_list, parent, false);
        return new ClientRecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ClientRecyclerViewHolder holder, final int position) {
        holder.tv1.setText(list.get(position).get_nombre());
        holder.tv2.setText(list.get(position).get_id());
        Glide.with(context)
                .using(new FirebaseImageLoader())
                .load(FirebaseStorage.getInstance().getReference(FOTOS_CLIENTES).child(list.get(position).get_id()))
                .centerCrop().into(holder.imageView);

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void OnItemClickListener(View v, int pos) {
               Snackbar.make(v, String.valueOf(list.get(position).get_fechaCumpleAnos()), Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Filter getFilter() {
        if(filter == null) {
            filter = new ClientsFilter((ArrayList<Cliente>) list, this);
        }
        return filter;
    }
}

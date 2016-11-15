package com.example.djc.kanquimaniapark.MainActivity.ClientsList;

import android.widget.Filter;

import com.example.djc.kanquimaniapark.Clases.Cliente;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dewyn on 11/13/2016.
 */

public class ClientsFilter extends Filter {

    ClientRecyclerAdapter adapter;
    List<Cliente> list;

    public ClientsFilter(ArrayList<Cliente> clientes, ClientRecyclerAdapter adapter){
        this.adapter = adapter;
        this.list = clientes;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
        if (constraint != null && constraint.length() > 0){
            constraint = constraint.toString().toUpperCase();

            ArrayList<Cliente> clientesFiltrados = new ArrayList<>();
            for (int i=0 ; i < list.size() ; i++){
                if (list.get(i).get_nombre().toUpperCase().contains(constraint)){
                    clientesFiltrados.add(list.get(i));
                }
            }
            results.count = clientesFiltrados.size();
            results.values = clientesFiltrados;
        } else {
            results.count = list.size();
            results.values = list;
        }

        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.list = (ArrayList<Cliente>)results.values;
        adapter.notifyDataSetChanged();
    }
}

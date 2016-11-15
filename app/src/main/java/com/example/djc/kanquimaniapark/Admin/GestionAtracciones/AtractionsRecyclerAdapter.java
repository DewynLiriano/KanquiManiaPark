package com.example.djc.kanquimaniapark.Admin.GestionAtracciones;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.djc.kanquimaniapark.Clases.Atraccion;
import com.example.djc.kanquimaniapark.Helpers.ItemClickListener;
import com.example.djc.kanquimaniapark.R;

import java.util.List;

/**
 * Created by dewyn on 10/26/2016.
 */
public class AtractionsRecyclerAdapter extends RecyclerView.Adapter<AtractionsVH> {

    private Context context;
    private List<Atraccion> list;
    private Dialog dialog;
    private CRUDAtractionsFireBaseHelper crudAtractions;
    private View focusView = null;

    public AtractionsRecyclerAdapter(Context context, List list){
        this.list = list;
        this.context = context;
    }


    @Override
    public AtractionsVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_atracciones_list,
                parent, false);
        return new AtractionsVH(v);
    }

    @Override
    public void onBindViewHolder(AtractionsVH holder, final int position) {
        holder.nombreET.setText(list.get(position).get_titulo());
        holder.precioET.setText(list.get(position).get_precio());

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void OnItemClickListener(View v, int pos) {
                dialog = new Dialog(context);
                dialog.setContentView(R.layout.edit_atraction_dialog);
                dialog.setTitle("Manejo de atracciones");

                crudAtractions = new CRUDAtractionsFireBaseHelper();

                //<editor-fold desc="Initialize Widgets">
                final EditText tituloET = (EditText)dialog.findViewById(R.id.show_atr_name);
                final EditText precioET = (EditText)dialog.findViewById(R.id.show_atr_price);
                final EditText tiempoET = (EditText)dialog.findViewById(R.id.show_atr_time);
                final Button editButton = (Button)dialog.findViewById(R.id.edit_atraction);
                final Button deleteButton = (Button)dialog.findViewById(R.id.delete_atraction);
                final Button acceptButton = (Button)dialog.findViewById(R.id.aceptar_atraction);
                final CheckBox checkIlimitado = (CheckBox)dialog.findViewById(R.id.edit_check_ilimitado_atraccion);
                //</editor-fold>

                checkIlimitado.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (checkIlimitado.isChecked()){
                            tiempoET.setClickable(false);
                            tiempoET.setEnabled(false);
                            tiempoET.setText(context.getText(R.string.ilimitado));
                        } else {
                            tiempoET.setEnabled(true);
                            tiempoET.setClickable(true);
                            tiempoET.setText("");
                        }
                    }
                });

                tituloET.setText(list.get(position).get_titulo());
                precioET.setText(list.get(position).get_precio());
                String time = list.get(position).get_tiempo();
                tiempoET.setText(time);

                editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tituloET.setEnabled(true);
                        precioET.setEnabled(true);
                        tiempoET.setEnabled(true);
                        editButton.setVisibility(View.GONE);
                        acceptButton.setVisibility(View.VISIBLE);
                        checkIlimitado.setVisibility(View.VISIBLE);
                    }
                });

                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        crudAtractions.deleteAtraction(list.get(position));
                        dialog.dismiss();
                    }
                });

                acceptButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean cancel = false;
                        String nombre, precio, tiempo;

                        if (tituloET.getText().toString().equals("")){
                            cancel = true;
                            tituloET.setError(context.getString(R.string.vacio));
                            focusView = tituloET;
                        } else if (precioET.getText().toString().equals("")){
                            cancel = true;
                            precioET.setError(context.getString(R.string.vacio));
                            focusView = precioET;
                        } else if(!checkIlimitado.isChecked()) {
                            if (tiempoET.getText().toString().equals("")) {
                                cancel = true;
                                tiempoET.setError(context.getString(R.string.vacio));
                                focusView = tiempoET;
                            }
                        }

                        for (Atraccion a : list){
                            if (!a.get_id().equals(list.get(position).get_id())){
                                if (a.get_titulo().equals(list.get(position).get_titulo())){
                                    cancel = true;
                                    tituloET.setError(context.getString(R.string.nombre_existe));
                                    focusView = tituloET;
                                }
                            }
                        }

                        if (cancel){
                            focusView.requestFocus();
                        } else{
                            nombre = tituloET.getText().toString();
                            precio = precioET.getText().toString();
                            tiempo = tiempoET.getText().toString();

                            Atraccion a = new Atraccion(list.get(position).get_id(), nombre, precio, tiempo);
                            crudAtractions.updateAtraction(a);
                            dialog.dismiss();
                            Toast.makeText(context, context.getString(R.string.cambio_realizado), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

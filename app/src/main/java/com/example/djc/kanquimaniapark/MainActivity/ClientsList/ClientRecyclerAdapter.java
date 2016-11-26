package com.example.djc.kanquimaniapark.MainActivity.ClientsList;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.djc.kanquimaniapark.Clases.Atraccion;
import com.example.djc.kanquimaniapark.Clases.Cliente;
import com.example.djc.kanquimaniapark.Helpers.ItemClickListener;
import com.example.djc.kanquimaniapark.Eventos.AddAtracctionsEvent;
import com.example.djc.kanquimaniapark.R;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientRecyclerAdapter extends  RecyclerView.Adapter<ClientRecyclerViewHolder> implements Filterable{

    private static final String FOTOS_CLIENTES = "FOTOS_CLIENTES";
    private static final String ATRACCIONES = "Atracciones";
    private static final String ID = "ID";
    private static final String NOMBRE = "Nombre";
    private static final String PRECIO = "Precio";
    private static final String TIEMPO = "Tiempo";

    public List<Cliente> list;
    private Spinner spinner;
    private List<Atraccion> atracciones;
    private Context context;
    private ClientsFilter filter;

    private ArrayAdapter<Atraccion> atraccionesAdapter;

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
                final Dialog dialog = new Dialog(context);
                dialog.setTitle("Informacion del cliente");
                dialog.setContentView(R.layout.select_client_ticket_dialog);
                dialog.setCancelable(false);

                //<editor-fold desc="Setting client's values">
                ImageView fotoDialog = (ImageView)dialog.findViewById(R.id.facturacion_client_dialog_foto);

                Glide.with(context).using(new FirebaseImageLoader()).load(FirebaseStorage.getInstance()
                        .getReference(FOTOS_CLIENTES).child(list.get(position).get_id()))
                        .centerCrop().into(fotoDialog);

                TextView nombteTV = (TextView) dialog.findViewById(R.id.facturacion_client_dialog_name);
                nombteTV.setText(list.get(position).get_nombre() + " " + list.get(position).get_apellido());
                TextView idTV = (TextView) dialog.findViewById(R.id.facturacion_client_dialog_id);
                idTV.setText(list.get(position).get_id());
                //</editor-fold>

                //<editor-fold desc="Attractions' spinner">
                atracciones = new ArrayList<>();

                atraccionesAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, atracciones);
                atraccionesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spinner = (Spinner)dialog.findViewById(R.id.facturacion_client_dialog_tickets_spinner);
                spinner.setAdapter(atraccionesAdapter);

                DatabaseReference atrRef = FirebaseDatabase.getInstance().getReference(ATRACCIONES);
                atrRef.addValueEventListener(getAtr);
                atrRef.keepSynced(true);
                //</editor-fold>

                Button cancelButton = (Button)dialog.findViewById(R.id.facturacion_client_cancelar_button);
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                Button aceptarButton = (Button)dialog.findViewById(R.id.facturacion_client_aceptar_button);
                aceptarButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = spinner.getSelectedItemPosition();
                        AddAtracctionsEvent event = new AddAtracctionsEvent(atracciones.get(pos), list.get(position));
                        EventBus.getDefault().post(event);
                        dialog.dismiss();
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

    @Override
    public Filter getFilter() {
        if(filter == null) {
            filter = new ClientsFilter((ArrayList<Cliente>) list, this);
        }
        return filter;
    }

    private ValueEventListener getAtr = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            GenericTypeIndicator<Map<String, Map<String, String>>> genin = new GenericTypeIndicator<Map<String, Map<String, String>>>() {};
            Map<String, Map<String,String>> map = dataSnapshot.getValue(genin);
            atracciones.clear();

            if (map != null){
                for (Map.Entry<String, Map<String, String>> entry : map.entrySet()){
                    if (entry != null){
                        HashMap value = (HashMap) entry.getValue();
                        Atraccion a = new Atraccion();
                        a.set_id((String)value.get(ID));
                        a.set_titulo((String)value.get(NOMBRE));
                        a.set_precio((String) value.get(PRECIO));
                        a.set_tiempo((String) value.get(TIEMPO));
                        atracciones.add(a);
                    }
                }
            }

            atraccionesAdapter.notifyDataSetChanged();
            spinner.setAdapter(atraccionesAdapter);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.v("ERROR", databaseError.getMessage());
        }
    };

}

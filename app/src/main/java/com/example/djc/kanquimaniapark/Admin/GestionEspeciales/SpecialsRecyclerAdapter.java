package com.example.djc.kanquimaniapark.Admin.GestionEspeciales;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.djc.kanquimaniapark.Admin.ItemClickListener;
import com.example.djc.kanquimaniapark.Clases.Atraccion;
import com.example.djc.kanquimaniapark.Clases.Especial;
import com.example.djc.kanquimaniapark.Clases.Producto;
import com.example.djc.kanquimaniapark.Helpers.DateValidator;
import com.example.djc.kanquimaniapark.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by dewyn on 10/27/2016.
 */
public class SpecialsRecyclerAdapter extends RecyclerView.Adapter<SpecialsVH> {

    private Context context;
    private List<Especial> especiales;
    private Dialog dialog;
    private CRUDSpecialsFireBaseHelper crudSpecials;
    private View focusView = null;
    private List<Atraccion> atracciones;
    private List<Producto> productos;


    private String ESPECIALES = "Especiales";
    private String ID = "ID";
    private String NOMBRE = "Nombre";
    private String PORCIENTO = "Porciento";
    private String FECHA_INICIO = "Fecha_Inicio";
    private String FECHA_FIN = "Fecha_Fin";
    private String PRODUCTOS = "Productos";
    private String DATEFORMAT = "dd/MM/yyyy";

    private String ATRACCIONES = "Atracciones";
    private String PRECIO = "Precio";
    private String TIEMPO = "Tiempo"; //Minutos
    private String TITULO = "Titulo";


    public SpecialsRecyclerAdapter(Context context, List especiales){
        this.context = context;
        this.especiales = especiales;
    }

    @Override
    public SpecialsVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_especiales_list, parent, false);
        return new SpecialsVH(v);
    }

    @Override
    public void onBindViewHolder(SpecialsVH holder, final int position) {
        holder.tvNombre.setText(especiales.get(position).get_nombre());
        holder.tvFechaInicio.setText(especiales.get(position).get_fechaInicio());
        holder.tvFechaFin.setText(especiales.get(position).get_fechaFin());

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void OnItemClickListener(View v, int pos) {
                dialog = new Dialog(context);
                dialog.setContentView(R.layout.edit_special_dialog);
                dialog.setTitle("Manejo de especiales");

                crudSpecials = new CRUDSpecialsFireBaseHelper();


                //<editor-fold desc="Create Widgets">
                final EditText nombreET = (EditText)dialog.findViewById(R.id.edit_especial_nombre);
                final EditText porcientoET = (EditText)dialog.findViewById(R.id.edit_especial_porciento);
                final EditText fechaInicioET = (EditText)dialog.findViewById(R.id.edit_fecha_inicio_especial);
                final EditText fechaFinET = (EditText)dialog.findViewById(R.id.edit_fecha_fin_especial);
                final ListView prodList = (ListView)dialog.findViewById(R.id.edit_products_list_especial);
                final ListView atrList = (ListView)dialog.findViewById(R.id.edit_atr_list_especial);
                final Button editEspecial = (Button)dialog.findViewById(R.id.edit_especial_button);
                final Button acceptEspecial = (Button)dialog.findViewById(R.id.accept_especial_button);
                final Button deleteEspecial = (Button)dialog.findViewById(R.id.delete_especial_button);
                //</editor-fold>

                productos = new ArrayList<>();
                atracciones = new ArrayList<>();
                final ArrayAdapter<Producto> prodAdpt;
                final ArrayAdapter<Atraccion> atrAdapt;



                //<editor-fold desc="PRODUCTOS Y ATRACCIONES">
                final ArrayList<Producto> productos_incluidos = new ArrayList<>();
                final ArrayList<Atraccion> atr_incluidos = new ArrayList<>();

                prodAdpt = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1,
                        productos_incluidos);
                atrAdapt = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1,
                        atr_incluidos);



                DatabaseReference atrRef = FirebaseDatabase.getInstance().getReference(ATRACCIONES);
                atrRef.addValueEventListener(new ValueEventListener() {
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
                        atrAdapt.notifyDataSetChanged();
                        poblateAtrs(position, atr_incluidos);
                        atrList.setAdapter(atrAdapt);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, databaseError.getMessage());
                    }
                });

                DatabaseReference prodRef = FirebaseDatabase.getInstance().getReference(PRODUCTOS);
                prodRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        GenericTypeIndicator<Map<String, Map<String, String>>> genin = new GenericTypeIndicator<Map<String, Map<String, String>>>() {};
                        Map<String, Map<String,String>> map = dataSnapshot.getValue(genin);
                        productos.clear();

                        if (map != null){
                            for (Map.Entry<String, Map<String, String>> entry : map.entrySet()){
                                if (entry != null){
                                    HashMap value = (HashMap) entry.getValue();
                                    Producto p = new Producto();
                                    p.set_id((String)value.get(ID));
                                    p.set_titulo((String)value.get(TITULO));
                                    p.set_precio((String) value.get(PRECIO));
                                    productos.add(p);
                                }
                            }
                        }
                        prodAdpt.notifyDataSetChanged();
                        poblateProds(position, productos_incluidos);
                        prodList.setAdapter(prodAdpt);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, "Error occurred: " + databaseError.getMessage());
                    }
                });
                //</editor-fold>

                nombreET.setText(especiales.get(position).get_nombre());
                porcientoET.setText(especiales.get(position).get_porciento());
                fechaInicioET.setText(especiales.get(position).get_fechaInicio());
                fechaFinET.setText(especiales.get(position).get_fechaFin());

                poblateProds(position, productos_incluidos);
                poblateAtrs(position, atr_incluidos);

                atrList.setAdapter(atrAdapt);
                prodList.setAdapter(prodAdpt);

                editEspecial.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*nombreET.setEnabled(true);
                        porcientoET.setEnabled(true);
                        fechaFinET.setEnabled(true);
                        fechaInicioET.setEnabled(true);
                        editEspecial.setVisibility(View.GONE);
                        acceptEspecial.setVisibility(View.VISIBLE);*/
                        dialog.dismiss();
                    }
                });

                deleteEspecial.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        crudSpecials.deleteSpecial(especiales.get(position));
                        dialog.dismiss();
                    }
                });

                //<editor-fold desc="comentado">
                /*acceptEspecial.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean cancel = false;
                        String nombre, porciento, fecha_inicio, fecha_fin;

                        if (nombreET.getText().toString().equals("")){
                            cancel = true;
                            nombreET.setError(context.getString(R.string.vacio));
                            focusView = nombreET;
                        } else if (porcientoET.getText().toString().equals("")){
                            cancel = true;
                            porcientoET.setError(context.getString(R.string.vacio));
                            focusView = porcientoET;
                        } else if (fechaInicioET.getText().toString().equals("") ||
                                !DateValidator.isDateValid(fechaInicioET.getText().toString(),DATEFORMAT)){
                            cancel = true;
                            fechaInicioET.setError(context.getString(R.string.fechaInvalida));
                            focusView = fechaInicioET;
                        } else if (fechaInicioET.getText().toString().equals("") ||
                                !DateValidator.isDateValid(fechaInicioET.getText().toString(),DATEFORMAT)){
                            cancel = true;
                            fechaFinET.setError(context.getString(R.string.fechaInvalida));
                            focusView = fechaFinET;
                        }


                        if (cancel){
                            focusView.requestFocus();
                        } else {
                            nombre = nombreET.getText().toString();
                            porciento = porcientoET.getText().toString();
                            fecha_inicio = fechaInicioET.getText().toString();
                            fecha_fin = fechaFinET.getText().toString();
                            Especial e = new Especial(especiales.get(position).get_id(), nombre, porciento,
                                    especiales.get(position).get_productos(), especiales.get(position).get_atracciones(), fecha_inicio, fecha_fin);

                            crudSpecials.updateSpecial(e);
                            dialog.dismiss();
                            Toast.makeText(context, context.getString(R.string.cambio_realizado), Toast.LENGTH_SHORT).show();
                        }
                    }
                });*/
                //</editor-fold>
                dialog.show();
            }
        });
    }

    private void poblateProds(int position, List<Producto> productos_incluidos) {
        if (especiales.get(position).get_productos() != null) {
            for (String id : especiales.get(position).get_productos()){
                for (Producto p : productos){
                    if (p.get_id().equals(id)){
                        productos_incluidos.add(p);
                    }
                }
            }
        }

    }

    private void poblateAtrs(int position, List<Atraccion> atr_incluidos){
        if (especiales.get(position).get_atracciones() != null) {
            for (String id : especiales.get(position).get_atracciones()){
                for (Atraccion a : atracciones){
                    if (a.get_id().equals(id)){
                        atr_incluidos.add(a);
                    }
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return especiales.size();
    }

}

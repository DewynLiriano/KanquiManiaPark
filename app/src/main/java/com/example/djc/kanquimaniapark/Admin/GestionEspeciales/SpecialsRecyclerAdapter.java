package com.example.djc.kanquimaniapark.Admin.GestionEspeciales;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
import com.example.djc.kanquimaniapark.Clases.Especial;
import com.example.djc.kanquimaniapark.Clases.Producto;
import com.example.djc.kanquimaniapark.Helpers.DateValidator;
import com.example.djc.kanquimaniapark.R;

import java.util.List;

/**
 * Created by dewyn on 10/27/2016.
 */
public class SpecialsRecyclerAdapter extends RecyclerView.Adapter<SpecialsVH> {

    private Context context;
    private List<Especial> especiales;
    private Dialog dialog;
    private CRUDSpecialsFireBaseHelper crudSpecials;
    private View focusView = null;
    private String DATEFORMAT = "dd/MM/yyyy";
    private ArrayAdapter adapter;

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
                final EditText nombreET = (EditText)v.findViewById(R.id.edit_especial_nombre);
                final EditText porcientoET = (EditText)v.findViewById(R.id.edit_especial_porciento);
                final EditText fechaInicioET = (EditText)v.findViewById(R.id.edit_fecha_inicio_especial);
                final EditText fechaFinET = (EditText)v.findViewById(R.id.edit_fecha_fin_especial);
                final ListView listView = (ListView)v.findViewById(R.id.edit_productos_agregados);
                final RadioGroup radios = (RadioGroup)v.findViewById(R.id.edit_radiogroup);
                final RadioButton atrRB = (RadioButton)v.findViewById(R.id.edit_especial_atracciones_rb);
                final RadioButton prodRB = (RadioButton)v.findViewById(R.id.edit_especial_productos_rb);
                final Button editEspecial = (Button)v.findViewById(R.id.edit_especial_button);
                final Button acceptEspecial = (Button)v.findViewById(R.id.accept_especial_button);
                final Button deleteEspecial = (Button)v.findViewById(R.id.delete_especial_button);
                final Spinner spinner = (Spinner)v.findViewById(R.id.edit_tipo_spinner);
                //</editor-fold>

                nombreET.setText(especiales.get(position).get_nombre());
                porcientoET.setText(especiales.get(position).get_porciento());
                fechaInicioET.setText(especiales.get(position).get_fechaInicio());
                fechaFinET.setText(especiales.get(position).get_fechaFin());
                adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1,
                        especiales.get(position).get_productos());
                listView.setAdapter(adapter);

                editEspecial.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nombreET.setEnabled(true);
                        porcientoET.setEnabled(true);
                        fechaFinET.setEnabled(true);
                        fechaInicioET.setEnabled(true);
                        listView.setClickable(true);
                        radios.setVisibility(View.VISIBLE);
                        editEspecial.setVisibility(View.GONE);
                        acceptEspecial.setVisibility(View.VISIBLE);
                    }
                });

                deleteEspecial.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        crudSpecials.deleteSpecial(especiales.get(position));
                        dialog.dismiss();
                    }
                });

                acceptEspecial.setOnClickListener(new View.OnClickListener() {
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
                            Especial e = new Especial(especiales.get(position).get_id(), nombre, porciento, especiales.get(position).get_productos(), fecha_inicio, fecha_fin);
                            crudSpecials.updateSpecial(e);
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
        return especiales.size();
    }
}

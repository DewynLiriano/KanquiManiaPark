package com.example.djc.kanquimaniapark.Admin.GestionEmpleados;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.djc.kanquimaniapark.Admin.ItemClickListener;
import com.example.djc.kanquimaniapark.Clases.Empleado;
import com.example.djc.kanquimaniapark.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class EmployeeRecyclerAdapter extends RecyclerView.Adapter<EmpViewHolder> {

    public String POSICIONES = "Posiciones";

    private List<Empleado> list;
    private Context context;
    private Dialog dialog;
    private Spinner spinner;
    private DatabaseReference dRef;
    private CRUDEmployeeFireBHelper crudEmployee;
    private List<String> posiciones;
    private ArrayAdapter<String> spinnerAdapter;
    private View focusView = null;


    public EmployeeRecyclerAdapter(Context context, List<Empleado> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public EmpViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_empleados_list, parent, false);
        dRef = FirebaseDatabase.getInstance().getReference(POSICIONES);
        dRef.keepSynced(true);
        return new EmpViewHolder(v);
    }

    @Override
    public void onBindViewHolder(EmpViewHolder holder, final int position) {
        holder.tv1.setText(list.get(position).get_nombre());
        holder.tv2.setText(list.get(position).get_tipo());

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void OnItemClickListener(View v, final int pos) {
                dialog = new Dialog(context);
                dialog.setContentView(R.layout.edit_empleado_dialog);
                dialog.setTitle(context.getString(R.string.gestionar_empleados));

                crudEmployee = new CRUDEmployeeFireBHelper();

                //<editor-fold desc="CREANDO WIDGETS">
                final EditText nombreTV = (EditText) dialog.findViewById(R.id.show_empname);
                final EditText apellidoTV = (EditText) dialog.findViewById(R.id.show_emplast);
                final EditText usernameTV = (EditText) dialog.findViewById(R.id.show_empusername);
                final EditText passTV = (EditText) dialog.findViewById(R.id.edit_empleado_pass);
                final TextView posicionTV = (TextView)dialog.findViewById(R.id.edit_posicion);
                spinner = (Spinner) dialog.findViewById(R.id.show_spinner_posiciones);
                final RadioButton maleRB = (RadioButton) dialog.findViewById(R.id.show_male_empsex);
                final RadioButton femaleRB = (RadioButton) dialog.findViewById(R.id.show_female_empsex);
                final Button editEmpleado = (Button)dialog.findViewById(R.id.edit_empleado);
                Button deleteEmpleado = (Button)dialog.findViewById(R.id.delete_empleado);
                final Button acceptChanges = (Button)dialog.findViewById(R.id.aceptar_edit);
                //</editor-fold>

                //<editor-fold desc="Spinner settings">
                posiciones = new ArrayList<>();
                spinnerCotroller(dialog);
                dRef.addChildEventListener(posRef);
                //</editor-fold>

                //<editor-fold desc="SETTINGS ACTUAL VALUES">
                nombreTV.setText(list.get(position).get_nombre());
                apellidoTV.setText(list.get(position).get_apellido());
                usernameTV.setText(list.get(position).get_userName());
                passTV.setText(list.get(position).get_contrasena());
                posicionTV.setText(list.get(position).get_tipo());
                //</editor-fold>

                //<editor-fold desc="FILTRANDO SEXO">
                switch (list.get(position).get_sexo()){
                    case "M":
                        maleRB.setChecked(true);
                        break;
                    case "F":
                        femaleRB.setChecked(true);
                }
                //</editor-fold>

                //<editor-fold desc="Edit Button Listener">
                editEmpleado.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nombreTV.setEnabled(true);
                        apellidoTV.setEnabled(true);
                        usernameTV.setEnabled(true);
                        passTV.setEnabled(true);
                        posicionTV.setVisibility(View.GONE);
                        spinner.setVisibility(View.VISIBLE);
                        maleRB.setClickable(true);
                        femaleRB.setClickable(true);
                        editEmpleado.setVisibility(View.GONE);
                        acceptChanges.setVisibility(View.VISIBLE);
                    }
                });
                //</editor-fold>

                //<editor-fold desc="Delete Button Listener">
                deleteEmpleado.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CRUDEmployeeFireBHelper mRef = new CRUDEmployeeFireBHelper();
                        mRef.deleteEmployee(list.get(position));
                        dialog.dismiss();
                    }
                });
                //</editor-fold>

                //<editor-fold desc="Accept Button Listener">
                acceptChanges.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean cancel = false;
                        String nombre, apellido, username, pass, posicion;
                        String sexo = "";

                        if (nombreTV.getText().toString().length() < 1){
                            cancel = true;
                            nombreTV.setError(context.getString(R.string.vacio));
                            focusView = nombreTV;
                        } else if (apellidoTV.getText().toString().length() < 1){
                            cancel = true;
                            apellidoTV.setError(context.getString(R.string.vacio));
                            focusView = apellidoTV;
                        } else if (usernameTV.getText().toString().length() < 1){
                            cancel = true;
                            usernameTV.setError(context.getString(R.string.vacio));
                            focusView = usernameTV;
                        } else if (passTV.getText().toString().length() < 1){
                            cancel = true;
                            passTV.setError(context.getString(R.string.vacio));
                            focusView = passTV;
                        } else if (!femaleRB.isChecked() && !maleRB.isChecked()) {
                            cancel = true;
                            sex_alertBuilder().show();
                        }

                        if (cancel){
                            focusView.requestFocus();
                        } else {
                            nombre = nombreTV.getText().toString();
                            apellido = apellidoTV.getText().toString();
                            username = usernameTV.getText().toString();
                            pass = passTV.getText().toString();
                            posicion = spinner.getSelectedItem().toString();

                            if  (maleRB.isChecked()){
                                sexo = "M";
                            } else if (femaleRB.isChecked()){
                                sexo = "F";
                            }
                            Empleado empleado = new Empleado(list.get(position).get_id(), nombre, apellido,
                                    sexo, username, pass, posicion);
                            crudEmployee.updateEmployee(empleado);
                            dialog.dismiss();
                            Toast.makeText(context, context.getString(R.string.cambio_realizado), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                //</editor-fold>
                dialog.show();
            }
        });
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    private void spinnerCotroller(Dialog dialog) {
         spinnerAdapter = new ArrayAdapter<>(dialog.getContext(),
                R.layout.support_simple_spinner_dropdown_item, posiciones);
        spinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
    }

    private ChildEventListener posRef = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
           //posicionesKeys.add(dataSnapshot.getKey());
            posiciones.add((String) dataSnapshot.getValue());
            spinnerAdapter.notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            posiciones.remove(dataSnapshot.getValue());
            spinnerAdapter.notifyDataSetChanged();
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    public AlertDialog.Builder sex_alertBuilder(){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.error))
                .setMessage(context.getString(R.string.sexo_invalido));
        alertBuilder.setNeutralButton(context.getString(R.string.ok), null);
        return alertBuilder;
    }
}

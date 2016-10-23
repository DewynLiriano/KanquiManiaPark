package com.example.djc.kanquimaniapark.Admin.GestionEmpleados;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.djc.kanquimaniapark.Clases.Empleado;
import com.example.djc.kanquimaniapark.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GestionEmpleados extends Fragment {

    public String EMPLEADOS = "Empleados";
    public String ID = "ID";
    public String NOMBRE = "Nombre";
    public String APELLIDO = "Apellido";
    public String SEXO = "Sexo";
    public String USERNAME = "Username";
    public String CONTRASENA = "Contrasena";
    public String POSICION = "Posicion";

    private RecyclerView recyclerView;
    private List<Empleado> empleados;
    private Spinner spinner;
    private Button crearEmpleadoBtn;
    //private AddEmployeeFireBHelper database;
    private EditText nombreET, apellidoET, usernameET, passET;
    private RadioButton radioHembra, radioVaron;

    EmployeeRecyclerAdapter adapter;
    private View focusView = null;

    public GestionEmpleados() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_empleados, container, false);

        //database = new AddEmployeeFireBHelper();
        empleados = new ArrayList<Empleado>();
        DatabaseReference dRef = FirebaseDatabase.getInstance().getReference("Empleados");
        //dRef.keepSynced(true);
        //dRef.addChildEventListener(crudEmpleados);
        dRef.addValueEventListener(crudEmpl);
        adapter = new EmployeeRecyclerAdapter(getContext(), empleados);
        recyclerViewController(view);
        initializeUtils(view);
        listeners(view);
        spinnerCotroller(view);
        // Inflate the layout for this fragment
        return view;
    }

    private void listeners(View view) {
        crearEmpleadoBtn.setOnClickListener(addClientOnClick);
    }

    private void initializeUtils(View view) {
        nombreET = (EditText)view.findViewById(R.id.empleado_nombre);
        apellidoET = (EditText)view.findViewById(R.id.empleado_apellido);
        usernameET = (EditText)view.findViewById(R.id.empleado_username);
        passET = (EditText)view.findViewById(R.id.empleado_pass);
        radioHembra = (RadioButton)view.findViewById(R.id.empleado_hembra);
        radioVaron = (RadioButton)view.findViewById(R.id.empleado_varon);
        crearEmpleadoBtn = (Button)view.findViewById(R.id.empleado_crear);
    }

    private void spinnerCotroller(View view) {
        spinner = (Spinner)view.findViewById(R.id.spinner_posiciones);
        String posiciones[] = {"Cajero", "Control Atracciones"};

        ArrayAdapter<String> adapterPosiciones = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, posiciones);
        adapterPosiciones.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapterPosiciones);
    }

    public void recyclerViewController(View view){
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerEmpleados);
        EmployeeRecyclerAdapter adapter = new EmployeeRecyclerAdapter(getContext(), empleados);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    View.OnClickListener addClientOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            boolean cancel = false;
            String nombre, apellido, username, pass, posicion;
            String sexo = "";

            if (nombreET.getText().toString().length() < 1){
                cancel = true;
                nombreET.setError(getString(R.string.vacio));
                focusView = nombreET;
            } else if (apellidoET.getText().toString().length() < 1){
                cancel = true;
                apellidoET.setError(getString(R.string.vacio));
                focusView = apellidoET;
            } else if (usernameET.getText().toString().length() < 1){
                cancel = true;
                usernameET.setError(getString(R.string.vacio));
                focusView = usernameET;
            } else if (passET.getText().toString().length() < 1){
                cancel = true;
                passET.setError(getString(R.string.vacio));
                focusView = passET;
            } else if (!radioHembra.isChecked() && !radioVaron.isChecked()) {
                cancel = true;
                sex_alertBuilder().show();
            }

            if (cancel){
                focusView.requestFocus();
            } else {
                nombre = nombreET.getText().toString();
                apellido = apellidoET.getText().toString();
                username = usernameET.getText().toString();
                pass = passET.getText().toString();
                posicion = spinner.getSelectedItem().toString();

                if  (radioVaron.isChecked()){
                    sexo = "M";
                } else if (radioHembra.isChecked()){
                    sexo = "F";
                }
                Empleado empleado = new Empleado("", nombre, apellido,
                        sexo, username, pass, posicion);
                addEmployee(empleado);
            }
        }
    };

    public AlertDialog.Builder sex_alertBuilder(){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext())
                .setTitle(getString(R.string.error))
                .setMessage(getString(R.string.sexo_invalido));
        alertBuilder.setNeutralButton(getString(R.string.ok), null);
        return alertBuilder;
    }

    ChildEventListener crudEmpleados = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                GenericTypeIndicator<Map<String, String>> genin = new GenericTypeIndicator<Map<String, String>>() {};
                Map<String, String> map = dataSnapshot.getValue(genin);

                if (map != null){
                    for (Map.Entry<String, String> entry : map.entrySet()){
                        if (entry != null){
                           // Toast.makeText(getContext(), entry.getValue(), Toast.LENGTH_SHORT).show();
                           // Toast.makeText(getContext(), entry.getValue(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }



                //Toast.makeText(getContext(), map.get(APELLIDO), Toast.LENGTH_SHORT).show();

                Empleado e = new Empleado();
                /*for (Map.Entry<String, Map<String,String>> m : map.entrySet()){
                    if (m!=null) {
                        HashMap value = (HashMap) m.getValue();
                        e.set_nombre((String)value.get(NOMBRE));
                        e.set_apellido((String)value.get(APELLIDO));
                        e.set_userName((String)value.get(USERNAME));
                        e.set_contrasena((String)value.get(CONTRASENA));
                        e.set_sexo((String)value.get(SEXO));
                        e.set_tipo((String)value.get(POSICION));
                    }
                }

                Toast.makeText(getContext(), e.get_apellido(), Toast.LENGTH_SHORT).show();
                */

                /*

                Empleado e = new Empleado();
                e.set_nombre((String)value.get(NOMBRE));
                e.set_apellido((String)value.get(APELLIDO));
                e.set_userName((String)value.get(USERNAME));
                e.set_contrasena((String)value.get(CONTRASENA));
                e.set_sexo((String)value.get(SEXO));
                e.set_tipo((String)value.get(POSICION));

                */


                /*Empleado e = new Empleado((String)dataSnapshot.child(NOMBRE).getValue(), (String)dataSnapshot.child(APELLIDO).getValue(), (String)dataSnapshot.child(SEXO).getValue(),
                    (String)dataSnapshot.child(USERNAME).getValue(), (String)dataSnapshot.child(CONTRASENA).getValue(), (String)dataSnapshot.child(POSICION).getValue());*/

                /*Empleado e = new Empleado();
                Map<String, Object> data;
                data = (Map<String, Object>) dataSnapshot.getValue();

                //e.set_id((String)dataSnapshot.child(ID).getValue());
                e.set_nombre((String)data.get(NOMBRE));
                e.set_nombre((String)data.get(APELLIDO));
                e.set_apellido((String)data.get(USERNAME));
                e.set_userName((String)data.get(CONTRASENA));
                e.set_contrasena((String)data.get(SEXO));
                e.set_sexo((String)data.get(SEXO));*/


                /*Toast.makeText(getContext(), (String)dataSnapshot.child(APELLIDO).getValue(), Toast.LENGTH_SHORT).show();
                Toast.makeText(getContext(), (String)dataSnapshot.child(NOMBRE).getValue(), Toast.LENGTH_SHORT).show();
                Toast.makeText(getContext(), (String)dataSnapshot.child(SEXO).getValue(), Toast.LENGTH_SHORT).show();
                Toast.makeText(getContext(), (String)dataSnapshot.child(USERNAME).getValue(), Toast.LENGTH_SHORT).show();
                Toast.makeText(getContext(), (String)dataSnapshot.child(CONTRASENA).getValue(), Toast.LENGTH_SHORT).show();
                Toast.makeText(getContext(), (String)dataSnapshot.child(POSICION).getValue(), Toast.LENGTH_SHORT).show();*/

                //Toast.makeText(getContext(), (int)dataSnapshot.getChildrenCount(), Toast.LENGTH_SHORT).show();
                /*
                empleados.add(e);
                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);*/
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                //recyclerView.setAdapter(adapter);
                //adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            /*Empleado e = new Empleado();
            //e.set_id((String)dataSnapshot.child(ID).getValue());
            e.set_nombre((String)dataSnapshot.child(NOMBRE).getValue());
            e.set_tipo((String)dataSnapshot.getChildren().iterator().next().child(POSICION).getValue());
            e.set_apellido((String)dataSnapshot.child(APELLIDO).getValue());
            e.set_userName((String)dataSnapshot.child(USERNAME).getValue());
            e.set_contrasena((String)dataSnapshot.child(CONTRASENA).getValue());
            e.set_sexo((String)dataSnapshot.child(SEXO).getValue());
            empleados.remove(e);*/
                //adapter.notifyDataSetChanged();
                // recyclerView.setAdapter(adapter);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

    ValueEventListener crudEmpl = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            GenericTypeIndicator<Map<String, Map<String, String>>> genin = new GenericTypeIndicator<Map<String, Map<String, String>>>() {};
            Map<String, Map<String,String>> map = dataSnapshot.getValue(genin);
            empleados.clear();

            if (map != null){
                for (Map.Entry<String, Map<String, String>> entry : map.entrySet()){
                    if (entry != null){
                        HashMap value = (HashMap) entry.getValue();
                        Empleado e = new Empleado();
                        e.set_id((String)value.get(ID));
                        e.set_nombre((String)value.get(NOMBRE));
                        e.set_apellido((String)value.get(APELLIDO));
                        e.set_userName((String)value.get(USERNAME));
                        e.set_contrasena((String)value.get(CONTRASENA));
                        e.set_sexo((String)value.get(SEXO));
                        e.set_tipo((String)value.get(POSICION));
                        empleados.add(e);
                    }
                }
            }

            sortEmpleados();
            adapter.notifyDataSetChanged();
            recyclerView.setAdapter(adapter);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    private void sortEmpleados() {
        Collections.sort(empleados, new Comparator<Empleado>() {
            @Override
            public int compare(Empleado o1, Empleado o2) {
                return o2.get_id().compareTo(o1.get_id());
            }
        });
    }

    public void addEmployee(Empleado empleado){
        DatabaseReference dRef = FirebaseDatabase.getInstance().getReference(EMPLEADOS).push();
        dRef.child(ID).setValue(dRef.getKey());
        dRef.child(NOMBRE).setValue(empleado.get_nombre());
        dRef.child(APELLIDO).setValue(empleado.get_apellido());
        dRef.child(SEXO).setValue(empleado.get_sexo());
        dRef.child(USERNAME).setValue(empleado.get_userName());
        dRef.child(CONTRASENA).setValue(empleado.get_contrasena());
        dRef.child(POSICION).setValue(empleado.get_tipo());
    }



}
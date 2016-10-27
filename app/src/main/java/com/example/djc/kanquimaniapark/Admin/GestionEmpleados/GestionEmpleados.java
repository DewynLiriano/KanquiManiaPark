package com.example.djc.kanquimaniapark.Admin.GestionEmpleados;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

public class GestionEmpleados extends Fragment {


    //<editor-fold desc="CONSTANTES">
    //NODO DE LOS EMPLEADOS
    private String EMPLEADOS = "Empleados";
    private String ID = "ID";
    private String NOMBRE = "Nombre";
    private String APELLIDO = "Apellido";
    private String SEXO = "Sexo";
    private String USERNAME = "Username";
    private String CONTRASENA = "Contrasena";
    private String POSICION = "Posicion";

    //NODO DE LAS POSICIONES
    private String POSICIONES = "Posiciones";
    //</editor-fold>

    private RecyclerView recyclerView;
    private List<Empleado> empleados;
    private List<String> posiciones;
    private ArrayAdapter<String> adapterPosiciones;
    private Spinner spinner;
    private Button crearEmpleadoBtn;
    private EditText nombreET, apellidoET, usernameET, passET;
    private RadioButton radioHembra, radioVaron;
    private CRUDEmployeeFireBHelper crudEmployee;
    private EmployeeRecyclerAdapter adapter;
    private View focusView = null;
    private DatabaseReference dRef;

    public GestionEmpleados() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_empleados, container, false);

        empleados = new ArrayList<>();
        posiciones = new ArrayList<>();
        dRef = FirebaseDatabase.getInstance().getReference(EMPLEADOS);
        crudEmployee = new CRUDEmployeeFireBHelper();
        dRef.keepSynced(true);
        adapter = new EmployeeRecyclerAdapter(getContext(), empleados);
        recyclerViewController(view);
        initializeUtils(view);
        listeners();
        spinnerCotroller(view);
        // Inflate the layout for this fragment
        return view;
    }

    private void listeners() {
        crearEmpleadoBtn.setOnClickListener(addClientOnClick);
        dRef.addValueEventListener(getEmpl);
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
        dRef = FirebaseDatabase.getInstance().getReference(POSICIONES);
        dRef.addChildEventListener(getPos);

        adapterPosiciones = new ArrayAdapter<>(getContext(),
                R.layout.support_simple_spinner_dropdown_item, posiciones);
        adapterPosiciones.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapterPosiciones);
    }

    public void recyclerViewController(View view){
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerEmpleados);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    //<editor-fold desc="Listener de agregar cliente">
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

            for (Empleado e : empleados){
                if (e.get_userName().equals(usernameET.getText().toString())){
                    cancel = true;
                    usernameET.setError(getString(R.string.usuario_existe));
                    focusView = usernameET;
                }
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

                crudEmployee.addEmployee(empleado);
                nombreET.setText("");
                apellidoET.setText("");
                usernameET.setText("");
                passET.setText("");
                focusView = nombreET;
                focusView.requestFocus();
            }
        }
    };
    //</editor-fold>

    public AlertDialog.Builder sex_alertBuilder(){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext())
                .setTitle(getString(R.string.error))
                .setMessage(getString(R.string.sexo_invalido));
        alertBuilder.setNeutralButton(getString(R.string.ok), null);
        return alertBuilder;
    }

    ValueEventListener getEmpl = new ValueEventListener() {
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
            Log.e(TAG, "Error occurred: " + databaseError.getMessage());
        }
    };

    ChildEventListener getPos = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            posiciones.add((String)dataSnapshot.getValue());
            adapterPosiciones.notifyDataSetChanged();
            spinner.setAdapter(adapterPosiciones);
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            posiciones.remove(dataSnapshot.getValue());
            adapterPosiciones.notifyDataSetChanged();
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

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

}
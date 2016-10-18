package com.example.djc.kanquimaniapark.Admin.GestionEmpleados;

import android.os.Bundle;
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
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.djc.kanquimaniapark.Clases.Empleado;
import com.example.djc.kanquimaniapark.Clases.Persona;
import com.example.djc.kanquimaniapark.R;

import java.util.ArrayList;
import java.util.Objects;

public class GestionEmpleados extends Fragment {

    private RecyclerView recyclerView;
    private Spinner spinner;
    private Button crearEmpleadoBtn;
    private EmployeeFireBaseHelper database;
    private EditText nombreET, apellidoET, usernameET, passET;
    private RadioButton radioHembra, radioVaron;
    //private String sexo = "";
    private View focusView = null;

    public GestionEmpleados() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_empleados, container, false);
        database = new EmployeeFireBaseHelper();
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

        ArrayAdapter<String> adapterPosiciones = new ArrayAdapter<>(getContext(),
                R.layout.support_simple_spinner_dropdown_item, posiciones);
        adapterPosiciones.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapterPosiciones);
    }

    public void recyclerViewController(View view){
        //Recycler view
        ArrayList<String> data = new ArrayList<>();

        for(int i = 0; i < 30; i++) {
            data.add("Patricio Carrasquilla " + i);
        }

        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerEmpleados);
        EmployeeRecyclerAdapter adapter = new EmployeeRecyclerAdapter(getContext(), data);
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
                //Toast.makeText(getContext(), sexo, Toast.LENGTH_SHORT).show();
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
                Empleado empleado = new Empleado(database.getCount(), nombre, apellido,
                        sexo, username, pass, posicion);
                database.addEmployee(empleado);
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
}
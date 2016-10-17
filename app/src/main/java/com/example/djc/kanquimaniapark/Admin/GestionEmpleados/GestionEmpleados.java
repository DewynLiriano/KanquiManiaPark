package com.example.djc.kanquimaniapark.Admin.GestionEmpleados;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.djc.kanquimaniapark.R;

import java.util.ArrayList;

public class GestionEmpleados extends Fragment {

    private RecyclerView recyclerView;
    private Spinner spinner;
    private Button crearEmpleadoBtn;
    private EmployeeFireBaseHelper database;

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
        recyclerViewController(view);
        spinnerCotroller(view);
        crearEmpleado(view);


        // Inflate the layout for this fragment
        return view;
    }

    private void crearEmpleado(View view) {
        crearEmpleadoBtn = (Button)view.findViewById(R.id.empleado_crear);

        crearEmpleadoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
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

}

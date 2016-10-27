package com.example.djc.kanquimaniapark.Admin.GestionAtracciones;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.djc.kanquimaniapark.Clases.Atraccion;
import com.example.djc.kanquimaniapark.Clases.Producto;
import com.example.djc.kanquimaniapark.R;
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


public class GestionAtracciones extends Fragment {

    private String ATRACCIONES = "Atracciones";
    private String ID = "ID";
    private String NOMBRE = "Nombre";
    private String PRECIO = "Precio";
    private String TIEMPO = "Tiempo"; //Minutos

    private CRUDAtractionsFireBaseHelper crudAtractions;
    private RecyclerView recyclerView;
    private EditText tituloET, precioET, tiempoET;
    private AtractionsRecyclerAdapter adapter;
    private DatabaseReference dRef;
    private List<Atraccion> atracciones;
    private Button crearAtractionButton;
    private View focusView = null;



    public GestionAtracciones() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_atracciones, container, false);

        atracciones = new ArrayList<>();
        dRef = FirebaseDatabase.getInstance().getReference(ATRACCIONES);
        dRef.keepSynced(true);
        crudAtractions = new CRUDAtractionsFireBaseHelper();
        adapter = new AtractionsRecyclerAdapter(getContext(), atracciones);
        recyclerViewController(view);
        initializeUtils(view);
        crearAtractionButton.setOnClickListener(addAtraction);
        dRef.addValueEventListener(getAtr);

        // Inflate the layout for this fragment
        return view;
    }

    private void initializeUtils(View view) {
        tituloET = (EditText)view.findViewById(R.id.nombre_atraccion);
        precioET = (EditText)view.findViewById(R.id.precio_atraccion);
        tiempoET = (EditText)view.findViewById(R.id.tiempo_atraccion);
        crearAtractionButton = (Button)view.findViewById(R.id.crear_atraccion_button);
    }

    private void recyclerViewController(View view) {
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_atracciones);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private View.OnClickListener addAtraction = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            boolean cancel = false;
            String nombre, precio, tiempo;

            if (tituloET.getText().toString().equals("")){
                cancel = true;
                tituloET.setError(getString(R.string.vacio));
                focusView = tituloET;
            } else if (precioET.getText().toString().equals("")){
                cancel = true;
                precioET.setError(getString(R.string.vacio));
                focusView = precioET;
            } else if (tiempoET.getText().toString().equals("")){
                cancel = true;
                tiempoET.setError(getString(R.string.vacio));
                focusView = tiempoET;
            }

            for (Atraccion a : atracciones){
                    if (a.get_titulo().equals(tituloET.getText().toString())){
                        cancel = true;
                        tituloET.setError(getString(R.string.nombre_existe));
                        focusView = tituloET;
                    }
            }

            if (cancel){
                focusView.requestFocus();
            } else{
                nombre = tituloET.getText().toString();
                precio = precioET.getText().toString();
                tiempo = tiempoET.getText().toString();

                Atraccion a = new Atraccion("", nombre, precio, tiempo);
                crudAtractions.addAtraction(a);

                tituloET.setText("");
                precioET.setText("");
                tiempoET.setText("");
                focusView = tituloET;
                focusView.requestFocus();
            }
        }
    };

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
            sortAtractions();
            adapter.notifyDataSetChanged();
            recyclerView.setAdapter(adapter);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.e(TAG, "Error occurred: " + databaseError.getMessage());
        }
    };

    private void sortAtractions() {
        Collections.sort(atracciones, new Comparator<Atraccion>() {
            @Override
            public int compare(Atraccion o1, Atraccion o2) {
                return o2.get_id().compareTo(o1.get_id());
            }
        });
    }
}

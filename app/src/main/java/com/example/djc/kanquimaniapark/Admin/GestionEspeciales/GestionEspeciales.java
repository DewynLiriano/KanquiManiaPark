package com.example.djc.kanquimaniapark.Admin.GestionEspeciales;

import android.app.DialogFragment;
import android.content.DialogInterface;
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
import android.widget.ListView;
import android.widget.Toast;

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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

public class GestionEspeciales extends Fragment {

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

    private CRUDSpecialsFireBaseHelper crudSpecials;
    private RecyclerView recyclerView;
    private SpecialsRecyclerAdapter adapter;
    private ArrayAdapter<Atraccion> atrAdapter;
    private ArrayAdapter<Producto> prodAdapter;

    private DatabaseReference dRef, atrRef, prodRef;
    private List<Especial> especiales;
    private List<Atraccion> atracciones;
    private List<Producto> productos;
    private View focusView;

    private EditText nombreET, porcientoET, fechaInicioET, fechaFinET;
    private ListView listProd, listAtr;
    private Button acceptEspecial;


    public GestionEspeciales() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_especiales, container, false);

        especiales = new ArrayList<>();
        atracciones = new ArrayList<>();
        productos = new ArrayList<>();
        dRef = FirebaseDatabase.getInstance().getReference(ESPECIALES);
        dRef.keepSynced(true);
        atrRef = FirebaseDatabase.getInstance().getReference(ATRACCIONES);
        atrRef.keepSynced(true);
        atrRef.addValueEventListener(getAtr);
        prodRef = FirebaseDatabase.getInstance().getReference(PRODUCTOS);
        prodRef.keepSynced(true);
        prodRef.addValueEventListener(getProducts);
        crudSpecials = new CRUDSpecialsFireBaseHelper();
        adapter = new SpecialsRecyclerAdapter(getContext(), especiales);
        recyclerViewController(view);
        initializeUtils(view);
        acceptEspecial.setOnClickListener(addEspecial);
        fechaInicioET.setOnFocusChangeListener(fechaInicioFocus);
        fechaFinET.setOnFocusChangeListener(fechaFinFocus);
        dRef.addValueEventListener(getEspeciales);
        // Inflate the layout for this fragment
        return view;
    }

    private void recyclerViewController(View view) {
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_especiales);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void initializeUtils(View v){
        nombreET = (EditText)v.findViewById(R.id.especial_nombre);
        porcientoET = (EditText)v.findViewById(R.id.especial_porciento);
        fechaInicioET = (EditText)v.findViewById(R.id.fecha_inicio_especial);
        fechaFinET = (EditText)v.findViewById(R.id.fecha_fin_especial);
        acceptEspecial = (Button)v.findViewById(R.id.aceptar_especial);
        listProd = (ListView)v.findViewById(R.id.especial_prod_lista);
        listAtr = (ListView)v.findViewById(R.id.especial_atr_lista);

        atrAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_multiple_choice, atracciones);

        prodAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_multiple_choice, productos);

        listProd.setAdapter(prodAdapter);
        listAtr.setAdapter(atrAdapter);
    }


    //<editor-fold desc="On Click Listeners">
    View.OnClickListener addEspecial = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(view.getContext());
            dialog.setTitle(getString(R.string.atencion));
            dialog.setMessage(getString(R.string.desea_agregar_productos_especial));

            dialog.setPositiveButton(getString(R.string.aceptar), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    boolean cancel = false;
                    String nombre, porciento, fechaInicio, fechaFin;
                    ArrayList<String> prod_seleccionados = new ArrayList<>();
                    ArrayList<String> atr_seleccionadas = new ArrayList<>();

                    //<editor-fold desc="get Atrs and Prods">
                    for (int j=0;j<listProd.getCount();j++){
                        if (listProd.isItemChecked(j)){
                           if (!prod_seleccionados.contains(productos.get(j).get_id())){
                               prod_seleccionados.add(productos.get(j).get_id());
                           }
                        } else {
                            if (prod_seleccionados.contains(productos.get(j).get_id())){
                                prod_seleccionados.remove(j);
                            }
                        }
                    }

                    for (int j=0;j<listAtr.getCount();j++){
                        if (listAtr.isItemChecked(j)){
                            if (!atr_seleccionadas.contains(atracciones.get(j).get_id())){
                                atr_seleccionadas.add(atracciones.get(j).get_id());
                            }
                        } else {
                            if (atr_seleccionadas.contains(atracciones.get(j).get_id())){
                                atr_seleccionadas.remove(j);
                            }
                        }
                    }
                    //</editor-fold>

                    if (nombreET.getText().toString().equals("")){
                        cancel = true;
                        nombreET.setError(getString(R.string.vacio));
                        focusView = nombreET;
                    } else if (porcientoET.getText().toString().equals("")){
                        cancel = true;
                        porcientoET.setError(getString(R.string.vacio));
                        focusView = porcientoET;
                    } else if (fechaInicioET.getText().toString().equals("")
                            || !DateValidator.isDateValid(fechaInicioET.getText().toString(), DATEFORMAT)){
                        cancel = true;
                        fechaInicioET.setError(getString(R.string.fechaInvalida));
                        focusView = fechaInicioET;
                    } else if (fechaFinET.getText().toString().equals("")
                            || !DateValidator.isDateValid(fechaFinET.getText().toString(), DATEFORMAT)){
                        cancel = true;
                        fechaFinET.setError(getString(R.string.fechaInvalida));
                        focusView = fechaFinET;
                    } else if (Integer.parseInt(porcientoET.getText().toString()) > 100
                            || Integer.parseInt(porcientoET.getText().toString()) <= 0){
                        cancel = true;
                        porcientoET.setError(getString(R.string.porciento_invalido));
                        focusView = porcientoET;
                    }

                    if (cancel){
                        focusView.requestFocus();
                    } else {
                        nombre = nombreET.getText().toString();
                        porciento = porcientoET.getText().toString();
                        fechaInicio = fechaInicioET.getText().toString();
                        fechaFin = fechaFinET.getText().toString();

                        Especial e = new Especial("", nombre, porciento, prod_seleccionados, atr_seleccionadas, fechaInicio, fechaFin);
                        crudSpecials.addSpecial(e);
                        Toast.makeText(getContext(), getString(R.string.especial_creado), Toast.LENGTH_SHORT).show();

                        nombreET.setText("");
                        porcientoET.setText("");
                        fechaInicioET.setText("");
                        fechaFinET.setText("");
                        listAtr.setAdapter(atrAdapter);
                        listProd.setAdapter(prodAdapter);
                        focusView = nombreET;
                        focusView.requestFocus();
                    }
                }
            });
            dialog.setNegativeButton(getString(R.string.cancelar), null);
            dialog.show();
        }
    };
    //</editor-fold>

    //<editor-fold desc="Value event listeners">
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
            atrAdapter.notifyDataSetChanged();
            listAtr.setAdapter(atrAdapter);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.e(TAG, "Error occurred: " + databaseError.getMessage());
        }
    };

    private ValueEventListener getProducts = new ValueEventListener() {
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
            prodAdapter.notifyDataSetChanged();
            listProd.setAdapter(prodAdapter);
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.e(TAG, "Error occurred: " + databaseError.getMessage());
        }
    };

    private ValueEventListener getEspeciales = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            especiales.clear();
            Map<String, Object> rootMap = (Map<String, Object>)dataSnapshot.getValue();
            if (rootMap != null){
                Collection<Object> objects = rootMap.values();
                for (Object o : objects){
                    if (o instanceof Map){
                        HashMap<String, Object> map = (HashMap<String, Object>) o;
                        Especial e = new Especial();
                        e.set_id((String) map.get(ID));
                        e.set_nombre((String) map.get(NOMBRE));
                        e.set_porciento((String) map.get(PORCIENTO));
                        e.set_fechaInicio((String) map.get(FECHA_INICIO));
                        e.set_fechaFin((String) map.get(FECHA_FIN));

                        HashMap<String, String> productsMap = (HashMap<String, String>)map.get(PRODUCTOS);
                        HashMap<String, String> atrsMap = (HashMap<String, String>)map.get(ATRACCIONES);
                        if (productsMap != null){
                            e.set_productos(new ArrayList<>(productsMap.values()));
                        }
                        if (atrsMap != null){
                            e.set_atracciones(new ArrayList<>(atrsMap.values()));
                        }
                        especiales.add(e);
                    }
                }
            }
            adapter.notifyDataSetChanged();
            recyclerView.setAdapter(adapter);
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.e(TAG, "Error occurred: " + databaseError.getMessage());
        }
    };
    //</editor-fold>

    //<editor-fold desc="OnFocusChangeListeners">
    View.OnFocusChangeListener fechaInicioFocus = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus){
                DialogFragment fragment = new FechaInicioDatePickerFragment();
                fragment.show(getActivity().getFragmentManager(), "Date Picker");
            }
        }
    };

    View.OnFocusChangeListener fechaFinFocus = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus){
                DialogFragment fragment = new FechaFinDatePickerFragment();
                fragment.show(getActivity().getFragmentManager(), "Date Picker");
            }
        }
    };
    //</editor-fold>

}

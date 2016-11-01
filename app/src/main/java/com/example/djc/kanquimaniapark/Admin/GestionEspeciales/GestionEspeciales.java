package com.example.djc.kanquimaniapark.Admin.GestionEspeciales;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.djc.kanquimaniapark.Clases.Atraccion;
import com.example.djc.kanquimaniapark.Clases.Especial;
import com.example.djc.kanquimaniapark.Clases.Producto;
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

public class GestionEspeciales extends Fragment {

    private String ESPECIALES = "Especiales";
    private String ID = "ID";
    private String NOMBRE = "Nombre";
    private String PORCIENTO = "Porciento";
    private String FECHA_INICIO = "Fecha_Inicio";
    private String FECHA_FIN = "Fecha_Fin";
    private String PRODUCTOS = "Productos";

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
    private List<Atraccion> atracciones, atr_seleccionadas;
    private List<Producto> productos, prod_seleccionados;
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
        atr_seleccionadas = new ArrayList<>();
        prod_seleccionados = new ArrayList<>();
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
        listProd.setOnItemClickListener(launchProdDialog);
        listAtr.setOnItemClickListener(launchAtrDialog);
        acceptEspecial.setOnClickListener(addEspecial);

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

    //<editor-fold desc="OnItemClickListeners">
    AdapterView.OnItemClickListener launchProdDialog = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, final int indice, long l) {
            CheckedTextView checkedTextView = ((CheckedTextView)view);
            checkedTextView.setChecked(!checkedTextView.isChecked());

            if (!listProd.isItemChecked(indice)){
                listProd.setItemChecked(indice, true);
            } else{
                listProd.setItemChecked(indice, false);
            }

        }
    };

    AdapterView.OnItemClickListener launchAtrDialog = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, final int indice, long l) {
            CheckedTextView checkedTextView = ((CheckedTextView)view);
            checkedTextView.setChecked(!checkedTextView.isChecked());
        }
    };
    //</editor-fold>

    View.OnClickListener addEspecial = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(view.getContext());
            dialog.setTitle(getString(R.string.atencion));
            dialog.setMessage(getString(R.string.desea_agregar_atraccion_especial));

            dialog.setPositiveButton(getString(R.string.aceptar), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    SparseBooleanArray clickedAtrPositions = listAtr.getCheckedItemPositions();
                    SparseBooleanArray clickedProdPositions = listProd.getCheckedItemPositions();

                    for (int j=0; j < listProd.getCount(); j++){
                        if (clickedProdPositions.get(j)){
                            prod_seleccionados.add(productos.get(j));
                        } else {
                            Toast.makeText(getContext(), "false", Toast.LENGTH_SHORT).show();
                        }
                    }

                   /* for (int j=0;j<atracciones.size();j++){
                        if (clickedAtrPositions.valueAt(j)){
                            atr_seleccionadas.add(atracciones.get(j));
                        }
                    }*/

                    /*if (clickedProdPositions != null){
                        Toast.makeText(getContext(), clickedProdPositions.size(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Null", Toast.LENGTH_SHORT).show();
                    }*/
                }
            });

            dialog.setNegativeButton(getString(R.string.cancelar), null);
            dialog.show();
        }
    };

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
    //</editor-fold>


}

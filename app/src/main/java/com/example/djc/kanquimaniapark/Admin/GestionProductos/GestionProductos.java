package com.example.djc.kanquimaniapark.Admin.GestionProductos;

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
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.djc.kanquimaniapark.Admin.GestionEmpleados.EmployeeRecyclerAdapter;
import com.example.djc.kanquimaniapark.BuildConfig;
import com.example.djc.kanquimaniapark.Clases.Empleado;
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

public class GestionProductos extends Fragment {

    private String PRODUCTOS = "Productos";
    private String ID = "ID";
    private String NOMBRE = "Titulo";
    private String PRECIO = "Precio";

    private ProductsRecyclerAdapter adapter;
    private RecyclerView recyclerView;
    private EditText nombreET, precioET;
    private CRUDProductsFireBaseHelper crudProducts;
    private DatabaseReference dRef;
    private List<Producto> productos;
    private Button crearProductButton;
    private View focusView = null;

    public GestionProductos() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_productos, container, false);

        productos = new ArrayList<>();
        dRef = FirebaseDatabase.getInstance().getReference(PRODUCTOS);
        dRef.keepSynced(true);
        crudProducts = new CRUDProductsFireBaseHelper();
        adapter = new ProductsRecyclerAdapter(getContext(), productos);
        recyclerViewController(view);
        initializeUtils(view);
        crearProductButton.setOnClickListener(addProduct);
        dRef.addValueEventListener(getProducts);
        // Inflate the layout for this fragment
        return view;
    }

    private void initializeUtils(View view) {
        nombreET = (EditText)view.findViewById(R.id.nombre_producto);
        precioET = (EditText)view.findViewById(R.id.precio_producto);
        crearProductButton = (Button)view.findViewById(R.id.crear_producto_button);
    }

    private void recyclerViewController(View view) {
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerProductos);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    View.OnClickListener addProduct = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            boolean cancel = false;
            String titulo, precio;

            if (nombreET.getText().toString().equals("")){
                cancel = true;
                nombreET.setError(getString(R.string.vacio));
                focusView = nombreET;
            } else if (precioET.getText().toString().equals("")){
                cancel = true;
                precioET.setError(getString(R.string.vacio));
                focusView = precioET;
            }

            for (Producto p : productos){
                if (p.get_titulo().equals(nombreET.getText().toString())){
                    cancel = true;
                    focusView = nombreET;
                }
            }

            if (cancel){
                focusView.requestFocus();
            } else {
                titulo = nombreET.getText().toString();
                precio = precioET.getText().toString();

                Producto p = new Producto("", titulo, precio);
                crudProducts.addProduct(p);
                nombreET.setText("");
                precioET.setText("");
                focusView = nombreET;
                focusView.requestFocus();
            }
        }
    };

    ValueEventListener getProducts = new ValueEventListener() {
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
                        p.set_titulo((String)value.get(NOMBRE));
                        p.set_precio((String) value.get(PRECIO));
                        productos.add(p);
                    }
                }
            }
            sortProducts();
            adapter.notifyDataSetChanged();
            recyclerView.setAdapter(adapter);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.e(TAG, "Error occurred: " + databaseError.getMessage());
        }
    };

    private void sortProducts() {
        Collections.sort(productos, new Comparator<Producto>() {
            @Override
            public int compare(Producto o1, Producto o2) {
                return o2.get_id().compareTo(o1.get_id());
            }
        });
    }

}

package com.example.djc.kanquimaniapark.MainActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.djc.kanquimaniapark.Admin.SimpleTabsActivity;
import com.example.djc.kanquimaniapark.CheckOut.CheckOutActivity;
import com.example.djc.kanquimaniapark.CheckOut.CloseActivityEvent;
import com.example.djc.kanquimaniapark.Clases.Atraccion;
import com.example.djc.kanquimaniapark.Clases.Cliente;
import com.example.djc.kanquimaniapark.Clases.IdentificadorEntrada;
import com.example.djc.kanquimaniapark.Clases.Producto;
import com.example.djc.kanquimaniapark.Clases.SelectedProduct;
import com.example.djc.kanquimaniapark.MainActivity.ClientsList.ClientRecyclerAdapter;
import com.example.djc.kanquimaniapark.CrearClientes.CrearCliente;
import com.example.djc.kanquimaniapark.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

public class MainActivity extends AppCompatActivity {

    //<editor-fold desc="Constantes">
    private static final String APELLIDO = "Apellido";
    private static final String FECHA_CUMPLEANOS = "Fecha_Cumpleanos";
    private static final String SEXO = "Sexo";
    private static final String NUMERO = "Numero_Telefono";
    private static final String CORREO = "Correo_Electronico";
    private static final String INDICADORES = "Identificadores";
    private static final String COLORES = "Colores";
    private static final String ID = "ID";
    private static final String FECHA = "Fecha";
    private static final String ATRACCIONES_ID = "Atracciones";
    private static final String ATRACCIONES = "Atracciones";
    private static final String PRODUCTOS = "Productos";
    private static final String NOMBRE = "Nombre";
    private static final String TITULO = "Titulo";
    private static final String PRECIO = "Precio";
    private static final String TIEMPO = "Tiempo";
    private static final String CLIENTES = "Clientes";
    //</editor-fold>

    //<editor-fold desc="Listas y Adaptadores">
    private List<Cliente> clientes;
    private ClientRecyclerAdapter adapter;
    private List<String> colores;
    private List<Atraccion> atracciones;
    private List<Producto> productos;
    private ArrayAdapter<Atraccion> atr_adapter;
    private ArrayAdapter<Producto> spinner_prod_adapter;
    private ArrayAdapter<String> color_adpt;

    private List<SelectedAttraction> selectedAttractions;
    private List<SelectedProduct> selectedProducts;
    private SelectedProductsAdapter selectedProductsAdapter;
    private SelectedAttractionsAdapter selectedAttractionsAdapter;
    //</editor-fold>

    private MainFireBaseHelper mainHelper;
    private RecyclerView recyclerView;
    private View focused = null;
    private DatabaseReference mRef, colorRef, atrRef, prodRef, clientRef;

    private ProgressDialog progressDialog;

    private Spinner products_spinner;
    private EditText counterET;

    private ListView added_products_list, added_tickets_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        EventBus.getDefault().register(this);

        progressDialog = new ProgressDialog(this);

        //<editor-fold desc="Inicializando">
        clientes = new ArrayList<>();
        adapter = new ClientRecyclerAdapter(MainActivity.this, clientes);
        colores = new ArrayList<>();
        atracciones = new ArrayList<>();
        productos = new ArrayList<>();
        selectedProducts = new ArrayList<>();
        selectedAttractions = new ArrayList<>();
        atr_adapter =  new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, atracciones);
        spinner_prod_adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, productos);
        color_adpt = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, colores);
        color_adpt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        selectedProductsAdapter = new SelectedProductsAdapter(MainActivity.this, selectedProducts);
        selectedAttractionsAdapter = new SelectedAttractionsAdapter(MainActivity.this, selectedAttractions);


        //</editor-fold>

        //<editor-fold desc="Firebase References">
        atrRef = FirebaseDatabase.getInstance().getReference(ATRACCIONES);
        atrRef.addValueEventListener(getAtr);

        colorRef = FirebaseDatabase.getInstance().getReference(COLORES);
        colorRef.addChildEventListener(getColores);

        mRef = FirebaseDatabase.getInstance().getReference(INDICADORES);
        mRef.limitToLast(1).addValueEventListener(getIden);
        mRef.keepSynced(true);

        clientRef = FirebaseDatabase.getInstance().getReference(CLIENTES);
        clientRef.addValueEventListener(getClients);
        clientRef.keepSynced(true);

        prodRef = FirebaseDatabase.getInstance().getReference(PRODUCTOS);
        prodRef.addValueEventListener(getProd);
        prodRef.keepSynced(true);

        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
        //</editor-fold>

        //<editor-fold desc="Facturacion Productos">
        products_spinner = (Spinner)findViewById(R.id.facturacion_spinner);
        products_spinner.setAdapter(atr_adapter);

        counterET = (EditText)findViewById(R.id.facturacion_products_counter);
        added_products_list = (ListView)findViewById(R.id.facturacion_products_list);
        added_products_list.setOnItemLongClickListener(onProductsLongClickListener);
        //</editor-fold>

        //<editor-fold desc="Facturacion Atracciones">
        added_tickets_list = (ListView)findViewById(R.id.facturacion_attractions_list);
        added_tickets_list.setAdapter(selectedAttractionsAdapter);
        added_tickets_list.setOnItemLongClickListener(onAttractionsLongClickListener);
        //</editor-fold>

        mainHelper = new MainFireBaseHelper();
        recyclerViewController();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isFirstLaunch()){
            Dialog dialog = set_colors();
            dialog.show();
        }
    }

    @Subscribe
    public void onEvent(AddAtracctionsEvent event){
        addAttraction(new SelectedAttraction(event.getAtraccion(), event.getCliente()));
    }

    @Subscribe
    public void onEvent(CloseActivityEvent event){
        Toast.makeText(MainActivity.this, "Factura Creada", Toast.LENGTH_SHORT).show();
        selectedProducts.clear();
        selectedAttractions.clear();
        selectedProductsAdapter.notifyDataSetChanged();
        selectedAttractionsAdapter.notifyDataSetChanged();
    }


    private boolean isFirstLaunch() {
        boolean isFirstLaunch;
        SharedPreferences sharedPref = MainActivity.this.getSharedPreferences("FECHA_INDICADOR", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String currentDate = sdf.format(Calendar.getInstance().getTimeInMillis());
        String lastDate = sharedPref.getString("ULTIMA_FECHA_GUARDADA", "NO_HOY");

        if (lastDate.equals(currentDate)){
            isFirstLaunch = false;
        } else {
            editor.putString("ULTIMA_FECHA_GUARDADA", currentDate);
            editor.apply();
            isFirstLaunch = true;
        }
        return isFirstLaunch;
    }

    //<editor-fold desc="Menu">
    //Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.option_menu, menu);

        MenuItem item = menu.findItem(R.id.menu_searcher_clients);
        SearchView searchView = (SearchView)item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.crear_producto:
                logIn_alertBuilder().show();
                return true;
            case R.id.menu_add_cliente:
                Intent intent = new Intent(getApplicationContext(), CrearCliente.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private Dialog set_colors() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.inicio_jornada_dialog);
        dialog.setTitle("Inicio de Jornada");
        dialog.setCancelable(false);
        final ListView list_atr = (ListView) dialog.findViewById(R.id.setting_lista_boletas);
        list_atr.setAdapter(atr_adapter);
        list_atr.setSelected(true);

        final Button acceptCol = (Button)dialog.findViewById(R.id.setting_color_button);
        final Button dismissButton = (Button)dialog.findViewById(R.id.setting_accept_idnt_button);

        final HashMap<String, Atraccion> sel_atr= new HashMap<>();
        final List<String> sel_col= new ArrayList<>();

        final Spinner spinner = (Spinner)dialog.findViewById(R.id.setting_ticket_color_spinner);
        spinner.setAdapter(color_adpt);
        final TextView nombreTV = (TextView)dialog.findViewById(R.id.setting_ticket_tipo);
        final TextView tiempoTV = (TextView)dialog.findViewById(R.id.setting_ticket_time);

        final int[] posAtr = {0};

        list_atr.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                posAtr[0] = position;
                nombreTV.setText(atracciones.get(position).get_titulo());
                tiempoTV.setText(atracciones.get(position).get_tiempo());
                focused = view;
                acceptCol.setEnabled(true);
            }
        });

        acceptCol.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (spinner.getSelectedItem() != null || focused != null){
                    String s  = spinner.getSelectedItem().toString();
                    sel_atr.put(s, atracciones.get(posAtr[0]));
                    sel_col.add(s);
                    color_adpt.remove(s);
                    color_adpt.notifyDataSetChanged();

                    atr_adapter.remove(atracciones.get(posAtr[0]));
                    atr_adapter.notifyDataSetChanged();
                    list_atr.setAdapter(atr_adapter);

                    focused.setBackgroundColor(getColor(R.color.silver));
                    focused.setEnabled(false);
                    focused.setClickable(false);

                    if (atracciones.size() == 0){
                        dismissButton.setEnabled(true);
                        acceptCol.setEnabled(false);
                    }
                }
            }
        });

        dismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IdentificadorEntrada ie = new IdentificadorEntrada();
                ie.set_id("");
                ie.set_colores(sel_col);

                HashMap<String, String> ids = new HashMap<String, String>();
                for (HashMap.Entry<String, Atraccion> o : sel_atr.entrySet()){
                    ids.put(o.getKey(), o.getValue().get_id());
                }

                ie.set_atraccionesID(ids);

                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                Calendar calendar = Calendar.getInstance();

                ie.set_fecha(dateFormat.format(calendar.getTimeInMillis()));

                mainHelper.addIdentificator(ie);
                dialog.dismiss();
            }
        });

        return dialog;
    }
    //</editor-fold>

    //<editor-fold desc="On Clicks">

    private AlertDialog logIn_alertBuilder(){
        final View[] focusView = {null};
        final String[] usuario = new String[1];
        final String[] contrasena = new String[1];
        final boolean[] cancel = {false};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final Context context = builder.getContext();
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.login_dialog, null, false);


        builder.setView(view)
                .setTitle(getString(R.string.login_title))
                .setCancelable(false)
                .setMessage(getString(R.string.login_info))
                .setNegativeButton(getString(R.string.cancelar), null);


        builder.setPositiveButton(getString(R.string.login_title), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final EditText usuarioET = (EditText)view.findViewById(R.id.login_name);
                final EditText passwordET = (EditText)view.findViewById(R.id.login_password);

                if (Objects.equals(usuarioET.getText().toString(), "")){
                    usuarioET.setError(getString(R.string.vacio));
                    focusView[0] = usuarioET;
                    cancel[0] = true;
                } else if (Objects.equals(passwordET.getText().toString(), "")){
                    passwordET.setError(getString(R.string.vacio));
                    focusView[0] = passwordET;
                    cancel[0] = true;
                }

                if (cancel[0]){
                    focusView[0].requestFocus();
                } else {
                    usuario[0] = usuarioET.getText().toString();
                    contrasena[0] = passwordET.getText().toString();
                    boolean success = mainHelper.signIn(usuario[0], contrasena[0]);

                    if (success){
                        Intent intent = new Intent(getApplicationContext(), SimpleTabsActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(MainActivity.this, "No se ha podido iniciar sesion", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        return builder.create();
    }

    //<editor-fold desc="onItemLongClickListener">
    private AdapterView.OnItemLongClickListener onProductsLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
            alert.setTitle(getString(R.string.atencion));
            alert.setMessage(getString(R.string.seguro_borrar_item));
            alert.setPositiveButton(getString(R.string.si), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    selectedProducts.remove(position);
                    selectedProductsAdapter.notifyDataSetChanged();
                    dialog.dismiss();
                }
            });
            alert.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            alert.show();
            return true;
        }
    };

    private AdapterView.OnItemLongClickListener onAttractionsLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
            alert.setTitle(getString(R.string.atencion));
            alert.setMessage(getString(R.string.seguro_borrar_item));
            alert.setPositiveButton(getString(R.string.si), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    selectedAttractions.remove(position);
                    selectedAttractionsAdapter.notifyDataSetChanged();
                    dialog.dismiss();
                }
            });
            alert.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            alert.show();
            return true;
        }
    };
    //</editor-fold>

    //</editor-fold>

    //<editor-fold desc="Firebase Events">
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
            atr_adapter.notifyDataSetChanged();
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    private ValueEventListener getProd = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            GenericTypeIndicator<Map<String, Map<String, String>>> genin = new GenericTypeIndicator<Map<String, Map<String, String>>>() {};
            Map<String, Map<String,String>> map = dataSnapshot.getValue(genin);
            progressDialog.show();
            productos.clear();

            if (map != null){
                for (Map.Entry<String, Map<String, String>> entry : map.entrySet()){
                    if (entry != null){
                        HashMap value = (HashMap) entry.getValue();
                        Producto p = new Atraccion();
                        p.set_id((String)value.get(ID));
                        p.set_titulo((String)value.get(TITULO));
                        p.set_precio((String) value.get(PRECIO));
                        productos.add(p);
                    }
                }
            }
            spinner_prod_adapter.notifyDataSetChanged();
            products_spinner.setAdapter(spinner_prod_adapter);
            progressDialog.dismiss();
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    private ChildEventListener getColores = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            colores.add((String) dataSnapshot.getValue());
            color_adpt.notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    private ValueEventListener getIden = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            HashMap root = (HashMap)dataSnapshot.getValue();

            IdentificadorEntrada i = new IdentificadorEntrada();

            if (root != null){
                Collection<Object> objects = root.values();
                for (Object o : objects){
                    if (o instanceof Map){
                        HashMap<String, Object> map = (HashMap<String, Object>) o;
                        i.set_id((String) map.get(ID));
                        i.set_fecha((String) map.get(FECHA));

                        HashMap<String, String> map_atracciones = (HashMap<String, String>) map.get(ATRACCIONES_ID);
                        if (map_atracciones != null){
                            i.set_atraccionesID(map_atracciones);
                        }

                        HashMap<String, String> lista_colores = (HashMap<String, String>) map.get(COLORES);
                        if (lista_colores != null){
                            i.set_colores(new ArrayList<>(lista_colores.values()));
                        }
                    }
                }
            } else {
                //dialog.show();
            }

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.v("Error", databaseError.getMessage());

        }
    };

    private ValueEventListener getClients = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            clientes.clear();
            HashMap rootMap = (HashMap) dataSnapshot.getValue();

            if (rootMap != null){
                Collection<Object> objects = rootMap.values();
                for (Object o : objects){
                    if (o instanceof Map){
                        HashMap<String, Object> map = (HashMap<String, Object>) o;
                        final Cliente c = new Cliente();
                        c.set_id( (String) map.get(ID));
                        c.set_nombre((String) map.get(NOMBRE));
                        c.set_apellido((String) map.get(APELLIDO));
                        c.set_fechaCumpleAnos((String) map.get(FECHA_CUMPLEANOS));
                        c.set_sexo((String) map.get(SEXO));
                        c.set_numero((String) map.get(NUMERO));
                        c.set_correo((String) map.get(CORREO));
                        clientes.add(c);
                    }
                }
            }
            sortClientes();
            adapter.notifyDataSetChanged();
            recyclerView.setAdapter(adapter);
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.e(TAG, "Error occurred: " + databaseError.getMessage());
        }
    };
    //</editor-fold>

    public void recyclerViewController(){
        //Recycler view
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void sortClientes() {
        Collections.sort(clientes, new Comparator<Cliente>() {
            @Override
            public int compare(Cliente o1, Cliente o2) {
                return o2.get_id().compareTo(o1.get_id());
            }
        });
    }

    public void increaseCounter(View view) {
       if (!counterET.getText().toString().equals("")){
           int actual = Integer.parseInt(counterET.getText().toString());
           counterET.setText(String.valueOf(actual + 1));
       }
    }

    public void decreaseCounter(View view) {
        if (!counterET.getText().toString().equals("")){
            int actual = Integer.parseInt(counterET.getText().toString());
           if (actual > 0){
               counterET.setText(String.valueOf(actual - 1));
           }
        }
    }

    public void addProduct(View view) {
        boolean there = false;
        int n = products_spinner.getSelectedItemPosition();
        int cant = Integer.parseInt(counterET.getText().toString());
        if  (cant > 0){
            SelectedProduct newSelection = new SelectedProduct(cant, productos.get(n));
            for (SelectedProduct s : selectedProducts){
                if (s.get_producto().equals(newSelection.get_producto())){
                    int pos = selectedProducts.indexOf(s);
                    selectedProducts.get(pos).set_cantidad(cant);
                    selectedProductsAdapter.notifyDataSetChanged();
                    there = true;
                    break;
                }
            }

            if (!there){
                selectedProducts.add(newSelection);
                selectedProductsAdapter.notifyDataSetChanged();
                added_products_list.setAdapter(selectedProductsAdapter);
            }
        }
        counterET.setText("0");
    }

    private void addAttraction(SelectedAttraction selectedAttraction){
        boolean there = false;

        for (SelectedAttraction s : selectedAttractions){
            if (s.getClient().equals(selectedAttraction.getClient())){
                if (s.getAtraccion().get_titulo().equals(selectedAttraction.getAtraccion().get_titulo())){
                    Toast.makeText(this, "Esta atracción ya se ha registrado", Toast.LENGTH_SHORT).show();
                    there = true;
                    break;
                }
            }
        }

        if (!there){
            selectedAttractions.add(selectedAttraction);
            selectedAttractionsAdapter.notifyDataSetChanged();
        }
    }

    public void doCheckOut(View view) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle(getString(R.string.atencion));
        alertDialog.setMessage(getString(R.string.proceder_checkout));
        alertDialog.setPositiveButton(getString(R.string.aceptar), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (selectedAttractions.size() != 0 || selectedProducts.size() != 0){
                    Bundle selectedAtrBundle = new Bundle();
                    selectedAtrBundle.putSerializable("SelectedAtr", (Serializable) selectedAttractions);

                    Bundle selectedProdBundle = new Bundle();
                    selectedProdBundle.putSerializable("SelectedProd", (Serializable) selectedProducts);

                    Intent intent = new Intent(MainActivity.this, CheckOutActivity.class);
                    intent.putExtras(selectedAtrBundle);
                    intent.putExtras(selectedProdBundle);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, getString(R.string.elegir_algo), Toast.LENGTH_SHORT).show();
                }
            }
        });

        alertDialog.setNegativeButton(getString(R.string.cancelar), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.create().show();
    }
}

package com.example.djc.kanquimaniapark.MainActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.icu.text.SymbolTable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
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
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.djc.kanquimaniapark.Admin.SimpleTabsActivity;
import com.example.djc.kanquimaniapark.Clases.Atraccion;
import com.example.djc.kanquimaniapark.Clases.IdentificadorEntrada;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private String INDICADORES = "Indicadores";
    private String COLORES = "Colores";
    private String ID = "ID";
    private String COLOR = "Color";
    private String FECHA = "Fecha";
    private String ATRACCIONES_ID = "Atracciones";
    private String ATRACCIONES = "Atracciones";
    private String NOMBRE = "Nombre";
    private String PRECIO = "Precio";
    private String TIEMPO = "Tiempo";

    private List<String> colores;
    private List<Atraccion> atracciones, selected_atr;
    private IdentificadorEntrada identificador;
    private ListView list_atr;
    private ArrayAdapter<Atraccion> atr_adapter;
    private ArrayAdapter<String> color_adpt;

    private FloatingActionButton plusFAB;
    private FloatingActionButton clientFAB;

    private LogInFireBaseHelper logInHelper;

    private Boolean isFabOpen = false;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;
    private RecyclerView recyclerView;

    private View focused = null;

    private DatabaseReference mRef, cRef, atrRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        colores = new ArrayList<>();
        atracciones = new ArrayList<>();
        selected_atr = new ArrayList<>();
        identificador = new IdentificadorEntrada();
        atr_adapter =  new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, atracciones);
        color_adpt = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, colores);


        atrRef = FirebaseDatabase.getInstance().getReference(ATRACCIONES);
        atrRef.addValueEventListener(getAtr);


        cRef = FirebaseDatabase.getInstance().getReference(COLORES);
        cRef.addChildEventListener(getColores);

        mRef = FirebaseDatabase.getInstance().getReference(INDICADORES);
        mRef.limitToLast(1).addListenerForSingleValueEvent(getIden);
        mRef.keepSynced(true);

        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }

        logInHelper = new LogInFireBaseHelper();
        plusFAB = (FloatingActionButton)findViewById(R.id.plusFAB);
        clientFAB = (FloatingActionButton)findViewById(R.id.fabAddClient);

        //Manejo de los Floating Buttons
        fabAnimator();
        //Manejo de Recycler View
        recyclerViewController();
    }

    //Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()){
            case R.id.crear_producto:
                logIn_alertBuilder().show();
                return true;
            case R.id.menu_identificador:
                set_colors().show();
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
        list_atr = (ListView)dialog.findViewById(R.id.setting_lista_boletas);
        list_atr.setAdapter(atr_adapter);
        list_atr.setSelection(0);
        list_atr.setSelected(true);
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
            }
        });


        Button acceptCol = (Button)dialog.findViewById(R.id.setting_color_button);
        final Button dismissButton = (Button)dialog.findViewById(R.id.setting_accept_idnt_button);


        final HashMap<String, Atraccion> sel_atr= new HashMap<>();
        final List<String> sel_col= new ArrayList<>();
        acceptCol.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                String s  = spinner.getSelectedItem().toString();
                sel_atr.put(s, atracciones.get(posAtr[0]));
                sel_col.add(s);
                color_adpt.remove(s);
                color_adpt.notifyDataSetChanged();
                focused.setBackgroundColor(getColor(R.color.silver));
                focused.setEnabled(false);
                focused.setClickable(false);

                if (sel_atr.size() == atracciones.size()){
                    dismissButton.setEnabled(true);
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

                logInHelper.addIdentificator(ie);
                dialog.dismiss();
            }
        });

        return dialog;
    }

    //--------------------------------------FUNCIONES----------------------------------------
    //FUNCION DONDE SE ANIMAN Y SE MANEJA EL CLICK DE LOS FLOATING BUTTONS.
    public void fabAnimator(){
        //Animaciones de los FABs
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_backward);
    }

    //FUNCION SE MANEJA EL RECYCLERVIEW
    public void recyclerViewController(){
        //Recycler view
        ArrayList<String> data = new ArrayList<>();

        for(int i = 0; i < 30; i++) {
            data.add("Panita " + i);
        }

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        ClientRecyclerAdapter adapter = new ClientRecyclerAdapter(this, data);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    //FUNCION DONDE SE ANIMAN LOS BOTONES AL DAR CLICK
    public void animateFAB(){

        if(isFabOpen){
            plusFAB.startAnimation(rotate_backward);
            clientFAB.startAnimation(fab_close);
            clientFAB.setClickable(false);
            isFabOpen = false;

        } else {
            plusFAB.startAnimation(rotate_forward);
            clientFAB.startAnimation(fab_open);
            clientFAB.setClickable(true);
            isFabOpen = true;
        }
    }

    //<editor-fold desc="On Clicks">
    //RENGLON DE OnClicks
    public void plusFabOnClick(View v){
        animateFAB();
    }

    public void clientFabOnClick(View v){
        Intent intent = new Intent(getApplicationContext(), CrearCliente.class);
        startActivity(intent);
    }

    public void mailFabOnClick(View v){
        //Toast.makeText(this, String.valueOf(helper.count), Toast.LENGTH_SHORT).show();
    }

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
                    boolean success = logInHelper.signIn(usuario[0], contrasena[0]);

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
    //</editor-fold>


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

    private ChildEventListener getColores = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            colores.add((String) dataSnapshot.getValue());
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
            Map<String, Object> root = (HashMap<String, Object>)dataSnapshot.getValue();
            if (root != null){
                identificador.set_id((String) root.get(ID));
                identificador.set_fecha((String) root.get(FECHA));

                HashMap<String, String> map_atracciones = (HashMap<String, String>) root.get(ATRACCIONES_ID);
                if (map_atracciones != null){
                    identificador.set_atraccionesID(map_atracciones);
                }

                HashMap<String, String> lista_colores = (HashMap<String, String>) root.get(COLORES);
                if (lista_colores != null){
                    identificador.set_colores(new ArrayList<String>(lista_colores.values()));
                }
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.v("Error", databaseError.getMessage());

        }
    };
}

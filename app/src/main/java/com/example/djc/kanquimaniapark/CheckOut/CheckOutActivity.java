package com.example.djc.kanquimaniapark.CheckOut;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.RT_Printer.BluetoothPrinter.BLUETOOTH.BluetoothPrintDriver;
import com.example.djc.kanquimaniapark.Clases.Atraccion;
import com.example.djc.kanquimaniapark.Clases.Cliente;
import com.example.djc.kanquimaniapark.Clases.Especial;
import com.example.djc.kanquimaniapark.Clases.Factura;
import com.example.djc.kanquimaniapark.Clases.Producto;
import com.example.djc.kanquimaniapark.Clases.SelectedProduct;
import com.example.djc.kanquimaniapark.Clases.SelectedAttraction;
import com.example.djc.kanquimaniapark.MainActivity.MainActivity;
import com.example.djc.kanquimaniapark.MainActivity.SelectedAttractionsAdapter;
import com.example.djc.kanquimaniapark.MainActivity.SelectedProductsAdapter;
import com.example.djc.kanquimaniapark.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

public class CheckOutActivity extends AppCompatActivity {

    //<editor-fold desc="Constantes">
    private static final String ESPECIALES = "Especiales";
    private static final String ATRACCIONES = "Atracciones";
    private static final String ID = "ID";
    private static final String NOMBRE = "Nombre";
    private static final String PORCIENTO = "Porciento";
    private static final String FECHA_INICIO = "Fecha_Inicio";
    private static final String FECHA_FIN = "Fecha_Fin";
    private static final String PRODUCTOS = "Productos";
    private static final String DATEFORMAT = "dd/MM/yyyy";
    private static final String TITULO = "Titulo";
    private static final String PRECIO = "Precio";
    private static final String TIEMPO = "Tiempo";
    //</editor-fold>

    //<editor-fold desc="Listas">
    private List<SelectedProduct> gottenProducts;
    private List<SelectedAttraction> gottenAttractions;
    private List<Especial> especiales;
    private List<String> foundEspeciales;
    private List<Producto> productos;
    private List<Atraccion> atracciones;
    //</editor-fold>

    //<editor-fold desc="Adapters">
    private SelectedProductsAdapter productsAdapter;
    private SelectedAttractionsAdapter attractionsAdapter;
    //</editor-fold>

    //<editor-fold desc="Views">
    private TextView productsSubTotalTV;
    private TextView attractionsSubTotalTV;
    private TextView totalTV;
    private TextView especialAplProd, especialAplAtr;

    private ListView products_listView, attractions_listView;
    private EditText receivedMoneyET;
    private TextView devueltaTV;
    private View focusView = null;
    private FloatingActionButton fab;
    //</editor-fold>

    //<editor-fold desc="Otros">
    private ProgressDialog progressDialog;
    private DatabaseReference espRef, atrRef, prodRef;
    //</editor-fold>

    //<editor-fold desc="Variables">
    private float total_final = 0f;
    private float products_sobtotal = 0f;
    private float attractions_subtotal = 0f;
    private float total_descontado = 0f;
    //</editor-fold>

    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothPrintDriver mChatService = null;

    // Intent request codes
    private static final int REQUEST_ENABLE_BT = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        EventBus.getDefault().register(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Guardando Cliente");
        progressDialog.setMessage("Guardando Cliente");
        progressDialog.setCancelable(false);

        //<editor-fold desc="Firebase database references">
        prodRef = FirebaseDatabase.getInstance().getReference(PRODUCTOS);
        prodRef.addValueEventListener(getProd);
        prodRef.keepSynced(true);
        atrRef = FirebaseDatabase.getInstance().getReference(ATRACCIONES);
        atrRef.addValueEventListener(getAtr);
        atrRef.keepSynced(true);
        espRef = FirebaseDatabase.getInstance().getReference(ESPECIALES);
        espRef.addValueEventListener(getEsp);
        espRef.keepSynced(true);
        //</editor-fold>

        //<editor-fold desc="Setting gotten data">
        gottenAttractions = (List<SelectedAttraction>) getIntent().getExtras().get("SelectedAtr");
        gottenProducts = (List<SelectedProduct>) getIntent().getExtras().get("SelectedProd");

        productsAdapter = new SelectedProductsAdapter(this, gottenProducts);
        attractionsAdapter = new SelectedAttractionsAdapter(this, gottenAttractions);

        products_listView = (ListView)findViewById(R.id.check_out_products_list);
        attractions_listView = (ListView)findViewById(R.id.check_out_attractions_list);

        products_listView.setAdapter(productsAdapter);
        attractions_listView.setAdapter(attractionsAdapter);
        //</editor-fold>

        //<editor-fold desc="Calcular precios">
        for (SelectedProduct sp : gottenProducts){
            products_sobtotal += Double.valueOf(sp.get_producto().get_precio()) * sp.get_cantidad();
        }

        productsSubTotalTV = (TextView)findViewById(R.id.check_out_subtotal_productos);
        productsSubTotalTV.setText(formatPrice(products_sobtotal));

        for (SelectedAttraction sa : gottenAttractions){
            attractions_subtotal += Double.valueOf(sa.getAtraccion().get_precio());
        }

        attractionsSubTotalTV = (TextView)findViewById(R.id.check_out_subtotal_attractions);
        attractionsSubTotalTV.setText(formatPrice(attractions_subtotal));

        total_final = products_sobtotal + attractions_subtotal;
        totalTV = (TextView)findViewById(R.id.check_out_total);
        totalTV.setText(formatPrice(total_final));
        //</editor-fold>

        especiales = new ArrayList<>();
        productos = new ArrayList<>();
        atracciones = new ArrayList<>();
        foundEspeciales = new ArrayList<>();

        receivedMoneyET =(EditText) findViewById(R.id.check_out_received_money);
        devueltaTV = (TextView)findViewById(R.id.check_out_devuelta);

        especialAplProd = (TextView)findViewById(R.id.check_out_especial_productos);
        especialAplAtr = (TextView)findViewById(R.id.check_out_especial_attracciones);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buildConfirmDialog();
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        try {
            if (mBluetoothAdapter.isEnabled()) {
                mChatService = new BluetoothPrintDriver(this, new Handler());
                BluetoothDevice device = mBluetoothAdapter.getRemoteDevice("00:0E:0E:0B:6B:F1");
                mChatService.connect(device);
            }
        } finally {

        }
    }
    @Override
    public void onResume(){
        super.onResume();
        if (mBluetoothAdapter.isEnabled()) {
            if(BluetoothPrintDriver.IsNoConnection()){
                mChatService = new BluetoothPrintDriver(this, mHandler);
                BluetoothDevice device = mBluetoothAdapter.getRemoteDevice("00:0E:0E:0B:6B:F1");
                mChatService.connect(device);
            }
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if (mChatService != null){
            mChatService.stop();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Subscribe
    public void onEvent(UpdateTotalsEvent event){
        boolean isProductsApplied = false, isAttractionsApplied = false;

        for (Especial e : event.getGottenEspecials()){
            if (e.get_productos() != null) {
                for (String s : e.get_productos()) {
                    for (SelectedProduct sp : gottenProducts) {
                        if (sp.get_producto().get_id().equals(s)) {
                            SimpleDateFormat sdf = new SimpleDateFormat(DATEFORMAT, Locale.getDefault());
                            try {
                                Date fechaFin = sdf.parse(e.get_fechaFin());
                                Date fechaHoy = sdf.parse(sdf.format(Calendar.getInstance().getTimeInMillis()));
                                if (fechaHoy.before(fechaFin) || fechaHoy.equals(fechaFin)) {
                                    if (!isProductsApplied) {
                                        foundEspeciales.add(e.get_id());
                                        float porciento = 0f;
                                        porciento += Double.valueOf(e.get_porciento());
                                        porciento /= 100f;
                                        float descuento = (products_sobtotal * porciento);
                                        total_descontado += descuento;
                                        products_sobtotal = products_sobtotal - descuento;
                                        especialAplProd.setText(e.get_nombre());
                                        productsSubTotalTV.setText(String.valueOf(products_sobtotal));
                                        isProductsApplied = true;
                                    }
                                }
                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }
                        }
                        if (isProductsApplied){
                            break;
                        }
                    }
                }
            }

            if (e.get_atracciones() != null) {
                for (String s : e.get_atracciones()) {
                    for (SelectedAttraction sa : gottenAttractions){
                        if (sa.getAtraccion().get_id().equals(s)){
                            SimpleDateFormat sdf = new SimpleDateFormat(DATEFORMAT, Locale.getDefault());
                            try {
                                Date fechaFin = sdf.parse(e.get_fechaFin());
                                Date fechaHoy = sdf.parse(sdf.format(Calendar.getInstance().getTimeInMillis()));
                                if (fechaHoy.before(fechaFin) || fechaHoy.equals(fechaFin)) {
                                    if (!isAttractionsApplied) {
                                        float porciento = 0f;
                                        porciento += Double.valueOf(e.get_porciento());
                                        porciento /= 100f;
                                        float descuento = (attractions_subtotal * porciento);
                                        total_descontado += descuento;
                                        attractions_subtotal = attractions_subtotal - descuento;
                                        especialAplAtr.setText(e.get_nombre());
                                        attractionsSubTotalTV.setText(String.valueOf(attractions_subtotal));
                                        isAttractionsApplied = true;
                                    }
                                }
                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }
                        }
                        if (isAttractionsApplied){
                            break;
                        }
                    }
                }
            }
        }
        total_final = products_sobtotal + attractions_subtotal;
        totalTV.setText(String.valueOf(total_final));
    }

    private String formatPrice(float precio){
        return "RD$ " + String.valueOf(precio);
    }

    public void setMoneyBack(View view){
        boolean cancel = false;
        float devuelta = 0f;
        try {
            devuelta += Double.valueOf(receivedMoneyET.getText().toString());
            devuelta -= total_final;
            if (devuelta < 0){
                receivedMoneyET.setError(getString(R.string.dinero_no_suficiente));
                focusView = receivedMoneyET;
                cancel = true;
            } else {
                devueltaTV.setText(String.valueOf(devuelta));
                fab.setVisibility(View.VISIBLE);
            }

            if (cancel){
                focusView.requestFocus();
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void buildConfirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(CheckOutActivity.this);
        builder.setTitle(getString(R.string.atencion));
        builder.setMessage(getString(R.string.desea_realizar_pedido));
        builder.setNegativeButton(getString(R.string.cancelar), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setPositiveButton(getString(R.string.aceptar), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Factura factura = new Factura();
                factura.set_id("");
                List<String> productsIds = new ArrayList<String>();
                for (SelectedProduct sp : gottenProducts){
                    productsIds.add(sp.get_producto().get_id());
                }
                factura.set_productosSeleccionados(productsIds);
                List<String> attractionsIds = new ArrayList<String>();
                for (SelectedAttraction sa : gottenAttractions){
                    attractionsIds.add(sa.getAtraccion().get_id());
                }
                factura.set_atraccionesSeleccionadas(attractionsIds);
                factura.set_especialesID(foundEspeciales);

                SimpleDateFormat sdf = new SimpleDateFormat(DATEFORMAT, Locale.getDefault());
                String date = sdf.format(Calendar.getInstance().getTimeInMillis());
                factura.set_fechaEmision(date);
                factura.set_totalDescontado(String.valueOf(total_descontado));
                factura.set_totalFinal(String.valueOf(total_final));

                CheckOutFireBaseHelper checkOutFirebaseHelper = new CheckOutFireBaseHelper();
                checkOutFirebaseHelper.addFactura(factura);
                checkOutFirebaseHelper.createAttractionsManager(gottenAttractions);


                if (BluetoothAdapter.getDefaultAdapter().isEnabled()){
                    Calendar c = Calendar.getInstance();
                    int hour = c.get(Calendar.HOUR_OF_DAY);
                    int min = c.get(Calendar.MINUTE);
                    int seg = c.get(Calendar.SECOND) + 5;

                    String end = String.valueOf(hour + ":" + min + ":" + seg);
                    SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:s", Locale.getDefault());

                    progressDialog.show();
                    if (BluetoothPrintDriver.IsNoConnection()) {
                        while (true){
                            if (sdf2.format(Calendar.getInstance().getTimeInMillis()).equals(end)){
                                break;
                            }
                        }
                    }
                    progressDialog.dismiss();
                    printFactura(factura);
                }

                Log.e("-----------factura", String.valueOf(factura.get_atraccionesSeleccionadas().size()));
                dialog.dismiss();
                EventBus.getDefault().post(new CloseActivityEvent());
                Toast.makeText(CheckOutActivity.this, "Factura Creada", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

         builder.create().show();
    }

    private void printFactura(Factura factura) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        String date = sdf.format(Calendar.getInstance().getTimeInMillis());

        String tmpString1 = "Factura KanquiMania Park";
        String tmpString2 = "Fecha: " + date;
        String tmpString4 = "Total: " + factura.get_totalFinal();
        String tmpString3 = "Total Descontado: " + factura.get_totalDescontado();
        String linea = "--------------------------------";

        BluetoothPrintDriver.Begin();
        BluetoothPrintDriver.SetBold((byte)0x01);
        BluetoothPrintDriver.SetAlignMode((byte)1);//
        BluetoothPrintDriver.SetLineSpacing((byte)50);
        BluetoothPrintDriver.SetFontEnlarge((byte)0x09);//ߣ
        BluetoothPrintDriver.BT_Write(tmpString1);
        BluetoothPrintDriver.SetBold((byte)0);
        BluetoothPrintDriver.LF();
        BluetoothPrintDriver.SetAlignMode((byte)0);//
        BluetoothPrintDriver.SetFontEnlarge((byte)0x00);//ĬϿȡĬϸ߶
        BluetoothPrintDriver.BT_Write(tmpString2);
        BluetoothPrintDriver.LF();
        if (factura.get_productos().size() > 0) {
            BluetoothPrintDriver.BT_Write("----------Productos-------------");
            BluetoothPrintDriver.LF();
            for(String P : factura.get_productos())
            {
               for (SelectedProduct sp : gottenProducts){
                   if (P.equals(sp.get_producto().get_id())){
                       BluetoothPrintDriver.BT_Write(String.valueOf(sp.get_cantidad()) +
                               " " + sp.get_producto().get_titulo() + "  RD$" + sp.get_producto().get_precio());
                       BluetoothPrintDriver.LF();
                   }
               }
            }
        }
        if (factura.get_atraccionesSeleccionadas().size() > 0) {
            BluetoothPrintDriver.BT_Write("----------Cintillos-------------");
            BluetoothPrintDriver.LF();
            for(String P : factura.get_atraccionesSeleccionadas()) {
                for (SelectedAttraction a : gottenAttractions){
                    if (P.equals(a.getAtraccion().get_id())){
                        BluetoothPrintDriver.BT_Write(String.valueOf(a.getAtraccion().get_tiempo())
                                + " mins " + a.getAtraccion().get_titulo() + "  RD$" + a.getAtraccion().get_precio());
                        BluetoothPrintDriver.LF();
                        gottenAttractions.remove(a);
                        break;
                    }
                }
            }
        }
        BluetoothPrintDriver.BT_Write(linea);
        BluetoothPrintDriver.LF();
        BluetoothPrintDriver.SetBold((byte)0x01);
        BluetoothPrintDriver.SetFontEnlarge((byte)0x00);
        BluetoothPrintDriver.BT_Write(tmpString3);
        BluetoothPrintDriver.LF();
        BluetoothPrintDriver.SetFontEnlarge((byte)0x00);
        BluetoothPrintDriver.BT_Write(tmpString4);
        BluetoothPrintDriver.SetBold((byte)0);
        BluetoothPrintDriver.LF();
        BluetoothPrintDriver.BT_Write(" ");
        BluetoothPrintDriver.LF();
        BluetoothPrintDriver.BT_Write(" ");
        BluetoothPrintDriver.LF();
        BluetoothPrintDriver.BT_Write(" ");
    }

    //<editor-fold desc="Value event listeners">
    private ValueEventListener getEsp = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            //progressDialog.show();
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
            //progressDialog.dismiss();
            EventBus.getDefault().post(new UpdateTotalsEvent(especiales));
            //adapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.v("Error", databaseError.getMessage());
        }
    };

    private ValueEventListener getProd = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            GenericTypeIndicator<Map<String, Map<String, String>>> genin = new GenericTypeIndicator<Map<String, Map<String, String>>>() {};
            Map<String, Map<String,String>> map = dataSnapshot.getValue(genin);
            //progressDialog.show();
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
            //progressDialog.dismiss();
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.e("Error", databaseError.getMessage());
        }
    };

    private ValueEventListener getAtr = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            GenericTypeIndicator<Map<String, Map<String, String>>> genin = new GenericTypeIndicator<Map<String, Map<String, String>>>() {};
            Map<String, Map<String,String>> map = dataSnapshot.getValue(genin);
            progressDialog.show();
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

            progressDialog.dismiss();
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.v("Error", databaseError.getMessage());
        }
    };
    //</editor-fold>

    private final Handler mHandler = new Handler() {};

}

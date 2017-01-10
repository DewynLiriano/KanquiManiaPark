package com.example.djc.kanquimaniapark.CrearClientes;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.ParseException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.djc.kanquimaniapark.Eventos.CloseActivityEvent;
import com.example.djc.kanquimaniapark.Clases.Cliente;
import com.example.djc.kanquimaniapark.MainActivity.ConnectivityEvent;
import com.example.djc.kanquimaniapark.MainActivity.MainActivity;
import com.example.djc.kanquimaniapark.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Locale;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class CrearCliente extends AppCompatActivity {

    private static final String TAG = "Mensaje";
    private EditText nombreET, apellidoET, correoET, cumpleET, numeroET;

    private ImageView fotoIV;
    private Uri uriFoto = null;
    private ClientFirebaseHelper clientFirebaseHelper;

    private String DATEFORMAT = "dd/MM/yyyy";
    private String sexo = "";
    private View focusView = null;
    private ProgressDialog progressDialog;

    private AlertDialog dialog;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cliente);
        Toolbar toolbar = (Toolbar) findViewById(R.id.client_toolbar);
        setSupportActionBar(toolbar);
        EventBus.getDefault().register(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        EventBus.getDefault().post(new CloseActivityEvent());

        dialog = changeStatus();
        handler = new Handler();
        Timer timer = new Timer();
        timer.schedule(timerTask, 1000, 5000);

        progressDialog = new ProgressDialog(this);
        //progressDialog.setTitle("Guardando Cliente");
        progressDialog.setMessage("Guardando Cliente");
        progressDialog.setCancelable(true);

        clientFirebaseHelper = new ClientFirebaseHelper();

        fotoIV = (ImageView) findViewById(R.id.foto);
        nombreET = (EditText) findViewById(R.id.nombreET);
        apellidoET = (EditText) findViewById(R.id.apellidoET);
        correoET = (EditText) findViewById(R.id.correoET);
        cumpleET = (EditText) findViewById(R.id.cumpleET);
        numeroET = (EditText)findViewById(R.id.numeroET);
    }

    @Subscribe
    public void onEvent(SuccessEvent event){
        Log.d(TAG, "Event triggered");
        if (event.isOK()){
            progressDialog.dismiss();
            Toast.makeText(this, getString(R.string.clienteAgregado), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Subscribe
    public void onEvent(ConnectivityEvent event){
        if (!event.connected){
            dialog.show();
        } else {
            dialog.dismiss();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void getBirthday(View v) {
        DialogFragment fragment = new ClientDatePickerFragment();
        fragment.show(getFragmentManager(), "Date Picker");
    }

    //MANEJO DEL CLICK DEL IMAGEVIEW DE LA FOTO.
    public void fotoOnClick(View v) {
        //INTENT PARA LLAMAR LA CAMARAPA
        Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //LANZA UNA ACTIVIDAD ESPERANDO UN RESULTADO (EN ESTE CASO UN BITMAP).
        startActivityForResult(intentCamera, 0);
    }

    //FUNCION QUE RECIBE LA FOTO DESDE LA CAMARA Y LA MUESTRA EN LA ACTIVIDAD.
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            uriFoto = data.getData();
            fotoIV.setImageURI(uriFoto);
        }
    }

    public void registrarCliente(View v) {
        boolean cancel = false;

        //CONTROL DE ENTRADA DE DATOS
        /*if (!DateValidator.isDateValid(cumpleET.getText().toString(), DATEFORMAT)) {
            //NOTIFICACION ERROR
            cumpleET.setError(getString(R.string.fechaInvalida));
            focusView = cumpleET;
            cancel = true;
        } else*/ if(Objects.equals(nombreET.getText().toString(), "")){
            nombreET.setError(getString(R.string.vacio) + getString(R.string.nombre));
            focusView = nombreET;
            cancel = true;
        } else if (Objects.equals(apellidoET.getText().toString(), "")) {
            apellidoET.setError(getString(R.string.vacio) + getString(R.string.apellido));
            focusView = apellidoET;
            cancel = true;
        } else if (Objects.equals(sexo, "")) {
            sex_alertBuilder().show();
            cancel = true;
        } else if (uriFoto == null){
            photo_alertBuilder().show();
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            progressDialog.show();
            String nombre = nombreET.getText().toString();
            String apellido = apellidoET.getText().toString();
            String correo = correoET.getText().toString();
            String cumple = cumpleET.getText().toString();
            String numero = numeroET.getText().toString();

            Cliente cliente = new Cliente(nextID() , nombre, apellido,
                    sexo, correo, cumple, numero);
            clientFirebaseHelper.addClient(cliente, uriFoto);
        }
    }

    public void getSexo(View v) {
        boolean checked = ((RadioButton) v).isChecked();
        // Check which radio button was clicked
        switch (v.getId()) {
            case R.id.radioVaron:
                if (checked)
                    sexo = "M";
                break;
            case R.id.radioHembra:
                if (checked)
                    sexo = "F";
                break;
        }
    }

    public AlertDialog.Builder sex_alertBuilder(){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(CrearCliente.this)
                .setTitle(getString(R.string.error))
                .setMessage(getString(R.string.sexo_invalido));
        alertBuilder.setNeutralButton(getString(R.string.ok), null);
        return alertBuilder;
    }

    public AlertDialog.Builder photo_alertBuilder(){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.error))
                .setMessage(getString(R.string.foto_invalido));
        alertBuilder.setNeutralButton(getString(R.string.ok), null);
        return alertBuilder;
    }

    private String formatID(int n){
        String s = String.format(Locale.getDefault(), "%04d", n+1);
        return "KPC" + String.valueOf(s);
    }

    private String nextID(){
        String last = clientFirebaseHelper.last_id;
        int number = 0;
        try {
            number = Integer.parseInt(last.replaceAll("[^0-9]", ""));
        } catch (ParseException e){
            Log.e("Parsing ID number", e.getMessage());
        }
        return formatID(number);
    }

    private TimerTask timerTask = new TimerTask() {
        public void run() {
            //use a handler to run a toast that shows the current timestamp
            handler.post(new Runnable() {
                public void run() {
                    Log.w("---------Task", "Corriendo");
                    if (isNetworkAvailable()){
                        EventBus.getDefault().post(new ConnectivityEvent(true));
                    } else {
                        EventBus.getDefault().post(new ConnectivityEvent(false));
                    }
                }
            });
        }
    };

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private AlertDialog changeStatus() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CrearCliente.this);
        builder.setTitle(getString(R.string.title_not_connected));
        builder.setMessage(getString(R.string.not_internet));
        builder.setCancelable(false);
        return builder.create();
    }
}

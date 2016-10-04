package com.example.djc.kanquimaniapark.CrearClientes;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.djc.kanquimaniapark.Clases.Cliente;
import com.example.djc.kanquimaniapark.Helpers.DateValidator;
import com.example.djc.kanquimaniapark.R;

import java.util.Objects;


public class CrearCliente extends AppCompatActivity {

    private EditText nombreET, apellidoET, correoET, cumpleET;
    private TextView idET;

    private ImageView fotoIV;
    private Bitmap bitmapFoto;
    private ClientFirebaseHelper clientFirebaseHelper;

    private String DATEFORMAT = "dd/MM/yyyy";
    private String sexo = "";
    private View focusView = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cliente);

        clientFirebaseHelper = new ClientFirebaseHelper();

        idET = (TextView)findViewById(R.id.idET);
        fotoIV = (ImageView) findViewById(R.id.foto);
        nombreET = (EditText) findViewById(R.id.nombreET);
        apellidoET = (EditText) findViewById(R.id.apellidoET);
        correoET = (EditText) findViewById(R.id.correoET);
        cumpleET = (EditText) findViewById(R.id.cumpleET);

        idET.setText(String.valueOf(clientFirebaseHelper.count+1));
    }

    //DATEPICKER DIALOG
    public void getBirthday(View v) {
        DialogFragment fragment = new DatePickerFragment();
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
            bitmapFoto = (Bitmap) data.getExtras().get("data");
            fotoIV.setImageBitmap(bitmapFoto);
        }
    }

    public void registrarCliente(View v) {
        boolean cancel = false;
        String nombre, apellido, correo, cumple;


        //CONTROL DE ENTRADA DE DATOS
        if (!DateValidator.isDateValid(cumpleET.getText().toString(), DATEFORMAT)) {
            //NOTIFICACION ERROR
            cumpleET.setError(getString(R.string.fechaInvalida));
            focusView = cumpleET;
            cancel = true;
        }
        else if (!correoET.getText().toString().contains("@")) {
            correoET.setError(getString(R.string.correoInvalido));
            focusView = correoET;
            cancel = true;
        } else if(Objects.equals(nombreET.getText().toString(), "")){
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
        } else if (bitmapFoto == null){
            photo_alertBuilder().show();
            cancel = true;
        }


        if (cancel) {
            focusView.requestFocus();
        } else {
            nombre = nombreET.getText().toString();
            apellido = apellidoET.getText().toString();
            correo = correoET.getText().toString();
            cumple = cumpleET.getText().toString();

            Cliente cliente = new Cliente(clientFirebaseHelper.count + 1, nombre, apellido, correo,
                    sexo, cumple, bitmapFoto);

            clientFirebaseHelper.addClient(cliente);
            Toast.makeText(this, getString(R.string.clienteAgregado), Toast.LENGTH_SHORT).show();
            finish();
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

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this)
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
}


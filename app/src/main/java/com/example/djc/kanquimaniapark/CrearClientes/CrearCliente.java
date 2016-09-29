package com.example.djc.kanquimaniapark.CrearClientes;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.djc.kanquimaniapark.Helpers.BitMapHelper;
import com.example.djc.kanquimaniapark.Helpers.DateValidator;
import com.example.djc.kanquimaniapark.R;

import java.util.Calendar;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CrearCliente extends AppCompatActivity {

    private EditText nombreET, apellidoET, correoET, cumpleET;

    private ImageView foto;
    private Bitmap bitmapFoto;

    private String dateFormat = "dd/MM/yyyy";
    private boolean cancel = false;
    private View focusView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cliente);
        ButterKnife.bind(this);

        foto = (ImageView)findViewById(R.id.foto);
        nombreET = (EditText)findViewById(R.id.nombreET);
        apellidoET = (EditText)findViewById(R.id.apellidoET);
        correoET = (EditText)findViewById(R.id.correoET);
        cumpleET = (EditText)findViewById(R.id.cumpleET);

    }

    //FUNCION QUE RECIBE LA FOTO DESDE LA CAMARA Y LA MUESTRA EN LA ACTIVIDAD.
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && resultCode == RESULT_OK) {
            bitmapFoto = (Bitmap) data.getExtras().get("data");
            foto.setImageBitmap(bitmapFoto);
        }
    }

    //DATEPICKER DIALOG
    public void getBirthday(View v){
        DialogFragment fragment = new DatePickerFragment();
        fragment.show(getFragmentManager(), "Date Picker");
    }

    //MANEJO DEL CLICK DEL IMAGEVIEW DE LA FOTO.
    public void fotoOnClick(View v){
        //INTENT PARA LLAMAR LA CAMARAPA
        Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //LANZA UNA ACTIVIDAD ESPERANDO UN RESULTADO (EN ESTE CASO UN BITMAP).
        startActivityForResult(intentCamera, 0);
    }

    public void registrarCliente(View v){
        String nombre, apellido, correo, cumple;

        //CONTROL DE ENTRADA DE DATOS

        if (!DateValidator.isDateValid(cumpleET.getText().toString(), dateFormat)){
            //NOTIFICACION ERROR
            cumpleET.setError(getString(R.string.fechaInvalida));
            focusView = cumpleET;
            cancel = true;
        } //
        else if (!correoET.getText().toString().contains("@")){
            correoET.setError(getString(R.string.correoInvalido));
            focusView = correoET;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            nombre = nombreET.getText().toString();
            apellido = apellidoET.getText().toString();
            correo = correoET.getText().toString();
            cumple = cumpleET.getText().toString();
        }
    }

}

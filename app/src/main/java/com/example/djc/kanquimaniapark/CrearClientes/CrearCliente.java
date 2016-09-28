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

import com.example.djc.kanquimaniapark.R;

import java.util.Calendar;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CrearCliente extends AppCompatActivity {

    @BindView(R.id.nombreET)
    public EditText nombreET;
    @BindView(R.id.apellidoET)
    public EditText apellidoET;
    @BindView(R.id.correoET)
    public EditText correoET;
    @BindView(R.id.cumpleET)
    public EditText cumpleET;

    private ImageView foto;
    private Bitmap bitmapFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cliente);
        ButterKnife.bind(this);

        //MANEJO DEL CLICK DEL IMAGEVIEW DE LA FOTO.
        foto = (ImageView)findViewById(R.id.foto);
        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //INTENT PARA LLAMAR LA CAMARAPA
                Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //LANZA UNA ACTIVIDAD ESPERANDO UN RESULTADO (EN ESTE CASO UN BITMAP).
                startActivityForResult(intentCamera, 0);
            }
        });

        //MANEJO DE SELECCION DE FECHA DE CUMPLEAÑOS
        /*cumpleET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    showDatePicker().show();
                }
            }
        });*/
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
    public void setBD(View v){
        DialogFragment fragment = new DatePickerFragment();
        fragment.show(getFragmentManager(), "Date Picker");
    }


}

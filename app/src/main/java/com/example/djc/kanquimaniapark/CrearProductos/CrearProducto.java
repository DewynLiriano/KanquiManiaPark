package com.example.djc.kanquimaniapark.CrearProductos;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.djc.kanquimaniapark.Clases.Producto;
import com.example.djc.kanquimaniapark.R;

import java.util.Objects;

public class CrearProducto extends AppCompatActivity {

    private EditText nombreET, precioET;
    private ProductsFireBaseHelper database;
    private View focusView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_producto);


        nombreET = (EditText)findViewById(R.id.nombre_producto);
        precioET = (EditText)findViewById(R.id.precio_producto);
        database = new ProductsFireBaseHelper();
    }

    public void crearProductoOnClick(View view) {

        boolean cancel = false;

        if (Objects.equals(nombreET.getText().toString(), "")) {
            cancel = true;
            nombreET.setError(getString(R.string.vacio));
            focusView = nombreET;
        } else if (Objects.equals(precioET.getText().toString(), "")){
            cancel = true;
            precioET.setError(getString(R.string.vacio));
            focusView = precioET;
        }

        if (cancel){
            focusView.requestFocus();

        }else {
            Producto producto = new Producto((int)database.count+1, nombreET.getText().toString(),
                    Float.parseFloat(precioET.getText().toString()), null);
            database.addProduct(producto);
            Toast.makeText(this, getString(R.string.producto_agregado), Toast.LENGTH_SHORT).show();
            finish();
        }

    }




}

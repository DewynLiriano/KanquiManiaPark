package com.example.djc.kanquimaniapark.CrearProductos;

import com.example.djc.kanquimaniapark.Clases.Producto;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by dewyn on 10/3/2016.
 */
public class ProductsFireBaseHelper {
    private String PRODUCTOS = "Productos";
    private String ID = "ID";
    private String NOMBRE = "Titulo";
    private String PRECIO = "Precio";
    private String ESPECIALES = "Especiales";

    private DatabaseReference database;
    public long count = 0;

    public ProductsFireBaseHelper(){
        database = FirebaseDatabase.getInstance().getReference(PRODUCTOS);

        //CONTADOR DE PRODUCTOS EXISTENTES
        setCounter();
    }

    public void addProduct(Producto producto){
        database.child(String.valueOf(String.valueOf(producto.get_id()))).child(NOMBRE).setValue(producto.get_titulo());
        database.child(String.valueOf(String.valueOf(producto.get_id()))).child(PRECIO).setValue(producto.get_precio());
        database.child(String.valueOf(String.valueOf(producto.get_id()))).child(ID).setValue(String.valueOf(producto.get_id()));

        if (producto.get_especiales() != null){
            for (int especial : producto.get_especiales()){
                database.child(String.valueOf(String.valueOf(producto.get_id()))).child(ESPECIALES).push().setValue(especial);
            }
        }
    }

    private void setCounter(){
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                count = dataSnapshot.getChildrenCount();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

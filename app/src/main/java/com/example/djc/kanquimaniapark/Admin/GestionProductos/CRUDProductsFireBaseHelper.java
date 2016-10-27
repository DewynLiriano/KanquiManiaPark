package com.example.djc.kanquimaniapark.Admin.GestionProductos;

import com.example.djc.kanquimaniapark.Clases.Producto;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.PublicKey;

/**
 * Created by dewyn on 10/3/2016.
 */
public class CRUDProductsFireBaseHelper {
    private String PRODUCTOS = "Productos";
    private String ID = "ID";
    private String NOMBRE = "Titulo";
    private String PRECIO = "Precio";

    private DatabaseReference dRef;

    public CRUDProductsFireBaseHelper(){
        dRef = FirebaseDatabase.getInstance().getReference(PRODUCTOS);
        dRef.keepSynced(true);
    }

    public void addProduct(Producto producto){
        dRef = FirebaseDatabase.getInstance().getReference(PRODUCTOS).push();
        dRef.child(ID).setValue(dRef.getKey());
        dRef.child(NOMBRE).setValue(producto.get_titulo());
        dRef.child(PRECIO).setValue(producto.get_precio());
    }

    public void updateProduct(Producto producto){
        dRef = FirebaseDatabase.getInstance().getReference(PRODUCTOS).child(producto.get_id());
        dRef.child(NOMBRE).setValue(producto.get_titulo());
        dRef.child(PRECIO).setValue(producto.get_precio());
    }

    public void deleteProduct(Producto producto){
        dRef = FirebaseDatabase.getInstance().getReference(PRODUCTOS).child(producto.get_id());
        dRef.removeValue();
    }
}

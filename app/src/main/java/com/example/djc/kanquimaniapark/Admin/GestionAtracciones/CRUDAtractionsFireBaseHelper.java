package com.example.djc.kanquimaniapark.Admin.GestionAtracciones;

import com.example.djc.kanquimaniapark.Clases.Atraccion;
import com.example.djc.kanquimaniapark.Clases.Producto;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by dewyn on 10/3/2016.
 */
public class CRUDAtractionsFireBaseHelper {
    private String ATRACCIONES = "Atracciones";
    private String ID = "ID";
    private String NOMBRE = "Nombre";
    private String PRECIO = "Precio";
    private String TIEMPO = "Tiempo"; //Minutos

    private DatabaseReference dRef;

    public CRUDAtractionsFireBaseHelper(){
        dRef = FirebaseDatabase.getInstance().getReference(ATRACCIONES);
        dRef.keepSynced(true);
    }

    public void addAtraction(Atraccion atraccion){
        dRef = FirebaseDatabase.getInstance().getReference(ATRACCIONES).push();
        dRef.child(ID).setValue(dRef.getKey());
        dRef.child(NOMBRE).setValue(atraccion.get_titulo());
        dRef.child(PRECIO).setValue(atraccion.get_precio());
        dRef.child(TIEMPO).setValue(atraccion.get_tiempo());
    }

    public void updateAtraction(Atraccion atraccion){
        dRef = FirebaseDatabase.getInstance().getReference(ATRACCIONES).child(atraccion.get_id());
        dRef.child(NOMBRE).setValue(atraccion.get_titulo());
        dRef.child(PRECIO).setValue(atraccion.get_precio());
        dRef.child(TIEMPO).setValue(atraccion.get_tiempo());
    }

    public void deleteAtraction(Atraccion atraccion){
        dRef = FirebaseDatabase.getInstance().getReference(ATRACCIONES).child(atraccion.get_id());
        dRef.removeValue();
    }
}

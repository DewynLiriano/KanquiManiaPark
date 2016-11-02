package com.example.djc.kanquimaniapark.Admin.GestionEspeciales;

import com.example.djc.kanquimaniapark.Clases.Atraccion;
import com.example.djc.kanquimaniapark.Clases.Especial;
import com.example.djc.kanquimaniapark.Clases.Producto;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by dewyn on 10/3/2016.
 */
public class CRUDSpecialsFireBaseHelper {
    private String ESPECIALES = "Especiales";
    private String ID = "ID";
    private String NOMBRE = "Nombre";
    private String PORCIENTO = "Porciento";
    private String FECHA_INICIO = "Fecha_Inicio";
    private String FECHA_FIN = "Fecha_Fin";
    private String PRODUCTOS = "Productos";
    private String ATRACCIONES = "Atracciones";

    private DatabaseReference dRef;

    public CRUDSpecialsFireBaseHelper(){
        dRef = FirebaseDatabase.getInstance().getReference(ESPECIALES);
        dRef.keepSynced(true);
    }

    public void addSpecial(Especial especial){
        dRef = FirebaseDatabase.getInstance().getReference(ESPECIALES).push();
        dRef.child(ID).setValue(dRef.getKey());
        dRef.child(NOMBRE).setValue(especial.get_nombre());
        dRef.child(PORCIENTO).setValue(especial.get_porciento());
        dRef.child(FECHA_INICIO).setValue(especial.get_fechaInicio());
        dRef.child(FECHA_FIN).setValue(especial.get_fechaFin());

        for (String p : especial.get_productos()){
            dRef.child(PRODUCTOS).push().setValue(p);
        }

        for (String a : especial.get_atracciones()){
            dRef.child(ATRACCIONES).push().setValue(a);
        }
    }

    public void updateSpecial(Especial especial){
        dRef = FirebaseDatabase.getInstance().getReference(ESPECIALES).child(especial.get_id());
        dRef.child(NOMBRE).setValue(especial.get_nombre());
        dRef.child(PORCIENTO).setValue(especial.get_porciento());
        dRef.child(FECHA_INICIO).setValue(especial.get_fechaInicio());
        dRef.child(FECHA_FIN).setValue(especial.get_fechaFin());

        for (String p : especial.get_productos()){
            dRef.child(PRODUCTOS).push().setValue(p);
        }

        for (String a : especial.get_atracciones()){
            dRef.child(ATRACCIONES).push().setValue(a);
        }
    }

    public void deleteSpecial(Especial especial){
        dRef = FirebaseDatabase.getInstance().getReference(ESPECIALES).child(especial.get_id());
        dRef.removeValue();
    }

    public void deleteSingleProduct(Especial especial, Producto producto){
        dRef = FirebaseDatabase.getInstance().getReference(ESPECIALES).child(especial.get_id())
                .child(PRODUCTOS).child(producto.get_id());

        dRef.removeValue();
    }
}

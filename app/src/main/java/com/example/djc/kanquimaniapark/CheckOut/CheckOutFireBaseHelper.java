package com.example.djc.kanquimaniapark.CheckOut;

import android.widget.Toast;

import com.example.djc.kanquimaniapark.Clases.Factura;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by dewyn on 11/26/2016.
 */

public class CheckOutFireBaseHelper {

    private static final String FACTURAS = "Facturas";
    private static final String ID = "ID";
    private static final String FECHA_EMISION = "Fecha_Emision";
    private static final String TOTAL_DESCONTADO = "Total_Descontado";
    private static final String TOTAL_FINAL = "Total_Final";
    private static final String PRODUCTOS_SELECCIONADOS = "Productos_Seleccionados";
    private static final String ATRACCIONES_SELECCIONADAS = "Atracciones_Seleccionadas";
    private static final String ESPECIALES_APLICADOS = "Especiales_Aplicados";

    private DatabaseReference factRef;

    public CheckOutFireBaseHelper(){
        factRef = FirebaseDatabase.getInstance().getReference(FACTURAS);
        factRef.keepSynced(true);
    }

    public void addFactura(Factura factura){
        factRef = FirebaseDatabase.getInstance().getReference(FACTURAS).push();
        factRef.child(ID).setValue(factRef.getKey());
        factRef.child(FECHA_EMISION).setValue(factura.get_fechaEmision());
        factRef.child(TOTAL_DESCONTADO).setValue(String.valueOf(factura.get_totalDescontado()));
        factRef.child(TOTAL_FINAL).setValue(String.valueOf(factura.get_totalFinal()));

        for (String s : factura.get_productos()){
            factRef.child(PRODUCTOS_SELECCIONADOS).push().setValue(s);
        }

        for (String s : factura.get_atraccionesSeleccionadas()){
            factRef.child(ATRACCIONES_SELECCIONADAS).push().setValue(s);
        }

        for (String s : factura.get_especialesID()){
            factRef.child(ESPECIALES_APLICADOS).push().setValue(s);
        }
    }
}

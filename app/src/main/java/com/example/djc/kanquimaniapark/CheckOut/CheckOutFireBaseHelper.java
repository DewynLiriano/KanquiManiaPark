package com.example.djc.kanquimaniapark.CheckOut;

import android.content.Intent;
import android.widget.Toast;

import com.example.djc.kanquimaniapark.Clases.Atraccion;
import com.example.djc.kanquimaniapark.Clases.Cliente;
import com.example.djc.kanquimaniapark.Clases.Factura;
import com.example.djc.kanquimaniapark.MainActivity.SelectedAttraction;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
    private static final String DATEFORMAT = "dd/MM/yyyy HH:mm";

    private static final String CONTROL_ATRACCIONES = "Control_Atracciones";
    private static final String CLIENTE_ID = "Cliente";
    private static final String ATRACCION_ID = "Atraccion_ID";
    private static final String HORA_ENTRADA = "Hora_Entrada";
    private static final String HORA_SALIDA = "Hora_Salida";

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

    public void createAttractionsManager(List<SelectedAttraction> selectedAttractions){
        for (SelectedAttraction sa : selectedAttractions){
            if (!sa.getAtraccion().get_tiempo().equals("Ilimitado")){
            factRef = FirebaseDatabase.getInstance().getReference(CONTROL_ATRACCIONES).push();
            factRef.child(ID).setValue(factRef.getKey());
            factRef.child(CLIENTE_ID).setValue(sa.getClient().get_id());
            factRef.child(ATRACCION_ID).setValue(sa.getAtraccion().get_id());

            SimpleDateFormat sdf = new SimpleDateFormat(DATEFORMAT, Locale.getDefault());
            factRef.child(HORA_ENTRADA).setValue(sdf.format(Calendar.getInstance().getTimeInMillis()));

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.MINUTE, Integer.valueOf(sa.getAtraccion().get_tiempo()));
                factRef.child(HORA_SALIDA).setValue(sdf.format(cal.getTime()));
            }

        }
    }
}

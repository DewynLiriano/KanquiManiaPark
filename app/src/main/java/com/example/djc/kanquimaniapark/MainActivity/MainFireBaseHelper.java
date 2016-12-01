package com.example.djc.kanquimaniapark.MainActivity;

import com.example.djc.kanquimaniapark.Clases.Atraccion;
import com.example.djc.kanquimaniapark.Clases.Empleado;
import com.example.djc.kanquimaniapark.Clases.IdentificadorEntrada;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by dewyn on 10/3/2016.
 */

public class MainFireBaseHelper {
    private static final String IDENTIFICADOR = "Identificadores";
    private static final String ID = "ID";
    private static final String FECHA = "Fecha";
    private static final String COLORES = "Colores";
    private static final String ATRACCIONES = "Atracciones";
    private static final String NOMBRE = "Nombre";
    private static final String USUARIOS = "Usuarios";
    private static final String CONTRASENA = "Contrasena";
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference dataRef;
    private Map<String, Map<String, String>> map;

    public MainFireBaseHelper(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        dataRef = firebaseDatabase.getReference(USUARIOS);
        dataRef.keepSynced(true);
        getUsuarios();
    }

    private void getUsuarios() {
        dataRef = firebaseDatabase.getReference(USUARIOS);
        dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<Map<String, Map<String, String>>> genin = new GenericTypeIndicator<Map<String, Map<String, String>>>() {};
                map = dataSnapshot.getValue(genin);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public boolean signIn(String username, String contrasena){
        dataRef = firebaseDatabase.getReference(USUARIOS);
        boolean success = false;

        if (map != null){
            for (Map.Entry<String, Map<String, String>> entry : map.entrySet()){
                if (entry != null){
                    HashMap value = (HashMap) entry.getValue();
                    if ((Objects.equals(value.get(NOMBRE), username))){
                        if (Objects.equals(value.get(CONTRASENA), contrasena)){
                            success = true;
                        }
                    }
                } else { success = false; }
            }
        }
        return success;
    }

    public void addIdentificator (IdentificadorEntrada iden){
        dataRef = FirebaseDatabase.getInstance().getReference(IDENTIFICADOR).push();
        dataRef.child(ID).setValue(dataRef.getKey());
        dataRef.child(FECHA).setValue(iden.get_fecha());

        for (HashMap.Entry<String, String> o : iden.get_atracciones().entrySet()){
            dataRef.child(ATRACCIONES).child(o.getKey()).setValue(o.getValue());
        }
    }
}
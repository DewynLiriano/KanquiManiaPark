package com.example.djc.kanquimaniapark.MainActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by dewyn on 10/3/2016.
 */

public class LogInFireBaseHelper {
    private String USUARIOS = "Usuarios";
    private DatabaseReference dataRef;
    private Map<String, Map<String, String>> map;

    public LogInFireBaseHelper(){
        dataRef = FirebaseDatabase.getInstance().getReference(USUARIOS);
        getUsuarios();
    }

    private void getUsuarios() {
        dataRef.addValueEventListener(new ValueEventListener() {
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
        boolean success = false;

        for (Map.Entry<String, Map<String, String>> entry : map.entrySet()){
            HashMap value = (HashMap) entry.getValue();
            if ((Objects.equals(value.get("Nombre"), username))){
                if (Objects.equals(value.get("Contrasena"), contrasena)){
                    success = true;
                }
            }
        }
        return success;
    }
}
package com.example.djc.kanquimaniapark.Admin.GestionEmpleados;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by dewyn on 10/17/2016.
 */

public class EmployeeFireBaseHelper {

    private String EMPLEADOS = "Empleados";
    private DatabaseReference database;
    public long count = 0;

    public EmployeeFireBaseHelper() {
        database = FirebaseDatabase.getInstance().getReference(EMPLEADOS);
        database.keepSynced(true);

        //CONTADOR DE EMPLEADOS EXISTENTES
        setCounter();

    }

    private void setCounter() {
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

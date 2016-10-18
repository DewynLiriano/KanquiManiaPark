package com.example.djc.kanquimaniapark.Admin.GestionEmpleados;

import com.example.djc.kanquimaniapark.Clases.Empleado;
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
    private String ID = "ID";
    private String NOMBRE = "Nombre";
    private String APELLIDO = "Apellido";
    private String SEXO = "Sexo";
    private String USERNAME = "Username";
    private String CONTRASENA = "Contrasena";
    private String POSICION = "Posicion";
    private DatabaseReference database;
    private long count = 0;

    public EmployeeFireBaseHelper() {
        database = FirebaseDatabase.getInstance().getReference(EMPLEADOS);
        database.keepSynced(true);

        //CONTADOR DE EMPLEADOS EXISTENTES
        setCounter();

    }

    public void addEmployee(Empleado empleado){
        database.child(String.valueOf(empleado.get_id())).child(ID).setValue(String.valueOf(empleado.get_id()));
        database.child(String.valueOf(empleado.get_id())).child(NOMBRE).setValue(String.valueOf(empleado.get_nombre()));
        database.child(String.valueOf(empleado.get_id())).child(APELLIDO).setValue(String.valueOf(empleado.get_apellido()));
        database.child(String.valueOf(empleado.get_id())).child(SEXO).setValue(String.valueOf(empleado.get_sexo()));
        database.child(String.valueOf(empleado.get_id())).child(USERNAME).setValue(String.valueOf(empleado.get_userName()));
        database.child(String.valueOf(empleado.get_id())).child(CONTRASENA).setValue(String.valueOf(empleado.get_contrasena()));
        database.child(String.valueOf(empleado.get_id())).child(POSICION).setValue(String.valueOf(empleado.get_tipo()));
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

    public long getCount() {
        return count;
    }
}

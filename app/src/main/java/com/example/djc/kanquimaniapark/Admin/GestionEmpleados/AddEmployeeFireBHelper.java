package com.example.djc.kanquimaniapark.Admin.GestionEmpleados;

import com.example.djc.kanquimaniapark.Clases.Empleado;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by dewyn on 10/17/2016.
 */

public class AddEmployeeFireBHelper {

    public String EMPLEADOS = "Empleados";
    public String ID = "ID";
    public String NOMBRE = "Nombre";
    public String APELLIDO = "Apellido";
    public String SEXO = "Sexo";
    public String USERNAME = "Username";
    public String CONTRASENA = "Contrasena";
    public String POSICION = "Posicion";
    public DatabaseReference dRef, addChildRef;
    public long count = 0;
    public List<Empleado> empleados;

    public AddEmployeeFireBHelper() {
        dRef = FirebaseDatabase.getInstance().getReference(EMPLEADOS);
        dRef.keepSynced(true);
        empleados = new ArrayList<Empleado>();
        //Contador
        //setCounter();
    }

    public void addEmployee(Empleado empleado){
        addChildRef = FirebaseDatabase.getInstance().getReference(EMPLEADOS).push();
        //addChildRef.child(ID).setValue(empleado.get_id());
        addChildRef.child(NOMBRE).setValue(empleado.get_nombre());
        addChildRef.child(APELLIDO).setValue(empleado.get_apellido());
        addChildRef.child(SEXO).setValue(empleado.get_sexo());
        addChildRef.child(USERNAME).setValue(empleado.get_userName());
        addChildRef.child(CONTRASENA).setValue(empleado.get_contrasena());
        addChildRef.child(POSICION).setValue(empleado.get_tipo());
    }

    private void setCounter(){
        dRef.addValueEventListener(new ValueEventListener() {
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

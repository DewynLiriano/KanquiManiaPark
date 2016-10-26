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

public class CRUDEmployeeFireBHelper {

    private String EMPLEADOS = "Empleados";
    private String ID = "ID";
    private String NOMBRE = "Nombre";
    private String APELLIDO = "Apellido";
    private String SEXO = "Sexo";
    private String USERNAME = "Username";
    private String CONTRASENA = "Contrasena";
    private String POSICION = "Posicion";

    public DatabaseReference dRef;


    public CRUDEmployeeFireBHelper() {
        dRef = FirebaseDatabase.getInstance().getReference(EMPLEADOS);
        dRef.keepSynced(true);
    }

    public void addEmployee(Empleado empleado){
        dRef = FirebaseDatabase.getInstance().getReference(EMPLEADOS).push();
        dRef.child(ID).setValue(dRef.getKey());
        dRef.child(NOMBRE).setValue(empleado.get_nombre());
        dRef.child(APELLIDO).setValue(empleado.get_apellido());
        dRef.child(SEXO).setValue(empleado.get_sexo());
        dRef.child(USERNAME).setValue(empleado.get_userName());
        dRef.child(CONTRASENA).setValue(empleado.get_contrasena());
        dRef.child(POSICION).setValue(empleado.get_tipo());
    }
    public void updateEmployee(Empleado empleado){
        dRef = FirebaseDatabase.getInstance().getReference(EMPLEADOS).child(empleado.get_id());
        dRef.child(NOMBRE).setValue(empleado.get_nombre());
        dRef.child(APELLIDO).setValue(empleado.get_apellido());
        dRef.child(SEXO).setValue(empleado.get_sexo());
        dRef.child(USERNAME).setValue(empleado.get_userName());
        dRef.child(CONTRASENA).setValue(empleado.get_contrasena());
        dRef.child(POSICION).setValue(empleado.get_tipo());
    }
    public void deleteEmployee(Empleado empleado){
        dRef = FirebaseDatabase.getInstance().getReference(EMPLEADOS).child(empleado.get_id());
        dRef.removeValue();
    }
}



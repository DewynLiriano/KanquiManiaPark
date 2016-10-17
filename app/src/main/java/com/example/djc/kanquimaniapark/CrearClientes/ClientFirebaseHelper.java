package com.example.djc.kanquimaniapark.CrearClientes;

import android.util.Base64;

import com.example.djc.kanquimaniapark.Clases.Cliente;
import com.example.djc.kanquimaniapark.Helpers.BitMapHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by dewyn on 10/1/2016.
 */

public class ClientFirebaseHelper {

    private String CLIENTS_CHILD = "Clientes";
    private String ID = "ID";
    private String NOMBRE = "Nombre";
    private String APELLIDO = "Apellido";
    private String CORREO = "Correo";
    private String NUMERO = "Numero";
    private String SEXO = "Sexo";
    private String CUMPLE = "Cumpleanos";
    private String FOTO = "Foto";

    private DatabaseReference database;

    public long count = 0;
    public ArrayList<Cliente> clientes = new ArrayList<>();

    public ClientFirebaseHelper(){
        database = FirebaseDatabase.getInstance().getReference(CLIENTS_CHILD);

        //CONTADOR DE CLIENTES EXISTENTES
        setCounter();
    }

    public void addClient(Cliente cliente){
        database.child(String.valueOf(cliente.get_id())).child(ID).setValue(String.valueOf(cliente.get_id()));
        database.child(String.valueOf(cliente.get_id())).child(NOMBRE).setValue(cliente.get_nombre());
        database.child(String.valueOf(cliente.get_id())).child(APELLIDO).setValue(cliente.get_apellido());
        database.child(String.valueOf(cliente.get_id())).child(CORREO).setValue(cliente.get_correo());
        database.child(String.valueOf(cliente.get_id())).child(NUMERO).setValue(cliente.get_numero());
        database.child(String.valueOf(cliente.get_id())).child(SEXO).setValue(cliente.get_sexo());
        database.child(String.valueOf(cliente.get_id())).child(CUMPLE).setValue(cliente.get_fechaCumpleAnos());
        byte[] data = BitMapHelper.getBytes(cliente.get_bitmapFoto());
        String fotoBase64 = Base64.encodeToString(data, Base64.DEFAULT);
        database.child(String.valueOf(cliente.get_id())).child(FOTO).setValue(fotoBase64);
    }

    private void setCounter(){
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

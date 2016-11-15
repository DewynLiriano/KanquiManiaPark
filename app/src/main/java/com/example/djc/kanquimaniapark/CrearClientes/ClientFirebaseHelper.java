package com.example.djc.kanquimaniapark.CrearClientes;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Base64;

import com.example.djc.kanquimaniapark.Clases.Cliente;
import com.example.djc.kanquimaniapark.Helpers.BitMapHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayOutputStream;
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
    private String CUMPLE = "Fecha_Cumpleanos";
    private String FOTO = "Foto";
    private static final String FOTOS_CLIENTES = "FOTOS_CLIENTES";

    private DatabaseReference database;
    private StorageReference storage;

    public long count = 0;

    public ClientFirebaseHelper(){
        database = FirebaseDatabase.getInstance().getReference(CLIENTS_CHILD);
        storage = FirebaseStorage.getInstance().getReference(FOTOS_CLIENTES);
        //CONTADOR DE CLIENTES EXISTENTES
        setCounter();
    }

    public void addClient(Cliente cliente, Uri uri){
        final boolean[] OK = {false};

        database.child(String.valueOf(cliente.get_id())).child(ID).setValue(String.valueOf(cliente.get_id()));
        database.child(String.valueOf(cliente.get_id())).child(NOMBRE).setValue(cliente.get_nombre());
        database.child(String.valueOf(cliente.get_id())).child(APELLIDO).setValue(cliente.get_apellido());
        database.child(String.valueOf(cliente.get_id())).child(CORREO).setValue(cliente.get_correo());
        database.child(String.valueOf(cliente.get_id())).child(NUMERO).setValue(cliente.get_numero());
        database.child(String.valueOf(cliente.get_id())).child(SEXO).setValue(cliente.get_sexo());
        database.child(String.valueOf(cliente.get_id())).child(CUMPLE).setValue(cliente.get_fechaCumpleAnos());

        StorageReference path = storage.child(cliente.get_id());
        path.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                EventBus.getDefault().post(new SuccessEvent(true));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                EventBus.getDefault().post(new SuccessEvent(false));
            }
        });
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

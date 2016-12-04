package com.example.djc.kanquimaniapark.MainActivity.ClientsList;

import android.util.Log;
import android.widget.Filter;
import android.widget.Toast;

import com.example.djc.kanquimaniapark.Clases.Cliente;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dewyn on 11/13/2016.
 */

public class ClientsFilter extends Filter {

    private static final String ID = "ID";
    private static final String NOMBRE = "Nombre";
    private static final String APELLIDO = "Apellido";
    private static final String FECHA_CUMPLEANOS = "Fecha_Cumpleanos";
    private static final String SEXO = "Sexo";
    private static final String CORREO = "Correo_Electronico";
    private static final String NUMERO = "Numero_Telefono";

    private ClientRecyclerAdapter adapter;
    private List<Cliente> list;
    private ArrayList<Cliente> clientsName, clientsLastName, clientsID;

    public String cons;

    private DatabaseReference cliRefName, cliRefLastName, cliRefID;

    public ClientsFilter(ArrayList<Cliente> clientes, ClientRecyclerAdapter adapter){
        this.adapter = adapter;
        this.list = clientes;

        clientsName = new ArrayList<>();
        clientsLastName = new ArrayList<>();
        clientsID = new ArrayList<>();
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
        cons = String.valueOf(constraint);

        cliRefName = FirebaseDatabase.getInstance().getReference("Clientes");
        cliRefLastName = FirebaseDatabase.getInstance().getReference("Clientes");
        cliRefID = FirebaseDatabase.getInstance().getReference("Clientes");
        //cliRefName.orderByChild("Nombre").equalTo(String.valueOf(constraint)).addValueEventListener(getNames);

        cliRefName.addValueEventListener(getNames);
        cliRefLastName.addValueEventListener(getLastName);
        //cliRefID.addValueEventListener(getIDs);


        if (constraint != null && constraint.length() > 0){
            constraint = constraint.toString().toUpperCase();
            ArrayList<Cliente> clientesFiltrados = new ArrayList<>();


            for (int i=0 ; i < list.size() ; i++){
                if (list.get(i).get_nombre().toUpperCase().contains(constraint)
                        || list.get(i).get_apellido().toUpperCase().contains(constraint)
                        || list.get(i).get_id().toUpperCase().contains(constraint)){
                    clientesFiltrados.add(list.get(i));
                }
            }

            for (Cliente c : clientsName){
                clientesFiltrados.add(c);
            }
            for (Cliente c : clientsLastName){
                clientesFiltrados.add(c);
            }
            for (Cliente c : clientsID){
                clientesFiltrados.add(c);
            }

            /*if (clientesFiltrados .size()== 0){
                clientesFiltrados = clientsName;
            }*/

            results.count = clientesFiltrados.size();
            results.values = clientesFiltrados;
        } else {
            results.count = list.size();
            results.values = list;
        }

        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.list = (ArrayList<Cliente>)results.values;
        adapter.notifyDataSetChanged();
    }

    private void sortClientes() {
        Collections.sort(clientsName, new Comparator<Cliente>() {
            @Override
            public int compare(Cliente o1, Cliente o2) {
                return o2.get_id().compareTo(o1.get_id());
            }
        });
    }

    private ValueEventListener getNames = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            clientsName.clear();
            HashMap rootMap = (HashMap) dataSnapshot.getValue();
            if (rootMap != null){
                Collection<Object> objects = rootMap.values();
                for (Object o : objects){
                    if (o instanceof Map){
                        HashMap<String, Object> map = (HashMap<String, Object>) o;
                        if (String.valueOf(map.get(NOMBRE)).contains(cons)){
                            final Cliente c = new Cliente();
                            c.set_id( (String) map.get(ID));
                            c.set_nombre((String) map.get(NOMBRE));
                            c.set_apellido((String) map.get(APELLIDO));
                            c.set_fechaCumpleAnos((String) map.get(FECHA_CUMPLEANOS));
                            c.set_sexo((String) map.get(SEXO));
                            c.set_numero((String) map.get(NUMERO));
                            c.set_correo((String) map.get(CORREO));
                            Log.i("------Cliente nombre", c.get_nombre());
                            clientsName.add(c);
                        }
                    }
                }
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    private ValueEventListener getLastName = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            clientsLastName.clear();
            HashMap rootMap = (HashMap) dataSnapshot.getValue();

            if (rootMap != null){
                Collection<Object> objects = rootMap.values();
                for (Object o : objects){
                    if (o instanceof Map){
                        HashMap<String, Object> map = (HashMap<String, Object>) o;
                        if (String.valueOf(map.get(APELLIDO)).contains(cons)){
                            final Cliente c = new Cliente();
                            c.set_id( (String) map.get(ID));
                            c.set_nombre((String) map.get(NOMBRE));
                            c.set_apellido((String) map.get(APELLIDO));
                            c.set_fechaCumpleAnos((String) map.get(FECHA_CUMPLEANOS));
                            c.set_sexo((String) map.get(SEXO));
                            c.set_numero((String) map.get(NUMERO));
                            c.set_correo((String) map.get(CORREO));
                            clientsLastName.add(c);
                        }
                    }
                }
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    /*private ValueEventListener getIDs = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            clientsID.clear();
            HashMap rootMap = (HashMap) dataSnapshot.getValue();

            if (rootMap != null){
                Collection<Object> objects = rootMap.values();
                for (Object o : objects){
                    if (o instanceof Map){
                        HashMap<String, Object> map = (HashMap<String, Object>) o;
                        if (String.valueOf(map.get(ID)).contains(cons.toUpperCase())){
                            final Cliente c = new Cliente();
                            c.set_id( (String) map.get(ID));
                            c.set_nombre((String) map.get(NOMBRE));
                            c.set_apellido((String) map.get(APELLIDO));
                            c.set_fechaCumpleAnos((String) map.get(FECHA_CUMPLEANOS));
                            c.set_sexo((String) map.get(SEXO));
                            c.set_numero((String) map.get(NUMERO));
                            c.set_correo((String) map.get(CORREO));
                            clientsID.add(c);
                        }
                    }
                }
            }
            if (clientsID.size() == dataSnapshot.getChildrenCount()){
                areIDsDone = true;
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };*/
}

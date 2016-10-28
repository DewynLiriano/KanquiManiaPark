package com.example.djc.kanquimaniapark.MainActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.example.djc.kanquimaniapark.Admin.SimpleTabsActivity;
import com.example.djc.kanquimaniapark.CrearClientes.CrearCliente;

import com.example.djc.kanquimaniapark.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton plusFAB;
    private FloatingActionButton invoiceFAB;
    private FloatingActionButton clientFAB;
    private FloatingActionButton mailFAB;

    private LogInFireBaseHelper logInHelper;

    private Boolean isFabOpen = false;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }

        logInHelper = new LogInFireBaseHelper();

        plusFAB = (FloatingActionButton)findViewById(R.id.plusFAB);
        clientFAB = (FloatingActionButton)findViewById(R.id.fabAddClient);
        invoiceFAB = (FloatingActionButton)findViewById(R.id.fabCreateInvoice);
        mailFAB = (FloatingActionButton)findViewById(R.id.fabSendMail);

        //Manejo de los Floating Buttons
        fabAnimator();
        //Manejo de Recycler View
        recyclerViewController();
    }

    //Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()){
            case R.id.crear_producto:
                logIn_alertBuilder().show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //--------------------------------------FUNCIONES----------------------------------------
    //FUNCION DONDE SE ANIMAN Y SE MANEJA EL CLICK DE LOS FLOATING BUTTONS.
    public void fabAnimator(){
        //Animaciones de los FABs
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_backward);
    }

    //FUNCION SE MANEJA EL RECYCLERVIEW
    public void recyclerViewController(){
        //Recycler view
        ArrayList<String> data = new ArrayList<>();

        for(int i = 0; i < 30; i++) {
            data.add("Panita " + i);
        }

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        ClientRecyclerAdapter adapter = new ClientRecyclerAdapter(this, data);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    //FUNCION DONDE SE ANIMAN LOS BOTONES AL DAR CLICK
    public void animateFAB(){

        if(isFabOpen){
            plusFAB.startAnimation(rotate_backward);
            clientFAB.startAnimation(fab_close);
            invoiceFAB.startAnimation(fab_close);
            mailFAB.startAnimation(fab_close);
            clientFAB.setClickable(false);
            invoiceFAB.setClickable(false);
            mailFAB.setClickable(false);
            isFabOpen = false;

        } else {
            plusFAB.startAnimation(rotate_forward);
            clientFAB.startAnimation(fab_open);
            invoiceFAB.startAnimation(fab_open);
            mailFAB.startAnimation(fab_open);
            clientFAB.setClickable(true);
            invoiceFAB.setClickable(true);
            mailFAB.setClickable(true);
            isFabOpen = true;
        }
    }

    //RENGLON DE OnClicks
    public void plusFabOnClick(View v){
        animateFAB();
    }

    public void clientFabOnClick(View v){
        Intent intent = new Intent(getApplicationContext(), CrearCliente.class);
        startActivity(intent);
    }

    public void mailFabOnClick(View v){
        //Toast.makeText(this, String.valueOf(helper.count), Toast.LENGTH_SHORT).show();
    }

    private AlertDialog logIn_alertBuilder(){
        final View[] focusView = {null};
        final String[] usuario = new String[1];
        final String[] contrasena = new String[1];
        final boolean[] cancel = {false};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final Context context = builder.getContext();
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.login_dialog, null, false);


        builder.setView(view)
                .setTitle(getString(R.string.login_title))
                .setCancelable(false)
                .setMessage(getString(R.string.login_info))
                .setNegativeButton(getString(R.string.cancelar), null);


        builder.setPositiveButton(getString(R.string.login_title), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final EditText usuarioET = (EditText)view.findViewById(R.id.login_name);
                final EditText passwordET = (EditText)view.findViewById(R.id.login_password);

                if (Objects.equals(usuarioET.getText().toString(), "")){
                    usuarioET.setError(getString(R.string.vacio));
                    focusView[0] = usuarioET;
                    cancel[0] = true;
                } else if (Objects.equals(passwordET.getText().toString(), "")){
                    passwordET.setError(getString(R.string.vacio));
                    focusView[0] = passwordET;
                    cancel[0] = true;
                }

                if (cancel[0]){
                    focusView[0].requestFocus();
                } else {
                    usuario[0] = usuarioET.getText().toString();
                    contrasena[0] = passwordET.getText().toString();
                    boolean success = logInHelper.signIn(usuario[0], contrasena[0]);

                    if (success){
                        Intent intent = new Intent(getApplicationContext(), SimpleTabsActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(MainActivity.this, "No se ha podido iniciar sesion", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        return builder.create();
    }
}

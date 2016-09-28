package com.example.djc.kanquimaniapark.MainActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.SimpleExpandableListAdapter;
import android.widget.Toast;

import com.example.djc.kanquimaniapark.CrearClientes.CrearCliente;
import com.example.djc.kanquimaniapark.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton plusFAB;
    private FloatingActionButton invoiceFAB;
    private FloatingActionButton clientFAB;
    private FloatingActionButton mailFAB;


    private Boolean isFabOpen = false;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;
    public RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        plusFAB = (FloatingActionButton)findViewById(R.id.plusFAB);
        clientFAB = (FloatingActionButton)findViewById(R.id.fabAddClient);
        invoiceFAB = (FloatingActionButton)findViewById(R.id.fabCreateInvoice);
        mailFAB = (FloatingActionButton)findViewById(R.id.fabSendMail);


        clientFAB = (FloatingActionButton)findViewById(R.id.fabAddClient);


        //Manejo de los Floating Buttons
        fabAnimator();
        //Manejo de Recycler View
        recyclerViewController();
    }

    //FUNCION DONDE SE ANIMAN Y SE MANEJA EL CLICK DE LOS FLOATING BUTTONS.
    public void fabAnimator(){
        //Animaciones de los FABs
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_backward);

        plusFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateFAB();
            }
        });

        clientFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CrearCliente.class);
                startActivity(intent);
            }
        });
    }

    //FUNCION SE MANEJA EL RECYCLERVIEW
    public void recyclerViewController(){
        //Recycler view
        ArrayList<String> data = new ArrayList<>();

        for(int i = 0; i < 30; i++) {
            data.add("Panita " + i);
        }

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        RecyclerAdapter adapter = new RecyclerAdapter(this, data);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

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


}

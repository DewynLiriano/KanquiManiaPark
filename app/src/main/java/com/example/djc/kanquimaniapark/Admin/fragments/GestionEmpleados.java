package com.example.djc.kanquimaniapark.Admin.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.djc.kanquimaniapark.MainActivity.RecyclerAdapter;
import com.example.djc.kanquimaniapark.R;

import java.util.ArrayList;

public class GestionEmpleados extends Fragment {

    private RecyclerView recyclerView;


    public GestionEmpleados() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_empleados, container, false);
        recyclerViewController(view);
        // Inflate the layout for this fragment
        return view;
    }

    public void recyclerViewController(View view){
        //Recycler view
        ArrayList<String> data = new ArrayList<>();

        for(int i = 0; i < 30; i++) {
            data.add("Panita " + i);
        }

        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerEmpleados);
        RecyclerAdapter adapter = new RecyclerAdapter(getContext(), data);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

}

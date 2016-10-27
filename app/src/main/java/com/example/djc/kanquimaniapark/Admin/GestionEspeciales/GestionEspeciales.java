package com.example.djc.kanquimaniapark.Admin.GestionEspeciales;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.djc.kanquimaniapark.R;

public class GestionEspeciales extends Fragment {

    public GestionEspeciales() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_especiales, container, false);



        // Inflate the layout for this fragment
        return view;
    }

}

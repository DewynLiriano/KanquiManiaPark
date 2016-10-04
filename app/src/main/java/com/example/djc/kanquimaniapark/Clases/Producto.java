package com.example.djc.kanquimaniapark.Clases;

import android.content.Intent;

import java.util.ArrayList;

/**
 * Created by dewyn on 9/26/2016.
 */

public class Producto {
    private int _id;
    private String _titulo;
    private double _precio;
    private ArrayList<Integer> _especiales;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_titulo() {
        return _titulo;
    }

    public void set_titulo(String _nombre) {
        this._titulo = _nombre;
    }

    public double get_precio() {
        return _precio;
    }

    public void set_precio(double _precio) {
        this._precio = _precio;
    }

    public ArrayList<Integer> get_especiales() {
        return _especiales;
    }

    public void set_especiales(ArrayList<Integer> especiales) {
        this._especiales = especiales;
    }

    public Producto(int id, String nombre, double precio, ArrayList<Integer> especiales) {
        set_id(id);
        set_titulo(nombre);
        set_precio(precio);
        set_especiales(especiales);
    }

    public Producto(){}
}

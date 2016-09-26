package com.example.djc.kanquimaniapark.Clases;

/**
 * Created by dewyn on 9/26/2016.
 */

public class Producto {
    private int _id;
    private String _nombre;
    private double _precio;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_name() {
        return _nombre;
    }

    public void set_nombre(String _nombre) {
        this._nombre = _nombre;
    }

    public double get_precio() {
        return _precio;
    }

    public void set_precio(double _precio) {
        this._precio = _precio;
    }

    public Producto(int id, String nombre, double precio) {
        set_id(id);
        set_nombre(nombre);
        set_precio(precio);
    }

    public Producto(){}
}

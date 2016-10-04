package com.example.djc.kanquimaniapark.Clases;

import java.util.ArrayList;

/**
 * Created by dewyn on 9/26/2016.
 */

public class Boleta extends Producto {

    private int _tiempo;

    public int get_tiempo() {
        return _tiempo;
    }

    public void set_tiempo(int _tiempo) {
        this._tiempo = _tiempo;
    }

    public Boleta(int _tiempo) {}

    public Boleta(int id, String nombre, double precio, int _tiempo, ArrayList<Integer> especiales) {
        super(id, nombre, precio, especiales);
        set_tiempo(_tiempo);
    }
}

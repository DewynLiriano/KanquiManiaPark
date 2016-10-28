package com.example.djc.kanquimaniapark.Clases;

/**
 * Created by dewyn on 10/26/2016.
 */

public class Atraccion extends Producto {

    private String _tiempo;

    public String get_tiempo() {
        return _tiempo;
    }

    public void set_tiempo(String _tiempo) {
        this._tiempo = _tiempo;
    }

    public Atraccion(String id, String nombre, String precio, String _tiempo) {
        super(id, nombre, precio);
        this._tiempo = _tiempo;
    }

    public Atraccion(){}

}

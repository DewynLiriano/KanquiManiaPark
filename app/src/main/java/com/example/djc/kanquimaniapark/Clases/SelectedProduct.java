package com.example.djc.kanquimaniapark.Clases;

/**
 * Created by dewyn on 11/22/2016.
 */

public class SelectedProduct {
    private int _cantidad;
    private Producto _producto;

    public SelectedProduct(int _cantidad, Producto _producto) {
        set_cantidad(_cantidad);
        set_producto(_producto);
    }

    public int get_cantidad() {
        return _cantidad;
    }

    public Producto get_producto() {
        return _producto;
    }

    public void set_cantidad(int _cantidad) {
        this._cantidad = _cantidad;
    }

    public void set_producto(Producto _producto) {
        this._producto = _producto;
    }
}

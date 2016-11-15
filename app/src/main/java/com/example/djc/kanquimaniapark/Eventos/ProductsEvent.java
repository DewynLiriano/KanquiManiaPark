package com.example.djc.kanquimaniapark.Eventos;

import com.example.djc.kanquimaniapark.Clases.Atraccion;
import com.example.djc.kanquimaniapark.Clases.Producto;

import java.util.List;

/**
 * Created by dewyn on 11/9/2016.
 */

public class ProductsEvent {
    private List<Producto> productos;

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    public ProductsEvent() {}
}

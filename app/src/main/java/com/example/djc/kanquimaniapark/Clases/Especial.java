package com.example.djc.kanquimaniapark.Clases;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by dewyn on 9/26/2016.
 */

public class Especial {
    private String _id;
    private String _nombre;
    private String _porciento;
    private ArrayList<Producto> _productos;
    private String _fechaInicio;
    private String _fechaFin;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_nombre() {
        return _nombre;
    }

    public void set_nombre(String _nombre) {
        this._nombre = _nombre;
    }

    public String get_porciento() {
        return _porciento;
    }

    public void set_porciento(String _porciento) {
        this._porciento = _porciento;
    }

    public ArrayList<Producto> get_productos() {
        return _productos;
    }

    public void set_productos(ArrayList<Producto> _productos) {
        this._productos = _productos;
    }

    public String get_fechaInicio() {
        return _fechaInicio;
    }

    public void set_fechaInicio(String _fechaInicio) {
        this._fechaInicio = _fechaInicio;
    }

    public String get_fechaFin() {
        return _fechaFin;
    }

    public void set_fechaFin(String _fechaFin) {
        this._fechaFin = _fechaFin;
    }


    public Especial(String _id, String _nombre, String _porciento, ArrayList<Producto> _productos,
                    String _fechaInicio, String _fechaFin) {
        set_id(_id);
        set_nombre(_nombre);
        set_porciento(_porciento);
        set_productos(_productos);
        set_fechaInicio(_fechaInicio);
        set_fechaFin(_fechaFin);
    }

    public Especial(){}
}

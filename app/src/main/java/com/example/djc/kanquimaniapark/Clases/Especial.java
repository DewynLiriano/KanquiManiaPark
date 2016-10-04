package com.example.djc.kanquimaniapark.Clases;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by dewyn on 9/26/2016.
 */

public class Especial {
    private int _id;
    private String _nombre;
    private float _porciento;
    private ArrayList<Producto> _productos;
    private Date _fechaInicio;
    private Date _fechaFin;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_nombre() {
        return _nombre;
    }

    public void set_nombre(String _nombre) {
        this._nombre = _nombre;
    }

    public float get_porciento() {
        return _porciento;
    }

    public void set_porciento(float _porciento) {
        this._porciento = _porciento;
    }

    public ArrayList<Producto> get_productos() {
        return _productos;
    }

    public void set_productos(ArrayList<Producto> _productos) {
        this._productos = _productos;
    }

    public Date get_fechaInicio() {
        return _fechaInicio;
    }

    public void set_fechaInicio(Date _fechaInicio) {
        this._fechaInicio = _fechaInicio;
    }

    public Date get_fechaFin() {
        return _fechaFin;
    }

    public void set_fechaFin(Date _fechaFin) {
        this._fechaFin = _fechaFin;
    }


    public Especial(int _id, String _nombre, float _porciento, ArrayList<Producto> _productos, Date _fechaInicio, Date _fechaFin) {
        set_id(_id);
        set_nombre(_nombre);
        set_porciento(_porciento);
        set_productos(_productos);
        set_fechaInicio(_fechaInicio);
        set_fechaFin(_fechaFin);
    }

    public Especial(){}
}

package com.example.djc.kanquimaniapark.Clases;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by dewyn on 9/26/2016.
 */

public class Factura {
    private int _id;
    private List<Producto> _productos;
    private List<ControlAtraccion> _controlAtracciones;
    private Date _fechaEmision;
    private List<Especial> _especiales;
    private double _totalDescontado;
    private double _totalFinal;
    private double _devuelta;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public List<Producto> get_productos() {
        return _productos;
    }

    public void set_productos(List<Producto> _productos) {
        this._productos = _productos;
    }

    public List<ControlAtraccion> get_controlAtracciones() {
        return _controlAtracciones;
    }

    public void set_controlAtracciones(List<ControlAtraccion> _controlAtracciones) {
        this._controlAtracciones = _controlAtracciones;
    }

    public Date get_fechaEmision() {
        return _fechaEmision;
    }

    public void set_fechaEmision(Date _fechaEmision) {
        this._fechaEmision = _fechaEmision;
    }

    public List<Especial> get_especiales() {
        return _especiales;
    }

    public void set_especiales(List<Especial> _especiales) {
        this._especiales = _especiales;
    }

    public double get_totalDescontado() {
        return _totalDescontado;
    }

    public void set_totalDescontado(double _totalDescontado) {
        this._totalDescontado = _totalDescontado;
    }

    public double get_totalFinal() {
        return _totalFinal;
    }

    public void set_totalFinal(double _totalFinal) {
        this._totalFinal = _totalFinal;
    }

    public double get_devuelta() {
        return _devuelta;
    }

    public void set_devuelta(double _devuelta) {
        this._devuelta = _devuelta;
    }

    public Factura(int _id, List<Producto> _productos, List<ControlAtraccion> _controlAtracciones,
                   Date _fechaEmision, List<Especial> _especiales, double _totalDescontado,
                   double _totalFinal, double _devuelta) {
        set_id(_id);
        set_productos(_productos);
        set_controlAtracciones(_controlAtracciones);
        set_fechaEmision(_fechaEmision);
        set_especiales(_especiales);
        set_totalDescontado(_totalDescontado);
        set_totalFinal(_totalFinal);
        set_devuelta(_devuelta);
    }
    public Factura(){}
}

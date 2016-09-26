package com.example.djc.kanquimaniapark.Clases;

import java.text.SimpleDateFormat;

/**
 * Created by dewyn on 9/26/2016.
 */

public class IndentificadorEntrada {
    private Boleta _boleta;
    private int _color;
    private SimpleDateFormat _fecha;

    public Boleta get_boleta() {
        return _boleta;
    }

    public void set_boleta(Boleta _boleta) {
        this._boleta = _boleta;
    }

    public int get_color() {
        return _color;
    }

    public void set_color(int _color) {
        this._color = _color;
    }

    public SimpleDateFormat get_fecha() {
        return _fecha;
    }

    public void set_fecha(SimpleDateFormat _fecha) {
        this._fecha = _fecha;
    }

    public IndentificadorEntrada(Boleta _boleta, int _color, SimpleDateFormat _fecha) {
        set_boleta(_boleta);
        set_color(_color);
        set_fecha(_fecha);
    }
}

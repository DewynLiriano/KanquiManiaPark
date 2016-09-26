package com.example.djc.kanquimaniapark.Clases;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dewyn on 9/26/2016.
 */

public class ControlAtraccion {
    private int _id;
    private Cliente _cliente;
    private Boleta _boleta;
    private Date _horaSalida;
    private IndentificadorEntrada _identificador;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public Cliente get_cliente() {
        return _cliente;
    }

    public void set_cliente(Cliente _cliente) {
        this._cliente = _cliente;
    }

    public Boleta get_boleta() {
        return _boleta;
    }

    public void set_boleta(Boleta _boleta) {
        this._boleta = _boleta;
    }

    public Date get_horaSalida() {
        return _horaSalida;
    }

    public void set_horaSalida(Date _horaSalida) {
        this._horaSalida = _horaSalida;
    }

    public IndentificadorEntrada get_identificador() {
        return _identificador;
    }

    public void set_identificador(IndentificadorEntrada _identificador) {
        this._identificador = _identificador;
    }

    public ControlAtraccion(int _id, Cliente _cliente, Boleta _boleta, Date _horaSalida, IndentificadorEntrada _identificador) {
        set_id(_id);
        set_cliente(_cliente);
        set_boleta(_boleta);
        set_horaSalida(_horaSalida);
        set_identificador(_identificador);
    }
    public ControlAtraccion(){}
}

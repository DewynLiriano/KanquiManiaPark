package com.example.djc.kanquimaniapark.Clases;

import java.util.Date;
import java.util.IdentityHashMap;
import java.util.List;

/**
 * Created by dewyn on 9/26/2016.
 */

public class Reporte {
    private int _id;
    private List<Factura> _facturas;
    private Date _fecha;
    private List<IndentificadorEntrada> _identificadores;
    private int _clientesTotales;
    private int _totalFacturas;
    private double _totalCierre;
    private int _totalBoletasVendidas;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public List<Factura> get_facturas() {
        return _facturas;
    }

    public void set_facturas(List<Factura> facturas) {
        this._facturas = facturas;
    }

    public Date get_fecha() {
        return _fecha;
    }

    public void set_fecha(Date _fecha) {
        this._fecha = _fecha;
    }

    public List<IndentificadorEntrada> get_identificadores() {
        return _identificadores;
    }

    public void set_identificadores(List<IndentificadorEntrada> _identificadores) {
        this._identificadores = _identificadores;
    }

    public int get_clientesTotales() {
        return _clientesTotales;
    }

    public void set_clientesTotales(int _clientesTotales) {
        this._clientesTotales = _clientesTotales;
    }

    public int get_totalFacturas() {
        return _totalFacturas;
    }

    public void set_totalFacturas(int _totalFacturas) {
        this._totalFacturas = _totalFacturas;
    }

    public double get_totalCierre() {
        return _totalCierre;
    }

    public void set_totalCierre(double _totalCierre) {
        this._totalCierre = _totalCierre;
    }

    public int get_totalBoletasVendidas() {
        return _totalBoletasVendidas;
    }

    public void set_totalBoletasVendidas(int _boletasVendidas) {
        this._totalBoletasVendidas = _boletasVendidas;
    }

    public Reporte(int _id, List<Factura> _facturas, Date _fecha, List<IndentificadorEntrada> _identificadores,
                   int _clientesTotales, int _totalFacturas, double _totalCierre, int _totalBoletasVendidas) {
        set_id(_id);
        set_facturas(_facturas);
        set_fecha(_fecha);
        set_identificadores(_identificadores);
        set_clientesTotales(_clientesTotales);
        set_totalFacturas(_totalFacturas);
        set_totalCierre(_totalCierre);
        set_totalBoletasVendidas(_totalBoletasVendidas);
    }
}

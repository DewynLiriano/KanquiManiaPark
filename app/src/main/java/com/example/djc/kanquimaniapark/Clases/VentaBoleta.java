package com.example.djc.kanquimaniapark.Clases;

/**
 * Created by dewyn on 9/26/2016.
 */

public class VentaBoleta {
    private int _id;
    private int _cantidad;
    private double _totalGanado;
    private Boleta _boleta;

    public int get_id() {
        return _id;
    }

    private void set_id(int _id) {
        this._id = _id;
    }

    public int get_cantidad() {
        return _cantidad;
    }

    public void set_cantidad(int _cantidad) {
        this._cantidad = _cantidad;
    }

    public double get_totalGanado() {
        return _totalGanado;
    }

    public void set_totalGanado(double _totalGanado) {
        this._totalGanado = _totalGanado;
    }

    public Boleta get_boleta() {
        return _boleta;
    }

    public void set_boleta(Boleta _boleta) {
        this._boleta = _boleta;
    }

    public VentaBoleta(int _id, int _cantidad, double _totalGanado, Boleta _boleta) {
        set_id(_id);
        set_cantidad(_cantidad);
        set_totalGanado(_totalGanado);
        set_boleta(_boleta);
    }
    public VentaBoleta(){}
}

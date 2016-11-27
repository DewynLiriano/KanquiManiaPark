package com.example.djc.kanquimaniapark.Clases;
import java.util.List;

/**
 * Created by dewyn on 9/26/2016.
 */

public class Factura {
    private String _id;
    private List<String> _productosSeleccionados;
    private List<String> _atraccionesSeleccionadas;
    private String _fechaEmision;
    private List<String> _especialesID;
    private String _totalDescontado;
    private String _totalFinal;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public List<String> get_productos() {
        return _productosSeleccionados;
    }

    public void set_productosSeleccionados(List<String> _productosSeleccionados) {
        this._productosSeleccionados = _productosSeleccionados;
    }

    public List<String> get_atraccionesSeleccionadas() {
        return _atraccionesSeleccionadas;
    }

    public void set_atraccionesSeleccionadas(List<String> _atraccionesSeleccionadas) {
        this._atraccionesSeleccionadas = _atraccionesSeleccionadas;
    }

    public String get_fechaEmision() {
        return _fechaEmision;
    }

    public void set_fechaEmision(String _fechaEmision) {
        this._fechaEmision = _fechaEmision;
    }

    public List<String> get_especialesID() {
        return _especialesID;
    }

    public void set_especialesID(List<String> _especialesID) {
        this._especialesID = _especialesID;
    }

    public String get_totalDescontado() {
        return _totalDescontado;
    }

    public void set_totalDescontado(String _totalDescontado) {
        this._totalDescontado = _totalDescontado;
    }

    public String get_totalFinal() {
        return _totalFinal;
    }

    public void set_totalFinal(String _totalFinal) {
        this._totalFinal = _totalFinal;
    }

    public Factura(String _id, List<String> _productosSeleccionados, List<String> _atraccionesSeleccionadas,
                   String _fechaEmision, List<String> _especialesID, String _totalDescontado,
                   String _totalFinal) {
        set_id(_id);
        set_productosSeleccionados(_productosSeleccionados);
        set_atraccionesSeleccionadas(_atraccionesSeleccionadas);
        set_fechaEmision(_fechaEmision);
        set_especialesID(_especialesID);
        set_totalDescontado(_totalDescontado);
        set_totalFinal(_totalFinal);
    }

    public Factura(){}
}

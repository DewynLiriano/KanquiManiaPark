package com.example.djc.kanquimaniapark.Clases;

import java.util.HashMap;
import java.util.List;

/**
 * Created by dewyn on 9/26/2016.
 */

public class IdentificadorEntrada {
    private String _id;
    private HashMap<String, String> _atraccionesID;
    private List<String> _colores;
    private String _fecha;

    public String get_id(){
        return _id;
    }

    public void set_id(String _id){
        this._id = _id;
    }

    public HashMap<String, String> get_atracciones() {
        return _atraccionesID;
    }

    public void set_atraccionesID(HashMap<String, String> _atraccionesID) {
        this._atraccionesID = _atraccionesID;
    }

    public List<String> get_colores() {
        return _colores;
    }

    public void set_colores(List<String> _color) {
        this._colores = _color;
    }

    public String get_fecha() {
        return _fecha;
    }

    public void set_fecha(String _fecha) {
        this._fecha = _fecha;
    }

    public IdentificadorEntrada(String _id, HashMap<String, String> _atraccionesID, List<String> _color, String _fecha) {
        set_id(_id);
        set_atraccionesID(_atraccionesID);
        set_colores(_color);
        set_fecha(_fecha);
    }

    public IdentificadorEntrada(){}
}

package com.example.djc.kanquimaniapark.Clases;

import java.util.HashMap;
import java.util.List;

/**
 * Created by dewyn on 9/26/2016.
 */

public class IdentificadorEntrada {
    private String _id;
    private HashMap<String, String> _atraccion_color;
    private String _fecha;

    public String get_id(){
        return _id;
    }

    public void set_id(String _id){
        this._id = _id;
    }

    public HashMap<String, String> get_atracciones() {
        return _atraccion_color;
    }

    public void set_atraccion_color(HashMap<String, String> _atraccion_color) {
        this._atraccion_color = _atraccion_color;
    }

    public String get_fecha() {
        return _fecha;
    }

    public void set_fecha(String _fecha) {
        this._fecha = _fecha;
    }

    public IdentificadorEntrada(String _id, HashMap<String, String> _atraccion_color, String _fecha) {
        set_id(_id);
        set_atraccion_color(_atraccion_color);
        set_fecha(_fecha);
    }

    public IdentificadorEntrada(){}
}

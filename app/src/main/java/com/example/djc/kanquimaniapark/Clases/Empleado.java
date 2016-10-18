package com.example.djc.kanquimaniapark.Clases;

/**
 * Created by dewyn on 10/11/2016.
 */

public class Empleado extends Persona {
    private String _userName;
    private String _contrasena;
    private String _tipo;

    public Empleado(long _id, String _nombre, String _apellido, String _sexo, String _userName, String _contrasena, String _tipo) {
        super(_id, _nombre, _apellido, _sexo);
        this._userName = _userName;
        this._contrasena = _contrasena;
        this._tipo = _tipo;
    }

    public Empleado(String _userName, String _contrasena, String _tipo) {
        this._userName = _userName;
        this._contrasena = _contrasena;
        this._tipo = _tipo;
    }

    public Empleado(){}

    public String get_userName() {
        return _userName;
    }

    public void set_userName(String _userName) {
        this._userName = _userName;
    }

    public String get_contrasena() {
        return _contrasena;
    }

    public void set_contrasena(String _contrasena) {
        this._contrasena = _contrasena;
    }

    public String get_tipo() {
        return _tipo;
    }

    public void set_tipo(String _tipo) {
        this._tipo = _tipo;
    }
}

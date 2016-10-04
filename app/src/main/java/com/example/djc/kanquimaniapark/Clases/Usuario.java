package com.example.djc.kanquimaniapark.Clases;

/**
 * Created by dewyn on 10/3/2016.
 */

public class Usuario {
    private int _id;
    private String _nombre;
    private String _contrasena;

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

    public String get_contrasena() {
        return _contrasena;
    }

    public void set_contrasena(String _correo) {
        this._contrasena = _correo;
    }

    public Usuario(int _id, String _nombre, String _contrasena) {
        set_id(_id);
        set_nombre(_nombre);
        set_contrasena(_contrasena);
    }

    public Usuario() {}
}

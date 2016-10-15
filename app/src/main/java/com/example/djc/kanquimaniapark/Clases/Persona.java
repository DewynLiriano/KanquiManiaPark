package com.example.djc.kanquimaniapark.Clases;

/**
 * Created by dewyn on 10/11/2016.
 */

public class Persona {
    private int _id;
    private String _nombre;
    private String _apellido;
    private String _fechaNacimiento;
    private String _sexo;

    public Persona(int _id, String _nombre, String _apellido, String _fechaNacimiento, String _sexo) {
        this._id = _id;
        this._nombre = _nombre;
        this._apellido = _apellido;
        this._fechaNacimiento = _fechaNacimiento;
        this._sexo = _sexo;
    }

    public Persona() {}

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

    public String get_apellido() {
        return _apellido;
    }

    public void set_apellido(String _apellido) {
        this._apellido = _apellido;
    }

    public String get_fechaNacimiento() {
        return _fechaNacimiento;
    }

    public void set_fechaNacimiento(String _fechaNacimiento) {
        this._fechaNacimiento = _fechaNacimiento;
    }

    public String get_sexo() {
        return _sexo;
    }

    public void set_sexo(String _sexo) {
        this._sexo = _sexo;
    }
}

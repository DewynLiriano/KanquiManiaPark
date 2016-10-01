package com.example.djc.kanquimaniapark.Clases;

import android.graphics.Bitmap;

/**
 * Created by dewyn on 9/26/2016.
 */

public class Cliente {
    private long _id;
    private String _nombre;
    private String _apellido;
    private String _sexo;
    private String _correo;
    private String _fechaCumpleAnos;
    private Bitmap _bitmapFoto;

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
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

    public String get_sexo() {
        return _sexo;
    }

    public void set_sexo(String _sexo) {
        this._sexo = _sexo;
    }

    public String get_correo() {
        return _correo;
    }

    public void set_correo(String _correo) {
        this._correo = _correo;
    }

    public String get_fechaCumpleAnos() {
        return _fechaCumpleAnos;
    }

    public void set_fechaCumpleAnos(String _fechaCumpleAnos) {
        this._fechaCumpleAnos = _fechaCumpleAnos;
    }

    public Bitmap get_bitmapFoto() {
        return _bitmapFoto;
    }

    public void set_bitmapFoto(Bitmap _bitmapFoto) {
        this._bitmapFoto = _bitmapFoto;
    }


    public Cliente(long _id, String _nombre, String _apellido, String _sexo, String _correo, String cumple, Bitmap _bitmapFoto) {
        set_id(_id);
        set_nombre(_nombre);
        set_apellido(_apellido);
        set_sexo(_sexo);
        set_correo(_correo);
        set_fechaCumpleAnos(cumple);
        set_bitmapFoto(_bitmapFoto);
    }

    public Cliente(){}
}

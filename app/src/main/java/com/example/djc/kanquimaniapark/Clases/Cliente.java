package com.example.djc.kanquimaniapark.Clases;

import android.graphics.Bitmap;

/**
 * Created by dewyn on 9/26/2016.
 */

public class Cliente extends Persona {
    private String _correo;
    private String _fechaCumpleAnos;
    private String _numero;
    private Bitmap _bitmapFoto;

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

    public String get_correo() {
        return _correo;
    }

    public void set_correo(String _correo) {
        this._correo = _correo;
    }

    public String get_numero() {
        return _numero;
    }

    public void set_numero(String _numero) {
        this._numero = _numero;
    }

    public Cliente(int _id, String _nombre, String _apellido, String _sexo, String _correo,
                   String _fechaCumpleAnos, String _numero, Bitmap _bitmapFoto) {
        super(_id, _nombre, _apellido, _sexo);
        this._correo = _correo;
        this._fechaCumpleAnos = _fechaCumpleAnos;
        this._numero = _numero;
        this._bitmapFoto = _bitmapFoto;
    }
    public Cliente(){}
}

package com.example.djc.kanquimaniapark.Clases;

import android.graphics.Bitmap;

/**
 * Created by dewyn on 9/26/2016.
 */

public class Cliente extends Persona {
    private String _fechaCumpleAnos;
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

    public Cliente(int _id, String _nombre, String _apellido, String _fechaNacimiento, String _sexo, String _fechaCumpleAnos, Bitmap _bitmapFoto) {
        super(_id, _nombre, _apellido, _fechaNacimiento, _sexo);
        this._fechaCumpleAnos = _fechaCumpleAnos;
        this._bitmapFoto = _bitmapFoto;
    }

    public Cliente(String _fechaCumpleAnos, Bitmap _bitmapFoto) {
        this._fechaCumpleAnos = _fechaCumpleAnos;
        this._bitmapFoto = _bitmapFoto;
    }

    public Cliente(){}
}

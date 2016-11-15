package com.example.djc.kanquimaniapark.Clases;

import android.graphics.Bitmap;
import android.net.Uri;

/**
 * Created by dewyn on 9/26/2016.
 */

public class Cliente extends Persona {
    private String _correo;
    private String _fechaCumpleAnos;
    private String _numero;
    //private Uri _uriFoto;

    public String get_fechaCumpleAnos() {
        return _fechaCumpleAnos;
    }

    public void set_fechaCumpleAnos(String _fechaCumpleAnos) {
        this._fechaCumpleAnos = _fechaCumpleAnos;
    }

    /*public Uri get_uriFoto() {
        return _uriFoto;
    }

    public void set_uriFoto(Uri _uriFoto) {
        this._uriFoto = _uriFoto;
    }*/

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

    public Cliente(String _id, String _nombre, String _apellido, String _sexo, String _correo,
                   String _fechaCumpleAnos, String _numero) {
        super(_id, _nombre, _apellido, _sexo);
        set_correo(_correo);
        set_fechaCumpleAnos(_fechaCumpleAnos);
        set_numero(_numero);
       // set_uriFoto(_uriFoto);
    }
    public Cliente(){}
}

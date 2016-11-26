package com.example.djc.kanquimaniapark.MainActivity;

import com.example.djc.kanquimaniapark.Clases.Atraccion;
import com.example.djc.kanquimaniapark.Clases.Cliente;

import java.io.Serializable;

/**
 * Created by dewyn on 11/22/2016.
 */
public class SelectedAttraction implements Serializable {
    private Atraccion atraccion;
    private Cliente client;

    public Atraccion getAtraccion() {
        return atraccion;
    }

    public void setAtraccion(Atraccion atraccion) {
        this.atraccion = atraccion;
    }

    public Cliente getClient() {
        return client;
    }

    public void setClient(Cliente client) {
        this.client = client;
    }

    public SelectedAttraction(Atraccion atraccion, Cliente client) {
        setAtraccion(atraccion);
        setClient(client);
    }
}

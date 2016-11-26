package com.example.djc.kanquimaniapark.MainActivity;

import com.example.djc.kanquimaniapark.Clases.Atraccion;
import com.example.djc.kanquimaniapark.Clases.Cliente;

/**
 * Created by dewyn on 11/22/2016.
 */

public class AddAtracctionsEvent {
    private Atraccion atraccion;
    private Cliente cliente;

    public AddAtracctionsEvent(Atraccion atraccion, Cliente cliente) {
        setAtraccion(atraccion);
        setCliente(cliente);
    }

    public Atraccion getAtraccion() {
        return atraccion;
    }

    public void setAtraccion(Atraccion atraccion) {
        this.atraccion = atraccion;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}

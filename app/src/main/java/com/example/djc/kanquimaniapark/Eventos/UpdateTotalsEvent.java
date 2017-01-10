package com.example.djc.kanquimaniapark.Eventos;

import com.example.djc.kanquimaniapark.Clases.Especial;

import java.util.List;

/**
 * Created by dewyn on 11/26/2016.
 */
public class UpdateTotalsEvent {
    private List<Especial> gottenEspecials;

    public List<Especial> getGottenEspecials() {
        return gottenEspecials;
    }

    public void setGottenEspecial(List<Especial> gottenEspecial) {
        this.gottenEspecials = gottenEspecial;
    }

    public UpdateTotalsEvent(List<Especial> gottenEspecial) {
        setGottenEspecial(gottenEspecial);
    }
}

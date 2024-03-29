package com.utp.ac.antonio_ng.examen.pktCaja;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.utp.ac.antonio_ng.examen.pktPersona.clsPersona;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class clsCliente {
    public clsCliente(String nombre) {
        persona.setNombre(nombre);
    }

    clsPersona persona = new clsPersona();

    public String get_nombre() {
        return persona.getNombreCompleto();
    }
}

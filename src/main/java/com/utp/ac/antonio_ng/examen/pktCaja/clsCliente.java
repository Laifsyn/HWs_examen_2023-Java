package com.utp.ac.antonio_ng.examen.pktCaja;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class clsCliente {
    @Override
    public String toString() {
        String ret = "";
        try {
            ret = new ObjectMapper().writeValueAsString(this);
        } catch (Exception e) {
            System.out.println(e);
        }
        return ret;
    }

    @JsonUnwrapped
    clsPersona persona;
    @JsonProperty("codigo")
    String id;

    public clsCliente() {
    }
/*
* Ejemplo de serialización de una cadena JSON a com.utp.ac.antonio_ng.examen.pktCaja.clsCliente
* */
    public static void main(String[] args) {
        try {
            clsCliente cliente = clsCliente.new_from_json("{\"código\":\"A1254\", \"cedula\":\"4-1754999\", \"nombre\":\"MARIA\", \"apellido\":\"MENDEZ\", \"teléfono\":64782545}");
            System.out.format("Resultado de serialización y deserialización: " + cliente + "\n");
        } catch (Exception error) {
            System.out.println("Couldn't parse the string");
            System.out.println(error);
        } finally {
            System.out.println("Finalizamos");
        }
    }
    static public clsCliente new_from_json(String json_string) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        clsCliente cliente = new clsCliente();
        cliente.persona = mapper.readValue(json_string, clsPersona.class);
        cliente.id = mapper.readValue(json_string, clsCliente.class).id;
        return cliente;
    }

    public clsPersona getPersona() {
        return persona;
    }

    public String getId() {
        return id;
    }
}

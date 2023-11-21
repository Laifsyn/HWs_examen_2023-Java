package com.utp.ac.antonio_ng.examen.pktCaja.pktProducto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.utp.ac.antonio_ng.examen.pktCaja.clsCliente;
import com.utp.ac.antonio_ng.examen.pktCaja.clsFactura;
import com.utp.ac.antonio_ng.examen.pktInventario.clsLoadedInventario;
import com.utp.ac.antonio_ng.examen.pktResourceFiles.FileResource;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class clsArticulo {
    public static void main(String[] args) {
        generar_datos();
    }

    public static List<clsArticulo> generar_datos() {
        // Leer y deserializar los datos del archivo
        List<clsArticulo> lista_articulos = List.of();
        ObjectMapper mapper = new ObjectMapper();
        try {
            File input_stream = new FileResource().getFileFromResource("inventario.json");
            lista_articulos = Arrays.asList(mapper.treeToValue(mapper.readTree(input_stream).get("data"), clsArticulo[].class));
        } catch (Exception e) {
            System.out.println("Error leyendo los datos de \"inventario.json\".");
            System.exit(1);
        }
        lista_articulos.forEach(System.out::println);
        System.out.println("\nFin de la deserialización.....\n");

        return lista_articulos;
    }

    @Override
    public String toString() {
        String ret = "";
        try {
            ret = new ObjectMapper().writeValueAsString(this);
        } catch (Exception e) {
            System.out.println("Falla en deserializando el objeto:\n" + e);
        }
        return ret;
    }

    @JsonProperty("CODIGO")
    String codigo;
    @JsonProperty("DESCRIPCION")
    String descripcion;
    @JsonProperty("IMPUESTO")
    int itbms;
    @JsonIgnore
    public boolean EXENTO;

    public int retrieve_itbms() {
        return itbms;
    }

    public void setItbms(String itbms) {
        int ret;
        switch (itbms) {
            case "1" -> ret = 10;
            case "7" -> ret = 7;
            default -> ret = 0;
        }
        this.itbms = ret;
        if (this.itbms == 0) EXENTO = true;
    }

    public String getItbms() {
        String ret;
        switch (itbms) {
            case 10 -> ret = "10";
            case 7 -> ret = "7";
            default -> ret = "0";
        }
        return ret;
    }

    @JsonProperty("CANTIDAD EXISTENTE")
    int cantidad;
    @JsonProperty("PRECIO UNITARIO")
    float costo;
    @JsonProperty("PRECIO DE VENTA")
    float precio;


}

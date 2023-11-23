package com.utp.ac.antonio_ng.examen.pktCaja.pktProducto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonIgnoreProperties

public class clsProducto implements Cloneable {
    @JsonUnwrapped
    clsArticulo articulo;

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

    @JsonIgnore
    public String get_codigo_articulo() {
        return articulo.codigo;
    }

    @JsonIgnore
    public float get_venta() {
        return articulo.precio;
    }

    @JsonIgnore
    public float get_itbms() {
        return (float) articulo.retrieve_itbms() / 100;
    }

    @JsonIgnore
    public String get_description() {
        return articulo.descripcion;
    }

    @JsonIgnore
    public int en_inventario() {
        return articulo.cantidad;
    }

    @JsonProperty("Cantidad de Instancia")
    Integer cantidad = 0; // La cantidad del artículo que tiene esta instancia

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * Devuelve una copia de la clase, pero con una cantidad definida
     *
     * @return
     */
    public clsProductoInmutable fetch_instance(int cantidad) {
        ObjectMapper objectMapper = new ObjectMapper();
        articulo.cantidad -= cantidad;
        setCantidad(articulo.cantidad);
        clsProductoInmutable ret = null;
        try {
            String producto_as_string = objectMapper.writeValueAsString(this);
            ret = objectMapper.readValue(producto_as_string, clsProductoInmutable.class);
        } catch (Exception e) {
            System.out.printf("Hubo un problema convirtiendo el objeto con codigo %s\n", this.get_codigo_articulo());
            System.exit(1);
        }
        ret.setCantidad(cantidad);
        return ret;
    }
}

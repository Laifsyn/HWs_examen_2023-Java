package com.utp.ac.antonio_ng.examen.pktCaja.pktProducto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonIgnoreProperties
public class clsProducto implements Cloneable {
    @JsonUnwrapped
    @JsonInclude
    clsArticulo articulo;

    @Override
    public String toString() {
        return articulo.toString();
    }

    public String get_codigo_articulo() {
        return articulo.codigo;
    }

    public float get_venta() {
        return articulo.precio;
    }

    public float get_itbms() {
        return (float) articulo.retrieve_itbms() / 100;
    }

    public String get_description() {
        return articulo.descripcion;
    }

    public int en_inventario() {
        return articulo.cantidad;
    }

    Integer cantidad = 0; // La cantidad del artículo que tiene esta instancia

    public int get_cantidad() {
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
        clsProductoInmutable ret = null;
        try {
            String producto_as_string = objectMapper.writeValueAsString(this);
            ret = objectMapper.readValue(producto_as_string, clsProductoInmutable.class);
        } catch (Exception e) {
            System.out.printf("Hubo un problema convirtiendo el objeto con codigo %s\n", this.get_codigo_articulo());
            System.exit(1);
        }
        ret.setCantidad(cantidad);
        articulo.cantidad -= cantidad;
        setCantidad(articulo.cantidad);
        return ret;
    }
}

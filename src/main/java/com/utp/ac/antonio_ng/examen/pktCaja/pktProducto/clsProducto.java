package com.utp.ac.antonio_ng.examen.pktCaja.pktProducto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

@JsonIgnoreProperties
public class clsProducto implements Cloneable {
    @JsonUnwrapped
    @JsonInclude
    clsArticulo articulo;

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

    public int en_reserva() {
        return articulo.cantidad;
    }

    Integer cantidad = 0; // La cantidad del artículo que tiene esta instancia

    public int get_cantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    protected Object clone() {
        Object clone = null;
        try {
            clone = super.clone();
        } catch (Exception e) {
            System.out.println("Falló la clonación.....\n" + e);
        }
        return clone;
    }

    /**
     * Devuelve una copia de la clase, pero con una cantidad definida
     *
     * @return
     */
    public clsProducto fetch_instance(int cantidad) {
        clsProducto ret = (clsProducto) clone();
        articulo.cantidad -= cantidad;
        ret.cantidad = cantidad;
        return ret;
    }
}

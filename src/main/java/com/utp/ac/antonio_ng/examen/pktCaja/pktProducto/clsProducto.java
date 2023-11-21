package com.utp.ac.antonio_ng.examen.pktCaja.pktProducto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.vavr.control.Validation;

@JsonIgnoreProperties
public class clsProducto {
    @JsonUnwrapped
    @JsonInclude
    clsArticulo articulo;

    /**
     * Interfaz Interna
     *
     * @return cantidad entera del producto
     */
    private int fetch(int amount) {
        return articulo.fetch(amount);
    }

    /**
     * Intenta restar la cantidad del inventario. En caso de no poder, devuelve la cantidad positiva que quede o 0 pero envuelto en un Invalid
     */
    public Validation<Integer, Integer> try_fetch(int amount) {
        Validation<Integer, Integer> ret;
        if (articulo.en_inventario() >= amount) {
            ret = Validation.valid(fetch(amount));
        } else if (articulo.en_inventario() < 0)
            ret = Validation.invalid(0);
        else
            ret = Validation.invalid(articulo.en_inventario());
        return ret;
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
}

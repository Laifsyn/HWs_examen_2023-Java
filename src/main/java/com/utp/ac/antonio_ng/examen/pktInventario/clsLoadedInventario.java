package com.utp.ac.antonio_ng.examen.pktInventario;

import com.utp.ac.antonio_ng.examen.pktCaja.pktProducto.clsProducto;
import com.utp.ac.antonio_ng.examen.pktResourceFiles.FileResource;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.control.Validation;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class clsLoadedInventario {
    HashMap<String, clsProducto> inventario = new HashMap<>();
    List<clsProducto> lista_producto;

    /**
     * Devuelve la cantidad en inventario si hay registro
     */
    public Optional<Integer> query(String codigo) {
        if (!inventario.containsKey(codigo))
            return Optional.empty();
        return Optional.of(inventario.get(codigo).get_cantidad());
    }

    /**
     * Método para usar cuando se puede garantizar la cantidad que queda en inventario.
     */
    public clsProducto fetch(String codigo, int a_substraer) {
        return inventario.get(codigo).fetch_instance(a_substraer);
    }

    /**
     * En caso de Ok:
     * Retorna una copia del Objeto con la cantidad pedida.
     * En caso de Error:
     * Retorna nada si no encuentra el producto en la base de datos.
     * Retorna la cantidad disponible si resulta haber menos de lo pedido.
     */
    public Validation<Optional<Integer>, clsProducto> try_fetch(String codigo, int amount) {
        if (!inventario.containsKey(codigo))
            return Validation.invalid(Optional.empty());

        Validation<Optional<Integer>, clsProducto> ret;
        clsProducto producto = inventario.get(codigo);
        int en_inventario = producto.get_cantidad();
        if (en_inventario >= amount) {
            ret = Validation.valid(fetch(codigo, amount));
        } else if (en_inventario <= 0)
            ret = Validation.invalid(Optional.of(0));
        else
            ret = Validation.invalid(Optional.of(en_inventario));
        return ret;
    }

    public clsLoadedInventario() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            File input_stream = new FileResource().getFileFromResource("lista_productos.json");
            lista_producto = Arrays.asList(mapper.treeToValue(mapper.readTree(input_stream).get("data"), clsProducto[].class));
        } catch (Exception e) {
            System.out.println("Error al leer los datos.......\n" + e);
            System.exit(400);
        }
        for (clsProducto producto : lista_producto) {
            producto.setCantidad(producto.en_reserva());
            inventario.put(producto.get_codigo_articulo(), producto);
        }
    }
}

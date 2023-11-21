package com.utp.ac.antonio_ng.examen.pktCaja;

import com.utp.ac.antonio_ng.examen.pktCaja.pktProducto.clsProducto;
import io.vavr.collection.List;

import java.util.HashMap;

public class clsFactura {
    clsCliente cliente;
    HashMap<String, List<clsProducto>> productos;

    public clsFactura(clsCliente _cliente) {
        cliente = _cliente;
    }
    public void append_producto(clsProducto producto) {
        String codigo = producto.get_codigo_articulo();
        List<clsProducto> lista = productos.getOrDefault(codigo, List.empty());
        lista.append(producto);
        productos.put(codigo, lista);
    }

}

package com.utp.ac.antonio_ng.examen.pktCaja.pktProducto;

public class clsProductoInmutable extends clsProducto {
    public clsProductoInmutable fetch_instance(int _cantidad) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int en_inventario() throws UnsupportedOperationException { // Elimina el acceso accidental al inventario
        throw new UnsupportedOperationException();
    }
}

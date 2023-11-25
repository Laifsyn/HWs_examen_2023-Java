package com.utp.ac.antonio_ng.examen;

import com.utp.ac.antonio_ng.examen.pktCaja.pktProducto.clsCobrar;
import com.utp.ac.antonio_ng.examen.pktInventario.clsLoadedInventario;
import com.utp.ac.antonio_ng.examen.pktPasswordAntonioNg.clsPasswordAntonioNg;

import javax.swing.JOptionPane;

public class Main {

    public static void main(String[] args) {
        Main entry = new Main();
    }

    public Main() {
        if (!clsPasswordAntonioNg.esperar_inicio_sesion()) {
            JOptionPane.showMessageDialog(null, "Inicio de sesión fallada. El programa se cerrará");
            System.exit(0);
        }
        System.out.println("Hola. Acabamos de leer. Inicio de sesión existosa");
        clsCobrar.facturar_al_cliente(new clsLoadedInventario());
        JOptionPane.showMessageDialog(null, "Finalizamos el programa");
        System.out.println("Acabamos con la ejecución del programa.");
        System.exit(0);
    }


}

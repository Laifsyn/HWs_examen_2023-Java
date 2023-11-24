package com.utp.ac.antonio_ng.examen.pktCaja;

import com.utp.ac.antonio_ng.examen.pktCaja.pktProducto.clsProductoInmutable;
import com.utp.ac.antonio_ng.examen.pktInventario.clsLoadedInventario;
import io.vavr.Tuple2;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class clsFactura {
    public static void main(String[] args) {
        clsLoadedInventario inventario = new clsLoadedInventario();
        ArrayList<clsProductoInmutable> productos = new ArrayList<>();
        String[] codigos = new String[]{"XYZ100", "ABC000", "QWE200", "RST300", "DEF400"};
        for (String codigo : codigos)
            productos.add(inventario.fetch(codigo, 3));
        clsFactura reporte = clsFactura.generarReporte(io.vavr.Tuple.of(new clsCliente("Nombre de Cliente"),
                productos));
    }

    clsCliente cliente;
    ArrayList<clsProductoInmutable> productos;

    protected clsFactura(Tuple2<clsCliente, ArrayList<clsProductoInmutable>> datos) {
        cliente = datos._1;
        productos = datos._2;
        generar_reporte();
    }

    public static clsFactura generarReporte(Tuple2<clsCliente, ArrayList<clsProductoInmutable>> datos) {
        clsFactura reporte = new clsFactura(datos);
        return reporte;
    }

    void generar_reporte() {
        JFrame frame = new JFrame("Reporte de " + cliente.get_nombre());
        frame.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridwidth = 5;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1;
        constraints.weighty = 1;
        acoplar_encabezado(frame, constraints);
        constraints.insets = new Insets(0, 20, 0, 20);
        constraints.gridy = 1;
        acoplar_datos_de_productos(frame, constraints);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setMinimumSize(frame.getPreferredSize());
    }

    void acoplar_encabezado(JFrame frame, GridBagConstraints frame_constraints) {
        JPanel panel = new JPanel(new GridBagLayout());
        JPanel panel_superior = new JPanel(new GridLayout(10, 1, 0, 0));
        JPanel panel_inferior = new JPanel(new GridLayout(1, 2, 5, 5));
        panel_superior.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.CYAN, Color.RED));
        panel_superior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel_superior.setBounds(500, 500, 200, 200);
        acoplar_JLabel("UNIVERSIDAD TECNOLOGICA DE PANAMA", panel_superior);
        acoplar_JLabel("CENTRO REGIONAL DE CHIRIQUI", panel_superior);
        acoplar_JLabel("", panel_superior);
        acoplar_JLabel("LICENCIATURA EN DESARROLLO DE SOFTWARE", panel_superior);
        acoplar_JLabel("EXAMEN SEMESTRAL", panel_superior);
        acoplar_JLabel("", panel_superior);
        acoplar_JLabel("ESTUDIANTE: Antonio Ng  \t  PROFESOR: EDUARDO BEITIA", panel_superior);
        acoplar_JLabel("", panel_superior);
        acoplar_JLabel("REPORTE DE VENTAS", panel_superior);
        acoplar_JLabel("", panel_superior);
        acoplar_JLabel("NOMBRE DEL CLIENTE: " + cliente.get_nombre(), panel_inferior);
        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MMM/yyyy", Locale.forLanguageTag(
                "es-ES")));
        acoplar_JLabel("FECHA DE VENTA: " + fecha, panel_inferior);

        panel_superior.setPreferredSize(new Dimension(800, 200));
        panel_inferior.setPreferredSize(new Dimension(800, 40));
        panel.setPreferredSize(new Dimension(800, 240));
        GridBagConstraints constraints = new GridBagConstraints();
        panel.add(panel_superior, constraints);
        constraints.gridx++;
        panel.add(panel_inferior, constraints);
        frame.add(panel, frame_constraints);

    }

    void acoplar_JLabel(String texto, JPanel panel) {
        panel.add(new JLabel(texto) {{
            setHorizontalAlignment(JLabel.CENTER);
        }}, Component.CENTER_ALIGNMENT);
    }

    void acoplar_datos_de_productos(JFrame frame, GridBagConstraints frame_constraints) {
        JPanel panel = new JPanel(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(0, 10, 0, 10);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;

        acoplar_titulo(panel, constraints, new String[]{"Codigo", "Descripcion", "Precio de Venta", "Cantidad",
                "Valor"});
        HashMap<Integer, Double> totales_itbms = new HashMap<>();
        totales_itbms.put(7, 0d);
        totales_itbms.put(10, 0d);
        double subtotal = 0; // Total sin el impuesto
        for (clsProductoInmutable producto : productos) {
            String codigo = producto.get_codigo_articulo();
            String descripcion = producto.get_description();
            String venta = convertir_a_formato_display(producto.get_venta());
            String cantidad = String.valueOf(producto.get_cantidad());

            double total = producto.get_venta() * producto.get_cantidad();
            totales_itbms.computeIfPresent(producto.retrieve_itbms(), (key, value) -> {
                value += producto.get_itbms() * total;
                return value;
            });
            subtotal += total;
            String valor = convertir_a_formato_display(total);
            acoplar_fila(panel, constraints, new String[]{codigo, descripcion, venta, cantidad, valor});
        }
        double total = subtotal;
        double total_impuestos = 0d;

        // Insertar SubTotal
        insertar_separador(panel, constraints);
        acoplar_resultado(panel, constraints, new String[]{"Subtotal: ", convertir_a_formato_display(subtotal)});

        // Acumulamos el impuesto, y imprimimos cada línea de impuesto
        insertar_separador(panel, constraints);
        for (Map.Entry<Integer, Double> entry : totales_itbms.entrySet()) {
            total_impuestos += entry.getValue();
            acoplar_resultado(panel, constraints, new String[]{String.format("%d%%", entry.getKey()),
                    convertir_a_formato_display(entry.getValue())});
        }
        total += total_impuestos;
        insertar_separador(panel, constraints);
        // Insertar Total de Impuestos
        acoplar_resultado(panel, constraints, new String[]{"Total de Impuestos: ",
                convertir_a_formato_display(total_impuestos)});
        insertar_separador(panel, constraints);
        insertar_separador(panel, constraints);
        // Insertar Total
        acoplar_resultado(panel, constraints, new String[]{"TOTAL A PAGAR: ", convertir_a_formato_display(total)});
        acoplar_resultado(panel, constraints, new String[]{" ", ""});


        frame.add(panel, frame_constraints);
    }

    void acoplar_titulo(JPanel panel, GridBagConstraints constraints, String[] fila) {
        int start_x = constraints.gridx;

        constraints.anchor = GridBagConstraints.CENTER;
        for (String celda : fila) {
            JLabel label = new JLabel(celda);
            panel.add(label, constraints);
            constraints.gridx++;
        }
        constraints.gridy++;
        constraints.gridx = start_x;
    }

    void acoplar_fila(JPanel panel, GridBagConstraints constraints, String[] fila) {
        int start_x = constraints.gridx;
        for (String celda : fila) {
            int alignment_constant = switch (constraints.gridx) {
                case 0, 1 -> SwingConstants.LEFT;
                case 3 -> SwingConstants.CENTER;
                default -> SwingConstants.RIGHT;
            };
            int width = switch (constraints.gridx) {
//                case 0, 4, 3 -> 100;
                case 1 -> 260;
                default -> 100;
            };
            JLabel label = new JLabel(celda);
            label.setPreferredSize(new Dimension(width, 25));
            label.setHorizontalAlignment(alignment_constant);
            panel.add(label, constraints);
            constraints.gridx++;
        }
        constraints.gridy++;
        constraints.gridx = start_x;
    }

    void insertar_separador(JPanel panel, GridBagConstraints constraints) {
        int start_x = constraints.gridx;
        for (String celda : new String[]{"", "", "", "", "-------------------"}) {
            JLabel label = new JLabel(celda);
            label.setPreferredSize(new Dimension(100, 5));
            constraints.anchor = GridBagConstraints.EAST;
            label.setFont(label.getFont().deriveFont(16f));
            panel.add(label, constraints);
            constraints.gridx++;
        }
        constraints.gridy++;
        constraints.gridx = start_x;
    }

    void acoplar_resultado(JPanel panel, GridBagConstraints constraints, String[] fila) {
        int start_x = constraints.gridx;
        for (String celda : new String[]{"", "", "", fila[0], fila[1]}) {
            JLabel label = new JLabel(celda);
            constraints.anchor = GridBagConstraints.EAST;
            label.setFont(new Font("Verdana", Font.BOLD, 12));
            panel.add(label, constraints);
            constraints.gridx++;
        }
        constraints.gridy++;
        constraints.gridx = start_x;

    }

    String convertir_a_formato_display(double numero) {
        StringBuilder resultado_de_conversion = new StringBuilder();
        Formatter formatter = new Formatter(resultado_de_conversion, Locale.US);
        formatter.format("%(,.2f", numero);
        return resultado_de_conversion.toString();
    }
}

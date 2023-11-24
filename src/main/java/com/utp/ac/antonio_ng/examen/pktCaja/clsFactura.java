package com.utp.ac.antonio_ng.examen.pktCaja;

import com.utp.ac.antonio_ng.examen.pktCaja.pktProducto.clsProductoInmutable;
import com.utp.ac.antonio_ng.examen.pktInventario.clsLoadedInventario;
import io.vavr.Tuple2;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
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
//        frame.setLayout(new GridLayout(2, 1));
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
//        constraints.weightx = 1;
//        constraints.weighty = 1;

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
        clsTablaTotales tabla = new clsTablaTotales();

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEtchedBorder(Color.CYAN, Color.RED));

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(0, 10, 0, 10);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;


        HashMap<Integer, Double> totales_itbms = new HashMap<Integer, Double>();
        totales_itbms.put(7, 0d);
        totales_itbms.put(10, 0d);
        double total_neto = 0;
        for (clsProductoInmutable producto : productos) {
            String codigo = producto.get_codigo_articulo();
            String descripcion = producto.get_description();
            String venta = String.valueOf(producto.get_venta());
            String cantidad = String.valueOf(producto.get_cantidad());

            double total = producto.get_venta() * producto.get_cantidad();
            totales_itbms.computeIfPresent(producto.retrieve_itbms(), (key, value) -> {
                value += producto.get_itbms() * total;
                return value;
            });
            total_neto += total;

            String valor = convertir_a_formato_display(total);
            tabla.insertar_fila(new String[]{codigo, descripcion, venta, cantidad, valor});
        }
        double total = total_neto;
        ArrayList<String> impuestos = new ArrayList<>();
        // Calculamos el total de la venta, y formateamos el resultado
        for (Map.Entry<Integer, Double> entry : totales_itbms.entrySet()) {
            total += entry.getValue();
            impuestos.add(String.format("%d%%", entry.getKey()));

            impuestos.add(convertir_a_formato_display(entry.getValue()));
            System.out.println("1-) " + String.format("%d%%", entry.getKey()));
            System.out.println("2-) " + convertir_a_formato_display(entry.getValue()));
        }
        frame.add(tabla.text_area, frame_constraints);
    }

    String convertir_a_formato_display(double numero) {
        StringBuilder resultado_de_conversion = new StringBuilder();
        Formatter formatter = new Formatter(resultado_de_conversion, Locale.US);
        formatter.format("%(,.2f", numero);
        System.out.println("Entrada: " + numero);
        return resultado_de_conversion.toString();
    }
}

class clsTablaTotales {
    String[] columnas = new String[]{"Código", "Descripcion", "Precio de Venta", "Cantidad", "Valor"};
    Integer[] columnas_width = new Integer[]{20, 400, 40, 20, 60};
    JTextArea text_area;

    clsTablaTotales() {
        text_area = new JTextArea();
//        text_area.setEditable(false);
        text_area.setColumns(80);
        text_area.setTabSize(5);
        Font font = new Font("Console", Font.BOLD,14);
        text_area.setFont(font);
    }

    void insertar_fila(String[] fila) {
        ((DefaultTableModel) tabla.getModel()).addRow(fila);
    }

    JTable tabla = new JTable() {
        {
            DefaultTableModel model = new DefaultTableModel();
            // Agregamos los nombres de las columnas
            for (String name : columnas)
                model.addColumn(name);

            // Actualizamos el modelo de la tabla
            setModel(model);

            // Editamos la anchura de descripción
            for (int index = 0; index < columnas.length; index++)
                getColumnModel().getColumn(index).setPreferredWidth(columnas_width[index]);
        }


        final DefaultTableCellRenderer render_right = new DefaultTableCellRenderer();
        final DefaultTableCellRenderer render_left = new DefaultTableCellRenderer();
        final DefaultTableCellRenderer render_middle = new DefaultTableCellRenderer();

        {
            render_right.setHorizontalAlignment(SwingConstants.RIGHT);
            render_left.setHorizontalAlignment(SwingConstants.LEFT);
            render_middle.setHorizontalAlignment(SwingConstants.CENTER);
        }

        @Override
        public TableCellRenderer getCellRenderer(int row, int col) {
            return switch (col) {
                case 1, 2 -> render_left;
                case 3, 5 -> render_right;
                default -> render_middle;
            };
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
}


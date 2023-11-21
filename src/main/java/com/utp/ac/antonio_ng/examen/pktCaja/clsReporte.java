package com.utp.ac.antonio_ng.examen.pktCaja;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.utp.ac.antonio_ng.examen.pktResourceFiles.FileResource;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class clsReporte {
    public clsReporte(clsCliente cliente, clsFactura[] facturas) {
        new TemplateReporte(cliente, facturas);
    }
}

class TemplateReporte {
    final String Titulo_universidad = new String("UNIVERSIDAD TECNOLÓGICA DE PANAMA".getBytes(), StandardCharsets.UTF_8);
    final String Sede = new String("CENTRO REGIONAL DE CHIRIQUI".getBytes(), StandardCharsets.UTF_8);
    final String Facultad = new String("INGENIERIA DE SISTEMAS COMPUTACIONALES".getBytes(), StandardCharsets.UTF_8);
    final String Carrera = new String("LICENCIATURA EN INGENIERIA DE SISTEMAS E INFORMACION".getBytes(), StandardCharsets.UTF_8);
    final String Grupo = "2IL112";
    final ArrayList<JLabel> Introduccion = new ArrayList<>() {
        {
            JLabel label = new JLabel();
            label.setText(String.format("<HTML><bold>%s</bold></HTML>", Titulo_universidad));
            add(label);
            label = new JLabel(); //<></>
            label.setText(String.format("<HTML><bold>%s</bold></HTML>", Sede));
            add(label);
            add(new JLabel());
            label = new JLabel(); //<></>
            label.setText(String.format("<HTML><bold>FACULTAD DE %s</bold></HTML>", Facultad));
            add(label);
            label = new JLabel(); //<></>
            label.setText(String.format("<HTML><bold>CARRERA: %s</bold></HTML>", Carrera));
            add(label);
            label = new JLabel(); //<></>
            label.setText(String.format("<HTML>GRUPO: %s</HTML>", Grupo));
            add(label);
            add(new JLabel("-"));
            label = new JLabel(); //<></>
            label.setText("Analisis de Antiguedad de Cuenta");
            add(label);
        }
    };
    final String Estudiante = "Antonio Ng";
    final String Docente = "Prof. Eduardo Beitia";
    FacturaTable table;
    clsCliente cliente;
    clsFactura[] facturas;

    JPanel panel = new JPanel(new GridBagLayout() {{
    }});
    LocalDateTime local_date = LocalDateTime.now();

    CountDownLatch latch = new CountDownLatch(1);

    TemplateReporte(clsCliente cliente, clsFactura[] factura) {
        this.cliente = cliente;
        this.facturas = factura;
        JFrame frame = new JFrame("Hola Reporte");
        frame.setLayout(new GridLayout(1, 1, 3, 3));
        frame.setMinimumSize(new Dimension(700, 400));
        add_introduccion();


        frame.getContentPane().add(new JScrollPane(panel) {{
            setMinimumSize(new Dimension(500, 350));
        }});
        frame.setVisible(true);
        frame.setSize(800, 768);
        frame.pack();
        try {
            latch.await();
        } catch (Exception ignored) {
        }

        frame.dispose();
//        frame.getContentPane().add(panel);
    }

    GridBagConstraints constraints = new GridBagConstraints() {{
        insets = new Insets(5, 0, 5, 0);
        gridx = 0;
        gridy = 0;
    }};

    void add_introduccion() {
        constraints.anchor = GridBagConstraints.NORTH;
        constraints.gridwidth = 2;
        for (JLabel label : Introduccion) {
            panel.add(label, constraints);
            constraints.gridy++;
        }
        constraints.gridwidth = 1;
        add_datos();
    }

    void add_datos() {
        constraints.gridx = 0;
        constraints.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel("ESTUDIANTE: " + Estudiante), constraints);
        constraints.anchor = GridBagConstraints.EAST;
        constraints.gridx++;
        panel.add(new JLabel("DOCENTE: " + Docente), constraints);
        constraints.gridy++;
        constraints.gridx = 0;
        constraints.anchor = GridBagConstraints.WEST;
        String date = local_date.format(DateTimeFormatter.ofPattern("EEEE dd 'de' MMMM 'de' yyyy", Locale.forLanguageTag("es-ES")));
        panel.add(new JLabel("FECHA: " + date), constraints);
        constraints.gridy++;
        panel.add(new JLabel("CODIGO DE CLIENTE: " + cliente.getId()), constraints);
        System.out.println(cliente);
        constraints.gridx++;
        constraints.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("NOMBRE: " + cliente.getPersona().getNombreCompleto()), constraints);
        constraints.anchor = GridBagConstraints.NORTH;
        constraints.gridy++;
        constraints.gridx = 0;
        constraints.gridwidth = 2;
        add_tablas();
        constraints.gridy++;
        add_button();
    }

    void add_tablas() {
        table = new FacturaTable();
        for (clsFactura factura : facturas)
            table.insert(factura);
        table.insert_table_ending();
        Insets old_insets = constraints.insets;
        constraints.insets = new Insets(0, 0, 0, 0);
        panel.add(table.getTableHeader(), constraints);
        constraints.gridy++;
        panel.add(table, constraints);
        constraints.insets = old_insets;
    }

    void add_button() {
        JButton bt = new JButton("OK") {{
            addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    latch.countDown();
                }
            });
        }};

        panel.add(bt, constraints);
    }
/**
 * Genera el reporte completo - Como datos de pruebas, usa un com.utp.ac.antonio_ng.examen.pktCaja.clsCliente, y procesa todos los datos en el com.utp.ac.antonio_ng.examen.pktCaja.clsFactura[] brindado
 * */
    public static void main(String[] args) {

        ObjectMapper mapper = new ObjectMapper();
        File input_stream;
        // Initializes the input data  for the test
        try {
            input_stream = new FileResource().getFileFromResource("lista_de_facturas/source_data.json");
            clsFactura[] lista_facturas = mapper.treeToValue(mapper.readTree(input_stream).get("data"), clsFactura[].class);
            input_stream = new FileResource().getFileFromResource("lista_de_clientes/source_data.json");
            clsCliente[] lista_clientes = mapper.treeToValue(mapper.readTree(input_stream).get("data"), clsCliente[].class);

            TemplateReporte t = new TemplateReporte(lista_clientes[0], lista_facturas);
        } catch (Exception e) {
            System.out.println("No se pudo hacer la operacion en " + clsReporte.class.getName());
            System.out.println(e);
        }
    }
}


class FacturaTable extends JTable {
    Object[] Totales = new Object[]{null, "Totales:", new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0)};

    void insert(clsFactura factura) {
        Object[] other = get_deuda(factura);

        Object[] row = new Object[]{factura.getFecha(), factura.getNumero_factura(), factura.getSaldo(), other[0], other[1], other[2], other[3], other[4]};
        for (int index = 2; index < row.length; index++) {
            if (row[index] != null) Totales[index] = ((BigDecimal) Totales[index]).add((BigDecimal) row[index]);
        }
        model.addRow(row);
    }

    void insert(Object[] row) {
        model.addRow(row);
    }


    public void setLista_facturas(List<clsFactura> lista_facturas) {
        this.lista_facturas = lista_facturas;
    }

    public void insert_table_ending() {
        // Inserting an empty line
        insert(new Object[]{null, null, null, null, null, null, null, null});
        setRowHeight(getRowCount() - 1, 4);
        // Inserting totales
        System.out.println(Arrays.toString(Totales));
        insert(Totales);
    }

    final DefaultTableCellRenderer renderRight = new DefaultTableCellRenderer() {{
        setHorizontalAlignment(SwingConstants.RIGHT);
    }};
    final DefaultTableCellRenderer renderLeft = new DefaultTableCellRenderer() {{
        setHorizontalAlignment(SwingConstants.LEFT);
    }};
    @Override
    public TableCellRenderer getCellRenderer(int row, int column) {
        if (column == 0)
            return renderRight;
        Object obj = getValueAt(row, column);
        if (obj != null) {
            String cell = getValueAt(row, column).toString();
            try {
                Double.valueOf(cell);
            } catch (Exception ignored) {
                return renderLeft;
            }
        }
        return renderRight;
    }

    List<clsFactura> lista_facturas;
/*
* Genera la tabla de información de los datos. Usa un com.utp.ac.antonio_ng.examen.pktCaja.clsFactura[] como argumento
* */
    public static void main(String[] args) throws URISyntaxException, IOException {

        ObjectMapper mapper = new ObjectMapper();
        File input_stream;
        // Initializes the input data  for the test
        input_stream = new FileResource().getFileFromResource("lista_de_facturas/source_data.json");
        clsFactura[] lista_facturas = mapper.treeToValue(mapper.readTree(input_stream).get("data"), clsFactura[].class);
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FacturaTable table = new FacturaTable();
        for (clsFactura factura : lista_facturas) {
            table.insert(factura);
        }

        table.insert_table_ending();

        JScrollPane scrollPane = new JScrollPane(table);
        frame.getContentPane().add(scrollPane);
        frame.setVisible(true);
        frame.setSize(968, 600);
        frame.setMinimumSize(new Dimension(600, 100));
    }

    Object[] get_deuda(final clsFactura factura) {
        long time_diff = TimeUnit.DAYS.convert(factura.compare_date(Date.from(Instant.now())), TimeUnit.MILLISECONDS);
        Object[] ret = new Object[5];
        int index = 0;

        for (Integer dia : DIAS) {
            if (time_diff < -dia) {
                ret[DIAS.length - index - 1] = factura.getSaldo();
                break;
            }
            index++;
        }
        if (time_diff > 30) ret[0] = null;
        return ret;
    }

    Integer[] DIAS;
    String[] HEADERS;
    DefaultTableModel model;

    public FacturaTable() {
        super(new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        });
        model = (DefaultTableModel) super.getModel();
        DIAS = new Integer[]{120, 90, 60, 30, 0};
        HEADERS = new String[]{"FECHA", "NUM.FACTURA", "MONTO", "MES CORR.", "30+Dias", "60+Dias", "90+Dias", "120+Dias"};
        ArrayList<TableColumn> columns = new ArrayList<>();
        for (String header : HEADERS)
            model.addColumn(header);
    }

}
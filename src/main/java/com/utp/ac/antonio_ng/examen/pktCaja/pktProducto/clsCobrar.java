package com.utp.ac.antonio_ng.examen.pktCaja.pktProducto;

import com.utp.ac.antonio_ng.examen.pktCaja.clsCliente;
import com.utp.ac.antonio_ng.examen.pktInventario.clsLoadedInventario;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.control.Try;

import javax.swing.*;
import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.List;

/**
 * Code Blocking Class - La finalización del i/o genera una lista de Productos
 */
public class clsCobrar {

    public static void main(String[] args) {
        System.setProperty("file.encoding", "UTF-16");
//        List<clsArticulo> e = clsArticulo.generar_datos();
        clsLoadedInventario inventario = new clsLoadedInventario();
//        System.out.println();
        clsCobrar.facturar_al_cliente(inventario);
    }


    public static Tuple2<clsCliente, List<clsProductoInmutable>> facturar_al_cliente(clsLoadedInventario inventario) {
        CountDownLatch latch = new CountDownLatch(1);
        clsCobrar GUI = new clsCobrar(inventario, latch);
        try {
            latch.await();
        } catch (Exception unexpected) {
            System.out.println("Unexpected error: " + unexpected);
            System.exit(1);
        }
        return null;
    }

    clsLoadedInventario inventario;
    CountDownLatch latch;
    JFrame frame = new JFrame("Cobrar... Antonio");

    clsCobrar(clsLoadedInventario inv, CountDownLatch countdown_latch) {
        inventario = inv;
        latch = countdown_latch;
        Dimension dimension = new Dimension(800, 800);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(dimension);
        frame.setLayout(new GridLayout(3, 1) {
        });
        acoplar_header(frame);
        acoplar_ingreso_de_datos(frame);
        acoplar_resultado_de_ingresos(frame);
//        BorderLayout e = new BorderLayout();
//        e.
        frame.setVisible(true);
        frame.pack();
    }

    void acoplar_header(JFrame frame) {
        LocalDateTime local_date = LocalDateTime.now();
        String fecha_actual = new String(local_date.format(DateTimeFormatter.ofPattern("EEEE dd 'de' MMMM 'de' yyyy", Locale.forLanguageTag("es-ES"))).getBytes(), StandardCharsets.UTF_8);
        System.out.println(fecha_actual + "\n\n");
        JLabel[] labels = new JLabel[]{new JLabel(""), new JLabel(""), new JLabel("UNIVERSIDAD TECNOLÓGICA DE PANAMA"), new JLabel("CENTRO REGIONAL DE CHIRIQUI"), new JLabel("FACULTAD DE INGENIERIA DE SISTEMAS COMPUTACIONALES"), new JLabel("INGENIERIA SISTEMAS INFORMACIÓN GENRENCIAL"), new JLabel("GRUPO: 2IL711"), new JLabel("CUENTA FACTURA (REPORTE DE VENTAS)"), new JLabel(""), new JLabel(""), new JLabel("ESTUDIANTE: Antonio Ng"), new JLabel("DOCENTE: Prof. Eduardo Beitia"), new JLabel("Fecha: " + fecha_actual), new JLabel("")};
        JPanel header = new JPanel(new GridLayout(labels.length / 2, 1, 5, 5));
        for (int index = 0; index < labels.length; index += 2) {
            labels[index].setHorizontalAlignment(JLabel.LEFT);
            String new_text;
            new_text = "   " + labels[index].getText();
            labels[index].setText(new_text);
            labels[index + 1].setHorizontalAlignment(JLabel.LEFT);
            header.add(labels[index]);
            header.add(labels[index + 1]);
        }
        frame.add(header);
    }

    void acoplar_ingreso_de_datos(JFrame frame) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        System.out.println(constraints.weightx);
        constraints.weightx = .1;
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.anchor = GridBagConstraints.WEST;

        // Buton de Ingresar Nombre
        JButton btn_ingresar_nombre = new JButton("Ingresar Nombre");
        panel.add(btn_ingresar_nombre, constraints);

        // Buton Ingresar Articulo y Entrada de Datos
        ButtonAgregar btn_ingresar_articulo;
        {

            constraints.gridx = 0;
            constraints.gridy = 1;
            GridBagConstraints original_constraints = (GridBagConstraints) constraints.clone();

            JPanel sub_panel = new JPanel(new GridBagLayout());
            constraints.insets = new Insets(0, 2, 0, 2);
            constraints.gridx = 1;
            constraints.gridy = 0;
            btn_ingresar_articulo = new ButtonAgregar(inventario);

            sub_panel.add(btn_ingresar_articulo, constraints);

            // TextField Ingresar Codigo
            constraints.gridx = 0;
            TextFieldIngresarCodigo text_field = new TextFieldIngresarCodigo(btn_ingresar_articulo);
            sub_panel.add(text_field, constraints);
            constraints = original_constraints;
            panel.add(sub_panel, constraints);
        }
        // Cantidad
        {
            constraints.gridx = 0;
            constraints.gridy = 2;
            GridBagConstraints original_constraints = (GridBagConstraints) constraints.clone();

            JPanel sub_panel = new JPanel(new GridBagLayout());
            constraints.insets = new Insets(0, 2, 0, 2);
            constraints.gridx = 1;
            constraints.gridy = 0;
            JLabel label = new JLabel("Cantidad");

            sub_panel.add(label, constraints);

            // TextField Ingresar Cantidad
            constraints.gridx = 0;
            TextFieldIngresarCantidad text_field = new TextFieldIngresarCantidad(btn_ingresar_articulo);
            sub_panel.add(text_field, constraints);
            constraints = original_constraints;
            panel.add(sub_panel, constraints);
        }
        // Agregar Teclado Numérico
        {
            constraints.gridx = 1;
            constraints.gridy = 1;
            constraints.gridheight = 2;
//            constraints.gridwidth = 1;
            GridBagConstraints original_constraints = (GridBagConstraints) constraints.clone();

            JPanel sub_panel = new JPanel(new GridBagLayout());

            constraints = original_constraints;
            panel.add(sub_panel, constraints);
        }
        frame.add(panel);
    }

    void acoplar_resultado_de_ingresos(JFrame frame) {
    }

}

class ButtonAgregar extends JButton {
    static public JButton button;
    clsLoadedInventario inventario;

    public ButtonAgregar(clsLoadedInventario inv) {
        super("Agregar");
        inventario = inv;
        button = this;
        if (input_is_valid().isEmpty()) block_button();
        addActionListener(e -> {

        });
    }

    /**
     * If it returns empty, it means the button should be blocked
     */
    Optional<Tuple2<String, Integer>> input_is_valid() {

        if (TextFieldIngresarCodigo.text_field.isEmpty()) return Optional.empty();
        if (TextFieldIngresarCantidad.text_field.isEmpty()) return Optional.empty();

        String codigo = TextFieldIngresarCodigo.text_field.get().getText().toUpperCase();
        String cantidad = TextFieldIngresarCantidad.text_field.get().getText().toUpperCase();
        if (inventario.query(codigo).isEmpty()) return Optional.empty();

        Try<Integer> attempt = Try.of(() -> Integer.parseInt(cantidad));
        if (attempt.isFailure()) return Optional.empty();

        return Optional.of(Tuple.of(codigo, attempt.get()));
    }

    boolean block_button() {
        setForeground(Color.BLACK);
        setBackground(Color.RED);
        setEnabled(false);
        return true;
    }

    boolean unblock_button() {
        setEnabled(true);
        setBackground(Color.BLUE.darker());
        setForeground(Color.WHITE);
        return false;
    }

}

class TextFieldIngresarCodigo extends TextField {
    ButtonAgregar binded_button;
    static Optional<TextFieldIngresarCodigo> text_field = Optional.empty();

    TextFieldIngresarCodigo(ButtonAgregar button) {
        setColumns(20);
        text_field = Optional.of(this);
        binded_button = button;
        addTextListener(e -> {
            if (binded_button.input_is_valid().isEmpty()) binded_button.block_button();
            else binded_button.unblock_button();
        });
    }
}


class TextFieldIngresarCantidad extends TextField {
    ButtonAgregar binded_button;
    static Optional<TextFieldIngresarCantidad> text_field = Optional.empty();

    TextFieldIngresarCantidad(ButtonAgregar button) {
        setColumns(7);
        text_field = Optional.of(this);
        binded_button = button;
        addTextListener(e -> {
            if (binded_button.input_is_valid().isEmpty()) binded_button.block_button();
            else binded_button.unblock_button();
        });
    }

}




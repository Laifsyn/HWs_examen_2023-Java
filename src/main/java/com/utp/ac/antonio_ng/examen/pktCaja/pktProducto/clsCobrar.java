package com.utp.ac.antonio_ng.examen.pktCaja.pktProducto;

import com.utp.ac.antonio_ng.examen.pktCaja.clsCliente;
import com.utp.ac.antonio_ng.examen.pktCaja.clsFactura;
import com.utp.ac.antonio_ng.examen.pktInventario.clsLoadedInventario;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.control.Try;
import io.vavr.control.Validation;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * Code Blocking Class - La finalización del i/o genera una lista de Productos
 */
public class clsCobrar {

    public static void main(String[] args) throws InterruptedException {
        System.setProperty("file.encoding", "UTF-8");
        clsLoadedInventario inventario = new clsLoadedInventario();

        System.out.println("Se debió haber generado la venta?");
        clsCobrar.facturar_al_cliente(inventario);
        inventario.print_inventario();
    }


    public static void facturar_al_cliente(clsLoadedInventario inventario) {
        CountDownLatch latch = new CountDownLatch(1);
        clsCobrar ventana_facturacion = new clsCobrar(inventario, latch);
        while (true) {
            try {
                ventana_facturacion.latch.await();
            } catch (InterruptedException e) {
                System.out.println("Unexpected Exception:" + e);
                System.exit(1);
            }
            System.out.println("Esperando reporte cierre");
            try {
                ventana_facturacion.inner_latch.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (JOptionPane.showConfirmDialog(null, "Desea continuar facturando?") != 0) {
                ventana_facturacion.frame.dispose();
                break;
            }
            ventana_facturacion.frame.dispose();
            ventana_facturacion = new clsCobrar(inventario, new CountDownLatch(1));
            System.out.println("Estado actual del inventario:...");
            inventario.print_inventario();
        }
    }

    clsFactura reporte_factura;
    clsLoadedInventario inventario;
    JButton btn_totalizar = new JButton("Facturar $0.00");
    JButton btn_ingresar_nombre;
    clsCliente cliente = new clsCliente("");
    CountDownLatch latch;
    CountDownLatch inner_latch;
    clsTablaDeArticulos tabla;
    JFrame frame = new JFrame("Cobrar... Antonio");

    clsCobrar(clsLoadedInventario inv, CountDownLatch countdown_latch) {
        inventario = inv;
        tabla = new clsTablaDeArticulos(inventario);
        latch = countdown_latch;
        Dimension dimension = new Dimension(800, 800);

        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setSize(dimension);
        frame.setLayout(new GridLayout(3, 1) {});
        acoplar_header(frame);
        acoplar_ingreso_de_datos(frame);
        acoplar_resultado_de_ingresos(frame);
//        BorderLayout e = new BorderLayout();
//        e.
        frame.setVisible(true);
        frame.pack();
        frame.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}

            @Override
            public void windowClosing(WindowEvent e) {}

            @Override
            public void windowClosed(WindowEvent e) {
                latch.countDown();
            }

            @Override
            public void windowIconified(WindowEvent e) {}

            @Override
            public void windowDeiconified(WindowEvent e) {}

            @Override
            public void windowActivated(WindowEvent e) {}

            @Override
            public void windowDeactivated(WindowEvent e) {}
        });
    }

    void acoplar_header(JFrame frame) {
        LocalDateTime local_date = LocalDateTime.now();
        String fecha_actual = new String(local_date.format(DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy",
                Locale.forLanguageTag("es-ES"))).getBytes(), StandardCharsets.UTF_8);
        System.out.println(fecha_actual + "\n\n");
        JLabel[] labels = new JLabel[]{new JLabel(""), new JLabel(""),
                new JLabel("UNIVERSIDAD TECNOLÓGICA DE PANAMA"), new JLabel("CENTRO REGIONAL DE CHIRIQUI"),
                new JLabel("FACULTAD DE INGENIERIA DE SISTEMAS COMPUTACIONALES"),
                new JLabel("INGENIERIA SISTEMAS " + "INFORMACIÓN GENRENCIAL"), new JLabel("GRUPO: 2IL711"),
                new JLabel("CUENTA FACTURA (REPORTE DE " + "VENTAS)"), new JLabel(""), new JLabel(""), new JLabel(
                "ESTUDIANTE: Antonio Ng"), new JLabel("DOCENTE" + ": Prof. Eduardo Beitia"), new JLabel(
                "Fecha: " + fecha_actual), new JLabel("")};
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
        constraints.weightx = .1;
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.anchor = GridBagConstraints.WEST;

        // Buton de Ingresar Nombre
        {
            btn_ingresar_nombre = new JButton("Ingresar Nombre");
            btn_ingresar_nombre.addActionListener(e -> {

                String initial_value = cliente.get_nombre();
                String nombre = JOptionPane.showInputDialog(null, "Ingrese su nombre", initial_value);
                JButton this_button = ((JButton) e.getSource());
                if (nombre == null || nombre.isBlank()) {
                    this_button.setText("Ingresar Nombre");
                    return;
                }
                cliente = new clsCliente(nombre);
                this_button.setText("Cliente: " + cliente.get_nombre());
            });
            panel.add(btn_ingresar_nombre, constraints);
        }

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
            btn_ingresar_articulo = new ButtonAgregar(inventario, tabla);
            sub_panel.add(btn_ingresar_articulo, constraints);

            // TextField Ingresar Codigo
            constraints.gridx = 0;
            TextFieldIngresarCodigo text_field = new TextFieldIngresarCodigo(btn_ingresar_articulo);
            sub_panel.add(text_field, constraints);
            constraints = original_constraints;
            panel.add(sub_panel, constraints);
        }

        // Ingresar Cantidad
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


        // Facturar
        {
            constraints.gridx = 0;
            constraints.gridy = 3;

            JPanel sub_panel = new JPanel();
            JButton boton = btn_totalizar;
            boton.setVisible(false);

            boton.addActionListener(e -> {
                frame.setEnabled(false);
                System.out.println("Generamos el reporte");
                Tuple2<clsFactura, CountDownLatch> set = clsFactura.generarReporte(Tuple.of(cliente,
                        tabla.productos_en_la_tabla), new CountDownLatch(1));
                inner_latch = set._2;
                latch.countDown();
            });

            tabla.tabla.getModel().addTableModelListener(e -> {
                boton.setVisible(true);
                boton.setText(String.format("Facturar: %.2f", tabla.totalizar()));
            });
            sub_panel.add(boton);
            panel.add(sub_panel, constraints);
        }

        // Agregar Teclado Numérico
        {
            GridBagConstraints original_constraints = (GridBagConstraints) constraints.clone();
            constraints.gridx = 1;
            constraints.gridy = 0;
            constraints.gridheight = 4;
            constraints.weighty = 1;
            constraints.weightx = 11;
            constraints.gridwidth = 1;
            JPanel digits_panel = new JPanel(new GridLayout(4, 3, 5, 5));
//            digits_panel.setBackground(Color.CYAN);
            JTextArea text_area = new JTextArea();
            for (Integer index : new Integer[]{7, 8, 9, 4, 5, 6, 1, 2, 3, -1, 0, -2}) {
                NumericButton btn = new NumericButton(index, text_area);
                btn.setPreferredSize(new Dimension(100, 50));
                digits_panel.add(btn);
            }
            Dimension text_area_prefered_size = digits_panel.getPreferredSize();
            text_area_prefered_size.width = 120;
            text_area.setFont(text_area.getFont().deriveFont(16f));
            text_area.setVisible(false);
            text_area.setLineWrap(true);
            text_area.setWrapStyleWord(true);
            text_area.setPreferredSize(text_area_prefered_size);

            JPanel sub_panel = new JPanel(new GridLayout(1, 2, 5, 5));
            sub_panel.add(digits_panel);
            sub_panel.add(text_area);
            panel.add(sub_panel, constraints);
            constraints = original_constraints;
        }
        frame.add(panel);
    }

    void acoplar_resultado_de_ingresos(JFrame frame) {
        // Acoplamos la Tabla de datos
        frame.add(tabla.scroll_pane);
    }

}

class ButtonAgregar extends JButton {
    static public JButton button;
    clsLoadedInventario inventario;
    Optional<Tuple2<String, Integer>> last_valid_input = Optional.empty();

    public ButtonAgregar(clsLoadedInventario inv, clsTablaDeArticulos tabla_de_datos) {
        super("Agregar");
        inventario = inv;
        button = this;
        if (input_is_valid().isEmpty()) block_button();
        addActionListener(e -> {
            if (last_valid_input.isEmpty()) return;
            Tuple2<String, Integer> inputs = last_valid_input.get();
            Optional<clsProductoInmutable> producto = tabla_de_datos.try_insert_producto(inputs._1, inputs._2);
            if (producto.isEmpty()) return;
            tabla_de_datos.insert_producto(producto.get());
            if (TextFieldIngresarCodigo.text_field.isEmpty()) return;
            TextFieldIngresarCodigo.text_field.get().requestFocus();
            TextFieldIngresarCodigo.text_field.get().selectAll();
            if (input_is_valid().isEmpty()) block_button();
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

        Optional<Integer> query_result = inventario.query(codigo);
        if (attempt.get() <= 0 || query_result.isEmpty() || query_result.get() <= 0) return Optional.empty();

        last_valid_input = Optional.of(Tuple.of(codigo, attempt.get()));
        return last_valid_input;
    }

    void block_button() {
        setForeground(Color.BLACK);
        setBackground(Color.RED);
        setEnabled(false);
    }

    void unblock_button() {
        setEnabled(true);
        setBackground(Color.BLUE.darker());
        setForeground(Color.WHITE);
    }

}

class clsTablaDeArticulos {
    public static void main(String[] args) {
        clsTablaDeArticulos e = new clsTablaDeArticulos(new clsLoadedInventario());
        Object ignored = e.try_insert_producto("XYZ100", 700);

    }

    private static final String[] Columnas = new String[]{"Linea", "Cantidad", "Precio", "Descripcion", "Subtotal",
            "%"};
    private static final Integer[] Columnas_width = new Integer[]{5, 10, 40, 200, 40, 10};
    final clsLoadedInventario inventario;
    JScrollPane scroll_pane;
    JTable tabla = new JTable() {
        {
            DefaultTableModel model = new DefaultTableModel();
            // Agregamos los nombres de las columnas
            for (String name : Columnas)
                model.addColumn(name);

            // Actualizamos el modelo de la tabla
            setModel(model);

            // Editamos la anchura de descripción
            for (int index = 0; index < Columnas.length; index++)
                getColumnModel().getColumn(index).setPreferredWidth(Columnas_width[index]);
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    clsTablaDeArticulos(clsLoadedInventario inventario) {
        this.inventario = inventario;
        scroll_pane = new JScrollPane(tabla);
        int suma = 0;
        for (Integer numero : Columnas_width)
            suma += numero;
        scroll_pane.setPreferredSize(new Dimension(suma, 200));
    }

    public double totalizar() {
        if (productos_en_la_tabla.isEmpty()) return 0d;
        double ret = 0d;
        for (clsProductoInmutable producto : productos_en_la_tabla)
            ret += producto.get_cantidad() * producto.get_venta();
        return ret;
    }

    /**
     * Provides a Reference to the inserted product
     */
    public Optional<clsProductoInmutable> try_insert_producto(String codigo, int amount) {
        Validation<Optional<Integer>, clsProductoInmutable> fetch_result = inventario.try_fetch(codigo, amount);
        if (fetch_result.isInvalid()) {
            if (fetch_result.getError().isEmpty()) return Optional.empty();
            int cantidad_sugerida = fetch_result.getError().get();
            if (cantidad_sugerida <= 0) return Optional.empty();

            String mensaje = String.format("El inventario no tiene >%d< unidades, pero tiene >%d< unidades " +
                    "disponibles. Desea llevarse lo que queda?", amount, cantidad_sugerida);
            int result = JOptionPane.showConfirmDialog(null, mensaje, "Confirmar comprar menos....",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            // 1 = No
            if (result == JOptionPane.NO_OPTION) return Optional.empty();
            return try_insert_producto(codigo, cantidad_sugerida);
        }
        clsProductoInmutable producto = fetch_result.get();
        return Optional.of(producto);
    }

    Integer line = 0;
    public ArrayList<clsProductoInmutable> productos_en_la_tabla = new ArrayList<>();

    void insert_producto(clsProductoInmutable producto) {
        line++;

        DefaultTableModel model = (DefaultTableModel) tabla.getModel();
        if (line > 1 && productos_en_la_tabla.get(line - 2).get_codigo_articulo().equals(producto.get_codigo_articulo())) {
            clsProductoInmutable last_input = productos_en_la_tabla.get(line - 2);
            producto.setCantidad(producto.get_cantidad() + last_input.get_cantidad());
            line--;
            model.removeRow(line - 1);
            productos_en_la_tabla.remove(line - 1);
        }
        productos_en_la_tabla.add(producto);
        Object[] row = new Object[]{line, producto.get_cantidad(), producto.get_venta(), producto.get_description(),
                String.format("%.2f", producto.get_cantidad() * producto.get_venta()),
                producto.articulo.retrieve_itbms()};
        model.addRow(row);
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

class NumericButton extends JButton {
    Integer value;
    Optional<TextFieldIngresarCantidad> target_textfield = TextFieldIngresarCantidad.text_field;
    static Integer failed = 0;
    Optional<Thread> active_thread = Optional.empty();
    static CountDownLatch cd = new CountDownLatch(5);
    static JTextArea jokes_area;

    NumericButton(Integer value, JTextArea text_area_jokes) {
        super(String.valueOf(value));
        jokes_area = text_area_jokes;
        this.value = value;
        if (value >= 0) addActionListener(e -> {
            if (target_textfield.isEmpty()) {
                System.out.println("Se desconoce el objetivo de cantidad para el buton " + value);
                return;
            }
            TextFieldIngresarCantidad text_field = target_textfield.get();
            text_field.setText(text_field.getText() + value);
        });
        else if (value == -1) {
            setText("Clear");
            addActionListener(e -> {
                if (target_textfield.isEmpty()) {
                    System.out.println("Se desconoce el objetivo de cantidad para el buton " + value);
                    return;
                }
                TextFieldIngresarCantidad text_field = target_textfield.get();
                text_field.setText("");
            });
        } else if (value == -2) {
            setText("");
            addActionListener(e -> {
                NumericButton button = (NumericButton) e.getSource();
                failed++;
                if (active_thread.isEmpty()) {
                    spawn_thread(button, text_area_jokes);
                } else {
                    active_thread.get().interrupt();
                    spawn_thread(button, text_area_jokes);
                }
                if (failed > 5) button.setText("???");
                text_area_jokes.setVisible(true);
                text_area_jokes.setText(RandomText.get(failed));

            });
        }

    }

    Integer spawns = 0;

    void spawn_thread(NumericButton button, JTextArea joke_text_area) {
        spawns++;
        Integer spawn_no = spawns;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
//                    System.out.println("Waiting for " + spawn_no);
                    Thread.sleep(4500);
//                    System.out.println("We finished waiting for No." + spawns);
                    button.setText("");
                    joke_text_area.setText("");
                    joke_text_area.setVisible(false);
                    failed = 0;
                } catch (Exception Ignored) {
                    System.out.println("Exception No." + spawn_no);
                }
            }
        });
        t.start();
        active_thread = Optional.of(t);
    }

}


class RandomText {
    static String[] Block_1 = new String[]{"Button has no assigned function.", "Clicking holds no consequential " +
            "impact.", "No outcome from repeated button presses.", "Functionality remains inert, unaffected by " +
            "clicks" + ".", "Absence of programmed response to clicks.", "Button lacks predefined operational " +
            "significance.", "Repeated clicks yield no discernible effect.", "No action results from persistent " +
            "clicks.", "Button " + "remains unresponsive to user input.",
            "Clicking won't initiate any programmed " + "action.", "Programmed " + "response is nonexistent for " +
            "clicks.", "No functional assignment for button " + "action.", "Persistent " + "clicking won't alter " +
            "system state.", "Button's purpose is undefined, inert.", "Clicking provides no " + "tangible system " +
            "feedback."};
    static String[] Block_2 = new String[]{"Clicking like it owes you money, huh?",
            "Spamming won't summon a pizza, " + "just saying.", "Are you trying to break the internet, one click at " +
            "a" + " time?", "This isn't Morse code for " + "'bring snacks,' you know?", "Button abuse! The pixels are" +
            " " + "crying!", "Congratulations! You've just won " + "the 'Clicker of the Day' award.", "Pushing " +
            "buttons like" + " it's an Olympic sport.", "Clicking won't make it" + " a magic portal, but nice try.",
            "Someone's on a " + "mission to redefine 'persistent.'", "Judging by your " + "clicks, you're training " +
            "for a thumb-wrestling " + "championship.", "Button Olympics training in progress.", "Expecting confetti?" +
            " Keep clicking.", "Desperate for attention? Button understands.", "Button is not a " + "stress ball.",
            "Trying to unlock " + "secret cat videos?", "Click therapy? Seeking zen through buttons.", "Button " +
            "applause: click harder for " + "encore.", "Button whisperer, mastering the silent art.",
            "World record" + " for clicks: within reach.", "Button pusher extraordinaire reporting for duty.",
            "Button honors your " + "relentless dedication.", "Breaking news: Button unimpressed, needs vacation.",
            "Button says: 'Calm down," + " warrior.'", "Click " + "count exceeds cosmic expectations.", "Button " +
            "endorses patience, not spam.", "Your " + "click: a " + "masterpiece of persistence.", "Button " +
            "contemplates life during your clicks.", "Button predicts " + "Nobel for clickology soon.", "Click " +
            "wizardry: unmatched, slightly eccentric.", "Button wonders if you " + "need coffee.",};
    static String[] Block_3 = new String[]{"Still clicking? Puzzle intensifies.",
            "Confused by relentless clicks, " + "seriously.", "Program bewildered. Infinite clicks detected.",
            "Button mystified by endless persistence.", "Endless clicks defy all logic.", "Program bewildered: " +
            "endless clicks persist.", "Infinity clicks: " + "program's existential crisis.", "Program ponders: " +
            "clicks beyond comprehension.", "Clicks exceed " + "algorithmic comprehension limit.", "Persistent " +
            "clicking: program in deep confusion.", "Endless clicks " + "baffle program's logic.", "Infinite clicks: "
            + "program questions reality.", "Endless persistence confuses " + "programmed logic.", "Program " +
            "mystified: " + "clicks never-ending saga.", "Clicks surpass program's patience " + "threshold.",
            "Unyielding clicks: " + "program in utter disbelief.", "Program contemplates: clicks without " + "purpose" +
            ".", "Clicks persist, " + "program's sanity questioned.", "Endless clicks: program in silent chaos.",
            "Program bewildered: clicks " + "relentless mystery.", "Clicks transcend program's understanding realm.",
            "Program astonished: clicks " + "beyond explanation.", "Unending clicks: program's perpetual surprise.",
            "Clicks defy program's expected " + "limits."};
    static String[] Block_4 = new String[]{"Click wizard: Why are you pressing?",
            "Button philosophy: What is the " + "truth of clicking?", "Button says: Halt, warrior.", "Button " +
            "meditation: Continual clicking and " + "tranquility.", "Click prophet: Unpredictable clicks.",
            "Button " + "apocalypse: Silent clicks.", "Press for " + "tea, the button knows.", "Click maniac: " +
            "Continuous clicking" + " has no end.", "Transcendent click: Cosmic " + "clicks anticipated.", "Button " +
            "miracle: Inexplicable " + "clicks.", "Your click - a masterpiece of persistence" + ".", "Button mystery:" +
            " Unsolvable clicks.", "Button Zen master: Clicking is Zen.", "Click artist: " + "Unpredictable " +
            "masterpiece.", "Button physics: " + "Soundless clicks.", "Button genius: Click for brilliance.", "Button" +
            " meditation: Profound philosophy of " + "clicking.", "Click magic: Magical and unpredictable clicks.",
            "Button poetry: Rhythm of clicks.", "Press" + " for laughter, the button knows.", "Click miracle: " +
            "Continual " + "mysterious clicks.", "Click prophecy: " + "Unpredictable clicks.", "Button silence: " +
            "Soundless clicks.", "Button genius: Click for brilliance.", "Button poetry: Rhythm of clicks.", "Click " +
            "magic: Magical and " + "unpredictable clicks.", "Button " + "adventure: Journey of clicks.", "Button " +
            "philosopher: Press for wisdom.", "Button art: Painting with " + "clicks.", "Click prophet: Unpredictable" +
            " clicks."};

    static String get(Integer input) {
        System.out.println(input);
        Random rand = new Random();
        String[] Block;
        if (input > 50) return "..............";
        else if (input > 30) Block = Block_4;
        else if (input > 15) Block = Block_3;
        else if (input > 6) Block = Block_2;
        else Block = Block_1;
        return input + "-) " + Block[rand.nextInt(Block.length)];
    }
}


package com.utp.ac.antonio_ng.examen.pktCaja.pktProducto;

import com.utp.ac.antonio_ng.examen.pktCaja.clsCliente;
import com.utp.ac.antonio_ng.examen.pktInventario.clsLoadedInventario;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.control.Try;

import javax.swing.*;
import java.awt.*;
import java.awt.font.NumericShaper;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;
import java.util.Random;
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
        String fecha_actual = new String(local_date.format(DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy", Locale.forLanguageTag("es-ES"))).getBytes(), StandardCharsets.UTF_8);
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
            GridBagConstraints original_constraints = (GridBagConstraints) constraints.clone();
            constraints.gridx = 1;
            constraints.gridy = 0;
            constraints.gridheight = 3;
            constraints.weighty = 1;
            constraints.weightx = 11;
            constraints.gridwidth = 1;
            JPanel digits_panel = new JPanel(new GridLayout(4, 3, 5, 5));
            digits_panel.setSize(300, 300);
            digits_panel.setBackground(Color.CYAN);
            JTextArea text_area = new JTextArea();
            for (Integer index : new Integer[]{7, 8, 9, 4, 5, 6, 1, 2, 3, -1, 0, -2}) {
                NumericButton btn = new NumericButton(index, text_area);
                btn.setPreferredSize(new Dimension(80, 40));
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
                if (failed > 5)
                    button.setText("???");
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
                    System.out.println("Waiting for " + spawn_no);
                    Thread.sleep(4500);
                    System.out.println("We finished waiting for No." + spawns);
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

class staticLabel extends JLabel {
    static JLabel instance = new JLabel();

    static void _setText(String text) {
        instance.setText(text);
    }
}

class RandomText {
    static String[] Block_1 = new String[]{
            "Button has no assigned function.",
            "Clicking holds no consequential impact.",
            "No outcome from repeated button presses.",
            "Functionality remains inert, unaffected by clicks.",
            "Absence of programmed response to clicks.",
            "Button lacks predefined operational significance.",
            "Repeated clicks yield no discernible effect.",
            "No action results from persistent clicks.",
            "Button remains unresponsive to user input.",
            "Clicking won't initiate any programmed action.",
            "Programmed response is nonexistent for clicks.",
            "No functional assignment for button action.",
            "Persistent clicking won't alter system state.",
            "Button's purpose is undefined, inert.",
            "Clicking provides no tangible system feedback."
    };
    static String[] Block_2 = new String[]{
            "Clicking like it owes you money, huh?",
            "Spamming won't summon a pizza, just saying.",
            "Are you trying to break the internet, one click at a time?",
            "This isn't Morse code for 'bring snacks,' you know?",
            "Button abuse! The pixels are crying!",
            "Congratulations! You've just won the 'Clicker of the Day' award.",
            "Pushing buttons like it's an Olympic sport.",
            "Clicking won't make it a magic portal, but nice try.",
            "Someone's on a mission to redefine 'persistent.'",
            "Judging by your clicks, you're training for a thumb-wrestling championship.",
            "Button Olympics training in progress.",
            "Expecting confetti? Keep clicking.",
            "Desperate for attention? Button understands.",
            "Button is not a stress ball.",
            "Trying to unlock secret cat videos?",
            "Click therapy? Seeking zen through buttons.",
            "Button applause: click harder for encore.",
            "Button whisperer, mastering the silent art.",
            "World record for clicks: within reach.",
            "Button pusher extraordinaire reporting for duty.",
            "Button honors your relentless dedication.",
            "Breaking news: Button unimpressed, needs vacation.",
            "Button says: 'Calm down, warrior.'",
            "Click count exceeds cosmic expectations.",
            "Button endorses patience, not spam.",
            "Your click: a masterpiece of persistence.",
            "Button contemplates life during your clicks.",
            "Button predicts Nobel for clickology soon.",
            "Click wizardry: unmatched, slightly eccentric.",
            "Button wonders if you need coffee.",
    };
    static String[] Block_3 = new String[]{
            "Still clicking? Puzzle intensifies.",
            "Confused by relentless clicks, seriously.",
            "Program bewildered. Infinite clicks detected.",
            "Button mystified by endless persistence.",
            "Endless clicks defy all logic.",
            "Program bewildered: endless clicks persist.",
            "Infinity clicks: program's existential crisis.",
            "Program ponders: clicks beyond comprehension.",
            "Clicks exceed algorithmic comprehension limit.",
            "Persistent clicking: program in deep confusion.",
            "Endless clicks baffle program's logic.",
            "Infinite clicks: program questions reality.",
            "Endless persistence confuses programmed logic.",
            "Program mystified: clicks never-ending saga.",
            "Clicks surpass program's patience threshold.",
            "Unyielding clicks: program in utter disbelief.",
            "Program contemplates: clicks without purpose.",
            "Clicks persist, program's sanity questioned.",
            "Endless clicks: program in silent chaos.",
            "Program bewildered: clicks relentless mystery.",
            "Clicks transcend program's understanding realm.",
            "Program astonished: clicks beyond explanation.",
            "Unending clicks: program's perpetual surprise.",
            "Clicks defy program's expected limits."
    };
    static String[] Block_4 = new String[]{
            "按钮大师: 点击有何深意？",
            "클릭 마법사: 왜 누르세요?", // Click wizard: Why are you pressing?
            "ボタン哲学: クリックの真実は？", // Button philosophy: What is the truth of clicking?
            "Кнопка говорит: стой, воин.", // Button says: Halt, warrior.
            "按鈕禪修: 持續點擊與平靜。", // Button meditation: Continual clicking and tranquility.
            "클릭 예언자: 예측 불가능한 클릭.", // Click prophet: Unpredictable clicks.
            "ボタンの黙示録: 静かなクリック。", // Button apocalypse: Silent clicks.
            "Нажмите для чаю, кнопка знает.", // Press for tea, the button knows.
            "点击狂魔: 持续点击无止境。", // Click maniac: Continuous clicking has no end.
            "초월 클릭: 우주적인 클릭 예상.", // Transcendent click: Cosmic clicks anticipated.
            "ボタンの奇跡: 不可解なクリック。", // Button miracle: Inexplicable clicks.
            "Ваш клик - шедевр настойчивости.", // Your click - a masterpiece of persistence.
            "버튼 미스테리: 해결 불가능한 클릭.", // Button mystery: Unsolvable clicks.
            "按钮禅师: 点击即是禅。", // Button Zen master: Clicking is Zen.
            "클릭 예술가: 예측할 수 없는 작품.", // Click artist: Unpredictable masterpiece.
            "ボタンの物理学: 無音のクリック。", // Button physics: Soundless clicks.
            "Кнопка гений: Кликните для гениальности.", // Button genius: Click for brilliance.
            "按钮冥想: 深沉的点击哲学。", // Button meditation: Profound philosophy of clicking.
            "클릭 마법: 마법 같은 예상불가 클릭.", // Click magic: Magical and unpredictable clicks.
            "ボタンの詩: クリックの韻律。", // Button poetry: Rhythm of clicks.
            "Нажмите для смеха, кнопка знает.", // Press for laughter, the button knows.
            "点击奇迹: 持续神秘的点击。", // Click miracle: Continual mysterious clicks.
            "클릭 예언: 예측 불가능한 클릭.", // Click prophecy: Unpredictable clicks.
            "ボタンの静寂: 無音のクリック。", // Button silence: Soundless clicks.
            "Кнопка гений: Кликайте для гениальности.", // Button genius: Click for brilliance.
            "按钮的诗: 点击的韵律。", // Button poetry: Rhythm of clicks.
            "클릭 마법: 마법과 예측할 수 없는 클릭.", // Click magic: Magical and unpredictable clicks.
            "ボタンの冒険: クリックの旅。", // Button adventure: Journey of clicks.
            "Кнопка философ: Нажмите для мудрости.", // Button philosopher: Press for wisdom.
            "按钮的艺术: 点击的绘画。", // Button art: Painting with clicks.
            "클릭 예언자: 예측 불가능한 클릭.", // Click prophet: Unpredictable clicks.
    };

    static String get(Integer input) {
        System.out.println(input);
        Random rand = new Random();
        String[] Block;
        if (input > 50)
            return "..............";
        else if (input > 30)
            Block = Block_4;
        else if (input > 15)
            Block = Block_3;
        else if (input > 6)
            Block = Block_2;
        else
            Block = Block_1;
        return input + "-) " + Block[rand.nextInt(Block.length)];
    }
}


import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

public class clsQueryingWindows {
    List<clsCliente> clientes;
    List<clsFactura> facturas;
    CountDownLatch latch;
    JFrame base_frame = new JFrame("Hola mundo : Proyecto Arreglo de Objetos");

    clsQueryingWindows(List<clsCliente> Clientes, List<clsFactura> Facturas) {
        this.clientes = Clientes;
        this.facturas = Facturas;
        draw_querying_windows();
    }

    void spawn_reporte() {
        latch = new CountDownLatch(1);
        try {
            latch.await();
        } catch (Exception e) {
            //noinspection ThrowablePrintedToSystemOut
            System.out.println(e);
        }
        String textfield = get_textfield_codigo_cliente().trim();
        Colored_Label jlabel = (Colored_Label) Comps.get(enumComponents.JL_CodigoCliente);
        if (textfield.isEmpty()) {
            jlabel.updateText(jlabel.initialized_text + "- Field can't be empty!! ", "red");
            return;
        }
        Optional<clsCliente> query_result = get_cliente_from_id(textfield);
        if (query_result.isEmpty()) {
            jlabel.updateText(jlabel.initialized_text + "- NO MATCH IN DATABASE!! ", "red");
            return;
        } else jlabel.updateText(jlabel.initialized_text, "Green");
        clsFactura[] facturas = query_facturas_de_cliente(textfield);
        new clsReporte(query_result.get(), facturas);
    }

    void draw_querying_windows() {

        base_frame.setVisible(true);
        base_frame.setSize(300, 200);
        base_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        base_frame.getContentPane().add(frame_add_querying_panel());
        base_frame.setMinimumSize(base_frame.getPreferredSize());
//        base_frame.pack();

        do {
            System.out.println("Intentando obtener reporte....");
            spawn_reporte();
            if (base_frame.getMinimumSize().width < base_frame.getPreferredSize().width ||
                    base_frame.getMinimumSize().height < base_frame.getPreferredSize().height
            )
                base_frame.setMinimumSize(base_frame.getPreferredSize());

            base_frame.pack();
            base_frame.setSize(base_frame.getWidth() + 10, base_frame.getHeight() + 10);
        } while (JOptionPane.showConfirmDialog(null, "Continuar?", "Querying...", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION);
        System.out.println("Finalizamos");
        base_frame.dispose();
    }

    JPanel frame_add_querying_panel() {
        JPanel querying_panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.NORTHEAST;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(3, 3, 3, 3);
        querying_panel.add(Comps.get(enumComponents.JL_CodigoCliente), constraints);
        constraints.gridy++;
        querying_panel.add(Comps.get(enumComponents.TF_CodigoCliente), constraints);
        constraints.gridx = 0;
        constraints.gridy++;
        querying_panel.add(Comps.get(enumComponents.BT_Buscar), constraints);
        comps_add_events();
        return querying_panel;
    }

    void comps_add_events() {
        JButton BT_Buscar = (JButton) Comps.get(enumComponents.BT_Buscar);
        BT_Buscar.addActionListener(e -> latch.countDown());
    }

    clsFactura[] query_facturas_de_cliente(String codigo) {
        ArrayList<clsFactura> facturas = new ArrayList<>();
        for (clsFactura factura : this.facturas)
            if (factura.getCliente_id().compareTo(codigo) == 0) facturas.add(factura);
        facturas.forEach(System.out::println);
        return facturas.toArray(new clsFactura[0]);
    }

    Optional<clsCliente> get_cliente_from_id(String id) {
        for (clsCliente cliente : clientes)
            if (cliente.getId().compareTo(id) == 0) return Optional.of(cliente);
        return Optional.empty();
    }

    String get_textfield_codigo_cliente() {
        JTextField textfield = (JTextField) Comps.get(enumComponents.TF_CodigoCliente);
        return textfield.getText();
    }
/*
* Si encuentras este main vacío, solo se me ocurre que es porque no se puede probar de manera individual.
* */
    public static void main(String[] args) {

    }

    HashMap<enumComponents, JComponent> Comps = new HashMap<>() {{
        put(enumComponents.BT_Buscar, new JButton("Buscar"));
        JTextField TextField = new JTextField();
        TextField.setColumns(20);
        put(enumComponents.TF_CodigoCliente, TextField);
        put(enumComponents.JL_CodigoCliente, new Colored_Label("Ingrese Codigo de Cliente"));

    }};
}

enum enumComponents {
    TF_CodigoCliente, JL_CodigoCliente, BT_Buscar
}

class Colored_Label extends JLabel {
    String initialized_text;

    public Colored_Label(String text) {
        super("");
        initialized_text = text;
        updateText(text, "black");
    }

    void updateText(String new_text, String color) {
        setText("<html><COLOR=" + color + "> " + new_text + "</></html>");
    }
}

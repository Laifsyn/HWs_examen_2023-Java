package pktPasswordAntonioNg;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;


public class clsPasswordAntonioNg {
    Integer attempts = 1;
    static final Integer password_attempts = 3;
    String usuario = "", mensaje = "";
    static final JLabel titulo = new JLabel("Ingrese su contraseña");
    public static boolean is_logged_in = false;
    // Solo me faltaba una manera de saber si se logró iniciar sesión de manera exitosa, asi que creé un acceso publico a la variable

    public static void main(String[] args) {
        if (!clsPasswordAntonioNg.esperar_inicio_sesion()) {
            JOptionPane.showMessageDialog(null, "Inicio de sesión fallada. El programa se cerrará");
            System.exit(0);
        }
        System.out.println("Hola. Acabamos de leer. Inicio de sesión existosa");
    }

    public static boolean esperar_inicio_sesion() {
        is_logged_in = false;
        boolean end = false;

        clsPasswordAntonioNg instance = new clsPasswordAntonioNg();
        while (!end) {
            end = instance.spawn();
        }
        return is_logged_in;
    }

    boolean spawn() {
        JPasswordField objLeerPasword = new JPasswordField();
        this.usuario = JOptionPane.showInputDialog(null, new Object[]{"Hace un buen dia. \nProcedamos a ingresar sesion!", "Ingrese su usuario"}, "");

        if (this.usuario == null)
            return false; // Intentamos denuevo si decide cancelar por accidente.
        while (this.attempts <= password_attempts) {
            int x = JOptionPane.showConfirmDialog(null, new Object[]{new JLabel("Bienvenido a Stark CO. " + this.usuario), titulo, objLeerPasword}, "Inicio de Sesion", JOptionPane.OK_CANCEL_OPTION);
            // El usuario oprimio un boton distinto a OK
            if (x != 0) {
                JOptionPane.showMessageDialog(null, "Decidio cancelar el programa.");
                return true;
            }

            // La contraseña se guarda en un vector de caracteres
            char[] vector = objLeerPasword.getPassword();
            String pass = new String(vector);

            if (pass.isEmpty())
                continue;
            if (pass.equals("fin")) {
                JOptionPane.showMessageDialog(null, "Perfecto " + usuario + " puede proceder con la ejecucion del programa.");
                is_logged_in = true;
                break;
            } else {
                if (this.attempts < password_attempts)
                    mensaje = String.format("Te quedan %d oportunidades.", password_attempts - this.attempts);
                else
                    mensaje = "Intentos agotados.";
                objLeerPasword.setText("");
                JOptionPane.showMessageDialog(null, usuario + "\nTe has equivocado de contraseña.\n" + mensaje);
                this.attempts++;
            }
        }
        if (this.attempts > password_attempts)
            JOptionPane.showMessageDialog(null, "Lo siento " + usuario + "\nHable con su supervisor para recuperar su contraseña");
        else
            JOptionPane.showMessageDialog(null, usuario + " Gracias por utilizar nuestro software");
        return true;
    }
}

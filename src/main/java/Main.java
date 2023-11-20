import com.fasterxml.jackson.databind.ObjectMapper;
import pktResourceFiles.FileResource;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

public class Main {
    List<clsCliente> lista_clientes;
    List<clsFactura> lista_facturas;

    public static void main(String[] args) {
        //Punto te inicio. Llamémoslo nuestro (punto de acceso/aplicación) a la base de datos
        new Main();
    }

    void _importar_datos() throws URISyntaxException, IOException {
        // Leer y deserializar los datos del archivo
        ObjectMapper mapper = new ObjectMapper();
        File input_stream = new FileResource().getFileFromResource("lista_de_clientes/source_data.json");
        lista_clientes = Arrays.asList(mapper.treeToValue(mapper.readTree(input_stream).get("data"), clsCliente[].class));
        input_stream = new FileResource().getFileFromResource("lista_de_facturas/source_data.json");
        lista_facturas = Arrays.asList(mapper.treeToValue(mapper.readTree(input_stream).get("data"), clsFactura[].class));

        lista_facturas.forEach(System.out::println);
        System.out.println();
        lista_clientes.forEach(System.out::println);
    }

    public Main() {
        try {
            _importar_datos();
        } catch (Exception E) {
            System.out.println("No se pudo importar los datos\n" + E);
            return;
        }
        new clsQueryingWindows(lista_clientes, lista_facturas);
    }


}

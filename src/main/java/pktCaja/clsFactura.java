package pktCaja;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class clsFactura {
    @JsonProperty("id")
    Integer numero_factura;
    @JsonProperty("fecha")
    String fecha;
    String cliente_id;
    @JsonProperty("monto")
    BigDecimal saldo;

    public clsFactura() {

    }

    public String toString() {
        String ret = "";
        try {
            ret = new ObjectMapper().writeValueAsString(this);
        } catch (Exception e) {
            System.out.println(e);
        }
        return ret;
    }

    @JsonIgnore
    public long getTime() {
        return get_parsed_fecha().getTime();
    }

    /**
     * Ejemplo de Serialización de un string JSON a pktCaja.clsFactura
     * */
    public static void main(String[] args) {
        try {
            clsFactura factura = new_from_json("{\"id\":12345, \"fecha\":\"2023/01/02\", \"cliente_id\":\"A1257\", \"monto\":12.45}");
            System.out.println("Success:" + factura);
        } catch (Exception ignored) {
            System.out.println("No se pudo convertir el texto!!");
        }
        System.out.println("Finalizamos la prueba!");
    }

    static clsFactura new_from_json(String json_string) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json_string, clsFactura.class);
    }

    /**
     * Returns the number of milliseconds since January 1, 1970, 00:00:00 GMT
     * represented by this {@code Date} object.
     *
     * @return the number of milliseconds between this {@code Date} object, and the other.
     */
    public long compare_date(Date other) {
        return getTime() - other.getTime();
    }

    public long compare_date(Date self, Date other) {
        return self.getTime() - other.getTime();
    }

    public Integer getNumero_factura() {
        return numero_factura;
    }

    @JsonIgnore
    public Date get_parsed_fecha() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
        Date date;
        try {

            date = format.parse(getFecha());
        } catch (Exception e) {
            date = new Date();
            System.out.println(e);
        }
        return date;
    }

    public String getFecha() {
        return fecha;
    }

    public String getCliente_id() {
        return cliente_id;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setCliente_id(String cliente_id) {
        this.cliente_id = cliente_id;
    }
}

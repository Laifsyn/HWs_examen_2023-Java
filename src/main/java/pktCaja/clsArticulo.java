package pktCaja;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class clsArticulo {
    public static void main(String[] args) {

    }
    @JsonProperty("codigo")
    String codigo;
    String descripcion;
    @JsonProperty("impuesto")
    int itbms;

    public void setItbms(String itbms) {
        int ret;
        switch (itbms) {
            case "1" -> ret = 10;
            case "7" -> ret = 7;
            default -> ret = 0;
        }
        this.itbms = ret;
    }

    public String getItbms() {
        String ret;
        switch (itbms) {
            case 10 -> ret = "10";
            case 7 -> ret = "7";
            default -> ret = "0";
        }
        return ret;
    }

    @JsonProperty("existencia")
    int en_reserva;
    float costo;
    float precio;


}

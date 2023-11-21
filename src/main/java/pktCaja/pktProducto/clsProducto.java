package pktCaja.pktProducto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
@JsonIgnoreProperties
public class clsProducto {
    @JsonUnwrapped
    clsArticulo articulo;

    public Integer fetch(int amount) {
        return 0;


    }
}

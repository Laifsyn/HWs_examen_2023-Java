package pktCaja.pktProducto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.vavr.control.Validation;

@JsonIgnoreProperties
public class clsProducto {
    @JsonUnwrapped
    clsArticulo articulo;

    public Validation<Integer, Integer> fetch(int fetching) {
        Validation<Integer, Integer> ret;
        if (articulo.en_inventario() >= fetching) {
            articulo.fetch(fetching);
            ret = Validation.valid(fetching);
        } else // Retorna la cantidad en inventario envuelta en un "Error"
            ret = Validation.invalid(articulo.en_inventario());
        
        return ret;
    }
}

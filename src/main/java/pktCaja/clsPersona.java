package pktCaja;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class clsPersona {
    @Override
    public String toString() {
        return String.format("%s:{Cedula: %s, Nombre Completo: %s %s, telefono: %d}", getClass().getName(), cedula, nombre, apellido, telefono);
    }

    String cedula;
    String nombre;
    String apellido;
    @JsonProperty("teléfono")
    Integer telefono;

    public void setTelefono(Integer telefono) {
        this.telefono = telefono;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCedula() {
        return cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    @JsonIgnore
    public String getNombreCompleto() {
        return getNombre() + " " + getApellido();
    }

    public Integer getTelefono() {
        return telefono;
    }
}


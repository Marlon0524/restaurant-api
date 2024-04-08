package com.restaurant.apirest.reservation;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Size;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@Entity
public class Reservation {
    @Id
    @GeneratedValue
    private Integer id;
    @Size(max = 60, message = "El nombre no puede tener más de 60 caracteres")
    private String name;
    private Integer document;
    @Max(value = 4, message = "La cantidad de comensales debe ser máximo 4")
    private Integer quantity;
    private String observations;
    private String startDateFormatted; // Campo de fecha en formato de cadena
    private Date startDate; // Campo de fecha como objeto Date

    public void setStartDateFormatted(String startDateFormatted) {
        // Validar el formato de la fecha
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        try {
            // Intenta parsear la fecha
            this.startDate = sdf.parse(startDateFormatted);
        } catch (ParseException e) {
            // Si hay un error de formato, lanza una excepción
            throw new IllegalArgumentException("Formato de fecha inválido. El formato debe ser dd/MM/yyyy HH:mm:ss");
        }
        // Si la fecha se parsea correctamente, establece el valor del campo startDateFormatted
        this.startDateFormatted = startDateFormatted;
    }

}

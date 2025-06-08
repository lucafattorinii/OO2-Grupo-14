
package com.grupo14.turnos.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record TurnoVistaDTO(
    LocalDate fecha,
    LocalTime hora,
    String estado,
    String nombreCliente,
    String nombreServicio
) {}
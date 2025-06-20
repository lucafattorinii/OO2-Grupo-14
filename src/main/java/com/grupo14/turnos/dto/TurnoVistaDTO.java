
package com.grupo14.turnos.dto;

import java.time.LocalTime;

import com.grupo14.turnos.modelo.EstadoTurno;
import com.grupo14.turnos.modelo.Fecha;

public record TurnoVistaDTO(
    Long id,
    Fecha fecha,
    LocalTime hora,
    EstadoTurno estado,
    String nombreCliente,
    String nombreServicio
) {}

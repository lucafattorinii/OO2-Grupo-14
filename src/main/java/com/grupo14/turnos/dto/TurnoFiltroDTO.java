package com.grupo14.turnos.dto;

import java.time.LocalDate;

public record TurnoFiltroDTO(
    LocalDate fechaDesde,
    LocalDate fechaHasta,
    Long clienteId,
    Long servicioId,
    String estado,
    String nombreCliente
) {
    public boolean tieneFiltros() {
        return fechaDesde != null || 
               fechaHasta != null || 
               clienteId != null || 
               servicioId != null || 
               (estado != null && !estado.isEmpty()) ||
               (nombreCliente != null && !nombreCliente.isEmpty());
    }
}

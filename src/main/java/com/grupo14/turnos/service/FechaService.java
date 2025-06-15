package com.grupo14.turnos.service;

import java.time.LocalDate;
import java.util.List;

import com.grupo14.turnos.dto.FechaDTO;
import com.grupo14.turnos.modelo.Fecha;

public interface FechaService {
	
	void crear(FechaDTO dto);
	Fecha crear(LocalDate localDate, Long direccionId);
    FechaDTO obtenerPorId(Long id);
    List<FechaDTO> listarTodas();
    void eliminar(Long id);

}

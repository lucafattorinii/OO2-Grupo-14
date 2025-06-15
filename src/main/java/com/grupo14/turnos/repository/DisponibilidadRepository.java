package com.grupo14.turnos.repository;

import com.grupo14.turnos.modelo.Disponibilidad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DisponibilidadRepository extends JpaRepository<Disponibilidad, Long> {
	List<Disponibilidad> findByServicios_IdServicio(Long servicioId);
}

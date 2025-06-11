package com.grupo14.turnos.repository;

import com.grupo14.turnos.modelo.Disponibilidad;

//import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DisponibilidadRepository extends JpaRepository<Disponibilidad, Integer> {
	//List<Disponibilidad> findByServiciosId(Integer servicioId);
}

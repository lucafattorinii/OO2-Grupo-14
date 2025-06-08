package com.grupo14.turnos.repository;

import com.grupo14.turnos.modelo.Especificacion;
import com.grupo14.turnos.modelo.Rubro;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EspecificacionRepository extends JpaRepository<Especificacion, Integer> {
	
	boolean existsByServicio_IdServicioAndDireccion_IdDireccionAndRubro(Long servicioId, Integer direccionId, Rubro rubro);


}
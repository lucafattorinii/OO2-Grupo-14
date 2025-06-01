package com.grupo14.turnos.repository;

import com.grupo14.turnos.modelo.Especificacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EspecificacionRepository extends JpaRepository<Especificacion, Integer> {
}
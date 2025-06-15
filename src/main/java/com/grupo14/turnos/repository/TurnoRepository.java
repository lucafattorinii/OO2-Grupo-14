package com.grupo14.turnos.repository;

import com.grupo14.turnos.modelo.Direccion;
import com.grupo14.turnos.modelo.Disponibilidad;
import com.grupo14.turnos.modelo.EstadoTurno;
import com.grupo14.turnos.modelo.Fecha;
import com.grupo14.turnos.modelo.Turno;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalTime;
import java.util.List;

public interface TurnoRepository extends JpaRepository<Turno, Long> {
	
	List<Turno> findByEstado(String estado);
    List<Turno> findByEstado(EstadoTurno estado);
    List<Turno> findByClienteId(Long clienteId);
    
    boolean existsByFechaAndHoraAndDisponibilidadAndFecha_Direccion(
    	    Fecha fecha,
    	    LocalTime hora,
    	    Disponibilidad disponibilidad,
    	    Direccion direccion
    	);

}
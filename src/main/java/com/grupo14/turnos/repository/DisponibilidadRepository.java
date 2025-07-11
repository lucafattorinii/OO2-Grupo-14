package com.grupo14.turnos.repository;

import com.grupo14.turnos.modelo.DiaSemana;
import com.grupo14.turnos.modelo.Disponibilidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalTime;
import java.util.List;

public interface DisponibilidadRepository extends JpaRepository<Disponibilidad, Long> {
	List<Disponibilidad> findByServicios_IdServicio(Long servicioId);
	@Query("SELECT DISTINCT d FROM Disponibilidad d LEFT JOIN FETCH d.servicios")
	List<Disponibilidad> findAllWithServicios();
	
	
    boolean existsByServicios_IdServicioAndDiaSemanaAndHoraInicioAndHoraFin(
            Long servicioId, DiaSemana diaSemana, LocalTime horaInicio, LocalTime horaFin);
            
    List<Disponibilidad> findByDiaSemana(DiaSemana diaSemana);

}

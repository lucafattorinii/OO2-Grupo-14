package com.grupo14.turnos.repository;

import com.grupo14.turnos.modelo.Servicio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ServicioRepository extends JpaRepository<Servicio, Long> {
    //List<Servicio> findByPrestadorId(Long prestadorId);
    List<Servicio> findByPrestador_Id(Long prestadorId);


    
}

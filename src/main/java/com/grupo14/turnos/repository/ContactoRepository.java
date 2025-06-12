package com.grupo14.turnos.repository;

import com.grupo14.turnos.modelo.Contacto;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactoRepository extends JpaRepository<Contacto, Long> {
	Optional<Contacto> findByUsuarioId(Long usuarioId);
}
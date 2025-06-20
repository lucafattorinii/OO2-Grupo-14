package com.grupo14.turnos.repository;

import com.grupo14.turnos.modelo.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByDni(Long dni);
    Optional<Cliente> findTopByOrderByNumeroClienteDesc();
}
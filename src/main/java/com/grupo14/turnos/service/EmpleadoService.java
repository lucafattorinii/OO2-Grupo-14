package com.grupo14.turnos.service;

import com.grupo14.turnos.dto.EmpleadoDTO;
import com.grupo14.turnos.exception.RecursoNoEncontradoException;
import com.grupo14.turnos.modelo.Empleado;
import com.grupo14.turnos.repository.EmpleadoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmpleadoService {
    private final EmpleadoRepository repo;

    public EmpleadoService(EmpleadoRepository repo) {
        this.repo = repo;
    }

    public EmpleadoDTO obtenerPorId(Integer id) {
        Empleado e = repo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Empleado no encontrado: " + id));
        return new EmpleadoDTO(e.getId(), e.getNombre(), e.getApellido(), e.getPuestoCargo());
    }

    public List<EmpleadoDTO> listarTodos() {
        return repo.findAll().stream()
                .map(e -> new EmpleadoDTO(e.getId(), e.getNombre(), e.getApellido(), e.getPuestoCargo()))
                .collect(Collectors.toList());
    }

    public EmpleadoDTO crear(EmpleadoDTO dto) {
        Empleado e = new Empleado();
        e.setNombre(dto.nombre());
        e.setApellido(dto.apellido());
        e.setPuestoCargo(dto.puestoCargo());
        Empleado guardado = repo.save(e);
        return new EmpleadoDTO(guardado.getId(), guardado.getNombre(), guardado.getApellido(), guardado.getPuestoCargo());
    }
}

package com.grupo14.turnos.service;

import com.grupo14.turnos.dto.PrestadorDTO;
import com.grupo14.turnos.exception.RecursoNoEncontradoException;
import com.grupo14.turnos.modelo.Prestador;
import com.grupo14.turnos.repository.PrestadorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrestadorService {

    private final PrestadorRepository repo;

    public PrestadorService(PrestadorRepository repo) {
        this.repo = repo;
    }

    public PrestadorDTO obtenerPorId(Integer id) {
        Prestador p = repo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Prestador no encontrado: " + id));
        return new PrestadorDTO(p.getId(), p.getRazonSocial(), p.getHabilitado());
    }

    public List<PrestadorDTO> listarTodos() {
        return repo.findAll().stream()
                .map(p -> new PrestadorDTO(p.getId(), p.getRazonSocial(), p.getHabilitado()))
                .collect(Collectors.toList());
    }

    public PrestadorDTO crear(PrestadorDTO dto) {
        Prestador p = new Prestador();
        p.setRazonSocial(dto.razonSocial());
        p.setHabilitado(dto.habilitado());
        Prestador guardado = repo.save(p);
        return new PrestadorDTO(guardado.getId(), guardado.getRazonSocial(), guardado.getHabilitado());
    }
}

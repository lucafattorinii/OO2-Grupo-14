package com.grupo14.turnos.service;

import com.grupo14.turnos.dto.ServicioDTO;
import com.grupo14.turnos.exception.RecursoNoEncontradoException;
import com.grupo14.turnos.modelo.Prestador;
import com.grupo14.turnos.modelo.Servicio;
import com.grupo14.turnos.repository.PrestadorRepository;
import com.grupo14.turnos.repository.ServicioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServicioService {

    private final ServicioRepository repo;
    private final PrestadorRepository prestadorRepo;

    public ServicioService(ServicioRepository repo, PrestadorRepository prestadorRepo) {
        this.repo = repo;
        this.prestadorRepo = prestadorRepo;
    }

    public ServicioDTO obtenerPorId(Long id) {
        Servicio s = repo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Servicio no encontrado: " + id));
        return new ServicioDTO(
                s.getIdServicio(),
                s.getNombre(),
                s.getDuracionMin(),
                s.getPrecio(),
                s.getPrestador().getId()
        );
    }

    public List<ServicioDTO> listarTodos() {
        return repo.findAll().stream()
                .map(s -> new ServicioDTO(
                        s.getIdServicio(),
                        s.getNombre(),
                        s.getDuracionMin(),
                        s.getPrecio(),
                        s.getPrestador().getId()
                ))
                .collect(Collectors.toList());
    }

    public ServicioDTO crear(ServicioDTO dto) {
        Prestador p = prestadorRepo.findById(dto.prestadorId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Prestador no encontrado: " + dto.prestadorId()));

        Servicio s = new Servicio();
        s.setNombre(dto.nombre());
        s.setDuracionMin(dto.duracionMin());
        s.setPrecio(dto.precio());
        s.setPrestador(p);

        Servicio guardado = repo.save(s);
        return new ServicioDTO(
                guardado.getIdServicio(),
                guardado.getNombre(),
                guardado.getDuracionMin(),
                guardado.getPrecio(),
                p.getId()
        );
    }
}

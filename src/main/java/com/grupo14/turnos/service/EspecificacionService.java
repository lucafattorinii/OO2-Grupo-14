package com.grupo14.turnos.service;

import com.grupo14.turnos.dto.EspecificacionDTO;
import com.grupo14.turnos.exception.RecursoNoEncontradoException;
import com.grupo14.turnos.modelo.Direccion;
import com.grupo14.turnos.modelo.Especificacion;
import com.grupo14.turnos.modelo.Servicio;
import com.grupo14.turnos.repository.DireccionRepository;
import com.grupo14.turnos.repository.EspecificacionRepository;
import com.grupo14.turnos.repository.ServicioRepository;
import com.grupo14.turnos.modelo.Rubro;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class EspecificacionService {

    private final EspecificacionRepository repo;
    private final ServicioRepository servicioRepo;
    private final DireccionRepository direccionRepo;

    public EspecificacionService(
        EspecificacionRepository repo,
        ServicioRepository servicioRepo,
        DireccionRepository direccionRepo
    ) {
        this.repo = repo;
        this.servicioRepo = servicioRepo;
        this.direccionRepo = direccionRepo;
    }

    public EspecificacionDTO obtenerPorId(Integer id) {
        Especificacion e = repo.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("Especificación no encontrada: " + id));
        return new EspecificacionDTO(
            e.getId(),
            e.getServicio().getIdServicio(),
            e.getRubro().name(),
            e.getDetalles(),
            e.getDireccion().getIdDireccion()
        );
    }

    public List<EspecificacionDTO> listarTodos() {
        return repo.findAll().stream()
            .map(e -> new EspecificacionDTO(
                e.getId(),
                e.getServicio().getIdServicio(),
                e.getRubro().name(),
                e.getDetalles(),
                e.getDireccion().getIdDireccion()
            ))
            .collect(Collectors.toList());
    }

    public EspecificacionDTO crear(EspecificacionDTO dto) {
        Servicio servicio = servicioRepo.findById(dto.servicioId())
            .orElseThrow(() -> new RecursoNoEncontradoException("Servicio no encontrado: " + dto.servicioId()));
        Direccion direccion = direccionRepo.findById(dto.direccionId())
            .orElseThrow(() -> new RecursoNoEncontradoException("Dirección no encontrada: " + dto.direccionId()));

        Especificacion e = new Especificacion();
        e.setServicio(servicio);
        e.setRubro(Rubro.valueOf(dto.rubro().toUpperCase()));
        e.setDetalles(dto.detalles());
        e.setDireccion(direccion);

        Especificacion guardada = repo.save(e);
        return new EspecificacionDTO(
            guardada.getId(),
            servicio.getIdServicio(),
            guardada.getRubro().name(),
            guardada.getDetalles(),
            direccion.getIdDireccion()
        );
    }
}

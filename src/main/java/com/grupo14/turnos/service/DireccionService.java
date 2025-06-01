package com.grupo14.turnos.service;

import com.grupo14.turnos.dto.DireccionDTO;
import com.grupo14.turnos.exception.RecursoNoEncontradoException;
import com.grupo14.turnos.modelo.Direccion;
import com.grupo14.turnos.repository.DireccionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DireccionService {

    private final DireccionRepository repo;

    public DireccionService(DireccionRepository repo) {
        this.repo = repo;
    }

    public List<DireccionDTO> listarTodos() {
        return repo.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public DireccionDTO obtenerPorId(Integer id) {
        Direccion dir = repo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Dirección no encontrada: " + id));
        return convertirADTO(dir);
    }

    public DireccionDTO crear(DireccionDTO dto) {
        Direccion dir = new Direccion();
        dir.setPais(dto.pais());
        dir.setProvincia(dto.provincia());
        dir.setCiudad(dto.ciudad());
        dir.setCalle(dto.calle());
        dir.setNumeroCalle(dto.numeroCalle());
        dir.setCodigoPostal(dto.codigoPostal());

        Direccion guardada = repo.save(dir);
        return convertirADTO(guardada);
    }

    private DireccionDTO convertirADTO(Direccion dir) {
        return new DireccionDTO(
                dir.getIdDireccion(),
                dir.getPais(),
                dir.getProvincia(),
                dir.getCiudad(),
                dir.getCalle(),
                dir.getNumeroCalle(),
                dir.getCodigoPostal()
        );
    }
}

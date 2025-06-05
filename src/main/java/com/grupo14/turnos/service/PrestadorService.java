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
        return convertirADTO(p);
    }

    public List<PrestadorDTO> listarTodos() {
        return repo.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public PrestadorDTO crear(PrestadorDTO dto) {
        Prestador p = new Prestador();
        actualizarPrestadorDesdeDTO(p, dto);
        Prestador guardado = repo.save(p);
        return convertirADTO(guardado);
    }
    
    public PrestadorDTO actualizar(Integer id, PrestadorDTO dto) {
        Prestador p = repo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Prestador no encontrado: " + id));
        actualizarPrestadorDesdeDTO(p, dto);
        Prestador guardado = repo.save(p);
        return convertirADTO(guardado);
    }
    
    public void eliminar(Integer id) {
        if (!repo.existsById(id)) {
            throw new RecursoNoEncontradoException("Prestador no encontrado: " + id);
        }
        repo.deleteById(id);
    }
    
    private PrestadorDTO convertirADTO(Prestador p) {
        return new PrestadorDTO(
            p.getId(), 
            p.getEmail(), 
            p.getContrasena(), 
            p.getRazonSocial(), 
            p.getHabilitado()
        );
    }
    
    private void actualizarPrestadorDesdeDTO(Prestador p, PrestadorDTO dto) {
        p.setEmail(dto.email());
        p.setContrasena(dto.contrasena());
        p.setRazonSocial(dto.razonSocial());
        p.setHabilitado(dto.habilitado());
    }
    
    public void actualizarPrestador(Integer id, String email, String contrasena, 
                                   String razonSocial, Boolean habilitado) {
        Prestador p = repo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Prestador no encontrado: " + id));
        p.setEmail(email);
        p.setContrasena(contrasena);
        p.setRazonSocial(razonSocial);
        p.setHabilitado(habilitado);
        repo.save(p);
    }
}


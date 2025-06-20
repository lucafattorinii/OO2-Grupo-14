package com.grupo14.turnos.service;

import com.grupo14.turnos.dto.PrestadorDTO;
import com.grupo14.turnos.exception.RecursoNoEncontradoException;
import com.grupo14.turnos.exception.TurnoNoEncontradoException;
import com.grupo14.turnos.modelo.Prestador;
import com.grupo14.turnos.modelo.Rol;
import com.grupo14.turnos.repository.PrestadorRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PrestadorService {

    private final PrestadorRepository repo;
    private final PasswordEncoder passwordEncoder;

    public PrestadorService(PrestadorRepository repo, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }

    public PrestadorDTO obtenerPorId(long id) {
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

    public PrestadorDTO actualizar(long id, PrestadorDTO dto) {
        Prestador p = repo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Prestador no encontrado: " + id));
        actualizarPrestadorDesdeDTO(p, dto);
        Prestador guardado = repo.save(p);
        return convertirADTO(guardado);
    }
    
    
    public void actualizar(PrestadorDTO dto) {
        Prestador prestador = repo.findById(dto.getId())
            .orElseThrow(() -> new TurnoNoEncontradoException("Prestador no encontrado con ID: " + dto.getId()));

        // Actualizamos solo los campos que se pueden editar
        prestador.setEmail(dto.getEmail());
        prestador.setRazonSocial(dto.getRazonSocial());

        repo.save(prestador);
    }




    public void eliminar(long id) {
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
        p.setEmail(dto.getEmail());
        if (dto.getContrasena() != null && !dto.getContrasena().isEmpty()) {
            p.setContrasena(passwordEncoder.encode(dto.getContrasena())); // ENCRIPTA la contraseña
        }
        p.setRazonSocial(dto.getRazonSocial());
        p.setHabilitado(dto.isHabilitado());
        p.setRol(Rol.PRESTADOR);
    }

    public void actualizarPrestador(long id, String email, String contrasena, String razonSocial, Boolean habilitado) {
        Prestador p = repo.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("Prestador no encontrado: " + id));
        p.setEmail(email);
        if (contrasena != null && !contrasena.isEmpty()) {
            p.setContrasena(passwordEncoder.encode(contrasena));
        }
        p.setRazonSocial(razonSocial);
        p.setHabilitado(habilitado);
        p.setRol(Rol.PRESTADOR);
        repo.save(p);
    }

    public PrestadorDTO buscarPorEmail(String email) {
        Prestador prestador = repo.findByEmail(email)
            .orElseThrow(() -> new RecursoNoEncontradoException("Prestador no encontrado con email: " + email));
        return convertirADTO(prestador);
    }

    public Optional<PrestadorDTO> obtenerUnico() {
        return repo.findTopByOrderByIdAsc()
                   .map(this::convertirADTO);
    }
}

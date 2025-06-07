package com.grupo14.turnos.service;

import com.grupo14.turnos.dto.EmpleadoDTO;
import com.grupo14.turnos.exception.RecursoNoEncontradoException;
import com.grupo14.turnos.modelo.Empleado;
import com.grupo14.turnos.repository.EmpleadoRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmpleadoService {
    private final EmpleadoRepository repo;
    private final PasswordEncoder passwordEncoder;

    public EmpleadoService(EmpleadoRepository repo, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }

    public EmpleadoDTO obtenerPorId(Integer id) {
        Empleado e = repo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Empleado no encontrado: " + id));
        return convertirADTO(e);
    }

    public List<EmpleadoDTO> listarTodos() {
        return repo.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public EmpleadoDTO crear(EmpleadoDTO dto) {
        Empleado e = new Empleado();
        actualizarEmpleadoDesdeDTO(e, dto);
        Empleado guardado = repo.save(e);
        return convertirADTO(guardado);
    }
    
    public EmpleadoDTO actualizar(Integer id, EmpleadoDTO dto) {
        Empleado e = repo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Empleado no encontrado: " + id));
        actualizarEmpleadoDesdeDTO(e, dto);
        Empleado guardado = repo.save(e);
        return convertirADTO(guardado);
    }
    
    public void eliminar(Integer id) {
        if (!repo.existsById(id)) {
            throw new RecursoNoEncontradoException("Empleado no encontrado: " + id);
        }
        repo.deleteById(id);
    }
    
    private EmpleadoDTO convertirADTO(Empleado e) {
        return new EmpleadoDTO(
            e.getId(), 
            e.getEmail(), 
            e.getContrasena(), 
            e.getNombre(), 
            e.getApellido(), 
            e.getDni(), 
            e.getCuit(), 
            e.getLegajo(), 
            e.getPuestoCargo()
        );
    }
    
    private void actualizarEmpleadoDesdeDTO(Empleado e, EmpleadoDTO dto) {
        e.setEmail(dto.email());

        if (dto.contrasena() != null && !dto.contrasena().isEmpty()) {
            e.setContrasena(passwordEncoder.encode(dto.contrasena())); // Encripta la contraseña
        }

        e.setNombre(dto.nombre());
        e.setApellido(dto.apellido());
        e.setDni(dto.dni());
        e.setCuit(dto.cuit());
        e.setLegajo(dto.legajo());
        e.setPuestoCargo(dto.puestoCargo());
    }
    
    public void actualizarEmpleado(Integer id, String email, String contrasena, Long dni, String nombre,
            String apellido, Long cuit, Integer legajo, String puestoCargo) {
			Empleado e = repo.findById(id)
			.orElseThrow(() -> new RecursoNoEncontradoException("Empleado no encontrado: " + id));
			
			e.setEmail(email);
			
			if (contrasena != null && !contrasena.isEmpty()) {
				e.setContrasena(passwordEncoder.encode(contrasena)); // Encripta la contraseña
			}
			
			e.setNombre(nombre);
			e.setApellido(apellido);
			e.setDni(dni);
			e.setCuit(cuit);
			e.setLegajo(legajo);
			e.setPuestoCargo(puestoCargo);
			repo.save(e);
}
}


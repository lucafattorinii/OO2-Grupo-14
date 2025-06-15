package com.grupo14.turnos.service;

import com.grupo14.turnos.dto.UsuarioDTO;
import com.grupo14.turnos.exception.EmailInvalidoException;
import com.grupo14.turnos.exception.RecursoNoEncontradoException;
import com.grupo14.turnos.modelo.Rol;
import com.grupo14.turnos.modelo.Usuario;
import com.grupo14.turnos.repository.UsuarioRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

	
    private final UsuarioRepository repo;
    private final BCryptPasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository repo) {
        this.repo = repo;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public List<UsuarioDTO> listarTodos() {
        return repo.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public UsuarioDTO obtenerPorId(long id) {
        Usuario usuario = repo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado: " + id));
        return convertirADTO(usuario);
    }

    public UsuarioDTO crear(UsuarioDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setEmail(dto.email());
        usuario.setContrasena(passwordEncoder.encode(dto.contrasena()));

        Usuario guardado = repo.save(usuario);
        return convertirADTO(guardado);
    }
    
    public UsuarioDTO actualizar(long id, UsuarioDTO dto) {
        Usuario usuario = repo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado: " + id));
        
        usuario.setEmail(dto.email());
        
        // Solo actualizar la contraseña si se proporciona una nueva
        if (dto.contrasena() != null && !dto.contrasena().isEmpty()) {
            usuario.setContrasena(passwordEncoder.encode(dto.contrasena()));
        }
        
        Usuario guardado = repo.save(usuario);
        return convertirADTO(guardado);
    }
    
    public void eliminar(long id) {
        if (!repo.existsById(id)) {
            throw new RecursoNoEncontradoException("Usuario no encontrado: " + id);
        }
        repo.deleteById(id);
    }
    
    public void actualizarUsuario(long id, String email, String contrasena) {
        Usuario usuario = repo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado: " + id));
        
        usuario.setEmail(email);
        
        // Solo actualizar la contraseña si se proporciona una nueva
        if (contrasena != null && !contrasena.isEmpty()) {
            usuario.setContrasena(passwordEncoder.encode(contrasena));
        }
        
        repo.save(usuario);
    }

    private UsuarioDTO convertirADTO(Usuario usuario) {
        return new UsuarioDTO(
                usuario.getId(),
                usuario.getEmail(),
                "", // No devolver la contraseña
                determinarRol(usuario)
        );
    }
    
    private Rol determinarRol(Usuario usuario) {
        if (usuario instanceof com.grupo14.turnos.modelo.Cliente) {
            return Rol.CLIENTE;
        } else if (usuario instanceof com.grupo14.turnos.modelo.Empleado) {
            return Rol.EMPLEADO;
        } else if (usuario instanceof com.grupo14.turnos.modelo.Prestador) {
            return Rol.PRESTADOR;
        } else {
            return usuario.getRol();  // Para casos como ADMIN u otros
        }
    }
    
    public UsuarioDTO login(String email, String contrasena) {

        // Validar formato del email
        if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new EmailInvalidoException("El email ingresado no tiene un formato válido.");
        }

        Usuario usuario = repo.findByEmail(email)
            .orElseThrow(() -> new RecursoNoEncontradoException("Credenciales inválidas"));

        if (!passwordEncoder.matches(contrasena, usuario.getContrasena())) {
            throw new RecursoNoEncontradoException("Credenciales inválidas");
        }

        return convertirADTO(usuario);
    }

    
    public UsuarioDTO buscarPorEmail(String email) {
        Usuario usuario = repo.findByEmail(email)
            .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con email: " + email));

        return new UsuarioDTO(
            usuario.getId(),
            usuario.getEmail(),
            usuario.getContrasena(), 
            usuario.getRol() 
        );
    }

}


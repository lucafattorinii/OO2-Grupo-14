package com.grupo14.turnos.service;

import com.grupo14.turnos.dto.ContactoDTO;
import com.grupo14.turnos.exception.RecursoNoEncontradoException;
import com.grupo14.turnos.modelo.Contacto;
import com.grupo14.turnos.modelo.Direccion;
import com.grupo14.turnos.modelo.Usuario;
import com.grupo14.turnos.repository.ContactoRepository;
import com.grupo14.turnos.repository.DireccionRepository;
import com.grupo14.turnos.repository.UsuarioRepository;

import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class ContactoService {

    private final ContactoRepository contactoRepo;
    private final UsuarioRepository usuarioRepo;
    private final DireccionRepository direccionRepo;

    public ContactoService(ContactoRepository contactoRepo, UsuarioRepository usuarioRepo, DireccionRepository direccionRepo) {
        this.contactoRepo = contactoRepo;
        this.usuarioRepo = usuarioRepo;
        this.direccionRepo = direccionRepo;
    }

    public ContactoDTO crearContactoParaUsuario(Long usuarioId, ContactoDTO dto) {
        Usuario usuario = usuarioRepo.findById(usuarioId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado: " + usuarioId));

        Direccion direccion = null;
        if (dto.direccionId() != null) {
            direccion = direccionRepo.findById(dto.direccionId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Dirección no encontrada: " + dto.direccionId()));
        }

        Contacto contacto = contactoRepo.findByUsuarioId(usuarioId)
                .orElse(Contacto.builder()
                    .usuario(usuario)
                    .build());

        contacto.setTelefono(dto.telefono());
        contacto.setDireccion(direccion);

        contactoRepo.save(contacto);

        return new ContactoDTO(contacto.getId(), contacto.getTelefono(),
                contacto.getDireccion() != null ? contacto.getDireccion().getIdDireccion() : null);
    }

    public ContactoDTO obtenerPorUsuarioId(Long usuarioId) {
        return contactoRepo.findByUsuarioId(usuarioId)
                .map(c -> new ContactoDTO(c.getId(), c.getTelefono(),
                        c.getDireccion() != null ? c.getDireccion().getIdDireccion() : null))
                .orElseThrow(() -> new RecursoNoEncontradoException("Contacto no encontrado para usuario ID: " + usuarioId));
    }
    
    
    public Optional<ContactoDTO> obtenerPorUsuarioIdOptional(Long usuarioId) {
        return contactoRepo.findByUsuarioId(usuarioId)
            .map(this::convertirADTO);
    }
    

    public void eliminarPorUsuarioId(Long usuarioId) {
        contactoRepo.findByUsuarioId(usuarioId).ifPresent(contactoRepo::delete);
    }
    
    private ContactoDTO convertirADTO(Contacto contacto) {
        ContactoDTO dto = null;
        if (contacto != null) {
            Long direccionId = (contacto.getDireccion() != null) ? contacto.getDireccion().getIdDireccion() : null;
            dto = new ContactoDTO(contacto.getId(), contacto.getTelefono(), direccionId);
        }
        return dto;
    }
}

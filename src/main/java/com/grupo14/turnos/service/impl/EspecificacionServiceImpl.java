package com.grupo14.turnos.service.impl;

import com.grupo14.turnos.dto.EspecificacionDTO;
import com.grupo14.turnos.exception.RecursoNoEncontradoException;
import com.grupo14.turnos.modelo.Direccion;
import com.grupo14.turnos.modelo.Especificacion;
import com.grupo14.turnos.modelo.Servicio;
import com.grupo14.turnos.repository.DireccionRepository;
import com.grupo14.turnos.repository.EspecificacionRepository;
import com.grupo14.turnos.repository.ServicioRepository;
import com.grupo14.turnos.service.EspecificacionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EspecificacionServiceImpl implements EspecificacionService {

    private final EspecificacionRepository repo;
    private final ServicioRepository servicioRepo;
    private final DireccionRepository direccionRepo;


    public EspecificacionServiceImpl(EspecificacionRepository repo, ServicioRepository servicioRepo,DireccionRepository direccionRepo) {
        this.repo = repo;
        this.servicioRepo = servicioRepo;
        this.direccionRepo = direccionRepo;
    }

    @Override
    public List<EspecificacionDTO> listarTodos() {
        return repo.findAll().stream().map(e -> {
            Servicio servicio = e.getServicio();  // no puede ser null
            if (servicio == null) {
                throw new IllegalStateException("La especificación con id " + e.getId() + " no tiene un servicio asignado.");
            }

            String nombreServicio = servicio.getNombre();

            String direccionTexto = Optional.ofNullable(e.getDireccion())
                    .map(d -> d.getCalle() + " " + d.getNumeroCalle())
                    .orElse("Dirección no disponible");

            Long direccionId = Optional.ofNullable(e.getDireccion())
                    .map(Direccion::getIdDireccion)
                    .orElse(null);

            return new EspecificacionDTO(
                    e.getId(),
                    servicio.getIdServicio(),
                    e.getRubro(),
                    e.getDetalles(),
                    direccionId,
                    nombreServicio,
                    direccionTexto
            );
        }).collect(Collectors.toList());
    }
    

    public boolean yaExiste(EspecificacionDTO dto) {
        return repo.existsByServicio_IdServicioAndDireccion_IdDireccionAndRubro(
        		  dto.servicioId(), dto.direccionId(), dto.rubro()
        		);

    }

    
    
    @Override
    public void crear(EspecificacionDTO dto) {
        Especificacion e = new Especificacion();
        e.setId(dto.id());
        e.setRubro(dto.rubro());
        e.setDetalles(dto.detalles());
        direccionRepo.findById(dto.direccionId()).ifPresent(e::setDireccion);

        servicioRepo.findById(dto.servicioId()).ifPresent(e::setServicio);
        
        if (yaExiste(dto)) {
            throw new RuntimeException("Ya existe una especificación con esos datos");
        }

        repo.save(e);
    }

    @Override
    public void actualizar(EspecificacionDTO dto) {
        Optional<Especificacion> optional = repo.findById(dto.id());
        if (optional.isPresent()) {
            Especificacion e = optional.get();
            e.setRubro(dto.rubro());
            e.setDetalles(dto.detalles());
            direccionRepo.findById(dto.direccionId()).ifPresent(e::setDireccion);
            servicioRepo.findById(dto.servicioId()).ifPresent(e::setServicio);
            repo.save(e);
        }
    }

    @Override
    public void eliminar(long id) {
        repo.deleteById(id);
    }

    @Override
    public Optional<Especificacion> buscarPorId(long id) {
        return repo.findById(id);
    }
    
    private EspecificacionDTO toDTO(Especificacion e) {
        return new EspecificacionDTO(
            e.getId(),
            e.getServicio().getIdServicio(),
            e.getRubro(),
            e.getDetalles(),
            e.getDireccion() != null ? e.getDireccion().getIdDireccion() : null,
            e.getServicio().getNombre(),
            e.getDireccion() != null ? e.getDireccion().toString() : null
        );
    }
    
    @Override
    public EspecificacionDTO crearSinId(EspecificacionDTO dto) {
        Servicio servicio = servicioRepo.findById(dto.servicioId())
            .orElseThrow(() -> new IllegalArgumentException("Servicio no encontrado con id: " + dto.servicioId()));

        Direccion direccion = null;
        if (dto.direccionId() != null) {
            direccion = direccionRepo.findById(dto.direccionId())
                .orElseThrow(() -> new IllegalArgumentException("Dirección no encontrada con id: " + dto.direccionId()));
        }

        Especificacion especificacion = Especificacion.builder()
            .servicio(servicio)
            .direccion(direccion)
            .rubro(dto.rubro())
            .detalles(dto.detalles())
            .build();

        // asignamos bidireccionalmente
        servicio.setEspecificacion(especificacion);

        // guardamos el servicio (con cascada se guarda la especificacion)
        servicioRepo.save(servicio);

        // ahora especificacion tiene id asignado
        return toDTO(especificacion);
    }
}

    
    
    
    


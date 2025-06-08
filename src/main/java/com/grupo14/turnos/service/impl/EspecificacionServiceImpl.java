package com.grupo14.turnos.service.impl;

import com.grupo14.turnos.dto.EspecificacionDTO;
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
            String nombreServicio = Optional.ofNullable(e.getServicio())
                    .map(Servicio::getNombre)
                    .orElse("Servicio no disponible");

            Integer direccionId = Optional.ofNullable(e.getDireccion())
                    .map(Direccion::getIdDireccion)
                    .orElse(null);

            return new EspecificacionDTO(
                    e.getId(),
                    e.getServicio() != null ? e.getServicio().getIdServicio() : null,
                    e.getRubro(),
                    e.getDetalles(),
                    direccionId,
                    nombreServicio
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
    public void eliminar(Integer id) {
        repo.deleteById(id);
    }

    @Override
    public Optional<Especificacion> buscarPorId(Integer id) {
        return repo.findById(id);
    }
}

    
    
    
    


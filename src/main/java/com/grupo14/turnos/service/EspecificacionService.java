package com.grupo14.turnos.service;

import com.grupo14.turnos.dto.EspecificacionDTO;

import com.grupo14.turnos.repository.EspecificacionRepository;

import com.grupo14.turnos.exception.RecursoNoEncontradoException;
import com.grupo14.turnos.modelo.Direccion;
import com.grupo14.turnos.modelo.Especificacion;
import com.grupo14.turnos.modelo.Servicio;
import com.grupo14.turnos.repository.DireccionRepository;
import com.grupo14.turnos.repository.ServicioRepository;
import com.grupo14.turnos.modelo.Rubro;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public interface EspecificacionService {

    /*private final EspecificacionRepository repo;
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
    }*/

    /*public EspecificacionDTO obtenerPorId(Integer id) {
        Especificacion e = repo.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("Especificación no encontrada: " + id));
        return convertirADTO(e);
    }*/

	List<EspecificacionDTO> listarTodos();
	void crear(EspecificacionDTO dto);

    void actualizar(EspecificacionDTO dto);

    void eliminar(Integer id);

    Optional<Especificacion> buscarPorId(Integer id);
    
   /// public void actualizarEspecificacion(Integer id, Long servicioId, Rubro rubro, String detalles, Integer direccionId);
    
    /*private EspecificacionDTO convertirADTO(Especificacion e) {
        return new EspecificacionDTO(
            e.getId(),
            e.getServicio().getIdServicio(),
            e.getRubro(),
            e.getDetalles(),
            e.getDireccion().getIdDireccion(), null
        );
    }
    
    public List<Rubro> listarRubros() {
        return List.of(Rubro.values());
    } */
}


package com.grupo14.turnos.service;

import com.grupo14.turnos.dto.ServicioDTO;
import com.grupo14.turnos.exception.RecursoNoEncontradoException;
import com.grupo14.turnos.exception.ServicioEnUsoException;
import com.grupo14.turnos.modelo.Prestador;
import com.grupo14.turnos.modelo.Servicio;
import com.grupo14.turnos.repository.PrestadorRepository;
import com.grupo14.turnos.repository.TurnoRepository;
import com.grupo14.turnos.repository.ServicioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ServicioService {
    private final TurnoRepository turnoRepository;

    private final ServicioRepository repo;
    private final PrestadorRepository prestadorRepo;
    
    

    public ServicioService(ServicioRepository repo, PrestadorRepository prestadorRepo, TurnoRepository turnoRepository) {
        this.repo = repo;
        this.prestadorRepo = prestadorRepo;
        this.turnoRepository = turnoRepository;
    }
    
    

    public ServicioDTO obtenerPorId(Long id) {
        Servicio s = repo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Servicio no encontrado: " + id));
        return convertirADTO(s);
    }

    public List<ServicioDTO> listarTodos() {
        return repo.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public ServicioDTO crear(ServicioDTO dto) {
        Prestador p = prestadorRepo.findById(dto.prestadorId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Prestador no encontrado: " + dto.prestadorId()));

        Servicio s = new Servicio();
        s.setNombre(dto.nombre());
        s.setDuracionMin(dto.duracionMin());
        s.setPrecio(dto.precio());
        s.setPrestador(p);

        Servicio guardado = repo.save(s);
        return convertirADTO(guardado);
    }
    
    public ServicioDTO actualizar(Long id, ServicioDTO dto) {
        Servicio s = repo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Servicio no encontrado: " + id));
        
        Prestador p = prestadorRepo.findById(dto.prestadorId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Prestador no encontrado: " + dto.prestadorId()));
        
        s.setNombre(dto.nombre());
        s.setDuracionMin(dto.duracionMin());
        s.setPrecio(dto.precio());
        s.setPrestador(p);
        
        Servicio guardado = repo.save(s);
        return convertirADTO(guardado);
    }
    
    @Transactional
    public void eliminar(Long id) {
        if (!repo.existsById(id)) {
            throw new RecursoNoEncontradoException("Servicio no encontrado: " + id);
        }
        
        // Verificar si hay turnos no cancelados
        boolean tieneTurnosNoCancelados = turnoRepository.existsTurnoNoCanceladoByServicioId(id);
        
        if (tieneTurnosNoCancelados) {
            throw new ServicioEnUsoException("No se puede eliminar el servicio porque tiene turnos activos (PENDIENTES o CONFIRMADOS).");
        }
        
        // Eliminar turnos cancelados asociados al servicio
        turnoRepository.deleteTurnosCanceladosByServicioId(id);
        
        // Finalmente, eliminar el servicio
        repo.deleteById(id);
    }
    
    public void actualizarServicio(Long id, String nombre, Integer duracionMin, Double precio, long prestadorId) {
        Servicio s = repo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Servicio no encontrado: " + id));
        
        Prestador p = prestadorRepo.findById(prestadorId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Prestador no encontrado: " + prestadorId));
        
        s.setNombre(nombre);
        s.setDuracionMin(duracionMin);
        s.setPrecio(precio);
        s.setPrestador(p);
        
        repo.save(s);
    }
    
    public List<ServicioDTO> buscarPorPrestadorId(Long prestadorId) {
        List<Servicio> lista = repo.findByPrestador_Id(prestadorId);

        return lista.stream().map(s -> new ServicioDTO(
        	    s.getIdServicio(),
        	    s.getNombre(),
        	    s.getDuracionMin(),
        	    s.getPrecio(),
        	    s.getPrestador().getId()
        	)).toList();

    }


    
    private ServicioDTO convertirADTO(Servicio s) {
        return new ServicioDTO(
                s.getIdServicio(),
                s.getNombre(),
                s.getDuracionMin(),
                s.getPrecio(),
                s.getPrestador().getId()
        );
    }
    
    public List<ServicioDTO> listarServiciosDelUnicoPrestador() {
        Prestador prestador = prestadorRepo.findTopByOrderByIdAsc()
                .orElseThrow(() -> new RecursoNoEncontradoException("No hay ningÃºn prestador cargado."));
        
        return repo.findByPrestador_Id(prestador.getId()).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    public ServicioDTO crearServicioSinSincronizar(ServicioDTO dto) {
        Prestador p = prestadorRepo.findById(dto.prestadorId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Prestador no encontrado: " + dto.prestadorId()));

        Servicio s = new Servicio();
        s.setNombre(dto.nombre());
        s.setDuracionMin(dto.duracionMin());
        s.setPrecio(dto.precio());
        s.asignarPrestadorSinActualizarColeccion(p);

        Servicio guardado = repo.save(s);
        return convertirADTO(guardado);
    }
    
   
}
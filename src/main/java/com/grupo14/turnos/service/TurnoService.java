package com.grupo14.turnos.service;

import com.grupo14.turnos.dto.TurnoDTO;
import com.grupo14.turnos.exception.RecursoNoEncontradoException;
import com.grupo14.turnos.modelo.Cliente;
import com.grupo14.turnos.modelo.Disponibilidad;
import com.grupo14.turnos.modelo.Servicio;
import com.grupo14.turnos.modelo.Turno;
import com.grupo14.turnos.modelo.EstadoTurno;
import com.grupo14.turnos.repository.ClienteRepository;
import com.grupo14.turnos.repository.DisponibilidadRepository;
import com.grupo14.turnos.repository.ServicioRepository;
import com.grupo14.turnos.repository.TurnoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TurnoService {

    private final TurnoRepository repo;
    private final ClienteRepository cliRepo;
    private final ServicioRepository serRepo;
    private final DisponibilidadRepository disRepo;

    public TurnoService(
        TurnoRepository repo,
        ClienteRepository cliRepo,
        ServicioRepository serRepo,
        DisponibilidadRepository disRepo
    ) {
        this.repo = repo;
        this.cliRepo = cliRepo;
        this.serRepo = serRepo;
        this.disRepo = disRepo;
    }
    
    public TurnoDTO obtenerPorId(Integer id) {
        Turno t = repo.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("Turno no encontrado: " + id));
        
        return convertirADTO(t);
    }

    public List<TurnoDTO> listarTodos() {
        return repo.findAll().stream()
            .map(this::convertirADTO)
            .collect(Collectors.toList());
    }

    public List<TurnoDTO> listarPorEstado(String estado) {
        EstadoTurno estadoEnum = EstadoTurno.valueOf(estado.toUpperCase());

        return repo.findByEstado(estadoEnum).stream()
            .map(this::convertirADTO)
            .collect(Collectors.toList());
    }

    public TurnoDTO crear(TurnoDTO dto) {
        Cliente cliente = cliRepo.findById(dto.clienteId())
            .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado: " + dto.clienteId()));

        Servicio servicio = serRepo.findById(dto.servicioId())
            .orElseThrow(() -> new RecursoNoEncontradoException("Servicio no encontrado: " + dto.servicioId()));

        Disponibilidad disponibilidad = disRepo.findById(dto.disponibilidadId())
            .orElseThrow(() -> new RecursoNoEncontradoException("Disponibilidad no encontrada: " + dto.disponibilidadId()));

        Turno turno = new Turno();
        turno.setCliente(cliente);
        turno.setServicio(servicio);
        turno.setDisponibilidad(disponibilidad);
        turno.setFecha(dto.fecha());
        turno.setHora(dto.hora());
        turno.setEstado(EstadoTurno.valueOf(dto.estado().toUpperCase()));

        Turno guardado = repo.save(turno);

        return convertirADTO(guardado);
    }
    
    public TurnoDTO actualizar(Integer id, TurnoDTO dto) {
        Turno turno = repo.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("Turno no encontrado: " + id));
        
        Cliente cliente = cliRepo.findById(dto.clienteId())
            .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado: " + dto.clienteId()));

        Servicio servicio = serRepo.findById(dto.servicioId())
            .orElseThrow(() -> new RecursoNoEncontradoException("Servicio no encontrado: " + dto.servicioId()));

        Disponibilidad disponibilidad = disRepo.findById(dto.disponibilidadId())
            .orElseThrow(() -> new RecursoNoEncontradoException("Disponibilidad no encontrada: " + dto.disponibilidadId()));
        
        turno.setCliente(cliente);
        turno.setServicio(servicio);
        turno.setDisponibilidad(disponibilidad);
        turno.setFecha(dto.fecha());
        turno.setHora(dto.hora());
        turno.setEstado(EstadoTurno.valueOf(dto.estado().toUpperCase()));
        
        Turno guardado = repo.save(turno);
        
        return convertirADTO(guardado);
    }
    
    public void eliminar(Integer id) {
        if (!repo.existsById(id)) {
            throw new RecursoNoEncontradoException("Turno no encontrado: " + id);
        }
        repo.deleteById(id);
    }
    
    public void actualizarTurno(Integer id, LocalDate fecha, LocalTime hora, 
                               String estado, Integer clienteId, 
                               Integer disponibilidadId, Long servicioId) {
        Turno turno = repo.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("Turno no encontrado: " + id));
        
        Cliente cliente = cliRepo.findById(clienteId)
            .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado: " + clienteId));

        Servicio servicio = serRepo.findById(servicioId)
            .orElseThrow(() -> new RecursoNoEncontradoException("Servicio no encontrado: " + servicioId));

        Disponibilidad disponibilidad = disRepo.findById(disponibilidadId)
            .orElseThrow(() -> new RecursoNoEncontradoException("Disponibilidad no encontrada: " + disponibilidadId));
        
        turno.setCliente(cliente);
        turno.setServicio(servicio);
        turno.setDisponibilidad(disponibilidad);
        turno.setFecha(fecha);
        turno.setHora(hora);
        turno.setEstado(EstadoTurno.valueOf(estado.toUpperCase()));
        
        repo.save(turno);
    }
    
    public void cambiarEstado(Integer id, String nuevoEstado) {
        Turno turno = repo.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("Turno no encontrado: " + id));
        
        turno.setEstado(EstadoTurno.valueOf(nuevoEstado.toUpperCase()));
        repo.save(turno);
    }
    
    private TurnoDTO convertirADTO(Turno t) {
        return new TurnoDTO(
            t.getId(),
            t.getFecha(),
            t.getHora(),
            t.getEstado().name(),
            t.getCliente().getId(),
            t.getDisponibilidad().getId(),
            t.getServicio().getIdServicio()
        );
    }
    
    public List<EstadoTurno> listarEstados() {
        return List.of(EstadoTurno.values());
    }
}


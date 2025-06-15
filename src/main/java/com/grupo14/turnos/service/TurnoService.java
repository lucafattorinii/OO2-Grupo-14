package com.grupo14.turnos.service;

import com.grupo14.turnos.dto.TurnoConFechaDTO;
import com.grupo14.turnos.dto.TurnoDTO;
import com.grupo14.turnos.dto.TurnoVistaDTO;
import com.grupo14.turnos.exception.HorarioNoDisponibleException;
import com.grupo14.turnos.exception.RecursoNoEncontradoException;
import com.grupo14.turnos.exception.TurnoNoEncontradoException;
import com.grupo14.turnos.modelo.Cliente;
import com.grupo14.turnos.modelo.Disponibilidad;
import com.grupo14.turnos.modelo.Servicio;
import com.grupo14.turnos.modelo.Turno;
import com.grupo14.turnos.modelo.EstadoTurno;
import com.grupo14.turnos.modelo.Fecha;
import com.grupo14.turnos.repository.ClienteRepository;
import com.grupo14.turnos.repository.DisponibilidadRepository;
import com.grupo14.turnos.repository.FechaRepository;
import com.grupo14.turnos.repository.ServicioRepository;
import com.grupo14.turnos.repository.TurnoRepository;

import org.springframework.beans.factory.annotation.Autowired;
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
    private final FechaRepository fechaRepo;
    private final FechaService fechaService;

    public TurnoService(
        TurnoRepository repo,
        ClienteRepository cliRepo,
        ServicioRepository serRepo,
        DisponibilidadRepository disRepo,
        FechaRepository fechaRepo,
        FechaService fechaService
    ) {
        this.repo = repo;
        this.cliRepo = cliRepo;
        this.serRepo = serRepo;
        this.disRepo = disRepo;
        this.fechaRepo= fechaRepo;
        this.fechaService = fechaService;
    }
    
    @Autowired
    private DisponibilidadService disponibilidadService;

    public TurnoDTO obtenerPorId(long id) {
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

    public TurnoConFechaDTO  crear(TurnoConFechaDTO dto) {
        Cliente cliente = cliRepo.findById(dto.clienteId())
            .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado: " + dto.clienteId()));

        Servicio servicio = serRepo.findById(dto.servicioId())
            .orElseThrow(() -> new RecursoNoEncontradoException("Servicio no encontrado: " + dto.servicioId()));

        Disponibilidad disponibilidad = disRepo.findById(dto.disponibilidadId())
            .orElseThrow(() -> new RecursoNoEncontradoException("Disponibilidad no encontrada: " + dto.disponibilidadId()));
        
     // Verificar que el horario esté disponible
        if (!disponibilidadService.estaDisponible(dto.disponibilidadId().longValue(), dto.fecha(), dto.hora())) {
            throw new HorarioNoDisponibleException("El horario ya está ocupado.");
        }



        // Crear la fecha usando el helper del servicio
        Fecha fecha = fechaService.crear(dto.fecha(), dto.direccionId());

        Turno turno = new Turno();
        turno.setCliente(cliente);
        turno.setServicio(servicio);
        turno.setDisponibilidad(disponibilidad);
        turno.setFecha(fecha);
        turno.setHora(dto.hora());
        turno.setEstado(dto.estado());

        Turno guardado = repo.save(turno);
        return convertirATurnoConFechaDTO(guardado);
    }
    
    public TurnoConFechaDTO actualizar(long id, TurnoConFechaDTO dto) {
        Turno turno = repo.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("Turno no encontrado: " + id));
        
        Cliente cliente = cliRepo.findById(dto.clienteId())
            .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado: " + dto.clienteId()));

        Servicio servicio = serRepo.findById(dto.servicioId())
            .orElseThrow(() -> new RecursoNoEncontradoException("Servicio no encontrado: " + dto.servicioId()));

        Disponibilidad disponibilidad = disRepo.findById(dto.disponibilidadId())
            .orElseThrow(() -> new RecursoNoEncontradoException("Disponibilidad no encontrada: " + dto.disponibilidadId()));

        // Usar el servicio para crear/actualizar Fecha
        Fecha fecha = fechaService.crear(dto.fecha(), dto.direccionId());

        turno.setCliente(cliente);
        turno.setServicio(servicio);
        turno.setDisponibilidad(disponibilidad);
        turno.setFecha(fecha);
        turno.setHora(dto.hora());
        turno.setEstado(dto.estado());

        Turno guardado = repo.save(turno);
        return convertirATurnoConFechaDTO(guardado);
    }
    
    public void eliminar(long id) {
        if (!repo.existsById(id)) {
            throw new RecursoNoEncontradoException("Turno no encontrado: " + id);
        }
        repo.deleteById(id);
    }
    
    public void actualizarTurno(long id, Long fechaId, LocalTime hora, 
           
    		String estado, long clienteId, 
            long disponibilidadId, long servicioId) {
			Turno turno = repo.findById(id)
					.orElseThrow(() -> new RecursoNoEncontradoException("Turno no encontrado: " + id));
			
			Cliente cliente = cliRepo.findById(clienteId)
					.orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado: " + clienteId));
			
			Servicio servicio = serRepo.findById(servicioId)
					.orElseThrow(() -> new RecursoNoEncontradoException("Servicio no encontrado: " + servicioId));
			
			Disponibilidad disponibilidad = disRepo.findById(disponibilidadId)
					.orElseThrow(() -> new RecursoNoEncontradoException("Disponibilidad no encontrada: " + disponibilidadId));
			
			Fecha fecha = fechaRepo.findById(fechaId)
					.orElseThrow(() -> new RecursoNoEncontradoException("Fecha no encontrada: " + fechaId));
			
			turno.setCliente(cliente);
			turno.setServicio(servicio);
			turno.setDisponibilidad(disponibilidad);
			turno.setFecha(fecha);
			turno.setHora(hora);
			turno.setEstado(EstadoTurno.valueOf(estado.toUpperCase()));
			
			repo.save(turno);
}
    
    public void cambiarEstado(long id, String nuevoEstado) {
        Turno turno = repo.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("Turno no encontrado: " + id));
        
        turno.setEstado(EstadoTurno.valueOf(nuevoEstado.toUpperCase()));
        repo.save(turno);
    }
    
    private TurnoDTO convertirADTO(Turno t) {
        return new TurnoDTO(
            t.getId(),
            t.getFecha().getId(), 
            t.getHora(),
            t.getEstado(),
            t.getCliente().getId(),
            t.getDisponibilidad().getId(),
            t.getServicio().getIdServicio()
        );
    }
    
    private TurnoConFechaDTO convertirATurnoConFechaDTO(Turno t) {
        return new TurnoConFechaDTO(
            t.getFecha().getFecha(),             // LocalDate fecha
            t.getHora(),                         // LocalTime hora
            t.getEstado(),                       // EstadoTurno estado
            t.getCliente().getId(),              // Long clienteId
            t.getDisponibilidad().getId(),      // Long disponibilidadId
            t.getServicio().getIdServicio(),    // Long servicioId
            t.getFecha().getDireccion().getIdDireccion() // Long direccionId
        );
    }
    
    public List<EstadoTurno> listarEstados() {
        return List.of(EstadoTurno.values());
    }
    
    private TurnoVistaDTO convertirATurnoVistaDTO(Turno t) {
        return new TurnoVistaDTO(
            t.getFecha(),
            t.getHora(),
            t.getEstado(),
            t.getCliente().getNombre(),    
            t.getServicio().getNombre()      
        );
    }
    
    public List<TurnoVistaDTO> listarPorClienteId(long clienteId) {
        if (!cliRepo.existsById(clienteId)) {
            throw new RecursoNoEncontradoException("Cliente no encontrado: " + clienteId);
        }

        return repo.findByClienteId(clienteId).stream()
            .map(this::convertirATurnoVistaDTO)
            .collect(Collectors.toList());
    }
}


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

    public List<TurnoDTO> listarTodos() {
        return repo.findAll().stream()
            .map(t -> new TurnoDTO(
                t.getId(),
                t.getFecha(),
                t.getHora(),
                t.getEstado().name(), 
                t.getCliente().getId(),
                t.getDisponibilidad().getId(),
                t.getServicio().getIdServicio()
            ))
            .collect(Collectors.toList());
    }

    public List<TurnoDTO> listarPorEstado(String estado) {
        EstadoTurno estadoEnum = EstadoTurno.valueOf(estado.toUpperCase());

        return repo.findByEstado(estadoEnum).stream()
            .map(t -> new TurnoDTO(
                t.getId(),
                t.getFecha(),
                t.getHora(),
                t.getEstado().name(), 
                t.getCliente().getId(),
                t.getDisponibilidad().getId(),
                t.getServicio().getIdServicio()
            ))
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

        return new TurnoDTO(
            guardado.getId(),
            guardado.getFecha(),
            guardado.getHora(),
            guardado.getEstado().name(),
            cliente.getId(),
            disponibilidad.getId(),
            servicio.getIdServicio()
        );
    }
}

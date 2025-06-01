package com.grupo14.turnos.service;

import com.grupo14.turnos.dto.DisponibilidadDTO;
import com.grupo14.turnos.exception.RecursoNoEncontradoException;
import com.grupo14.turnos.modelo.Disponibilidad;
import com.grupo14.turnos.modelo.Empleado;
import com.grupo14.turnos.modelo.Servicio;
import com.grupo14.turnos.repository.DisponibilidadRepository;
import com.grupo14.turnos.repository.EmpleadoRepository;
import com.grupo14.turnos.repository.ServicioRepository;
import org.springframework.stereotype.Service;
import com.grupo14.turnos.modelo.DiaSemana;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DisponibilidadService {

    private final DisponibilidadRepository repo;
    private final EmpleadoRepository empRepo;
    private final ServicioRepository servRepo;

    public DisponibilidadService(
            DisponibilidadRepository repo,
            EmpleadoRepository empRepo,
            ServicioRepository servRepo
    ) {
        this.repo = repo;
        this.empRepo = empRepo;
        this.servRepo = servRepo;
    }

    public DisponibilidadDTO obtenerPorId(Integer id) {
        Disponibilidad d = repo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Disponibilidad no encontrada: " + id));

        return new DisponibilidadDTO(
                d.getId(),
                d.getDiaSemana().name(),
                d.getHoraInicio().toString(),
                d.getHoraFin().toString(),
                d.getServicio().getIdServicio(),
                d.getEmpleado().getId()
        );
    }

    public List<DisponibilidadDTO> listarTodos() {
        return repo.findAll().stream()
                .map(d -> new DisponibilidadDTO(
                        d.getId(),
                        d.getDiaSemana().name(),
                        d.getHoraInicio().toString(),
                        d.getHoraFin().toString(),
                        d.getServicio().getIdServicio(),
                        d.getEmpleado().getId()
                ))
                .collect(Collectors.toList());
    }

    public DisponibilidadDTO crear(DisponibilidadDTO dto) {
        Empleado emp = empRepo.findById(dto.empleadoId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Empleado no encontrado: " + dto.empleadoId()));

        Servicio serv = servRepo.findById(dto.servicioId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Servicio no encontrado: " + dto.servicioId()));

        Disponibilidad d = new Disponibilidad();
        d.setEmpleado(emp);
        d.setServicio(serv);
        d.setDiaSemana(DiaSemana.valueOf(dto.diaSemana().toUpperCase()));
        d.setHoraInicio(LocalTime.parse(dto.horaInicio()));
        d.setHoraFin(LocalTime.parse(dto.horaFin()));

        Disponibilidad guardada = repo.save(d);

        return new DisponibilidadDTO(
                guardada.getId(),
                guardada.getDiaSemana().name(),
                guardada.getHoraInicio().toString(),
                guardada.getHoraFin().toString(),
                guardada.getServicio().getIdServicio(),
                guardada.getEmpleado().getId()
        );
    }
}

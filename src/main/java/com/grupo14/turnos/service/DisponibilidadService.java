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

        return convertirADTO(d);
    }

    public List<DisponibilidadDTO> listarTodos() {
        return repo.findAll().stream()
                .map(this::convertirADTO)
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
        d.setDiaSemana(DiaSemana.valueOf(dto.diaSemana()));
        d.setHoraInicio(LocalTime.parse(dto.horaInicio()));
        d.setHoraFin(LocalTime.parse(dto.horaFin()));

        Disponibilidad guardada = repo.save(d);

        return convertirADTO(guardada);
    }
    
    public DisponibilidadDTO actualizar(Integer id, DisponibilidadDTO dto) {
        Disponibilidad d = repo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Disponibilidad no encontrada: " + id));
        
        Empleado emp = empRepo.findById(dto.empleadoId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Empleado no encontrado: " + dto.empleadoId()));

        Servicio serv = servRepo.findById(dto.servicioId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Servicio no encontrado: " + dto.servicioId()));
        
        d.setEmpleado(emp);
        d.setServicio(serv);
        d.setDiaSemana(DiaSemana.valueOf(dto.diaSemana()));
        d.setHoraInicio(LocalTime.parse(dto.horaInicio()));
        d.setHoraFin(LocalTime.parse(dto.horaFin()));
        
        Disponibilidad guardada = repo.save(d);
        
        return convertirADTO(guardada);
    }
    
    public void eliminar(Integer id) {
        if (!repo.existsById(id)) {
            throw new RecursoNoEncontradoException("Disponibilidad no encontrada: " + id);
        }
        repo.deleteById(id);
    }
    
    public void actualizarDisponibilidad(Integer id, String diaSemana, String horaInicio, 
                                        String horaFin, Long servicioId, Integer empleadoId) {
        Disponibilidad d = repo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Disponibilidad no encontrada: " + id));
        
        Empleado emp = empRepo.findById(empleadoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Empleado no encontrado: " + empleadoId));

        Servicio serv = servRepo.findById(servicioId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Servicio no encontrado: " + servicioId));
        
        d.setEmpleado(emp);
        d.setServicio(serv);
        d.setDiaSemana(DiaSemana.valueOf(diaSemana));
        d.setHoraInicio(LocalTime.parse(horaInicio));
        d.setHoraFin(LocalTime.parse(horaFin));
        
        repo.save(d);
    }
    
    private DisponibilidadDTO convertirADTO(Disponibilidad d) {
        return new DisponibilidadDTO(
                d.getId(),
                d.getDiaSemana().name(),
                d.getHoraInicio().toString(),
                d.getHoraFin().toString(),
                d.getServicio().getIdServicio(),
                d.getEmpleado().getId()
        );
    }
    
    public List<DiaSemana> listarDiasSemana() {
        return List.of(DiaSemana.values());
    }
    /*
    public List<DisponibilidadDTO> obtenerPorServicio(Integer servicioId) {
        List<Disponibilidad> lista = repo.findByServiciosId(servicioId); // consulta para relación muchos a muchos
        return lista.stream()
            .map(this::convertirADTO)
            .collect(Collectors.toList());
    }*/
}


package com.grupo14.turnos.service;

import com.grupo14.turnos.dto.DisponibilidadDTO;
import com.grupo14.turnos.exception.RecursoNoEncontradoException;
import com.grupo14.turnos.modelo.Disponibilidad;
import com.grupo14.turnos.modelo.Servicio;
import com.grupo14.turnos.repository.DisponibilidadRepository;
import com.grupo14.turnos.repository.ServicioRepository;
import org.springframework.stereotype.Service;
import com.grupo14.turnos.modelo.DiaSemana;

import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DisponibilidadService {

    private final DisponibilidadRepository repo;
    private final ServicioRepository servRepo;

    public DisponibilidadService(
            DisponibilidadRepository repo,
            ServicioRepository servRepo
    ) {
        this.repo = repo;
        this.servRepo = servRepo;
    }

    public DisponibilidadDTO obtenerPorId(long id) {
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
        Disponibilidad d = new Disponibilidad();
        d.setDiaSemana(dto.diaSemana());
        d.setHoraInicio(dto.horaInicio());
        d.setHoraFin(dto.horaFin());

        // Asociar múltiples servicios
        if (dto.servicioIds() != null && !dto.servicioIds().isEmpty()) {
            Set<Servicio> servicios = dto.servicioIds().stream()
                .map(id -> servRepo.findById(id)
                    .orElseThrow(() -> new RecursoNoEncontradoException("Servicio no encontrado: " + id)))
                .collect(Collectors.toSet());

            d.setServicios(servicios);
        }

        Disponibilidad guardada = repo.save(d);
        return convertirADTO(guardada);
    }
    
    public DisponibilidadDTO actualizar(Long id, DisponibilidadDTO dto) {
        Disponibilidad d = repo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Disponibilidad no encontrada: " + id));

        d.setDiaSemana(dto.diaSemana());
        d.setHoraInicio(dto.horaInicio());
        d.setHoraFin(dto.horaFin());

        if (dto.servicioIds() != null && !dto.servicioIds().isEmpty()) {
            Set<Servicio> servicios = dto.servicioIds().stream()
                .map(sid -> servRepo.findById(sid)
                        .orElseThrow(() -> new RecursoNoEncontradoException("Servicio no encontrado: " + sid)))
                .collect(Collectors.toSet());
            d.setServicios(servicios);
        } else {
            d.getServicios().clear();
        }

        Disponibilidad guardada = repo.save(d);
        return convertirADTO(guardada);
    }
    
    public void eliminar(long id) {
        if (!repo.existsById(id)) {
            throw new RecursoNoEncontradoException("Disponibilidad no encontrada: " + id);
        }
        repo.deleteById(id);
    }
    
    
    public void actualizarDisponibilidad(long id, String diaSemana, String horaInicio,  
            String horaFin, Set<Long> servicioIds) {

			Disponibilidad d = repo.findById(id)
			.orElseThrow(() -> new RecursoNoEncontradoException("Disponibilidad no encontrada: " + id));
			
			d.setDiaSemana(DiaSemana.valueOf(diaSemana.toUpperCase())); // aseguramos mayúsculas válidas para el enum
			d.setHoraInicio(LocalTime.parse(horaInicio));
			d.setHoraFin(LocalTime.parse(horaFin));
			
			// Actualizar los servicios asociados
			if (servicioIds != null && !servicioIds.isEmpty()) {
					Set<Servicio> servicios = servicioIds.stream()
					.map(sid -> servRepo.findById(sid)
					.orElseThrow(() -> new RecursoNoEncontradoException("Servicio no encontrado: " + sid)))
					.collect(Collectors.toSet());
			
					d.setServicios(servicios);
			} else {
					d.getServicios().clear(); // O conserva los actuales, según tu lógica
			}
			
			repo.save(d);
    }
    
    private DisponibilidadDTO convertirADTO(Disponibilidad d) {
        Set<Long> servicioIds = d.getServicios().stream()
                .map(Servicio::getIdServicio)
                .collect(Collectors.toSet());

        return new DisponibilidadDTO(
                d.getId(),
                d.getDiaSemana(),
                d.getHoraInicio(),
                d.getHoraFin(),
                servicioIds
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

package com.grupo14.turnos.service;

import com.grupo14.turnos.dto.DisponibilidadDTO;
import com.grupo14.turnos.exception.DisponibilidadDuplicadaException;
import com.grupo14.turnos.exception.RecursoNoEncontradoException;
import com.grupo14.turnos.modelo.Disponibilidad;
import com.grupo14.turnos.modelo.Servicio;
import com.grupo14.turnos.repository.DisponibilidadRepository;
import com.grupo14.turnos.repository.ServicioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        // 1) Armar la entidad base
        Disponibilidad d = new Disponibilidad();
        d.setDiaSemana(dto.diaSemana());
        d.setHoraInicio(dto.horaInicio());
        d.setHoraFin(dto.horaFin());

        // 2) Cargar y validar los servicios
        if (dto.servicioIds() != null && !dto.servicioIds().isEmpty()) {

            Set<Servicio> servicios = dto.servicioIds().stream()
                .map(id -> servRepo.findById(id)
                    .orElseThrow(() -> new RecursoNoEncontradoException("Servicio no encontrado: " + id)))
                .collect(Collectors.toSet());

            //CHEQUEO DE DUPLICADOS 
            for (Servicio s : servicios) {
                boolean duplicada = repo
                    .existsByServicios_IdServicioAndDiaSemanaAndHoraInicioAndHoraFin(      
                        s.getIdServicio(),                                                
                        dto.diaSemana(),                                                  
                        dto.horaInicio(),                                                 
                        dto.horaFin());                                                   

                if (duplicada) {
                    throw new DisponibilidadDuplicadaException(                           
                        "Ya existe una disponibilidad idéntica para el servicio ID "      
                        + s.getIdServicio());                                             
                }
            }

            d.setServicios(servicios);
        }

        // 3) Guardar y devolver
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
    
    @Transactional(readOnly = true)
    public List<DisponibilidadDTO> listarTodosConServicios() {
        return repo.findAllWithServicios().stream()
            .map(this::convertirADTO)
            .collect(Collectors.toList());
    }
    
    /**
     * Obtiene todas las disponibilidades para un día de la semana específico.
     * @param diaSemana Nombre del día de la semana (en mayúsculas)
     * @return Lista de DTOs de disponibilidad para el día especificado
     * @throws IllegalArgumentException si el nombre del día no es válido
     */
    public List<DisponibilidadDTO> obtenerPorDiaSemana(String diaSemana) {
        try {
            DiaSemana dia = DiaSemana.valueOf(diaSemana.toUpperCase());
            return repo.findByDiaSemana(dia).stream()
                    .map(this::convertirADTO)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Día de la semana no válido: " + diaSemana, e);
        }
    }
    
    public List<String> obtenerDiasDisponibles(Long disponibilidadId) {
        Disponibilidad disponibilidad = repo.findById(disponibilidadId)
            .orElseThrow(() -> new RecursoNoEncontradoException("Disponibilidad no encontrada: " + disponibilidadId));
            
        // Convertir el día de la semana al formato largo en español
        String diaEnEspanol = switch (disponibilidad.getDiaSemana()) {
            case LUNES -> "Lunes";
            case MARTES -> "Martes";
            case MIERCOLES -> "Miércoles";
            case JUEVES -> "Jueves";
            case VIERNES -> "Viernes";
            case SABADO -> "Sábado";
            case DOMINGO -> "Domingo";
        };
        
        return List.of(diaEnEspanol);
    }
}

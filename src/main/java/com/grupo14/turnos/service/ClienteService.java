package com.grupo14.turnos.service;

import com.grupo14.turnos.dto.ClienteDTO;
import com.grupo14.turnos.dto.TurnoConFechaDTO;
import com.grupo14.turnos.exception.RecursoNoEncontradoException;
import com.grupo14.turnos.modelo.Cliente;
import com.grupo14.turnos.modelo.Disponibilidad;
import com.grupo14.turnos.modelo.EstadoTurno;
import com.grupo14.turnos.modelo.Fecha;
import com.grupo14.turnos.modelo.Rol;
import com.grupo14.turnos.modelo.Servicio;
import com.grupo14.turnos.repository.ClienteRepository;
import com.grupo14.turnos.repository.DisponibilidadRepository;
import com.grupo14.turnos.repository.ServicioRepository;
import com.grupo14.turnos.repository.TurnoRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {

	private final ClienteRepository repo;
	private final PasswordEncoder passwordEncoder;
	private final ServicioRepository serRepo;
	private final DisponibilidadRepository disRepo;
	private final FechaService fechaService;
	private final TurnoService turnoService;
	private final TurnoRepository turnoRepo;

	public ClienteService(ClienteRepository repo, PasswordEncoder passwordEncoder, ServicioRepository serRepo, DisponibilidadRepository disRepo, 
			FechaService fechaService, TurnoService turnoService, TurnoRepository turnoRepo) {
		this.repo = repo;
		this.passwordEncoder = passwordEncoder;
		this.serRepo= serRepo;
		this.disRepo= disRepo;
		this.fechaService= fechaService;
		this.turnoService= turnoService;
		this.turnoRepo= turnoRepo;
	}

	public ClienteDTO obtenerPorId(long id) {
		Cliente c = repo.findById(id)
				.orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado: " + id));
		return mapToDTO(c);
	}

	public List<ClienteDTO> listarTodos() {
		List<ClienteDTO> clientes = repo.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());

		System.out.println("Clientes obtenidos en ClienteService: " + clientes); // Debug

		return clientes;
	}

	public void eliminarPorId(long id) {
		if (!repo.existsById(id)) {
			throw new RecursoNoEncontradoException("Cliente no existe: " + id);
		}
		repo.deleteById(id);
	}

	public ClienteDTO actualizarCliente(long id, String email, String contrasena, Long dni,
            String nombre, String apellido) {
			Cliente c = repo.findById(id)
			.orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado: " + id));
			
			// reasigna todos los campos
			c.setEmail(email);
			
			// Encriptar la contraseña solo si viene no nula
			if (contrasena != null && !contrasena.isEmpty()) {
				c.setContrasena(passwordEncoder.encode(contrasena));
			}
			
			c.setDni(dni);
			c.setNombre(nombre);
			c.setApellido(apellido);
			c.setRol(Rol.CLIENTE);
			
			Cliente actualizado = repo.save(c);
			return mapToDTO(actualizado);
		}

	public ClienteDTO crear(ClienteDTO dto) {
	    Long proximoNumero = repo.findTopByOrderByNumeroClienteDesc()
	        .map(c -> c.getNumeroCliente() + 1)
	        .orElse(1L); // valor inicial si no hay clientes

	    Cliente c = new Cliente();
	    c.setEmail(dto.email());
	    c.setContrasena(passwordEncoder.encode(dto.contrasena())); //Encriptacion 
	    c.setNumeroCliente(proximoNumero);
	    c.setDni(dto.dni());
	    c.setNombre(dto.nombre());
	    c.setApellido(dto.apellido());
	    c.setRol(Rol.CLIENTE);

	    Cliente guardado = repo.save(c);
	    System.out.println("Guardando cliente en BD: " + c.getEmail() + " | Número asignado: " + proximoNumero);

	    return mapToDTO(guardado);
	}

	private ClienteDTO mapToDTO(Cliente c) {
		return new ClienteDTO(c.getId(), c.getEmail(), c.getContrasena(), c.getNumeroCliente(), c.getDni(),
				c.getNombre(), c.getApellido());
	}
	
	public TurnoConFechaDTO crearTurnoCliente(TurnoConFechaDTO dto) {
	    Servicio servicio = serRepo.findById(dto.servicioId())
	        .orElseThrow(() -> new RecursoNoEncontradoException("Servicio no encontrado: " + dto.servicioId()));
	    Disponibilidad disponibilidad = disRepo.findById(dto.disponibilidadId())
	        .orElseThrow(() -> new RecursoNoEncontradoException("Disponibilidad no encontrada: " + dto.disponibilidadId()));

	    int duracionMin = servicio.getDuracionMin();
	    LocalTime horaInicio = disponibilidad.getHoraInicio();
	    LocalTime horaFin = disponibilidad.getHoraFin();
	    LocalTime horaSolicitada = dto.hora();

	    // Validar que la hora esté dentro del rango permitido
	    if (horaSolicitada.isBefore(horaInicio) || horaSolicitada.plusMinutes(duracionMin).isAfter(horaFin)) {
	        throw new IllegalArgumentException("La hora solicitada está fuera del rango permitido");
	    }

	    // Validar que la hora solicitada coincide con un múltiplo de duración desde horaInicio
	    long minutosDesdeInicio = java.time.Duration.between(horaInicio, horaSolicitada).toMinutes();
	    if (minutosDesdeInicio % duracionMin != 0) {
	        throw new IllegalArgumentException("La hora solicitada no coincide con un turno válido según la duración del servicio");
	    }

	    // Crear la fecha para validar disponibilidad
	    Fecha fecha = fechaService.crear(dto.fecha(), dto.direccionId());

	    // Verificar disponibilidad del turno
	    boolean hayConflicto = turnoRepo.existsByFechaAndHoraAndDisponibilidadAndFecha_Direccion(
	        fecha, horaSolicitada, disponibilidad, fecha.getDireccion());

	    if (hayConflicto) {
	        throw new IllegalStateException("El turno seleccionado ya está ocupado. Por favor, elija otro horario o ubicación.");
	    }

	    // Crear el turno usando la lógica actual del TurnoService
	    TurnoConFechaDTO nuevo = new TurnoConFechaDTO(
	        dto.fecha(),
	        horaSolicitada,
	        EstadoTurno.PENDIENTE,
	        dto.clienteId(),
	        dto.disponibilidadId(),
	        dto.servicioId(),
	        dto.direccionId()
	    );

	    return turnoService.crear(nuevo);
	}

}

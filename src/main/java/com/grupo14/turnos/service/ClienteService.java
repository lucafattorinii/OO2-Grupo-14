package com.grupo14.turnos.service;

import com.grupo14.turnos.dto.ClienteDTO;
import com.grupo14.turnos.exception.RecursoNoEncontradoException;
import com.grupo14.turnos.modelo.Cliente;
import com.grupo14.turnos.modelo.Rol;
import com.grupo14.turnos.repository.ClienteRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {

	private final ClienteRepository repo;
	private final PasswordEncoder passwordEncoder;

	public ClienteService(ClienteRepository repo, PasswordEncoder passwordEncoder) {
		this.repo = repo;
		this.passwordEncoder = passwordEncoder;
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

	public ClienteDTO actualizarCliente(long id, String email, String contrasena, Long numeroCliente, Long dni,
            String nombre, String apellido) {
			Cliente c = repo.findById(id)
			.orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado: " + id));
			
			// reasigna todos los campos
			c.setEmail(email);
			
			// Encriptar la contraseña solo si viene no nula
			if (contrasena != null && !contrasena.isEmpty()) {
				c.setContrasena(passwordEncoder.encode(contrasena));
			}
			
			c.setNumeroCliente(numeroCliente);
			c.setDni(dni);
			c.setNombre(nombre);
			c.setApellido(apellido);
			c.setRol(Rol.CLIENTE);
			
			Cliente actualizado = repo.save(c);
			return mapToDTO(actualizado);
		}

	public ClienteDTO crear(ClienteDTO dto) {
		Cliente c = new Cliente();
		c.setEmail(dto.email());
		c.setContrasena(passwordEncoder.encode(dto.contrasena())); //Encriptacion 
		c.setNumeroCliente(dto.numeroCliente());
		c.setDni(dto.dni());
		c.setNombre(dto.nombre());
		c.setApellido(dto.apellido());
		c.setRol(Rol.CLIENTE);
		Cliente guardado = repo.save(c);
		return mapToDTO(guardado);
	}

	private ClienteDTO mapToDTO(Cliente c) {
		return new ClienteDTO(c.getId(), c.getEmail(), c.getContrasena(), c.getNumeroCliente(), c.getDni(),
				c.getNombre(), c.getApellido());
	}

}

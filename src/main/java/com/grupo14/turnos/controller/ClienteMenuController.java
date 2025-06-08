package com.grupo14.turnos.controller;

import com.grupo14.turnos.dto.ClienteDTO;
import com.grupo14.turnos.dto.TurnoDTO;
import com.grupo14.turnos.dto.TurnoVistaDTO;
import com.grupo14.turnos.dto.UsuarioDTO;
import com.grupo14.turnos.modelo.Usuario;
import com.grupo14.turnos.service.ClienteService;
import com.grupo14.turnos.service.DisponibilidadService;
import com.grupo14.turnos.service.ServicioService;
import com.grupo14.turnos.service.TurnoService;
import jakarta.servlet.http.HttpSession;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/cliente")
public class ClienteMenuController {

	private final TurnoService turnoService;
	private final ServicioService servicioService;
	private final DisponibilidadService disponibilidadService;
	private final ClienteService clienteService;

    // Inyección por constructor
	public ClienteMenuController(TurnoService turnoService, ServicioService servicioService, DisponibilidadService disponibilidadService, ClienteService clienteService) {
	    this.turnoService = turnoService;
	    this.servicioService = servicioService;
	    this.disponibilidadService = disponibilidadService;
	    this.clienteService = clienteService;
	}

    @GetMapping("/menu")
    public String menuCliente(HttpSession session, Model model) {
        UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login"; // si no está logueado, lo mando a login
        }
        model.addAttribute("usuario", usuario);
        return "cliente/menu";
    }

    @GetMapping("/mis-turnos")
    public String verMisTurnos(HttpSession session, Model model) {
        String vista;

        UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("usuario");
        if (usuario == null) {
            vista = "redirect:/login";
        } else {
        	List<TurnoVistaDTO> misTurnos = turnoService.listarPorClienteId(usuario.id());
        	model.addAttribute("turnos", misTurnos);
            vista = "cliente/mis-turnos";
        }

        return vista;
    }
    
    @PostMapping("/turnos/{id}/eliminar")
    public String eliminarTurno(@PathVariable Integer id, HttpSession session) {
        String vista = "redirect:/mis-turnos"; 
        
        UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("usuario");
        if (usuario == null) {
            vista = "redirect:/login";
        } else {
            turnoService.eliminar(id);
        }
        
        return vista;
    }
    
    @GetMapping("/crear-turno")
    public String formularioNuevoTurno(HttpSession session, Model model) {
        String vista = "cliente/crear-turno";
        UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("usuario");
        if (usuario == null) {
            vista = "redirect:/login";
        } else {
            model.addAttribute("servicios", servicioService.listarTodos());
            model.addAttribute("disponibilidades", disponibilidadService.listarTodos());
        }
        return vista;
    }

    @PostMapping("/crear-turno")
    public String crearNuevoTurno(
            HttpSession session,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime hora,
            @RequestParam Integer disponibilidadId,
            @RequestParam Long servicioId
    ) {
        String vista = "redirect:/cliente/mis-turnos";
        UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("usuario");
        if (usuario == null) {
            vista = "redirect:/login";
        } else {
            TurnoDTO nuevo = new TurnoDTO(
                null, fecha, hora, "PENDIENTE", usuario.id(), disponibilidadId, servicioId
            );
            turnoService.crear(nuevo);
        }
        return vista;
    }

    @GetMapping("/editar-datos")
    public String editarDatos(HttpSession session, Model model) {
        String vista = "cliente/editar-datos";
        UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("usuario");
        if (usuario == null) {
            vista = "redirect:/login";
        } else {
            ClienteDTO cliente = clienteService.obtenerPorId(usuario.id());
            model.addAttribute("cliente", cliente);
        }
        return vista;
    }

    @PostMapping("/editar-datos")
    public String guardarEdicion(
            HttpSession session,
            @RequestParam String email,
            @RequestParam String contrasena,
            @RequestParam Long numeroCliente,
            @RequestParam Long dni,
            @RequestParam String nombre,
            @RequestParam String apellido
    ) {
        String vista = "redirect:/cliente/menu";
        UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("usuario");
        if (usuario == null) {
            vista = "redirect:/login";
        } else {
            clienteService.actualizarCliente(usuario.id(), email, contrasena, numeroCliente, dni, nombre, apellido);
        }
        return vista;
    }
}
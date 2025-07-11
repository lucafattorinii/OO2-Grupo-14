package com.grupo14.turnos.controller;

import com.grupo14.turnos.dto.ClienteDTO;
import com.grupo14.turnos.dto.ContactoDTO;
import com.grupo14.turnos.dto.DireccionDTO;
import com.grupo14.turnos.dto.TurnoConFechaDTO;
import com.grupo14.turnos.dto.TurnoDTO;
import com.grupo14.turnos.dto.TurnoVistaDTO;
import com.grupo14.turnos.dto.UsuarioDTO;
import com.grupo14.turnos.exception.RecursoNoEncontradoException;
import com.grupo14.turnos.modelo.EstadoTurno;
import com.grupo14.turnos.modelo.Fecha;
import com.grupo14.turnos.modelo.Usuario;
import com.grupo14.turnos.repository.FechaRepository;
import com.grupo14.turnos.service.ClienteService;
import com.grupo14.turnos.service.ContactoService;
import com.grupo14.turnos.service.DireccionService;
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
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/cliente")
public class ClienteMenuController {

	private final TurnoService turnoService;
	private final ServicioService servicioService;
	private final DisponibilidadService disponibilidadService;
	private final ClienteService clienteService;
	private final FechaRepository fechaRepository;
	private final DireccionService direccionService;
	private final ContactoService contactoService;

    // Inyección por constructor
	public ClienteMenuController(TurnoService turnoService, ServicioService servicioService, DisponibilidadService disponibilidadService, ClienteService clienteService,
			FechaRepository fechaRepository, DireccionService direccionService, ContactoService contactoService) {
	    this.turnoService = turnoService;
	    this.servicioService = servicioService;
	    this.disponibilidadService = disponibilidadService;
	    this.clienteService = clienteService;
	    this.fechaRepository = fechaRepository;
	    this.direccionService= direccionService;
	    this.contactoService = contactoService;
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
    public String eliminarTurno(@PathVariable Long id, HttpSession session) {
        String vista = "redirect:/cliente/mis-turnos"; 
        
        UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("usuario");
        if (usuario == null) {
            vista = "redirect:/login";
        } else {
            try {
                turnoService.eliminar(id);
            } catch (Exception e) {
                // Log the error if needed
                System.err.println("Error al eliminar el turno: " + e.getMessage());
            }
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
            model.addAttribute("direcciones", direccionService.listarTodos());
        }
        return vista;
    }

    @PostMapping("/crear-turno")
    public String crearNuevoTurno(
            HttpSession session,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime hora,
            @RequestParam Long disponibilidadId,
            @RequestParam Long servicioId,
            @RequestParam(required = false) Long direccionId,  
            Model model
    ) {
        String vista;

        UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }

        // Cargar datos necesarios para el formulario en caso de error
        model.addAttribute("servicios", servicioService.listarTodos());
        model.addAttribute("disponibilidades", disponibilidadService.listarTodos());
        model.addAttribute("direcciones", direccionService.listarTodos());

        try {
            // Usar una dirección por defecto si no se proporciona
            Long direccionFinal = direccionId != null ? direccionId : 1L;
            
            TurnoConFechaDTO nuevo = new TurnoConFechaDTO(
                fecha,
                hora,
                EstadoTurno.PENDIENTE,
                usuario.id(),
                disponibilidadId,
                servicioId,
                direccionFinal
            );

            clienteService.crearTurnoCliente(nuevo);
            return "redirect:/cliente/mis-turnos?exito=Turno+creado+exitosamente";
            
        } catch (IllegalArgumentException | IllegalStateException e) {
            // Errores de validación o negocio
            model.addAttribute("error", e.getMessage());
            return "cliente/crear-turno";
            
        } catch (Exception e) {
            // Errores inesperados
            model.addAttribute("error", "Error inesperado al crear el turno. Por favor, intente nuevamente.");
            return "cliente/crear-turno";
        }
    }

    @GetMapping("/disponibilidad/{disponibilidadId}/dias")
    @ResponseBody
    public List<String> obtenerDiasDisponibles(@PathVariable Long disponibilidadId) {
        return disponibilidadService.obtenerDiasDisponibles(disponibilidadId);
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
            @RequestParam Long dni,
            @RequestParam String nombre,
            @RequestParam String apellido
    ) {
        String vista = "redirect:/cliente/menu";
        UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("usuario");
        if (usuario == null) {
            vista = "redirect:/login";
        } else {
            clienteService.actualizarCliente(usuario.id(), email, contrasena, dni, nombre, apellido);
        }
        return vista;
    }
    
    
    @GetMapping("/editar-contacto")
    public String mostrarEditarContacto(HttpSession session, Model model) {
        UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("usuario");
        if (usuario == null) return "redirect:/login";

        // Cargar contacto (si existe) y pasarlo al modelo
        Optional<ContactoDTO> contactoOpt = contactoService.obtenerPorUsuarioIdOptional(usuario.id());
        contactoOpt.ifPresent(c -> model.addAttribute("contacto", c));

        // También cargar la dirección si tiene una
        if (contactoOpt.isPresent() && contactoOpt.get().direccionId() != null) {
            DireccionDTO dir = direccionService.obtenerPorId(contactoOpt.get().direccionId());
            model.addAttribute("direccion", dir);
        }

        model.addAttribute("direcciones", direccionService.listarTodos());
        return "cliente/editar-contacto";
    }
    
    @PostMapping("/editar-contacto")
    public String guardarContacto(
            HttpSession session,
            @RequestParam String telefono,
            @RequestParam(required = false) Long direccionId,
            @RequestParam(required = false) String pais,
            @RequestParam(required = false) String provincia,
            @RequestParam(required = false) String ciudad,
            @RequestParam(required = false) String calle,
            @RequestParam(required = false) String numeroCalle,
            @RequestParam(required = false) String codigoPostal,
            Model model
    ) {
        String vista = "cliente/editar-contacto";

        UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("usuario");

        if (usuario == null) {
            vista = "redirect:/login";
        } else {
            try {
                Long idDireccionFinal = direccionId;

                if (direccionId == null && pais != null && !pais.isBlank()) {
                    // Crear nueva dirección
                    DireccionDTO nuevaDir = new DireccionDTO(null, pais, provincia, ciudad, calle, numeroCalle, codigoPostal);
                    idDireccionFinal = direccionService.crear(nuevaDir).idDireccion();
                } else if (direccionId != null && pais != null && !pais.isBlank()) {
                    // Actualizar dirección existente
                    DireccionDTO dto = new DireccionDTO(direccionId, pais, provincia, ciudad, calle, numeroCalle, codigoPostal);
                    direccionService.actualizar(direccionId, dto);
                }

                // Crear o actualizar contacto
                ContactoDTO dto = new ContactoDTO(null, telefono, idDireccionFinal);
                contactoService.crearContactoParaUsuario(usuario.id(), dto);

                vista = "redirect:/cliente/menu";

            } catch (Exception e) {
                model.addAttribute("direcciones", direccionService.listarTodos());
                model.addAttribute("error", "No se pudo guardar el contacto: " + e.getMessage());
            }
        }

        return vista;
    }
    
    @PostMapping("/eliminar-contacto")
    public String eliminarContacto(HttpSession session,
                                   @RequestParam(required = false) boolean eliminarDireccion) {
        UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }

        Optional<ContactoDTO> contactoOpt = contactoService.obtenerPorUsuarioIdOptional(usuario.id());

        if (contactoOpt.isPresent()) {
            ContactoDTO contacto = contactoOpt.get();

            contactoService.eliminarPorUsuarioId(usuario.id());

            if (eliminarDireccion && contacto.direccionId() != null) {
                direccionService.eliminar(contacto.direccionId());
            }
        }

        return "redirect:/cliente/editar-contacto?mensaje=Contacto+eliminado+correctamente";
    }
}

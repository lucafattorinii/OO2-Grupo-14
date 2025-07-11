package com.grupo14.turnos.controller;

import com.grupo14.turnos.dto.DisponibilidadDTO;
import com.grupo14.turnos.dto.ServicioDTO;
import com.grupo14.turnos.dto.UsuarioDTO;
import com.grupo14.turnos.modelo.DiaSemana;
import com.grupo14.turnos.service.DisponibilidadService;
import com.grupo14.turnos.service.PrestadorService;
import com.grupo14.turnos.service.ServicioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/prestador")
public class PrestadorDisponibilidadController {

    private final DisponibilidadService disponibilidadService;
    private final ServicioService servicioService;
    private final PrestadorService prestadorService;

    public PrestadorDisponibilidadController(
            DisponibilidadService disponibilidadService,
            ServicioService servicioService,
            PrestadorService prestadorService) {
        this.disponibilidadService = disponibilidadService;
        this.servicioService = servicioService;
        this.prestadorService = prestadorService;
    }

    @GetMapping("/disponibilidades")
    public String verDisponibilidades(HttpSession session, Model model) {
        UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("usuario");
        if (usuario == null || usuario.rol() != com.grupo14.turnos.modelo.Rol.PRESTADOR) {
            return "redirect:/login";
        }

        // Obtener el ID del prestador actual
        var prestador = prestadorService.buscarPorEmail(usuario.email());
        if (prestador == null) {
            return "redirect:/login";
        }

        // Obtener los servicios del prestador
        List<ServicioDTO> servicios = servicioService.listarTodos().stream()
                .filter(s -> s.prestadorId().equals(prestador.id()))
                .collect(Collectors.toList());

        // Obtener las disponibilidades del prestador (por sus servicios)
        List<DisponibilidadDTO> disponibilidades = disponibilidadService.listarTodos().stream()
                .filter(d -> {
                    // Filtrar solo las disponibilidades que tienen al menos un servicio del prestador
                    if (d.servicioIds() != null) {
                        Set<Long> servicioIdsPrestador = servicios.stream()
                                .map(ServicioDTO::id)
                                .collect(Collectors.toSet());
                        return d.servicioIds().stream().anyMatch(servicioIdsPrestador::contains);
                    }
                    return false;
                })
                .collect(Collectors.toList());

        model.addAttribute("disponibilidades", disponibilidades);
        model.addAttribute("servicios", servicios);
        model.addAttribute("diasSemana", DiaSemana.values());
        model.addAttribute("nuevaDisponibilidad", new DisponibilidadDTO(null, null, null, null, null));

        return "prestador/disponibilidades";
    }

    @PostMapping("/disponibilidades/guardar")
    public String guardarDisponibilidad(
            @ModelAttribute("nuevaDisponibilidad") DisponibilidadDTO disponibilidadDTO,
            @RequestParam("horaInicio") String horaInicioStr,
            @RequestParam("horaFin") String horaFinStr,
            @RequestParam(value = "servicioIds", required = false) List<Long> servicioIds,
            HttpSession session) {
        
        UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("usuario");
        if (usuario == null || usuario.rol() != com.grupo14.turnos.modelo.Rol.PRESTADOR) {
            return "redirect:/login";
        }

        // Convertir las cadenas de hora a LocalTime
        LocalTime horaInicio = LocalTime.parse(horaInicioStr);
        LocalTime horaFin = LocalTime.parse(horaFinStr);

        // Crear un nuevo DTO con los datos convertidos
        DisponibilidadDTO nuevaDisponibilidad = new DisponibilidadDTO(
                null,
                disponibilidadDTO.diaSemana(),
                horaInicio,
                horaFin,
                servicioIds != null ? Set.copyOf(servicioIds) : Set.of()
        );

        disponibilidadService.crear(nuevaDisponibilidad);
        return "redirect:/prestador/disponibilidades";
    }

    @PostMapping("/disponibilidades/eliminar/{id}")
    public String eliminarDisponibilidad(@PathVariable Long id, HttpSession session) {
        UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("usuario");
        if (usuario == null || usuario.rol() != com.grupo14.turnos.modelo.Rol.PRESTADOR) {
            return "redirect:/login";
        }

        disponibilidadService.eliminar(id);
        return "redirect:/prestador/disponibilidades";
    }
}

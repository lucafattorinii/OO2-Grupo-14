package com.grupo14.turnos.controller;

import com.grupo14.turnos.dto.ServicioDTO;
import com.grupo14.turnos.dto.UsuarioDTO;
import com.grupo14.turnos.modelo.Rol;
import com.grupo14.turnos.dto.PrestadorDTO;
import com.grupo14.turnos.service.PrestadorService;
import com.grupo14.turnos.service.ServicioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/prestador")
public class PrestadorMenuController {

    @Autowired
    private PrestadorService prestadorService;

    @Autowired
    private ServicioService servicioService;

    @GetMapping("/menu")
    public String menuPrestador(HttpSession session, Model model) {
        UsuarioDTO usuarioDTO = (UsuarioDTO) session.getAttribute("usuario");
        if (usuarioDTO == null || usuarioDTO.rol() != Rol.PRESTADOR) {
            System.out.println("Usuario no tiene rol PRESTADOR o no está logueado");
            return "redirect:/login";
        }

        PrestadorDTO prestadorDTO = prestadorService.buscarPorEmail(usuarioDTO.email());
        if (prestadorDTO == null) {
            return "redirect:/login";
        }

        List<ServicioDTO> servicios = servicioService.listarTodos().stream()
                .filter(s -> s.prestadorId().equals(prestadorDTO.id()))
                .collect(Collectors.toList());

        model.addAttribute("prestador", prestadorDTO);
        model.addAttribute("servicios", servicios);

        System.out.println("Entró a /prestador/menu");
        System.out.println("Usuario en sesión: " + usuarioDTO);

        return "prestador/menu";
    }


    @PostMapping("/guardar-servicio")
    public String guardarServicio(@RequestParam String nombre,
                                @RequestParam Integer duracionMin,
                                @RequestParam Double precio,
                                HttpSession session) {
        
        UsuarioDTO usuarioDTO = (UsuarioDTO) session.getAttribute("usuario");
        if (usuarioDTO == null || usuarioDTO.rol() != Rol.PRESTADOR) {
            return "redirect:/login";
        }

        
        PrestadorDTO prestador = prestadorService.buscarPorEmail(usuarioDTO.email());
        if (prestador == null) {
            return "redirect:/login";
        }

        
        ServicioDTO nuevo = new ServicioDTO(null, nombre, duracionMin, precio, prestador.id());
        servicioService.crear(nuevo);

        return "redirect:/prestador/menu";
    }

    @PostMapping("/eliminar-servicio")
    public String eliminarServicio(@RequestParam Long id, HttpSession session) {
        PrestadorDTO prestador = (PrestadorDTO) session.getAttribute("prestador");
        if (prestador == null) return "redirect:/login";

        servicioService.eliminar(id);
        return "redirect:/prestador/menu";
    }

}

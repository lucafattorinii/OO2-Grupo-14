package com.grupo14.turnos.controller;

import com.grupo14.turnos.dto.ClienteDTO;
import com.grupo14.turnos.dto.PrestadorDTO;
import com.grupo14.turnos.dto.ServicioDTO;
import com.grupo14.turnos.dto.UsuarioDTO;
import com.grupo14.turnos.modelo.Rol;
import com.grupo14.turnos.service.ClienteService;
import com.grupo14.turnos.service.PrestadorService;
import com.grupo14.turnos.service.ServicioService;
import com.grupo14.turnos.service.UsuarioService;
import jakarta.servlet.http.HttpSession;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private ClienteService clienteService;
    @Autowired
    private PrestadorService prestadorService;
    @Autowired
    private ServicioService servicioService;

    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // cerrar sesión
        return "redirect:/login";
    }

    @PostMapping("/login")
    public String procesarLogin(
        @RequestParam String email,
        @RequestParam String contrasena,
        HttpSession session,
        Model model
    ) {
        String destino;

        try {
            UsuarioDTO usuario = usuarioService.login(email, contrasena);
            session.setAttribute("usuario", usuario);

            Rol rol = usuario.rol(); 
            switch (rol) {
                case CLIENTE:
                    destino = "redirect:/cliente/menu";
                    break;
                case EMPLEADO:
                    destino = "redirect:/empleado/menu";
                    break;
                case PRESTADOR:
                    destino = "redirect:/prestador/menu";
                    break;
                default:
                    model.addAttribute("error", "Rol desconocido.");
                    destino = "login";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Credenciales inválidas.");
            destino = "login";
        }

        return destino;
    }
    
    @GetMapping("/registro")
    public String mostrarFormularioRegistro() {
        return "cliente/registrar";
    }

    @PostMapping("/registrar")
    public String registrarCliente(
        @RequestParam String email,
        @RequestParam String contrasena,
        @RequestParam Long numeroCliente,
        @RequestParam Long dni,
        @RequestParam String nombre,
        @RequestParam String apellido,
        Model model
    ) {
        String vista;
        try {
            ClienteDTO dto = new ClienteDTO(null, email, contrasena, numeroCliente, dni, nombre, apellido);
            clienteService.crear(dto);
            model.addAttribute("mensaje", "Registro exitoso. Ahora puedes iniciar sesión.");
            vista = "login";
        } catch (Exception e) {
            model.addAttribute("error", "Error al registrar cliente: " + e.getMessage());
            vista = "cliente/registrar";
        }

        return vista;
    }
    
    @GetMapping("/visitante")
    public String vistaVisitante(Model model) {
        PrestadorDTO prestador = prestadorService.obtenerUnico()
            .orElseThrow(() -> new RuntimeException("No hay prestador disponible")); 

        List<ServicioDTO> servicios = servicioService.listarServiciosDelUnicoPrestador();

        model.addAttribute("prestador", prestador);
        model.addAttribute("servicios", servicios);

        return "visitante"; 
    }
}

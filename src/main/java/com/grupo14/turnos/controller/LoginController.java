package com.grupo14.turnos.controller;

import com.grupo14.turnos.dto.PrestadorDTO;
import com.grupo14.turnos.dto.UsuarioDTO;
import com.grupo14.turnos.service.UsuarioService;
import com.grupo14.turnos.service.PrestadorService;
import jakarta.servlet.http.HttpSession;
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
    private PrestadorService prestadorService;

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

            String rol = usuario.rol().toUpperCase();

            switch (rol) {
                case "CLIENTE":
                    destino = "redirect:/cliente/menu";
                    break;
                case "EMPLEADO":
                    destino = "redirect:/empleado/menu";
                    break;
                case "PRESTADOR":
				PrestadorDTO prestadorDTO = prestadorService.buscarPorEmail(email);
                    session.setAttribute("prestador", prestadorDTO); // importante esto
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
}

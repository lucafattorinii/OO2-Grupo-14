package com.grupo14.turnos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }
    

    
    @GetMapping("/logout")
    public String logout() {
        // Aquí iría la lógica para cerrar la sesión
        return "redirect:/login";
    }
    
    @PostMapping("/login")
    public String procesarLogin(
        @RequestParam String email,
        @RequestParam String contrasena,
        Model model
    ) {
        // Aquí iría la lógica para validar el login
        // Por ahora, simplemente redirigimos al home
        return "redirect:/";
    }
}


package com.grupo14.turnos.config;

import com.grupo14.turnos.dto.PrestadorDTO;
import com.grupo14.turnos.service.PrestadorService;
import com.grupo14.turnos.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class DataInitializer {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PrestadorService prestadorService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        // Crear ADMIN si no existe
        if (usuarioRepository.findByEmail("grupo14.tp@gmail.com").isEmpty()) {
            var admin = new com.grupo14.turnos.modelo.Usuario();
            admin.setEmail("grupo14.tp@gmail.com");
            admin.setContrasena(passwordEncoder.encode("admin123"));
            admin.setRol(com.grupo14.turnos.modelo.Rol.ADMIN); 

            usuarioRepository.save(admin);
            System.out.println("Admin creado correctamente.");
        } else {
            System.out.println("Admin ya existe, no se vuelve a crear.");
        }

     // Crear PRESTADOR si no existe
        if (usuarioRepository.findByEmail("prestador.grupo14@gmail.com").isEmpty()) {
            PrestadorDTO prestadorDTO = new PrestadorDTO(
                null,
                "prestador.grupo14@gmail.com",
                "prestador123",         // contraseña sin encriptar (se encripta en el service)
                "Prestador de ejemplo", // razon social
                true                    // habilitado
            );

            prestadorService.crear(prestadorDTO);
            System.out.println("Prestador creado correctamente.");
        } else {
            System.out.println("Prestador ya existe, no se vuelve a crear.");
        }
    }
}

package com.grupo14.turnos.config;

import com.grupo14.turnos.modelo.Usuario;
import com.grupo14.turnos.modelo.Rol;
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
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        // Verifica si ya existe el admin
        if (usuarioRepository.findByEmail("grupo14.tp@gmail.com").isEmpty()) {
            Usuario admin = new Usuario();
            admin.setEmail("grupo14.tp@gmail.com");
            admin.setContrasena(passwordEncoder.encode("admin123"));
            admin.setRol(Rol.ADMIN); 

            usuarioRepository.save(admin);
            System.out.println("Admin creado correctamente.");
        } else {
            System.out.println("Admin ya existe, no se vuelve a crear.");
        }
    }
}
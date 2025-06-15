package com.grupo14.turnos.service.impl;

import com.grupo14.turnos.dto.UsuarioDTO;
import com.grupo14.turnos.service.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioService usuarioService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UsuarioDTO usuario = usuarioService.buscarPorEmail(email);

        if (usuario == null) {
            throw new UsernameNotFoundException("Usuario no encontrado con email: " + email);
        }

        return new User(
            usuario.email(),
            usuario.contrasena(), // ⚠️ Tiene que estar encriptada
            List.of(new SimpleGrantedAuthority("ROLE_" + usuario.rol().name()))
        );
    }
}
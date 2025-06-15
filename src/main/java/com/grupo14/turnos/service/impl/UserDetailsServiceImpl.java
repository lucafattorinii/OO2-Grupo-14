package com.grupo14.turnos.service.impl;

import com.grupo14.turnos.dto.UsuarioDTO;
import com.grupo14.turnos.service.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioService usuarioService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println(">>> Entró al loadUserByUsername con email: " + email);

        UsuarioDTO usuario = usuarioService.buscarPorEmail(email);

        if (usuario == null) {
            System.out.println(">>> Usuario no encontrado");
            throw new UsernameNotFoundException("Usuario no encontrado con email: " + email);
        }

        System.out.println(">>> Usuario encontrado: " + usuario.email() + " con rol " + usuario.rol());

        return new User(
            usuario.email(),
            usuario.contrasena(),
            List.of(new SimpleGrantedAuthority("ROLE_" + usuario.rol().name()))
        );
    }
}
package com.grupo14.turnos.service;

import com.grupo14.turnos.dto.AuthRequestDTO;
import com.grupo14.turnos.dto.AuthResponseDTO;
import com.grupo14.turnos.exception.RecursoNoEncontradoException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final AuthenticationManager authManager;

    public AuthService(AuthenticationManager authManager) {
        this.authManager = authManager;
    }

    public AuthResponseDTO login(AuthRequestDTO request) {
        try {
            Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.username(), 
                    request.password()
                )
            );
            SecurityContextHolder.getContext().setAuthentication(auth);
            return new AuthResponseDTO("Login exitoso");
        } catch (BadCredentialsException e) {
            throw new RecursoNoEncontradoException("Usuario o contraseña incorrectos");
        }
    }
}
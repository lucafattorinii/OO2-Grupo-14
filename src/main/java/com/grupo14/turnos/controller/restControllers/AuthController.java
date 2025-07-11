package com.grupo14.turnos.controller.restControllers;

import com.grupo14.turnos.dto.AuthRequestDTO;
import com.grupo14.turnos.dto.AuthResponseDTO;
import com.grupo14.turnos.exception.RecursoNoEncontradoException;
import com.grupo14.turnos.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "API para la autenticación de usuarios")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Iniciar sesión",
              description = "Autentica un usuario y devuelve un mensaje de éxito")
    @ApiResponse(responseCode = "200", 
                description = "Autenticación exitosa",
                content = @Content(schema = @Schema(implementation = AuthResponseDTO.class)))
    @ApiResponse(responseCode = "404", 
                description = "Credenciales inválidas",
                content = @Content)
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Credenciales de autenticación",
                required = true,
                content = @Content(schema = @Schema(implementation = AuthRequestDTO.class))
            )
            @RequestBody AuthRequestDTO request) {
        try {
            AuthResponseDTO response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (RecursoNoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
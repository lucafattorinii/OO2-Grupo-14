package com.grupo14.turnos.dto;

import com.grupo14.turnos.modelo.Rol;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrestadorDTO {
    private Long id;
    private String email;
    private String contrasena;
    private String razonSocial;
    private Boolean habilitado;
    private Rol rol;

    public PrestadorDTO() {
        // Constructor vacío requerido por Spring
    }

    public PrestadorDTO(Long id, String email, String contrasena, String razonSocial, Boolean habilitado) {
        this.id = id;
        this.email = email;
        this.contrasena = contrasena;
        this.razonSocial = razonSocial;
        this.habilitado = habilitado;
        this.rol = Rol.PRESTADOR;
    }

    public PrestadorDTO(Long id, String email, String contrasena, String razonSocial, Boolean habilitado, Rol rol) {
        this.id = id;
        this.email = email;
        this.contrasena = contrasena;
        this.razonSocial = razonSocial;
        this.habilitado = habilitado;
        this.rol = rol;
    }
}

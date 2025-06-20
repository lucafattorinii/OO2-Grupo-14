package com.grupo14.turnos.dto;

import com.grupo14.turnos.modelo.Rol;

import jakarta.validation.constraints.NotNull;

public class PrestadorDTO {
    private Long id;
    private String email;
    private String contrasena;
    private String razonSocial;
    private boolean habilitado;

    // Constructor sin argumentos (necesario para Spring)
    public PrestadorDTO() {}

    // Constructor completo (necesario para DataInitializer y otros usos)
    public PrestadorDTO(Long id, String email, String contrasena, String razonSocial, boolean habilitado) {
        this.id = id;
        this.email = email;
        this.contrasena = contrasena;
        this.razonSocial = razonSocial;
        this.habilitado = habilitado;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public boolean isHabilitado() {
        return habilitado;
    }

    public void setHabilitado(boolean habilitado) {
        this.habilitado = habilitado;
    }
}
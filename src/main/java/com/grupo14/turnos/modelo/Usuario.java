package com.grupo14.turnos.modelo;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usuario")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected long id;

    @Column(name = "email", length = 100, unique = true, nullable = false)
    private String email;

    @Column(name = "contrasena", length = 255, nullable = false)
    private String contrasena;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol", length = 20, nullable = false)
    private Rol rol;

    public Usuario(String email, String contrasena, Rol rol) {
        this.email = email;
        this.contrasena = contrasena;
        this.rol = rol;
    }
}

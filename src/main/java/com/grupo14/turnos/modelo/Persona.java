package com.grupo14.turnos.modelo;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "persona")
@PrimaryKeyJoinColumn(name = "id", referencedColumnName = "id")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Persona extends Usuario {

    @Column(name = "nombre", length = 100)
    private String nombre;

    @Column(name = "apellido", length = 100)
    private String apellido;

    @Column(name = "dni")
    private Long dni;

  
    public Persona(String email, String contrasena, Rol rol,
                   String nombre, String apellido, Long dni) {
        super(email, contrasena, rol);
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
    }
}

package com.grupo14.turnos.modelo;

import jakarta.persistence.*;


@Entity
@Table(name = "persona")
@PrimaryKeyJoinColumn(name = "id", referencedColumnName = "id")
public class Persona extends Usuario {

    @Column(name = "nombre", length = 100)
    private String nombre;

    @Column(name = "apellido", length = 100)
    private String apellido;

    @Column(name = "dni")
    private Long dni;

  
    public Persona() { }

    public Persona(String email, String contrasena,
                   String nombre, String apellido, Long dni) {
        super(email, contrasena);
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
    }

    

    // getters y setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Long getDni() {
        return dni;
    }

    public void setDni(Long dni) {
        this.dni = dni;
    }
}

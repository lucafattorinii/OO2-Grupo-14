package com.grupo14.turnos.modelo;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "cliente")
@PrimaryKeyJoinColumn(name = "id", referencedColumnName = "id")
public class Cliente extends Persona {

    @Column(name = "numero_cliente")
    private Long numeroCliente;

   
    @OneToMany(
        mappedBy = "cliente",
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    private Set<Turno> turnos = new HashSet<>();

 
    // Constructores
  
    public Cliente() { }

    public Cliente(String email, String contrasena, String rol,
                   String nombre, String apellido, Long dni,
                   Long numeroCliente) {
        super(email, contrasena, rol, nombre, apellido, dni);
        this.numeroCliente = numeroCliente;
    }

  
    // Getters / Setters
    
    public Long getNumeroCliente() {
        return numeroCliente;
    }

    public void setNumeroCliente(Long numeroCliente) {
        this.numeroCliente = numeroCliente;
    }

    public Set<Turno> getTurnos() {
        return turnos;
    }

    public void setTurnos(Set<Turno> turnos) {
        this.turnos = turnos;
    }

    
    public void addTurno(Turno turno) {
        turnos.add(turno);
        turno.setCliente(this);
    }

    public void removeTurno(Turno turno) {
        turnos.remove(turno);
        turno.setCliente(null);
    }
}

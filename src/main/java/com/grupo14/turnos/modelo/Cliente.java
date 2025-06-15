package com.grupo14.turnos.modelo;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "cliente")
@PrimaryKeyJoinColumn(name = "id", referencedColumnName = "id")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    // Constructor personalizado (sin Lombok, porque hereda parámetros del padre)
    public Cliente(String email, String contrasena, Rol rol,
                   String nombre, String apellido, Long dni,
                   Long numeroCliente) {
        super(email, contrasena, rol, nombre, apellido, dni);
        this.numeroCliente = numeroCliente;
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

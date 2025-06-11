package com.grupo14.turnos.modelo;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "empleado")
@PrimaryKeyJoinColumn(name = "id", referencedColumnName = "id")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Empleado extends Persona {

    @Column(name = "cuit")
    private Long cuit;

    @Column(name = "legajo")
    private Integer legajo;

    @Column(name = "puesto_cargo", length = 100)
    private String puestoCargo;

    // Un empleado pertenece a UN servicio
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "servicio_id",
        referencedColumnName = "id_servicio",
        foreignKey = @ForeignKey(name = "empleado_servicio_fk")
    )
    private Servicio servicio;

    // Constructor que incluye atributos del padre y el servicio
    public Empleado(String email, String contrasena, Rol rol,
                    String nombre, String apellido, Long dni,
                    Long cuit, Integer legajo, String puestoCargo,
                    Servicio servicio) {
        super(email, contrasena, rol, nombre, apellido, dni);
        this.cuit = cuit;
        this.legajo = legajo;
        this.puestoCargo = puestoCargo;
        this.servicio = servicio;
    }
}
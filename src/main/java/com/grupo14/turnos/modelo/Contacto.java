package com.grupo14.turnos.modelo;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "contacto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contacto {

    @Id
    @Column(name = "id")
    private Long id; // Ahora depende del ID de Usuario

    @Column(name = "telefono", length = 20)
    private String telefono;

    // Relación 1 a 1 con Direccion (no bidireccional)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "direccion_id",
        referencedColumnName = "id_direccion",
        foreignKey = @ForeignKey(name = "contacto_direccion_fk")
    )
    private Direccion direccion;

    // Relación 1 a 1 con Usuario (comparten ID, y Contacto depende de Usuario)
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId // Comparte el mismo ID con Usuario
    @JoinColumn(
        name = "id",
        referencedColumnName = "id",
        foreignKey = @ForeignKey(name = "contacto_usuario_fk")
    )
    private Usuario usuario;
}
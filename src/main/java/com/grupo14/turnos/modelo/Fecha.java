package com.grupo14.turnos.modelo;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "fecha")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Fecha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Enumerated(EnumType.STRING)
    @Column(name = "dia_semana", nullable = false)
    private DiaSemana diaSemana;

    // Relación uno a uno con Turno (obligatoria)
    @OneToOne(mappedBy = "fecha", optional = false)
    private Turno turno;

    // Relación uno a uno con Dirección 
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "direccion_id",
        referencedColumnName = "id_direccion",
        foreignKey = @ForeignKey(name = "fecha_direccion_fk")
    )
    private Direccion direccion;
}

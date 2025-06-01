package com.grupo14.turnos.modelo;

import jakarta.persistence.*;
import java.time.LocalTime;

/**
 * Tabla SQL:
 *   CREATE TABLE disponibilidad (
 *     id INT AUTO_INCREMENT PRIMARY KEY,
 *     dia_semana ENUM('LUNES','MARTES','MIERCOLES','JUEVES','VIERNES','SABADO','DOMINGO'),
 *     hora_inicio TIME,
 *     hora_fin TIME,
 *     servicio_id BIGINT,
 *     empleado_id INT,
 *     FOREIGN KEY (servicio_id)  REFERENCES servicio(id_servicio),
 *     FOREIGN KEY (empleado_id)  REFERENCES empleado(id)
 *   );
 *
 * Cada Disponibilidad pertenece a un Servicio y a un Empleado.
 */
@Entity
@Table(name = "disponibilidad")
public class Disponibilidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "dia_semana", nullable = false)
    private DiaSemana diaSemana;

    @Column(name = "hora_inicio")
    private LocalTime horaInicio;

    @Column(name = "hora_fin")
    private LocalTime horaFin;

    /**
     * Relación MANY-TO-ONE con Servicio.
     * En la tabla disponibilidad, columna servicio_id.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "servicio_id",
        referencedColumnName = "id_servicio",
        nullable = false,
        foreignKey = @ForeignKey(name = "disponibilidad_servicio_fk")
    )
    private Servicio servicio;

    /**
     * Relación MANY-TO-ONE con Empleado.
     * En la tabla disponibilidad, columna empleado_id.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "empleado_id",
        referencedColumnName = "id",
        nullable = false,
        foreignKey = @ForeignKey(name = "disponibilidad_empleado_fk")
    )
    private Empleado empleado;

    // ------------------------------
    // Constructores
    // ------------------------------
    public Disponibilidad() { }

    public Disponibilidad(DiaSemana diaSemana, LocalTime horaInicio,
                          LocalTime horaFin, Servicio servicio, Empleado empleado) {
        this.diaSemana = diaSemana;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.setServicio(servicio);
        this.setEmpleado(empleado);
    }

    // ------------------------------
    // Getters / Setters
    // ------------------------------
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DiaSemana getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(DiaSemana diaSemana) {
        this.diaSemana = diaSemana;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }

    public Servicio getServicio() {
        return servicio;
    }

    /**
     * Sincroniza relación bidireccional con Servicio.
     */
    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
        if (servicio != null && !servicio.getDisponibilidades().contains(this)) {
            servicio.getDisponibilidades().add(this);
        }
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    /**
     * Sincroniza relación bidireccional con Empleado.
     */
    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
        if (empleado != null && !empleado.getDisponibilidades().contains(this)) {
            empleado.getDisponibilidades().add(this);
        }
    }
}

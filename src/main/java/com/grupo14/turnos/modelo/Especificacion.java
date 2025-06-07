package com.grupo14.turnos.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "especificacion")
public class Especificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "direccion_id",
        referencedColumnName = "id_direccion",
        foreignKey = @ForeignKey(name = "especificacion_direccion_fk")
    )
    private Direccion direccion;

    @Enumerated(EnumType.STRING)
    @Column(name = "rubro", nullable = false)
    private Rubro rubro;

    @Column(name = "detalles", columnDefinition = "TEXT")
    private String detalles;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "servicio_id",
        referencedColumnName = "id_servicio",
        unique = true,
        nullable = false,
        foreignKey = @ForeignKey(name = "especificacion_servicio_fk")
    )
    private Servicio servicio;


    public Especificacion() { }

    public Especificacion(Direccion direccion, Rubro rubro,
                         String detalles, Servicio servicio) {
        this.direccion = direccion;
        this.rubro = rubro;
        this.detalles = detalles;
        this.setServicio(servicio);
    }


    // getters y setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public Rubro getRubro() {
        return rubro;
    }

    public void setRubro(Rubro rubro) {
        this.rubro = rubro;
    }

    public String getDetalles() {
        return detalles;
    }

    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
        /*if (servicio != null && servicio.getEspecificacion() == null) {
            servicio.setEspecificacion(this);
        }*/
    }

}

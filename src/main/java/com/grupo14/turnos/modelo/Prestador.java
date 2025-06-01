package com.grupo14.turnos.modelo;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "prestador")
@PrimaryKeyJoinColumn(name = "id", referencedColumnName = "id")
public class Prestador extends Usuario {

    @Column(name = "razon_social", length = 100)
    private String razonSocial;

    @Column(name = "habilitado")
    private Boolean habilitado;

 
    @OneToMany(
        mappedBy = "prestador",
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    private Set<Servicio> servicios = new HashSet<>();

   
    public Prestador() { }

    public Prestador(String email, String contrasena,
                     String razonSocial, Boolean habilitado) {
        super(email, contrasena);
        this.razonSocial = razonSocial;
        this.habilitado = habilitado;
    }

    /// getters y setters
    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public Boolean getHabilitado() {
        return habilitado;
    }

    public void setHabilitado(Boolean habilitado) {
        this.habilitado = habilitado;
    }

    public Set<Servicio> getServicios() {
        return servicios;
    }

    public void setServicios(Set<Servicio> servicios) {
        this.servicios = servicios;
    }

    
    public void addServicio(Servicio servicio) {
        servicios.add(servicio);
        servicio.setPrestador(this);
    }

    public void removeServicio(Servicio servicio) {
        servicios.remove(servicio);
        servicio.setPrestador(null);
    }
}

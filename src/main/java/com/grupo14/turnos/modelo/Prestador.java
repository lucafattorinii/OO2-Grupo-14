package com.grupo14.turnos.modelo;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "prestador")
@PrimaryKeyJoinColumn(name = "id", referencedColumnName = "id")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

  
    public Prestador(String email, String contrasena, Rol rol,
                     String razonSocial, Boolean habilitado) {
        super(email, contrasena, rol);
        this.razonSocial = razonSocial;
        this.habilitado = habilitado;
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

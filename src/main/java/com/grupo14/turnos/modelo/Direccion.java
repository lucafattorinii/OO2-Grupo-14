package com.grupo14.turnos.modelo;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "direccion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Direccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_direccion")
    private long idDireccion;

    @Column(name = "pais", length = 50)
    private String pais;

    @Column(name = "provincia", length = 50)
    private String provincia;

    @Column(name = "ciudad", length = 50)
    private String ciudad;

    @Column(name = "calle", length = 100)
    private String calle;

    @Column(name = "numero_calle", length = 10)
    private String numeroCalle;

    @Column(name = "codigo_postal", length = 10)
    private String codigoPostal;

    // Constructor con todos los campos excepto idDireccion
    @Builder
    public Direccion(String pais, String provincia, String ciudad,
                     String calle, String numeroCalle, String codigoPostal) {
        this.pais = pais;
        this.provincia = provincia;
        this.ciudad = ciudad;
        this.calle = calle;
        this.numeroCalle = numeroCalle;
        this.codigoPostal = codigoPostal;
    }
}

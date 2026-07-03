package com.J.M_CODERS.KinalCodeQuest.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "capitulo_historia")
@Getter
@Setter
@NoArgsConstructor
public class CapituloHistoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCapitulo;

    @Column(name = "id_capitulo_padre")
    private Integer idCapituloPadre;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(length = 50)
    private String epoca;

    @Column(length = 30)
    private String tipoContenido;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String contenidoNarrativo;

    @Column(length = 255)
    private String rutaImagen;

    @Column(nullable = false)
    private Integer costoCantidad;

    @Column(nullable = false, length = 30)
    private String tipoMoneda;

    @Column(length = 100)
    private String bonusRecompensa;

    @Transient
    private boolean desbloqueado;

    @Transient
    private boolean imagenDesbloqueada;

    @Transient
    private Integer costoLaboriosidad;
}
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

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String contenidoNarrativo;

    @Column(nullable = false)
    private Integer expRequerida;

    @Transient
    private boolean desbloqueado;
}
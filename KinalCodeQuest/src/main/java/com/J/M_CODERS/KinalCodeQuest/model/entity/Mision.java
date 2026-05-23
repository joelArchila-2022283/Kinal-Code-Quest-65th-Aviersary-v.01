package com.J.M_CODERS.KinalCodeQuest.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "mision")
@Getter
@Setter
@NoArgsConstructor
public class Mision {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idMision;

    @ManyToOne
    @JoinColumn(name = "id_area", nullable = false)
    private AreaTecnica area;

    @Column(nullable = false, length = 100)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descripcionNarrativa;

    @Column(columnDefinition = "TEXT")
    private String codigoBase;

    @Column(columnDefinition = "TEXT")
    private String criterioEvaluacion;

    private Integer expRecompensa;
}

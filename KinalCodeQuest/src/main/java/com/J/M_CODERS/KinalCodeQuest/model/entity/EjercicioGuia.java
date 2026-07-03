package com.J.M_CODERS.KinalCodeQuest.model.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad que representa los ejercicios guía para principiantes
 * Son ejercicios sencillos que ayudan a aprender Java
 */
@Entity
@Table(name = "ejercicio_guia")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class EjercicioGuia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idEjercicio;

    @Column(nullable = false, length = 50)
    private String titulo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String enunciado;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String codigoTemplate;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String criterioEvaluacion;

    @Column(nullable = false, length = 20)
    private String dificultad; // FACIL, INTERMEDIO

    @Column(nullable = false)
    private Integer puntosRecompensa; // 10 puntos por defecto

    @Column(nullable = false)
    private Integer orden; // Orden de presentación (1-10)

    @Column(nullable = false)
    private Boolean activo;

    @Column(columnDefinition = "TEXT")
    private String pistas;
}


package com.J.M_CODERS.KinalCodeQuest.model.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "progreso_ejercicio")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProgresoEjercicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_jugador")
    private Jugador jugador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ejercicio")
    private EjercicioGuia ejercicio;

    @Column(name = "fecha_completado")
    private LocalDateTime fechaCompletado;

    @Column(name = "completada")
    private Boolean completada = false;

    @Column(name = "intentos")
    private Integer intentos = 0;

    @Column(name = "ultimo_codigo_enviado", columnDefinition = "TEXT")
    private String ultimoCodigoEnviado;

}


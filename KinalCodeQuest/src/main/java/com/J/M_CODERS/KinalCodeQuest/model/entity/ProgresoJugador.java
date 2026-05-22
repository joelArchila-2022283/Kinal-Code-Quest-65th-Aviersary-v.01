package com.J.M_CODERS.KinalCodeQuest.model.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "progreso_jugador", uniqueConstraints = @UniqueConstraint(columnNames = {"id_jugador", "id_mision"}))
@Getter
@Setter
@NoArgsConstructor
public class ProgresoJugador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idProgreso;

    @ManyToOne
    @JoinColumn(name = "id_jugador")
    private Jugador jugador;

    @ManyToOne
    @JoinColumn(name = "id_mision")
    private Mision mision;

    private Boolean completada = false;
    private Integer intentos = 0;

    @Column(columnDefinition = "TEXT")
    private String ultimoCodigoEnviado;

    private LocalDateTime fechaCompletado;
}

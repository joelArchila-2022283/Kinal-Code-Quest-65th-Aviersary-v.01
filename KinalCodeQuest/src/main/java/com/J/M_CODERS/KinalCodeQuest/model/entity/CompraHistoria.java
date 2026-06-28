package com.J.M_CODERS.KinalCodeQuest.model.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "compra_historia")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CompraHistoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCompra;

    // Relación con el Jugador que hace la compra
    @ManyToOne
    @JoinColumn(name = "id_jugador", nullable = false)
    private Jugador jugador;

    // Relación con el Archivo Histórico comprado
    @ManyToOne
    @JoinColumn(name = "id_capitulo", nullable = false)
    private CapituloHistoria capituloHistoria;

    @Column(nullable = false)
    private LocalDateTime fechaCompra;

    @PrePersist
    protected void onCreate() {
        fechaCompra = LocalDateTime.now();
    }
}
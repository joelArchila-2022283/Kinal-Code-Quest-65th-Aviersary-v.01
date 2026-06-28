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
    private Integer idCapituloPadre; // Enlace al nodo principal (Null si es historia core)

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(length = 50)
    private String epoca; // Ej: 'MARZO_1961' o 'FILOSOFÍA'

    @Column(length = 30)
    private String tipoContenido; // 'PRINCIPAL', 'FOTO_ANTIGUA', 'VALOR_INSTITUCIONAL'

    @Column(columnDefinition = "TEXT", nullable = false)
    private String contenidoNarrativo;

    @Column(length = 255)
    private String rutaImagen;

    // --- SISTEMA DE COSTOS Y ECONOMÍA ---
    @Column(nullable = false)
    private Integer costoCantidad; // Cuánto cuesta (puntos o EXP)

    @Column(nullable = false, length = 30)
    private String tipoMoneda; // 'EXP', 'RESPONSABILIDAD', 'LABORIOSIDAD', 'SOLIDARIDAD'

    @Column(length = 100)
    private String bonusRecompensa; // Texto estético del beneficio obtenido

    @Transient
    private boolean desbloqueado;
}
package com.J.M_CODERS.KinalCodeQuest.model.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "jugador")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Jugador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idJugador;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, length = 50)
    private String nombreAvatar;

    @Column(nullable = false, length = 30)
    private String rol;

    // --- ESTADÍSTICAS ---
    @Column(name = "experiencia")
    private Integer experiencia;

    @Column(name = "ptos_responsabilidad")
    private Integer ptosResponsabilidad;

    @Column(name = "ptos_laboriosidad")
    private Integer ptosLaboriosidad;

    // --- BILLETERA ACTIVA ---
    @Column(name = "saldo_responsabilidad")
    private Integer saldoResponsabilidad;

    @Column(name = "saldo_laboriosidad")
    private Integer saldoLaboriosidad;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;

    @PrePersist
    protected void onCreate() {
        fechaRegistro = LocalDateTime.now();
        experiencia = (experiencia == null) ? 0 : experiencia;
        rol = (rol == null) ? "ROLE_ESTUDIANTE" : rol;

        // Inicialización segura para evitar nulos
        ptosResponsabilidad = (ptosResponsabilidad == null) ? 0 : ptosResponsabilidad;
        ptosLaboriosidad = (ptosLaboriosidad == null) ? 0 : ptosLaboriosidad;

        saldoResponsabilidad = (saldoResponsabilidad == null) ? 0 : saldoResponsabilidad;
        saldoLaboriosidad = (saldoLaboriosidad == null) ? 0 : saldoLaboriosidad;
    }
}
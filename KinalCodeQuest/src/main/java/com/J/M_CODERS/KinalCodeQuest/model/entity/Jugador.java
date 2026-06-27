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

    // --- ESTADÍSTICAS HISTÓRICAS (Solo suben - Nivel de acceso y prestigio) ---
    private Integer experiencia;
    private Integer ptosResponsabilidad;
    private Integer ptosSolidaridad;
    private Integer ptosLaboriosidad;

    // --- BILLETERA ACTIVA (Saldos que se restan al comprar archivos del lore) ---
    private Integer saldoResponsabilidad;
    private Integer saldoSolidaridad;
    private Integer saldoLaboriosidad;

    private LocalDateTime fechaRegistro;

    @PrePersist
    protected void onCreate() {
        fechaRegistro = LocalDateTime.now();
        experiencia = (experiencia == null) ? 0 : experiencia;
        rol = (rol == null) ? "ROLE_ESTUDIANTE" : rol;

        // Inicialización de puntos históricos
        ptosResponsabilidad = 0;
        ptosSolidaridad = 0;
        ptosLaboriosidad = 0;

        // Inicialización de la billetera
        saldoResponsabilidad = 0;
        saldoSolidaridad = 0;
        saldoLaboriosidad = 0;
    }
}
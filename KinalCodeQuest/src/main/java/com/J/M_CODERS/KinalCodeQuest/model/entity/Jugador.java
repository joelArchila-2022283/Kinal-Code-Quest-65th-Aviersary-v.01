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

    private Integer experiencia;
    private Integer ptosResponsabilidad;
    private Integer ptosSolidaridad;
    private Integer ptosLaboriosidad;

    private LocalDateTime fechaRegistro;

    @PrePersist
    protected void onCreate() {
        fechaRegistro = LocalDateTime.now();
        experiencia = (experiencia == null) ? 0 : experiencia;
        rol = (rol == null) ? "ROLE_ESTUDIANTE" : rol;
        ptosResponsabilidad = 0;
        ptosSolidaridad = 0;
        ptosLaboriosidad = 0;
    }
}
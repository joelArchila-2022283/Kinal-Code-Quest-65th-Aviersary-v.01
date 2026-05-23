package com.J.M_CODERS.KinalCodeQuest.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "area_tecnica")
@Getter @Setter
@NoArgsConstructor
public class AreaTecnica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idArea;

    @Column(nullable = false, length = 50)
    private String nombre;

    private String descripcionTematica;
}

package com.J.M_CODERS.KinalCodeQuest.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

/* Para manejar la logica de las preguntas de opccion multiple */

@Getter @Setter
@AllArgsConstructor
public class PreguntaTrivia {
    private Integer idPregunta;
    private Integer idAreaAsociada; // Vincula la pregunta al nivel/área técnica
    private String enunciado;
    private List<String> opciones;
    private Integer respuestaCorrectaIndex; // Índice de la respuesta correcta (0 a 3)
    private String justificacion; // Explicación educativa si fallan
}
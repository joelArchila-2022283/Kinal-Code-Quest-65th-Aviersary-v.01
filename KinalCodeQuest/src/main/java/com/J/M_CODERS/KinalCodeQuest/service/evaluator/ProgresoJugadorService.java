package com.J.M_CODERS.KinalCodeQuest.service.evaluator;

import com.J.M_CODERS.KinalCodeQuest.model.entity.ProgresoJugador;
import com.J.M_CODERS.KinalCodeQuest.model.entity.Jugador;
import com.J.M_CODERS.KinalCodeQuest.model.entity.Mision;
import java.util.List;

public interface ProgresoJugadorService {
    ProgresoJugador obtenerORegistrarProgreso(Jugador jugador, Mision mision);
    ProgresoJugador registrarIntento(ProgresoJugador progreso, String codigo, boolean esCorrecto);
    List<ProgresoJugador> listarProgresoPorJugador(Jugador jugador);
}
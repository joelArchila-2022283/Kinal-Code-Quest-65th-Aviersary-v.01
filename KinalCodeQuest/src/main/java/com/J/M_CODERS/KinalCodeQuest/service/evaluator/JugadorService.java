package com.J.M_CODERS.KinalCodeQuest.service.evaluator;

import com.J.M_CODERS.KinalCodeQuest.model.entity.Jugador;

public interface JugadorService {
    Jugador registrarJugador(Jugador jugador);
    Jugador autenticar(String username, String password);
    Jugador buscarPorId(Integer id);
    Jugador actualizarProgreso(Jugador jugador);
    Jugador guardar(Jugador jugador);
}

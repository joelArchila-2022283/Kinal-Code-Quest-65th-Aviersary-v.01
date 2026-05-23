package com.J.M_CODERS.KinalCodeQuest.service.impl;

import com.J.M_CODERS.KinalCodeQuest.model.entity.Jugador;
import com.J.M_CODERS.KinalCodeQuest.repository.JugadorRepository;
import com.J.M_CODERS.KinalCodeQuest.service.evaluator.JugadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JugadorServiceImplements implements JugadorService {

    @Autowired
    private JugadorRepository jugadorRepository;

    @Override
    public Jugador registrarJugador(Jugador jugador) {
        return jugadorRepository.save(jugador);
    }

    @Override
    public Jugador autenticar(String username, String password) {
        Jugador jugador = jugadorRepository.findByUsername(username).orElse(null);
        if (jugador != null && jugador.getPassword().equals(password)) {
            return jugador;
        }
        return null;
    }

    @Override
    public Jugador buscarPorId(Integer id) {
        return jugadorRepository.findById(id).orElse(null);
    }

    @Override
    public Jugador actualizarProgreso(Jugador jugador) {
        return jugadorRepository.save(jugador);
    }
}
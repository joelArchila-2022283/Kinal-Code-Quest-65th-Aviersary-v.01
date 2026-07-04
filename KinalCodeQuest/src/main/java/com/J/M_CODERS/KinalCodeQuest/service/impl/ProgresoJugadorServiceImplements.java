package com.J.M_CODERS.KinalCodeQuest.service.impl;

import com.J.M_CODERS.KinalCodeQuest.model.entity.Jugador;
import com.J.M_CODERS.KinalCodeQuest.model.entity.Mision;
import com.J.M_CODERS.KinalCodeQuest.model.entity.ProgresoJugador;
import com.J.M_CODERS.KinalCodeQuest.repository.ProgresoJugadorRepository;
import com.J.M_CODERS.KinalCodeQuest.repository.JugadorRepository;
import com.J.M_CODERS.KinalCodeQuest.service.evaluator.ProgresoJugadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProgresoJugadorServiceImplements implements ProgresoJugadorService {

    @Autowired
    private ProgresoJugadorRepository progresoRepository;

    @Autowired
    private JugadorRepository jugadorRepository;

    @Override
    public ProgresoJugador obtenerORegistrarProgreso(Jugador jugador, Mision mision) {
        return progresoRepository.findByJugadorAndMision(jugador, mision)
                .orElseGet(() -> {
                    ProgresoJugador nuevo = new ProgresoJugador();
                    nuevo.setJugador(jugador);
                    nuevo.setMision(mision);
                    nuevo.setCompletada(false);
                    nuevo.setIntentos(0);
                    nuevo.setUltimoCodigoEnviado(mision.getCodigoBase());
                    return progresoRepository.save(nuevo);
                });
    }

    @Override
    public ProgresoJugador registrarIntento(ProgresoJugador progreso, String codigo, boolean esCorrecto) {
        // Se incrementa siempre el contador de telemetría e historial para el Dashboard
        progreso.setIntentos(progreso.getIntentos() + 1);
        progreso.setUltimoCodigoEnviado(codigo);

        Jugador jugador = progreso.getJugador();

        // CASO 1: El código es correcto y es la PRIMERA VEZ que completa la misión (Puntos Únicos)
        if (esCorrecto && !progreso.getCompletada()) {
            progreso.setCompletada(true);
            progreso.setFechaCompletado(LocalDateTime.now());

            Mision m = progreso.getMision();
            jugador.setExperiencia(jugador.getExperiencia() + m.getExpRecompensa());

            // Regla de Responsabilidad basada en la eficiencia de intentos
            if (progreso.getIntentos() <= 2) {
                jugador.setPtosResponsabilidad(jugador.getPtosResponsabilidad() + 10);
            } else {
                jugador.setPtosResponsabilidad(jugador.getPtosResponsabilidad() + 5);
            }

            // Regla de Laboriosidad premiando la persistencia
            if (progreso.getIntentos() >= 4) {
                jugador.setPtosLaboriosidad(jugador.getPtosLaboriosidad() + 15);
            } else {
                jugador.setPtosLaboriosidad(jugador.getPtosLaboriosidad() + 5);
            }

            jugadorRepository.save(jugador);

        }
        // CASO 2: El código es incorrecto, pero la misión NO ha sido completada aún
        // Evita el farmeo infinito de puntos de consolación enviando fallos intencionales
        else if (!esCorrecto && !progreso.getCompletada()) {
            jugador.setPtosLaboriosidad(jugador.getPtosLaboriosidad() + 1);
            jugadorRepository.save(jugador);
        }

        // CASO 3 (Implícito): Si la misión ya tiene 'progreso.getCompletada() == true',
        // el sistema registra el nuevo código e intento, pero ignora la asignación de puntos.

        return progresoRepository.save(progreso);
    }

    @Override
    public List<ProgresoJugador> listarProgresoPorJugador(Jugador jugador) {
        return progresoRepository.findByJugador(jugador);
    }
}
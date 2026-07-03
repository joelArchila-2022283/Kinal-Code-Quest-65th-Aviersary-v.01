package com.J.M_CODERS.KinalCodeQuest.service.evaluator;

import com.J.M_CODERS.KinalCodeQuest.model.entity.EjercicioGuia;
import com.J.M_CODERS.KinalCodeQuest.model.entity.Jugador;
import com.J.M_CODERS.KinalCodeQuest.model.entity.ProgresoEjercicio;
import com.J.M_CODERS.KinalCodeQuest.repository.EjercicioGuiaRepository;
import com.J.M_CODERS.KinalCodeQuest.repository.JugadorRepository;
import com.J.M_CODERS.KinalCodeQuest.repository.ProgresoEjercicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EjercicioGuiaService {

    @Autowired
    private EjercicioGuiaRepository ejercicioRepo;

    @Autowired
    private ProgresoEjercicioRepository progresoRepo;

    @Autowired
    private JugadorRepository jugadorRepo;

    public List<EjercicioGuia> listarTodosActivos() {
        return ejercicioRepo.findAll();
    }

    public Optional<EjercicioGuia> buscarPorId(Integer id) {
        return ejercicioRepo.findById(id);
    }

    public java.util.List<Integer> ejerciciosCompletadosIds(Jugador jugador) {
        java.util.List<ProgresoEjercicio> lista = progresoRepo.findByJugador(jugador);
        java.util.List<Integer> ids = new java.util.ArrayList<>();
        if (lista != null) {
            for (ProgresoEjercicio p : lista) {
                if (Boolean.TRUE.equals(p.getCompletada()) && p.getEjercicio() != null && p.getEjercicio().getIdEjercicio() != null) {
                    ids.add(p.getEjercicio().getIdEjercicio());
                }
            }
        }
        return ids;
    }

    /**
     * Registra un intento (fallido o no) del jugador sobre un ejercicio guía.
     * Si no existe registro, lo crea; si existe incrementa el contador y guarda el último código.
     */
    public void registrarIntento(Jugador jugador, EjercicioGuia ejercicio, String codigoEnviado) {
        if (jugador == null || ejercicio == null) return;
        Optional<ProgresoEjercicio> existente = progresoRepo.findByJugadorAndEjercicio(jugador, ejercicio);
        ProgresoEjercicio p;
        if (existente.isPresent()) {
            p = existente.get();
            Integer actuales = p.getIntentos() == null ? 0 : p.getIntentos();
            p.setIntentos(actuales + 1);
            p.setUltimoCodigoEnviado(codigoEnviado);
        } else {
            p = new ProgresoEjercicio();
            p.setJugador(jugador);
            p.setEjercicio(ejercicio);
            p.setIntentos(1);
            p.setUltimoCodigoEnviado(codigoEnviado);
            p.setCompletada(false);
        }
        progresoRepo.save(p);

        // Otorgar 1 punto de laboriosidad por intento fallido como policy leve
        try {
            int ptos = jugador.getPtosLaboriosidad() == null ? 0 : jugador.getPtosLaboriosidad();
            int saldo = jugador.getSaldoLaboriosidad() == null ? 0 : jugador.getSaldoLaboriosidad();
            jugador.setPtosLaboriosidad(ptos + 1);
            jugador.setSaldoLaboriosidad(saldo + 1);
            jugadorRepo.save(jugador);
        } catch (Exception ex) {
            // Ignorar si falla al registrar puntos por intento
        }
    }

    /**
     * Marca un ejercicio como completado por un jugador. Si no estaba completado, otorga puntos.
     */
    @Transactional
    public boolean completarEjercicio(Jugador jugador, EjercicioGuia ejercicio) {
        if (jugador == null || ejercicio == null) return false;
        try {
            Optional<ProgresoEjercicio> existente = progresoRepo.findByJugadorAndEjercicio(jugador, ejercicio);
            ProgresoEjercicio p;
            if (existente.isPresent()) {
                p = existente.get();
                if (Boolean.TRUE.equals(p.getCompletada())) {
                    return false; // ya completado previamente
                }
                // marcar completado y mantener contador de intentos
                p.setCompletada(true);
                p.setFechaCompletado(LocalDateTime.now());
            } else {
                p = new ProgresoEjercicio();
                p.setJugador(jugador);
                p.setEjercicio(ejercicio);
                p.setCompletada(true);
                p.setFechaCompletado(LocalDateTime.now());
                p.setIntentos(1);
            }
            progresoRepo.save(p);

            // Otorgar puntos de laboriosidad (puntosRecompensa por ejercicio)
            int puntos = ejercicio.getPuntosRecompensa() == null ? 10 : ejercicio.getPuntosRecompensa();
            int actuales = jugador.getPtosLaboriosidad() == null ? 0 : jugador.getPtosLaboriosidad();
            jugador.setPtosLaboriosidad(actuales + puntos);

            int saldo = jugador.getSaldoLaboriosidad() == null ? 0 : jugador.getSaldoLaboriosidad();
            jugador.setSaldoLaboriosidad(saldo + puntos);

            jugadorRepo.save(jugador);

            return true;
        } catch (Exception ex) {
            // Fallback defensivo dentro del servicio: intentar crear/guardar progreso y puntos
            try {
                ProgresoEjercicio p = new ProgresoEjercicio();
                p.setJugador(jugador);
                p.setEjercicio(ejercicio);
                p.setCompletada(true);
                p.setFechaCompletado(LocalDateTime.now());
                p.setIntentos(p.getIntentos() == null ? 1 : p.getIntentos());
                progresoRepo.save(p);

                int puntos = ejercicio.getPuntosRecompensa() == null ? 10 : ejercicio.getPuntosRecompensa();
                int actuales = jugador.getPtosLaboriosidad() == null ? 0 : jugador.getPtosLaboriosidad();
                jugador.setPtosLaboriosidad(actuales + puntos);
                int saldo = jugador.getSaldoLaboriosidad() == null ? 0 : jugador.getSaldoLaboriosidad();
                jugador.setSaldoLaboriosidad(saldo + puntos);
                jugadorRepo.save(jugador);
                return true;
            } catch (Exception ex2) {
                // Si aún falla, devolver false para que el controlador lo informe
                return false;
            }
        }
    }
}


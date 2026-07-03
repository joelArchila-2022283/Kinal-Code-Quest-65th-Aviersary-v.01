package com.J.M_CODERS.KinalCodeQuest.repository;

import com.J.M_CODERS.KinalCodeQuest.model.entity.ProgresoEjercicio;
import com.J.M_CODERS.KinalCodeQuest.model.entity.Jugador;
import com.J.M_CODERS.KinalCodeQuest.model.entity.EjercicioGuia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProgresoEjercicioRepository extends JpaRepository<ProgresoEjercicio, Integer> {
    Optional<ProgresoEjercicio> findByJugadorAndEjercicio(Jugador jugador, EjercicioGuia ejercicio);
    List<ProgresoEjercicio> findByJugador(Jugador jugador);
}


package com.J.M_CODERS.KinalCodeQuest.repository;

import com.J.M_CODERS.KinalCodeQuest.model.entity.Jugador;
import com.J.M_CODERS.KinalCodeQuest.model.entity.Mision;
import com.J.M_CODERS.KinalCodeQuest.model.entity.ProgresoJugador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProgresoJugadorRepository extends JpaRepository<ProgresoJugador, Integer> {
    Optional<ProgresoJugador> findByJugadorAndMision(Jugador jugador, Mision mision);
    List<ProgresoJugador> findByJugador(Jugador jugador);
}

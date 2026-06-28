package com.J.M_CODERS.KinalCodeQuest.repository;

import com.J.M_CODERS.KinalCodeQuest.model.entity.CompraHistoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompraHistoriaRepository extends JpaRepository<CompraHistoria, Integer> {

    // Devuelve la lista de todas las compras de un jugador específico
    List<CompraHistoria> findByJugadorIdJugador(Integer idJugador);

    // Verifica de forma lógica si un jugador ya compró un capítulo específico
    boolean existsByJugadorIdJugadorAndCapituloHistoriaIdCapitulo(Integer idJugador, Integer idCapitulo);
}
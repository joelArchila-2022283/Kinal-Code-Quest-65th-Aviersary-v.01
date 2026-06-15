package com.J.M_CODERS.KinalCodeQuest.repository;

import com.J.M_CODERS.KinalCodeQuest.model.entity.Jugador;
import com.J.M_CODERS.KinalCodeQuest.model.entity.Mision;
import com.J.M_CODERS.KinalCodeQuest.model.entity.ProgresoJugador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProgresoJugadorRepository extends JpaRepository<ProgresoJugador, Integer> {
    Optional<ProgresoJugador> findByJugadorAndMision(Jugador jugador, Mision mision);
    List<ProgresoJugador> findByJugador(Jugador jugador);

    //    MÉTRICAS DE TELEMETRÍA Y ANALÍTICA

    // 1. Cuenta la cantidad total de errores lógicos / fallas de compilación
    @Query("SELECT COALESCE(SUM(p.intentos), 0) - COUNT(CASE WHEN p.completada = true THEN 1 END) FROM ProgresoJugador p WHERE p.jugador.idJugador = :idJugador")
    long countErroresByJugador(@Param("idJugador") Integer idJugador);

    // 2. Suma el total general de ejecuciones en el Javac virtual
    @Query("SELECT COALESCE(SUM(p.intentos), 0) FROM ProgresoJugador p WHERE p.jugador.idJugador = :idJugador")
    long sumTotalIntentosByJugador(@Param("idJugador") Integer idJugador);

    // 3. Devuelve los registros de misiones ordenados de MAYOR a MENOR número de intentos
    @Query("SELECT p FROM ProgresoJugador p WHERE p.jugador.idJugador = :idJugador AND p.intentos > 0 ORDER BY p.intentos DESC")
    List<ProgresoJugador> findHistorialDificultadDesc(@Param("idJugador") Integer idJugador);
}
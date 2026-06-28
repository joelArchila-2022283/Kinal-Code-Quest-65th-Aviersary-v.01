package com.J.M_CODERS.KinalCodeQuest.service.evaluator;

import com.J.M_CODERS.KinalCodeQuest.model.entity.CapituloHistoria;
import com.J.M_CODERS.KinalCodeQuest.model.entity.Jugador;
import java.util.List;

public interface HistoriaService {

    // Obtiene todos los capítulos procesando dinámicamente si están desbloqueados o comprados
    List<CapituloHistoria> obtenerHistoriaParaJugador(Jugador jugador);

    // Procesa la compra de un archivo secundario gastando el saldo correspondiente
    boolean comprarCapituloSecundario(Integer idJugador, Integer idCapitulo);
}
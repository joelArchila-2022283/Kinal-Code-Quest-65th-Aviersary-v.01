package com.J.M_CODERS.KinalCodeQuest.service.impl;

import com.J.M_CODERS.KinalCodeQuest.model.entity.CapituloHistoria;
import com.J.M_CODERS.KinalCodeQuest.model.entity.CompraHistoria;
import com.J.M_CODERS.KinalCodeQuest.model.entity.Jugador;
import com.J.M_CODERS.KinalCodeQuest.repository.CapituloHistoriaRepository;
import com.J.M_CODERS.KinalCodeQuest.repository.CompraHistoriaRepository;
import com.J.M_CODERS.KinalCodeQuest.repository.ProgresoJugadorRepository; // Si usas el de jugador para actualizar
import com.J.M_CODERS.KinalCodeQuest.repository.JugadorRepository;
import com.J.M_CODERS.KinalCodeQuest.service.evaluator.HistoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class HistoriaServiceImplements implements HistoriaService {

    @Autowired
    private CapituloHistoriaRepository capituloRepository;

    @Autowired
    private CompraHistoriaRepository compraRepository;

    @Autowired
    private JugadorRepository jugadorRepository; // O el service de jugador que uses para persistir

    @Override
    public List<CapituloHistoria> obtenerHistoriaParaJugador(Jugador jugador) {
        List<CapituloHistoria> todosLosCapitulos = capituloRepository.findAll();
        int expActual = (jugador.getExperiencia() != null) ? jugador.getExperiencia() : 0;

        for (CapituloHistoria cap : todosLosCapitulos) {
            if ("PRINCIPAL".equals(cap.getTipoContenido())) {
                // La historia principal se desbloquea comparando con el umbral de EXP (costo_cantidad)
                cap.setDesbloqueado(expActual >= cap.getCostoCantidad());
            } else {
                // Las secundarias (fotos/valores) están desbloqueadas si ya existe un registro de compra permanente
                boolean yaComprado = compraRepository.existsByJugadorIdJugadorAndCapituloHistoriaIdCapitulo(
                        jugador.getIdJugador(), cap.getIdCapitulo());
                cap.setDesbloqueado(yaComprado);
            }
        }
        return todosLosCapitulos;
    }

    @Override
    @Transactional // Asegura que si algo falla, no se queden los puntos a medias o mal cobrados
    public boolean comprarCapituloSecundario(Integer idJugador, Integer idCapitulo) {
        Jugador jugador = jugadorRepository.findById(idJugador).orElse(null);
        CapituloHistoria capitulo = capituloRepository.findById(idCapitulo).orElse(null);

        if (jugador == null || capitulo == null) {
            return false;
        }

        // 1. Validar que no haya sido comprado antes
        boolean yaComprado = compraRepository.existsByJugadorIdJugadorAndCapituloHistoriaIdCapitulo(idJugador, idCapitulo);
        if (yaComprado) {
            return false;
        }

        // 2. Validar que el nodo padre (historia principal) ya esté desbloqueado por su EXP antes de comprar el hijo
        if (capitulo.getIdCapituloPadre() != null) {
            CapituloHistoria padre = capituloRepository.findById(capitulo.getIdCapituloPadre()).orElse(null);
            int expActual = (jugador.getExperiencia() != null) ? jugador.getExperiencia() : 0;
            if (padre != null && expActual < padre.getCostoCantidad()) {
                return false; // El capítulo principal asociado está bloqueado
            }
        }

        // 3. Evaluar fondos de la cartera según el tipo de moneda solicitado
        int costo = capitulo.getCostoCantidad();
        boolean fondosSuficientes = false;

        switch (capitulo.getTipoMoneda().toUpperCase()) {
            case "LABORIOSIDAD":
                if (jugador.getSaldoLaboriosidad() >= costo) {
                    jugador.setSaldoLaboriosidad(jugador.getSaldoLaboriosidad() - costo);
                    fondosSuficientes = true;
                }
                break;
            case "RESPONSABILIDAD":
                if (jugador.getSaldoResponsabilidad() >= costo) {
                    jugador.setSaldoResponsabilidad(jugador.getSaldoResponsabilidad() - costo);
                    fondosSuficientes = true;
                }
                break;
            case "SOLIDARIDAD":
                if (jugador.getSaldoSolidaridad() >= costo) {
                    jugador.setSaldoSolidaridad(jugador.getSaldoSolidaridad() - costo);
                    fondosSuficientes = true;
                }
                break;
        }

        if (!fondosSuficientes) {
            return false; // Saldo insuficiente en la billetera virtual
        }

        // 4. Guardar los cambios del saldo reducido del jugador
        jugadorRepository.save(jugador);

        // 5. Registrar el "recibo" de compra para desbloquear de forma permanente
        CompraHistoria compra = new CompraHistoria();
        compra.setJugador(jugador);
        compra.setCapituloHistoria(capitulo);
        compraRepository.save(compra);

        return true;
    }
}
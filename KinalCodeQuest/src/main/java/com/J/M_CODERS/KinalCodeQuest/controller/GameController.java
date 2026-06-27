package com.J.M_CODERS.KinalCodeQuest.controller;

import com.J.M_CODERS.KinalCodeQuest.model.entity.*;
import com.J.M_CODERS.KinalCodeQuest.repository.ProgresoJugadorRepository;
import com.J.M_CODERS.KinalCodeQuest.service.evaluator.AreaTecnicaService;
import com.J.M_CODERS.KinalCodeQuest.service.evaluator.HistoriaService;
import com.J.M_CODERS.KinalCodeQuest.service.evaluator.MisionService;
import com.J.M_CODERS.KinalCodeQuest.service.evaluator.JugadorService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/game")
public class GameController {

    @Autowired
    private AreaTecnicaService areaService;

    @Autowired
    private MisionService misionesService;

    @Autowired
    private JugadorService jugadorService;

    @Autowired
    private ProgresoJugadorRepository progresoRepository;

    @Autowired
    private HistoriaService historiaService;

    // Banco de preguntas en memoria
    private final List<PreguntaTrivia> bancoPreguntas = Arrays.asList(
            new PreguntaTrivia(1, 1, "¿Cuál de los siguientes es un tipo de dato primitivo en Java?",
                    Arrays.asList("String", "int", "Integer", "Scanner"), 1, "Los tipos primitivos como 'int' almacenan valores directamente en Stack."),
            new PreguntaTrivia(2, 1, "¿Cómo se declara una constante inmutable en Java?",
                    Arrays.asList("const int X = 10;", "final int X = 10;", "static int X = 10;", "immutable int X = 10;"), 1, "La palabra clave 'final' define una constante."),
            new PreguntaTrivia(3, 2, "¿Qué estructura garantiza que el código se ejecute al menos una vez?",
                    Arrays.asList("for", "while", "do-while", "if-else"), 2, "El ciclo 'do-while' evalúa la condición al final."),
            new PreguntaTrivia(4, 3, "¿Qué colección usarías para almacenar una lista dinámica?",
                    Arrays.asList("Array[]", "ArrayList<String>", "int[]", "ListMap"), 1, "ArrayList permite añadir/eliminar elementos dinámicamente.")
    );

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Jugador jugadorSesion = (Jugador) session.getAttribute("usuarioLogueado");
        if (jugadorSesion == null) return "redirect:/auth/login";

        Jugador jugadorActualizado = jugadorService.buscarPorId(jugadorSesion.getIdJugador());
        session.setAttribute("usuarioLogueado", jugadorActualizado);

        model.addAttribute("jugador", jugadorActualizado);
        model.addAttribute("areas", areaService.listarTodas());
        model.addAttribute("erroresTotales", progresoRepository.countErroresByJugador(jugadorActualizado.getIdJugador()));

        return "game/dashboard";
    }

    @GetMapping("/historia")
    public String verHistoria(HttpSession session, Model model) {
        Jugador jugadorSesion = (Jugador) session.getAttribute("usuarioLogueado");
        if (jugadorSesion == null) return "redirect:/auth/login";

        Jugador jugador = jugadorService.buscarPorId(jugadorSesion.getIdJugador());
        List<CapituloHistoria> capitulos = historiaService.obtenerHistoriaParaJugador(jugador);

        model.addAttribute("jugador", jugador);
        model.addAttribute("capitulos", capitulos);
        return "game/historia";
    }

    @PostMapping("/historia/comprar/{idCapitulo}")
    public String comprarArchivo(@PathVariable Integer idCapitulo, HttpSession session, RedirectAttributes redirectAttributes) {
        Jugador jugadorSesion = (Jugador) session.getAttribute("usuarioLogueado");
        if (jugadorSesion == null) return "redirect:/auth/login";

        boolean compraExitosa = historiaService.comprarCapituloSecundario(jugadorSesion.getIdJugador(), idCapitulo);

        if (compraExitosa) {
            redirectAttributes.addFlashAttribute("mensajeExito", "[NÚCLEO]: Archivo descifrado con éxito.");
        } else {
            redirectAttributes.addFlashAttribute("mensajeError", "[ERROR]: Fondos insuficientes o nodo principal bloqueado.");
        }
        return "redirect:/game/historia";
    }

    @GetMapping("/play/trivia/{idArea}")
    public String lanzarTrivia(@PathVariable Integer idArea, HttpSession session, Model model) {
        if (session.getAttribute("usuarioLogueado") == null) return "redirect:/auth/login";

        model.addAttribute("area", areaService.buscarPorId(idArea));
        model.addAttribute("preguntas", bancoPreguntas.stream().filter(p -> p.getIdAreaAsociada().equals(idArea)).collect(Collectors.toList()));
        return "game/trivia";
    }

    @PostMapping("/play/trivia/evaluar")
    public String evaluarTrivia(@RequestParam Map<String, String> params, HttpSession session, Model model) {
        Jugador jugadorSesion = (Jugador) session.getAttribute("usuarioLogueado");
        if (jugadorSesion == null) return "redirect:/auth/login";

        Jugador jugador = jugadorService.buscarPorId(jugadorSesion.getIdJugador());
        Integer idArea = Integer.parseInt(params.get("idArea"));

        // Filtrar preguntas del área
        List<PreguntaTrivia> preguntasNivel = bancoPreguntas.stream()
                .filter(p -> p.getIdAreaAsociada().equals(idArea))
                .collect(Collectors.toList());

        int correctas = 0;
        for (PreguntaTrivia pregunta : preguntasNivel) {
            String respuestaEnviada = params.get("pregunta_" + pregunta.getIdPregunta());
            if (respuestaEnviada != null && Integer.parseInt(respuestaEnviada) == pregunta.getRespuestaCorrectaIndex()) {
                correctas++;
            }
        }

        boolean aprobado = (correctas == preguntasNivel.size());

        if (aprobado) {
            // 1. Otorgar Experiencia
            jugador.setExperiencia((jugador.getExperiencia() != null ? jugador.getExperiencia() : 0) + 50);

            // 2. Otorgar Saldo según Área
            switch (idArea) {
                case 1: jugador.setSaldoResponsabilidad((jugador.getSaldoResponsabilidad() == null ? 0 : jugador.getSaldoResponsabilidad()) + 20); break;
                case 2: jugador.setSaldoSolidaridad((jugador.getSaldoSolidaridad() == null ? 0 : jugador.getSaldoSolidaridad()) + 20); break;
                case 3: jugador.setSaldoLaboriosidad((jugador.getSaldoLaboriosidad() == null ? 0 : jugador.getSaldoLaboriosidad()) + 20); break;
            }

            jugadorService.guardar(jugador);
            session.setAttribute("usuarioLogueado", jugador);
        }

        model.addAttribute("jugador", jugador);
        model.addAttribute("aprobado", aprobado);
        model.addAttribute("expGanada", aprobado ? 50 : 0);
        return "game/resultado_trivia";
    }

    @GetMapping("/play")
    public String iniciarJuego(HttpSession session, Model model) {
        Jugador jugadorSesion = (Jugador) session.getAttribute("usuarioLogueado");
        if (jugadorSesion == null) return "redirect:/auth/login";
        model.addAttribute("jugador", jugadorService.buscarPorId(jugadorSesion.getIdJugador()));
        model.addAttribute("areas", areaService.listarTodas());
        return "game/play";
    }
}
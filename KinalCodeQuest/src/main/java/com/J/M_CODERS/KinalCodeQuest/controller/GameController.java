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

/* Controlador central encargado de la navegación principal y los módulos del juego */
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

    // Banco de preguntas en memoria de Java (Niveles del 1 al 4)
    private final List<PreguntaTrivia> bancoPreguntas = Arrays.asList(
            new PreguntaTrivia(1, 1, "¿Cuál de los siguientes es un tipo de dato primitivo en Java?",
                    Arrays.asList("String", "int", "Integer", "Scanner"), 1, "Los tipos primitivos como 'int' almacenan valores directamente en Stack."),
            new PreguntaTrivia(2, 1, "¿Cómo se declara una constante inmutable en Java?",
                    Arrays.asList("const int X = 10;", "final int X = 10;", "static int X = 10;", "immutable int X = 10;"), 1, "La palabra clave 'final' define una constante."),
            new PreguntaTrivia(3, 2, "¿Qué estructura garantiza que el código se ejecute al menos una vez?",
                    Arrays.asList("for", "while", "do-while", "if-else"), 2, "El ciclo 'do-while' evalúa la condición al final."),
            new PreguntaTrivia(4, 2, "¿Cuál es el resultado de un ciclo 'for (int i = 0; i < 3; i++)' si imprimimos el valor de 'i' consecutivamente?",
                    Arrays.asList("0 1 2 3", "1 2 3", "0 1 2", "0 0 0"), 2, "El ciclo se rompe de forma inmediata en el momento en que 'i' incrementa a 3."),
            new PreguntaTrivia(5, 3, "¿Qué colección usarías para almacenar una lista dinámica?",
                    Arrays.asList("Array[]", "ArrayList<String>", "int[]", "ListMap"), 1, "ArrayList permite añadir/eliminar elementos dinámicamente."),
            new PreguntaTrivia(6, 3, "¿Qué estructura de datos usarías para buscar una herramienta por su 'código de activo' de forma rápida (complejidad O(1))?",
                    Arrays.asList("ArrayList", "LinkedList", "HashMap", "Stack"), 2, "HashMap utiliza llaves hashing para acceder a los valores instantáneamente."),
            new PreguntaTrivia(7, 4, "¿Cuál es la firma correcta para un método que recibe un voltaje (double) y retorna true si es menor a 220.0v?",
                    Arrays.asList("void validar(double v)", "boolean validar(double v)", "int validar(double v)", "main(double v)"), 1, "Un método encargado de validar condiciones lógicas debe especificar el tipo de retorno 'boolean'.")
    );

    /* Renderiza el panel de control del usuario con sus estadísticas y accesos directos */
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Jugador jugadorSesion = (Jugador) session.getAttribute("usuarioLogueado");
        if (jugadorSesion == null) return "redirect:/auth/login";

        Jugador jugadorActualizado = jugadorService.buscarPorId(jugadorSesion.getIdJugador());
        session.setAttribute("usuarioLogueado", jugadorActualizado);

        // Cómputo de analíticas avanzadas restaurado
        long erroresTotales = progresoRepository.countErroresByJugador(jugadorActualizado.getIdJugador());
        long intentosTotales = progresoRepository.sumTotalIntentosByJugador(jugadorActualizado.getIdJugador());
        List<ProgresoJugador> misionesDificiles = progresoRepository.findHistorialDificultadDesc(jugadorActualizado.getIdJugador());

        if (erroresTotales < 0) erroresTotales = 0;

        model.addAttribute("jugador", jugadorActualizado);
        model.addAttribute("areas", areaService.listarTodas());
        model.addAttribute("erroresTotales", erroresTotales);
        model.addAttribute("intentosTotales", intentosTotales);
        model.addAttribute("misionesDificiles", misionesDificiles);

        return "game/dashboard";
    }

    /* Apartado progresivo de la historia que lee de la DB y evalúa la EXP y compras */
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

    /* Maneja la compra de un archivo secreto usando las divisas institucionales */
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

    /* MÓDULO INICIAR JUEGO: Selector de Niveles */
    @GetMapping("/play")
    public String iniciarJuego(HttpSession session, Model model) {
        Jugador jugadorSesion = (Jugador) session.getAttribute("usuarioLogueado");
        if (jugadorSesion == null) return "redirect:/auth/login";

        model.addAttribute("jugador", jugadorService.buscarPorId(jugadorSesion.getIdJugador()));
        model.addAttribute("areas", areaService.listarTodas());
        return "game/play";
    }

    /* Carga el cuestionario interactivo del Nivel/Área Técnica seleccionada */
    @GetMapping("/play/trivia/{idArea}")
    public String lanzarTrivia(@PathVariable Integer idArea, HttpSession session, Model model) {
        if (session.getAttribute("usuarioLogueado") == null) return "redirect:/auth/login";

        AreaTecnica area = areaService.buscarPorId(idArea);
        List<PreguntaTrivia> preguntasNivel = bancoPreguntas.stream()
                .filter(p -> p.getIdAreaAsociada().equals(idArea))
                .collect(Collectors.toList());

        if (preguntasNivel.isEmpty()) {
            return "redirect:/game/play?error=no_questions";
        }

        model.addAttribute("area", area);
        model.addAttribute("preguntas", preguntasNivel);
        return "game/trivia";
    }

    /* Evalúa el examen de la trivia y premia al usuario con EXP y saldos */
    @PostMapping("/play/trivia/evaluar")
    public String evaluarTrivia(@RequestParam Map<String, String> params, HttpSession session, Model model) {
        Jugador jugadorSesion = (Jugador) session.getAttribute("usuarioLogueado");
        if (jugadorSesion == null) return "redirect:/auth/login";

        Jugador jugador = jugadorService.buscarPorId(jugadorSesion.getIdJugador());
        Integer idArea = Integer.parseInt(params.get("idArea"));

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
        int expGanada = aprobado ? 50 : 0;

        if (aprobado) {
            jugador.setExperiencia((jugador.getExperiencia() != null ? jugador.getExperiencia() : 0) + expGanada);

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
        model.addAttribute("expGanada", expGanada);
        model.addAttribute("correctas", correctas);
        model.addAttribute("total", preguntasNivel.size());
        return "game/resultado_trivia";
    }

    /* Despliega la bitácora completa de misiones generales disponibles */
    @GetMapping("/misiones")
    public String misionesGenerales(HttpSession session, Model model) {
        Jugador jugador = (Jugador) session.getAttribute("usuarioLogueado");
        if (jugador == null) return "redirect:/auth/login";

        model.addAttribute("jugador", jugador);
        model.addAttribute("misiones", misionesService.listarTodas());
        return "game/misiones";
    }

    /* Filtra y expone los retos de programación correspondientes a un área en específico */
    @GetMapping("/area/{idArea}")
    public String verMisionesArea(@PathVariable Integer idArea, HttpSession session, Model model) {
        if (session.getAttribute("usuarioLogueado") == null) return "redirect:/auth/login";

        model.addAttribute("area", areaService.buscarPorId(idArea));
        model.addAttribute("misiones", misionesService.listarPorArea(idArea));
        return "game/misiones";
    }
}
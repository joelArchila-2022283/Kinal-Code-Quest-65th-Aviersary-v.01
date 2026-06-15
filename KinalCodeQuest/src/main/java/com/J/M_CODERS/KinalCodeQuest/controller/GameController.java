package com.J.M_CODERS.KinalCodeQuest.controller;

import com.J.M_CODERS.KinalCodeQuest.model.entity.*;
import com.J.M_CODERS.KinalCodeQuest.repository.ProgresoJugadorRepository;
import com.J.M_CODERS.KinalCodeQuest.service.evaluator.AreaTecnicaService;
import com.J.M_CODERS.KinalCodeQuest.service.evaluator.MisionService;
import com.J.M_CODERS.KinalCodeQuest.service.evaluator.JugadorService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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

    /* Renderiza el panel de control del usuario con sus estadísticas y accesos directos */
    @Autowired
    private ProgresoJugadorRepository progresoRepository;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Jugador jugadorSesion = (Jugador) session.getAttribute("usuarioLogueado");
        if (jugadorSesion == null) return "redirect:/auth/login";

        Jugador jugadorActualizado = jugadorService.buscarPorId(jugadorSesion.getIdJugador());
        session.setAttribute("usuarioLogueado", jugadorActualizado);

        // Cómputo de analíticas avanzadas
        long erroresTotales = progresoRepository.countErroresByJugador(jugadorActualizado.getIdJugador());
        long intentosTotales = progresoRepository.sumTotalIntentosByJugador(jugadorActualizado.getIdJugador());
        List<ProgresoJugador> misionesDificiles = progresoRepository.findHistorialDificultadDesc(jugadorActualizado.getIdJugador());

        // Asegurar que erroresTotales nunca sea negativo
        if (erroresTotales < 0) erroresTotales = 0;

        model.addAttribute("jugador", jugadorActualizado);
        model.addAttribute("areas", areaService.listarTodas());

        // Atributos de telemetría enviados al HTML
        model.addAttribute("erroresTotales", erroresTotales);
        model.addAttribute("intentosTotales", intentosTotales);
        model.addAttribute("misionesDificiles", misionesDificiles);

        return "game/dashboard";
    }

    /* Apartado progresivo de la historia que evalúa la EXP del jugador para desbloquear capítulos */
    @GetMapping("/historia")
    public String verHistoria(HttpSession session, Model model) {
        Jugador jugadorSesion = (Jugador) session.getAttribute("usuarioLogueado");
        if (jugadorSesion == null) return "redirect:/auth/login";

        Jugador jugador = jugadorService.buscarPorId(jugadorSesion.getIdJugador());
        int expActual = (jugador.getExperiencia() != null) ? jugador.getExperiencia() : 0;

        List<CapituloHistoria> capitulos = new ArrayList<>();

        // CAPÍTULO 1: El Origen
        CapituloHistoria cap1 = new CapituloHistoria();
        cap1.setIdCapitulo(1);
        cap1.setTitulo("El Cimiento del Núcleo (1961)");
        cap1.setExpRequerida(0);
        cap1.setContenidoNarrativo("El Centro Educativo Técnico Laboral Kinal nació en 1961 en Guatemala, gracias a la iniciativa de un grupo de profesionales e ingenieros motivados por las enseñanzas de San Josemaría Escrivá de Balaguer. Su meta fundamental era brindar oportunidades de superación técnica y humana a jóvenes y adultos de escasos recursos. Iniciando en instalaciones humildes en la zona 12, Kinal revolucionó la educación técnica en el país promoviendo que el trabajo diario es un medio para alcanzar la excelencia humana y la santificación.");
        cap1.setDesbloqueado(expActual >= cap1.getExpRequerida());
        capitulos.add(cap1);

        // CAPÍTULO 2: La Primera Expansión
        CapituloHistoria cap2 = new CapituloHistoria();
        cap2.setIdCapitulo(2);
        cap2.setTitulo("Migración de Datos y Expansión (1970-1980)");
        cap2.setExpRequerida(100);
        cap2.setContenidoNarrativo("A medida que la demanda de técnicos calificados crecía en Guatemala, la sede inicial de la zona 12 empezó a quedarse pequeña. Durante las décadas de los 70 y 80, la Fundación expandió sus programas nocturnos y de formación acelerada para adultos trabajadores. Fue en esta era donde se consolidaron los pilares formativos tradicionales, demostrando que la disciplina técnica y la ética profesional podían transformar radicalmente el panorama industrial y laboral de las familias guatemaltecas.");
        cap2.setDesbloqueado(expActual >= cap2.getExpRequerida());
        capitulos.add(cap2);

        // CAPÍTULO 3: La Nueva Sede
        CapituloHistoria cap3 = new CapituloHistoria();
        cap3.setIdCapitulo(3);
        cap3.setTitulo("El Gran Servidor: Sede Zona 7 (1990)");
        cap3.setExpRequerida(250);
        cap3.setContenidoNarrativo("Un hito trascendental ocurrió en la década de los 90 con el traslado definitivo de la institución a sus amplias e innovadoras instalaciones actuales en la Zona 7 de la Ciudad de Guatemala. Diseñado específicamente para albergar talleres mecánicos, eléctricos y laboratorios industriales, este nuevo complejo arquitectónico permitió a Kinal cuadriplicar su capacidad operativa y dar vida al plan de Perito Técnico para jóvenes de nivel diversificado.");
        cap3.setDesbloqueado(expActual >= cap3.getExpRequerida());
        capitulos.add(cap3);

        // CAPÍTULO 4: El Desembarco Digital
        CapituloHistoria cap4 = new CapituloHistoria();
        cap4.setIdCapitulo(4);
        cap4.setTitulo("Inyección del Compilador: Perito en Informática");
        cap4.setExpRequerida(450);
        cap4.setContenidoNarrativo("Con la llegada del nuevo milenio y la inminente automatización global, Kinal integró a su matriz de estudio la carrera de Perito en Informática. Los antiguos talleres de herramientas manuales abrieron paso a servidores, redes estructuradas, bases de datos y desarrollo de software lógico. Los estudiantes dejaron de ser solo operarios de maquinaria para transformarse en arquitectos digitales, capaces de escribir código estructurado bajo rigurosos estándares de calidad internacional.");
        cap4.setDesbloqueado(expActual >= cap4.getExpRequerida());
        capitulos.add(cap4);

        // CAPÍTULO 5: El Legado Vivo
        CapituloHistoria cap5 = new CapituloHistoria();
        cap5.setIdCapitulo(5);
        cap5.setTitulo("Kinal en la Red Global: El Legado Vivo");
        cap5.setExpRequerida(700);
        cap5.setContenidoNarrativo("Hoy en día, con más de seis décadas de trayectoria ininterrumpida, el Centro Técnico Laboral Kinal sigue transformando vidas. Su filosofía operativa original no ha cambiado: formar profesionales con alta capacidad técnica pero, sobre todo, con sólidos valores de Laboriosidad, Responsabilidad y Solidaridad. Cada línea de código que compilas en esta terminal rinde homenaje a los miles de egresados que mueven y desarrollan la infraestructura tecnológica de Guatemala.");
        cap5.setDesbloqueado(expActual >= cap5.getExpRequerida());
        capitulos.add(cap5);

        model.addAttribute("jugador", jugador);
        model.addAttribute("capitulos", capitulos);

        return "game/historia";
    }


    /* Redirige a la interfaz base del motor interactivo clásico */
    @GetMapping("/game")
    public String gameCore(HttpSession session, Model model) {
        Jugador jugadorSesion = (Jugador) session.getAttribute("usuarioLogueado");
        if (jugadorSesion == null) return "redirect:/auth/login";

        Jugador jugadorActualizado = jugadorService.buscarPorId(jugadorSesion.getIdJugador());
        session.setAttribute("usuarioLogueado", jugadorActualizado);

        model.addAttribute("jugador", jugadorActualizado);
        model.addAttribute("areas", areaService.listarTodas());

        return "game/game";
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
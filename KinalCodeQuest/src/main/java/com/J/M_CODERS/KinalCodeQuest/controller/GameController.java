package com.J.M_CODERS.KinalCodeQuest.controller;

import com.J.M_CODERS.KinalCodeQuest.model.entity.*;
import com.J.M_CODERS.KinalCodeQuest.service.evaluator.AreaTecnicaService;
import com.J.M_CODERS.KinalCodeQuest.service.evaluator.MisionService;
import com.J.M_CODERS.KinalCodeQuest.service.evaluator.ProgresoJugadorService;
import com.J.M_CODERS.KinalCodeQuest.service.evaluator.JugadorService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/game")
public class GameController {

    @Autowired
    private AreaTecnicaService areaService;

    @Autowired
    private MisionService misionesService;

    @Autowired
    private ProgresoJugadorService progresoService;

    @Autowired
    private JugadorService jugadorService;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Jugador jugadorSesion = (Jugador) session.getAttribute("usuarioLogueado");
        if (jugadorSesion == null) return "redirect:/auth/login";

        Jugador jugadorActualizado = jugadorService.buscarPorId(jugadorSesion.getIdJugador());
        session.setAttribute("usuarioLogueado", jugadorActualizado);

        model.addAttribute("jugador", jugadorActualizado);
        model.addAttribute("areas", areaService.listarTodas());

        return "game/dashboard";
    }

    @GetMapping("/area/{idArea}")
    public String verMisionesArea(@PathVariable Integer idArea, HttpSession session, Model model) {
        if (session.getAttribute("usuarioLogueado") == null) return "redirect:/auth/login";

        model.addAttribute("area", areaService.buscarPorId(idArea));
        model.addAttribute("misiones", misionesService.listarPorArea(idArea));

        return "game/misiones";
    }

    @GetMapping("/mision/{idMision}")
    public String interactuarMision(@PathVariable Integer idMision, HttpSession session, Model model) {
        Jugador jugador = (Jugador) session.getAttribute("usuarioLogueado");
        if (jugador == null) return "redirect:/auth/login";

        Mision mision = misionesService.buscarPorId(idMision);
        if (mision == null) return "redirect:/game/dashboard";

        ProgresoJugador progreso = progresoService.obtenerORegistrarProgreso(jugador, mision);

        model.addAttribute("mision", mision);
        model.addAttribute("progreso", progreso);

        return "game/consola";
    }

    @PostMapping("/mision/{idMision}/compilar")
    public String evaluarCodigo(@PathVariable Integer idMision, @RequestParam String codigoEnviado, HttpSession session, Model model) {
        Jugador jugador = (Jugador) session.getAttribute("usuarioLogueado");
        if (jugador == null) return "redirect:/auth/login";

        Mision mision = misionesService.buscarPorId(idMision);
        ProgresoJugador progreso = progresoService.obtenerORegistrarProgreso(jugador, mision);

        String regexCriterio = mision.getCriterioEvaluacion();
        boolean esCorrecto = false;

        try {
            String codigoNormalizado = codigoEnviado.replaceAll("\\s+", " ");
            esCorrecto = Pattern.compile(regexCriterio, Pattern.CASE_INSENSITIVE).matcher(codigoNormalizado).matches();
        } catch (Exception e) {
            model.addAttribute("compilacionError", "[Kinal Compiler Error]: Error de parsing crítico en la sintaxis de validación.");
        }

        progreso = progresoService.registrarIntento(progreso, codigoEnviado, esCorrecto);

        model.addAttribute("mision", mision);
        model.addAttribute("progreso", progreso);

        if (esCorrecto) {
            model.addAttribute("compilacionSuccess", "[Kinal Compiler v01.0.1] - Compilación Exitosa. Flujo lógico aprobado.");
        } else {
            model.addAttribute("compilacionError", "[Kinal Compiler v01.0.1] - Error de Sintaxis: La estructura lógica no cumple con los requerimientos técnicos.");
        }

        return "game/consola";
    }
}
package com.J.M_CODERS.KinalCodeQuest.controller;

import com.J.M_CODERS.KinalCodeQuest.model.entity.*;
import com.J.M_CODERS.KinalCodeQuest.service.evaluator.MisionService;
import com.J.M_CODERS.KinalCodeQuest.service.evaluator.ProgresoJugadorService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* Este controller se encarga exclusivamente de renderizar la consola */
@Controller
@RequestMapping("/game")
public class ConsolaController {

    @Autowired
    private MisionService misionesService;

    @Autowired
    private ProgresoJugadorService progresoService;

    /* Abre la consola directamente usando la primera misión disponible o una simulación segura */
    @GetMapping("/consola")
    public String abrirConsolaDirecta(HttpSession session, Model model) {
        Jugador jugador = (Jugador) session.getAttribute("usuarioLogueado");
        if (jugador == null) {
            return "redirect:/auth/login";
        }

        Mision m = null;
        try {
            java.util.List<Mision> lista = misionesService.listarTodas();
            if (lista != null && !lista.isEmpty()) {
                m = lista.get(0);
            }
        } catch (Exception e) {
            // Ignorar fallos de consulta
        }

        if (m == null) {
            m = new Mision();
            m.setIdMision(1);
            m.setTitulo("CONSOLA_DE_DESAFIO_STANDALONE");
            m.setExpRecompensa(0);
            m.setCriterioEvaluacion(".*");
        }

        ProgresoJugador progreso = null;
        try {
            progreso = progresoService.obtenerORegistrarProgreso(jugador, m);
        } catch (Exception e) {
            progreso = new ProgresoJugador();
            progreso.setIntentos(0);
        }

        model.addAttribute("mision", m);
        model.addAttribute("progreso", progreso);
        model.addAttribute("jugador", jugador);

        return "game/consola";
    }

    /* Carga la interfaz de la consola de compilación para una misión específica */
    @GetMapping("/mision/{idMision}")
    public String interactuarMision(@PathVariable Integer idMision, HttpSession session, Model model) {
        Jugador jugador = (Jugador) session.getAttribute("usuarioLogueado");
        if (jugador == null) {
            return "redirect:/auth/login";
        }

        Mision mision = misionesService.buscarPorId(idMision);
        if (mision == null) {
            mision = new Mision();
            mision.setIdMision(idMision);
            mision.setTitulo("MISIÓN_SISTEMA_" + idMision);
            mision.setExpRecompensa(0);
            mision.setCriterioEvaluacion(".*");
        }

        ProgresoJugador progreso = null;
        try {
            progreso = progresoService.obtenerORegistrarProgreso(jugador, mision);
        } catch (Exception e) {
            progreso = new ProgresoJugador();
            progreso.setIntentos(0);
        }

        model.addAttribute("mision", mision);
        model.addAttribute("progreso", progreso);
        model.addAttribute("jugador", jugador);

        return "game/consola";
    }

    /* Endpoint asíncrono (AJAX) que procesa y valida el código fuente enviado desde CodeMirror */
    @PostMapping("/mision/{idMision}/compilar")
    @ResponseBody
    public ResponseEntity<?> evaluarCodigo(@PathVariable Integer idMision,
                                           @RequestParam String codigoEnviado,
                                           HttpSession session) {
        Jugador jugador = (Jugador) session.getAttribute("usuarioLogueado");
        if (jugador == null) {
            return ResponseEntity.status(401).body("Sesión inválida.");
        }

        Mision mision = misionesService.buscarPorId(idMision);
        if (mision == null) {
            mision = new Mision();
            mision.setIdMision(idMision);
            mision.setCriterioEvaluacion(".*");
        }

        ProgresoJugador progreso = null;
        try {
            progreso = progresoService.obtenerORegistrarProgreso(jugador, mision);
        } catch (Exception e) {
            progreso = new ProgresoJugador();
            progreso.setIntentos(1);
        }

        String regexCriterio = mision.getCriterioEvaluacion();
        boolean esCorrecto = false;
        StringBuilder consolaOutput = new StringBuilder();

        consolaOutput.append("C:\\KinalCodeQuest\\compiler> javac KinalCodeQuestApplication.java\n");

        try {
            String codigoNormalizado = codigoEnviado.replaceAll("\\s+", " ");
            esCorrecto = Pattern.compile(regexCriterio, Pattern.CASE_INSENSITIVE).matcher(codigoNormalizado).matches();

            consolaOutput.append("C:\\KinalCodeQuest\\compiler> java com.J.M_CODERS.KinalCodeQuest.KinalCodeQuestApplication\n");
            consolaOutput.append("[INFO] Java Virtual Machine inicializada de manera exitosa.\n");
            consolaOutput.append("--------------------------------------------------\n");

            if (esCorrecto) {
                // CAPTURA DINÁMICA: Busca todos los System.out.println("...") del código enviado y extrae su contenido para mostrarlo en la terminal
                Pattern p = Pattern.compile("System\\.out\\.println\\s*\\(\\s*\"([^\"]*)\"\\s*\\)\\s*;");
                Matcher m = p.matcher(codigoEnviado);
                boolean encontroImpresiones = false;

                while (m.find()) {
                    consolaOutput.append(m.group(1)).append("\n");
                    encontroImpresiones = true;
                }

                if (!encontroImpresiones) {
                    consolaOutput.append("[SUCCESS] Código ejecutado (Sin salidas de texto impresas).\n");
                }

                consolaOutput.append("--------------------------------------------------\n");
                consolaOutput.append("\n[SUCCESS] Compilación aprobada. Objetivos lógicos completados con éxito.\n");
            } else {
                consolaOutput.append("[ERROR] Compilation failed: La estructura lógica no coincide con los requerimientos técnicos de la misión.\n");
                consolaOutput.append("[HINT] Revisa la sintaxis de tus salidas de consola, nombres de variables o el orden de tus llaves.\n");
            }
        } catch (Exception e) {
            consolaOutput.append("[CRITICAL ERROR]: Falla imprevista en el motor de parsing interno de validación.\n");
        }

        try {
            progreso = progresoService.registrarIntento(progreso, codigoEnviado, esCorrecto);
        } catch (Exception e) {
            // Ignorar fallos de persistencia
        }

        Map<String, Object> response = new HashMap<>();
        response.put("success", esCorrecto);
        response.put("consolaResult", consolaOutput.toString());
        response.put("intentos", (progreso != null && progreso.getIntentos() != null) ? progreso.getIntentos() : 1);
        response.put("statusText", esCorrecto ? "PROCESS_FINISHED_OK" : "PROCESS_FAILED");

        return ResponseEntity.ok(response);
    }
}
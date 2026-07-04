package com.J.M_CODERS.KinalCodeQuest.controller;

import com.J.M_CODERS.KinalCodeQuest.model.entity.*;
import com.J.M_CODERS.KinalCodeQuest.service.evaluator.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* Este controller se encarga exclusivamente de renderizar la consola */
@Controller
@RequestMapping("/game")
public class ConsolaController {

    @Autowired
    private MisionService misionesService;

    @Autowired
    private ProgresoJugadorService progresoService;

    @Autowired
    private JavaCodeExecutorService javaExecutorService;

    @Autowired
    private JugadorService jugadorService;

    @Autowired
    private EjercicioGuiaService ejercicioGuiaService;

    /* Abre la consola directamente usando la primera misión disponible o una simulación segura */
    @GetMapping("/consola")
    public String abrirConsolaDirecta(HttpSession session, Model model) {
        Jugador jugadorSesion = (Jugador) session.getAttribute("usuarioLogueado");
        if (jugadorSesion == null) {
            return "redirect:/auth/login";
        }

        // REFRESCO DE DATOS: Forzar lectura al Kernel de la Base de Datos para evitar EXP en 0
        Jugador jugador = jugadorService.buscarPorId(jugadorSesion.getIdJugador());
        session.setAttribute("usuarioLogueado", jugador);

        Mision m = null;
        List<Mision> listaMisiones = null;

        try {
            listaMisiones = misionesService.listarTodas();
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
        model.addAttribute("listaMisiones", listaMisiones);

        try {
            var ejercicios = ejercicioGuiaService.listarTodosActivos();
            if (ejercicios == null || ejercicios.isEmpty()) {
                ejercicios = obtenerEjerciciosPorDefecto();
            }
            model.addAttribute("listaEjercicios", ejercicios);

            List<Integer> completados = ejercicioGuiaService.ejerciciosCompletadosIds(jugador);
            model.addAttribute("ejerciciosCompletados", completados);
        } catch (Exception e) {
            model.addAttribute("listaEjercicios", obtenerEjerciciosPorDefecto());
        }

        return "game/consola";
    }

    /* Carga la interfaz de la consola de compilación para una misión específica */
    @GetMapping("/mision/{idMision}")
    public String interactuarMision(@PathVariable Integer idMision, HttpSession session, Model model) {
        Jugador jugadorSesion = (Jugador) session.getAttribute("usuarioLogueado");
        if (jugadorSesion == null) {
            return "redirect:/auth/login";
        }

        // REFRESCO DE DATOS: Sincronizar el estado del jugador con la base de datos
        Jugador jugador = jugadorService.buscarPorId(jugadorSesion.getIdJugador());
        session.setAttribute("usuarioLogueado", jugador);

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

        List<Mision> listaMisiones = null;
        try {
            listaMisiones = misionesService.listarTodas();
        } catch (Exception e) {
            // Ignorar
        }

        model.addAttribute("mision", mision);
        model.addAttribute("progreso", progreso);
        model.addAttribute("jugador", jugador);
        model.addAttribute("listaMisiones", listaMisiones);

        try {
            var ejercicios = ejercicioGuiaService.listarTodosActivos();
            if (ejercicios == null || ejercicios.isEmpty()) {
                ejercicios = obtenerEjerciciosPorDefecto();
            }
            model.addAttribute("listaEjercicios", ejercicios);

            List<Integer> completados = ejercicioGuiaService.ejerciciosCompletadosIds(jugador);
            model.addAttribute("ejerciciosCompletados", completados);
        } catch (Exception e) {
            model.addAttribute("listaEjercicios", obtenerEjerciciosPorDefecto());
        }

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

        JavaCodeExecutorService.ExecutionResult executionResult = javaExecutorService.ejecutarCodigo(codigoEnviado);
        String regexCriterio = mision.getCriterioEvaluacion();
        boolean esCorrecto = false;

        if (executionResult.success) {
            if (regexCriterio != null && !regexCriterio.isEmpty() && !regexCriterio.equals(".*")) {
                esCorrecto = javaExecutorService.validarConCriterio(codigoEnviado, regexCriterio);
            } else {
                esCorrecto = true;
            }
        }

        try {
            // El servicio procesa internamente la asignación de puntos únicos o de consolación
            progreso = progresoService.registrarIntento(progreso, codigoEnviado, esCorrecto);
        } catch (Exception e) {
            // Ignorar fallos de persistencia
        }

        // OPTIMIZADO: Se eliminó la asignación duplicada de puntos fijos y se recarga el jugador actualizado por el servicio
        try {
            Jugador jugadorActualizado = jugadorService.buscarPorId(jugador.getIdJugador());
            session.setAttribute("usuarioLogueado", jugadorActualizado);
        } catch (Exception e) {
            // Ignorar si hay problemas al actualizar la sesión
        }

        Map<String, Object> response = new HashMap<>();
        response.put("success", esCorrecto);
        response.put("executionSuccess", executionResult.success);
        response.put("consolaResult", executionResult.consolaOutput);
        response.put("error", executionResult.error);
        response.put("tiempoEjecucion", executionResult.tiempoEjecucion);
        response.put("intentos", (progreso != null && progreso.getIntentos() != null) ? progreso.getIntentos() : 1);
        response.put("statusText", esCorrecto ? "PROCESS_FINISHED_OK" : (executionResult.success ? "EXECUTION_OK_CRITERIA_FAILED" : "PROCESS_FAILED"));

        return ResponseEntity.ok(response);
    }

    /* Endpoint para marcar un ejercicio guía como completado y otorgar puntos de laboriosidad */
    @PostMapping("/ejercicio/{idEjercicio}/completar")
    @ResponseBody
    public ResponseEntity<?> completarEjercicio(@PathVariable Integer idEjercicio, @RequestBody(required = false) Map<String, String> body, HttpSession session) {
        Jugador jugador = (Jugador) session.getAttribute("usuarioLogueado");
        if (jugador == null) {
            return ResponseEntity.status(401).body(Map.of("awarded", false, "mensaje", "Sesión inválida."));
        }
        var opt = ejercicioGuiaService.buscarPorId(idEjercicio);
        if (opt.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("awarded", false, "mensaje", "Ejercicio no encontrado"));
        }

        EjercicioGuia ejercicio = opt.get();
        String codigoEnviado = null;
        if (body != null) codigoEnviado = body.get("codigo");
        if (codigoEnviado == null) codigoEnviado = "";

        // Validar similitud con el criterio del ejercicio
        boolean similar = false;
        try {
            String criterio = ejercicio.getCriterioEvaluacion();
            if (criterio != null && !criterio.isEmpty() && !".*".equals(criterio)) {
                similar = javaExecutorService.validarConCriterio(codigoEnviado, criterio);
            } else {
                similar = javaExecutorService.compararCodigo(codigoEnviado, ejercicio.getCodigoTemplate());
            }
        } catch (Exception ex) {
            similar = javaExecutorService.compararCodigo(codigoEnviado, ejercicio.getCodigoTemplate());
        }

        if (!similar) {
            // Registrar intento fallido para telemetría
            try {
                ejercicioGuiaService.registrarIntento(jugador, ejercicio, codigoEnviado);
            } catch (Exception ex) {
                // Ignorar errores de persistencia de telemetría
            }
            return ResponseEntity.ok(Map.of("awarded", false, "mensaje", "el ejercicio no es similar, intenta de nuevo"));
        }

        // Delegar la lógica y fallback al servicio para asegurar consistencia transaccional
        boolean otorgado = ejercicioGuiaService.completarEjercicio(jugador, ejercicio);

        // Recargar jugador desde BD para asegurar valores actualizados
        Jugador jugadorActualizado = jugadorService.buscarPorId(jugador.getIdJugador());
        session.setAttribute("usuarioLogueado", jugadorActualizado);

        Map<String, Object> resp = new HashMap<>();
        resp.put("awarded", otorgado);
        resp.put("nuevosPuntosLaboriosidad", jugadorActualizado != null ? jugadorActualizado.getPtosLaboriosidad() : null);
        resp.put("mensaje", otorgado ? "Ejercicio validado y puntos otorgados" : "Ejercicio ya completado o no fue posible asignar puntos");
        return ResponseEntity.ok(resp);
    }

    // Ejercicios por defecto en caso de que la base de datos esté vacía (no persisten)
    private List<EjercicioGuia> obtenerEjerciciosPorDefecto() {
        List<EjercicioGuia> list = new java.util.ArrayList<>();

        list.add(EjercicioGuia.builder().idEjercicio(1).orden(1).titulo("Hello World").descripcion("Imprime en consola el texto: HOLA KINAL").codigoTemplate("public class Main {\n    public static void main(String[] args) {\n        System.out.println(\"HOLA KINAL\");\n    }\n}").puntosRecompensa(10).activo(true).build());
        list.add(EjercicioGuia.builder().idEjercicio(2).orden(2).titulo("Suma básica").descripcion("Suma dos números y muestra el resultado").codigoTemplate("public class Calculator {\n    public static void main(String[] args) {\n        int num1 = 15;\n        int num2 = 7;\n        System.out.println(\"Suma: \" + (num1 + num2));\n        System.out.println(\"Resta: \" + (num1 - num2));\n        System.out.println(\"Multiplicación: \" + (num1 * num2));\n        System.out.println(\"División: \" + (num1 / num2));\n    }\n}").puntosRecompensa(10).activo(true).build());
        list.add(EjercicioGuia.builder().idEjercicio(3).orden(3).titulo("Bucle For").descripcion("Imprime números del 1 al 5 usando un for").codigoTemplate("public class ForLoop {\n    public static void main(String[] args) {\n        for (int i = 1; i <= 5; i++) {\n            System.out.println(i);\n        }\n    }\n}").puntosRecompensa(10).activo(true).build());
        list.add(EjercicioGuia.builder().idEjercicio(4).orden(4).titulo("If-Else").descripcion("Verifica si un número es par o impar").codigoTemplate("public class Conditional {\n    public static void main(String[] args) {\n        int n = 4;\n        if (n % 2 == 0) {\n            System.out.println(\"Par\");\n        } else {\n            System.out.println(\"Impar\");\n        }\n    }\n}").puntosRecompensa(10).activo(true).build());
        list.add(EjercicioGuia.builder().idEjercicio(5).orden(5).titulo("Array básico").descripcion("Crea un array de 3 elementos y muéstralos").codigoTemplate("public class ArrayExample {\n    public static void main(String[] args) {\n        int[] numeros = {10,20,30};\n        for (int num : numeros) System.out.println(num);\n    }\n}").puntosRecompensa(10).activo(true).build());
        list.add(EjercicioGuia.builder().idEjercicio(6).orden(6).titulo("Método simple").descripcion("Crea un método que sume dos números y lo llame desde main").codigoTemplate("public class Methods {\n    static int sumar(int a, int b) { return a + b; }\n    public static void main(String[] args) {\n        System.out.println(sumar(5,3));\n    }\n}").puntosRecompensa(10).activo(true).build());
        list.add(EjercicioGuia.builder().idEjercicio(7).orden(7).titulo("ArrayList básico").descripcion("Usa ArrayList para almacenar y mostrar elementos").codigoTemplate("import java.util.ArrayList;\npublic class ListExample {\n    public static void main(String[] args) {\n        ArrayList<String> frutas = new ArrayList<>();\n        frutas.add(\"Manzana\");\n        frutas.add(\"Banana\");\n        System.out.println(frutas);\n    }\n}").puntosRecompensa(10).activo(true).build());
        list.add(EjercicioGuia.builder().idEjercicio(8).orden(8).titulo("While Loop").descripcion("Usa while para counting hasta 3").codigoTemplate("public class WhileLoop {\n    public static void main(String[] args) {\n        int i = 0;\n        while (i <= 3) {\n            System.out.println(i); i++;\n        }\n    }\n}").puntosRecompensa(10).activo(true).build());
        list.add(EjercicioGuia.builder().idEjercicio(9).orden(9).titulo("Try-Catch básico").descripcion("Captura una excepción de conversión de String a int").codigoTemplate("public class ErrorHandling {\n    public static void main(String[] args) {\n        try { Integer.parseInt(\"abc\"); } catch (NumberFormatException e) { System.out.println(\"Error de conversión\"); }\n    }\n}").puntosRecompensa(10).activo(true).build());
        list.add(EjercicioGuia.builder().idEjercicio(10).orden(10).titulo("Recursión (factorial)").descripcion("Calcula el factorial de un número usando recursión").codigoTemplate("public class Recursion {\n    static int factorial(int n) { if (n <= 1) return 1; return n * factorial(n-1); }\n    public static void main(String[] args) { System.out.println(factorial(5)); }\n}").puntosRecompensa(10).activo(true).build());

        return list;
    }
}
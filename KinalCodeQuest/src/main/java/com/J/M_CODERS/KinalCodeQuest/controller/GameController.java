package com.J.M_CODERS.KinalCodeQuest.controller;

import com.J.M_CODERS.KinalCodeQuest.model.entity.*;
import com.J.M_CODERS.KinalCodeQuest.repository.ProgresoJugadorRepository;
import com.J.M_CODERS.KinalCodeQuest.service.evaluator.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;
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

    @Autowired
    private EjercicioGuiaService ejercicioGuiaService;

    @Autowired
    private com.J.M_CODERS.KinalCodeQuest.repository.ProgresoEjercicioRepository progresoEjercicioRepository;
    // Banco de preguntas en memoria de Java optimizado (Niveles del 1 al 10 completificados)
    private final List<PreguntaTrivia> bancoPreguntas = Arrays.asList(
            // Nivel 1: Sintaxis y Variables Básicas
            new PreguntaTrivia(1, 1, "¿Cuál de los siguientes es un tipo de dato primitivo en Java?",
                    Arrays.asList("String", "int", "Integer", "Scanner"), 1, "Los tipos primitivos como 'int', 'char' y 'boolean' almacenan valores directamente en la memoria Stack y no son objetos."),
            new PreguntaTrivia(2, 1, "¿Cómo se declara una constante inmutable en Java?",
                    Arrays.asList("const int X = 10;", "final int X = 10;", "static int X = 10;", "immutable int X = 10;"), 1, "La palabra clave 'final' define una variable cuyo valor no puede cambiar tras su primera asignación."),

            // Nivel 2: Estructuras de Control y Flujo
            new PreguntaTrivia(3, 2, "¿Qué estructura garantiza que el bloque de código se ejecute al menos una vez de forma obligatoria?",
                    Arrays.asList("for", "while", "do-while", "if-else"), 2, "La condición de un bucle 'do-while' se evalúa al final del ciclo, asegurando siempre una primera ejecución."),
            new PreguntaTrivia(4, 2, "¿Cuál es el resultado de un ciclo 'for (int i = 0; i < 3; i++)' si imprimimos el valor de 'i' consecutivamente?",
                    Arrays.asList("0 1 2 3", "1 2 3", "0 1 2", "0 0 0"), 2, "El ciclo se rompe de forma inmediata en el momento en que 'i' incrementa a 3, imprimiendo únicamente los índices 0, 1 y 2."),

            // Nivel 3: Colecciones e Inventarios
            new PreguntaTrivia(5, 3, "¿Qué colección utilizarías para almacenar una lista dinámica de herramientas que cambia de tamaño dinámicamente?",
                    Arrays.asList("Array[]", "ArrayList<String>", "int[]", "ListMap"), 1, "ArrayList permite añadir o eliminar elementos dinámicamente, ideal para gestionar listas de inventario dinámicas."),
            new PreguntaTrivia(6, 3, "¿Qué estructura de datos usarías para buscar una herramienta por su 'código de activo' de forma rápida (complejidad O(1))?",
                    Arrays.asList("ArrayList", "LinkedList", "HashMap", "Stack"), 2, "HashMap utiliza llaves hashing para acceder a los valores instantáneamente sin recorrer toda la lista."),

            // Nivel 4: Estructura de Métodos - Lógica de Sensores
            new PreguntaTrivia(7, 4, "¿Cuál es la firma correcta para un método que recibe un voltaje (double) y retorna true si es menor a 220.0v?",
                    Arrays.asList("void validar(double v)", "boolean validar(double v)", "int validar(double v)", "main(double v)"), 1, "Un método encargado de validar condiciones lógicas debe especificar el tipo de retorno 'boolean' en su firma para responder con true o false."),

            // Nivel 5: POO Básica
            new PreguntaTrivia(8, 5, "¿Qué palabra clave se utiliza para crear una nueva instancia (objeto) de una clase en Java?",
                    Arrays.asList("create", "class", "new", "instanceof"), 2, "El operador 'new' asigna memoria dinámica en el Heap para un nuevo objeto e invoca a su constructor."),
            new PreguntaTrivia(9, 5, "¿Qué es un constructor en una clase de Java?",
                    Arrays.asList("Un método para destruir objetos", "Un método especial que inicializa el objeto y se llama igual que la clase", "Una herramienta para compilar archivos", "Una variable global inmutable"), 1, "El constructor configura los valores iniciales de un objeto al ser instanciado con el operador new."),

            // Nivel 6: Herencia y Polimorfismo
            new PreguntaTrivia(10, 6, "¿Qué palabra reservada se emplea en Java para implementar la herencia entre clases?",
                    Arrays.asList("implements", "extends", "inherits", "super"), 1, "La palabra clave 'extends' indica que una subclase hereda los atributos y métodos de una superclase."),
            new PreguntaTrivia(11, 6, "¿Qué anotación se recomienda colocar antes de un método que ha sido redefinido en una subclase?",
                    Arrays.asList("@Overwrite", "@Deprecated", "@Override", "@Interface"), 2, "La anotación '@Override' le avisa al compilador que estamos sobreescribiendo deliberadamente un método de la clase padre."),

            // Nivel 7: Encapsulamiento y Abstracción
            new PreguntaTrivia(12, 7, "¿Cuál es el modificador de acceso más restrictivo que oculta variables para que solo se lean en la misma clase?",
                    Arrays.asList("public", "protected", "default", "private"), 3, "El modificador 'private' restringe el acceso al elemento únicamente dentro de la propia clase."),
            new PreguntaTrivia(13, 7, "¿Cuál es una característica fundamental de una Interfaz en Java?",
                    Arrays.asList("Puede instanciarse directamente", "Solo contiene variables privadas", "Define contratos de comportamiento con métodos abstractos sin cuerpo", "No permite clases heredadas"), 2, "Las interfaces definen comportamientos que otras clases están obligadas a implementar."),

            // Nivel 8: Excepciones
            new PreguntaTrivia(14, 8, "¿Qué bloque de código se ejecuta SIEMPRE en una estructura de control de excepciones, haya ocurrido un error o no?",
                    Arrays.asList("catch", "try", "finally", "throws"), 2, "El bloque 'finally' es de ejecución obligatoria y se usa típicamente para liberar recursos o cerrar flujos."),
            new PreguntaTrivia(15, 8, "¿Cuál de las siguientes es la superclase de todas las excepciones y errores en Java?",
                    Arrays.asList("Exception", "Throwable", "RuntimeException", "Error"), 1, "La clase 'Throwable' está en la cúspide de la jerarquía de manejo de errores en Java."),

            // Nivel 9: Archivos I/O
            new PreguntaTrivia(16, 9, "¿Qué clase es la más adecuada para leer texto de un archivo línea por línea de manera eficiente?",
                    Arrays.asList("FileWriter", "File", "BufferedReader", "OutputStream"), 2, "BufferedReader almacena caracteres en un búfer para proveer una lectura eficiente línea por línea con .readLine()."),

            // Nivel 10: JDBC
            new PreguntaTrivia(17, 10, "¿Qué interfaz de JDBC se utiliza para ejecutar consultas SQL parametrizadas de forma segura contra inyecciones?",
                    Arrays.asList("Connection", "Statement", "PreparedStatement", "DriverManager"), 2, "PreparedStatement precompila la sentencia SQL y ayuda a mitigar ataques maliciosos de inyección de código SQL.")
    );

    /* Renderiza el panel de control del usuario con sus estadísticas y accesos directos */
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

        if (erroresTotales < 0) erroresTotales = 0;

        model.addAttribute("jugador", jugadorActualizado);
        model.addAttribute("areas", areaService.listarTodas());
        model.addAttribute("erroresTotales", erroresTotales);
        model.addAttribute("intentosTotales", intentosTotales);
        model.addAttribute("misionesDificiles", misionesDificiles);

        // Métrica adicional: número de ejercicios guía completados por el jugador (para diagnóstico)
        try {
            java.util.List<Integer> ejerciciosCompletadosIds = ejercicioGuiaService.ejerciciosCompletadosIds(jugadorActualizado);
            model.addAttribute("ejerciciosCompletadosCount", ejerciciosCompletadosIds != null ? ejerciciosCompletadosIds.size() : 0);
        } catch (Exception ex) {
            model.addAttribute("ejerciciosCompletadosCount", 0);
        }

        // Añadir historial de intentos sobre ejercicios guía del jugador para visualización (opción B)
        try {
            List<com.J.M_CODERS.KinalCodeQuest.model.entity.ProgresoEjercicio> historial = progresoEjercicioRepository.findByJugador(jugadorActualizado);
            model.addAttribute("progresoEjercicios", historial);
        } catch (Exception ex) {
            model.addAttribute("progresoEjercicios", java.util.Collections.emptyList());
        }
        return "game/dashboard";
    }

    /* Redirige a la interfaz base del motor interactivo clásico */
    @GetMapping("/game")
    public String gameCore(HttpSession session, Model model) {
        Jugador jugadorSesion = (Jugador) session.getAttribute("usuarioLogueado");
        if (jugadorSesion == null) return "redirect:/auth/login";

        Jugador jugadorActualizado = jugadorService.buscarPorId(jugadorSesion.getIdJugador());
        session.setAttribute("usuarioLogueActive", jugadorActualizado);

        model.addAttribute("jugador", jugadorActualizado);
        model.addAttribute("areas", areaService.listarTodas());

        return "game/game";
    }

    /* MÓDULO INICIAR JUEGO: Selector de Niveles y Manual Técnico de Usuario integrado */
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

    /* Evalúa el examen de la trivia y premia al usuario con un sistema balanceado de EXP y Responsabilidad */
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
        List<String> feedback = new ArrayList<>();

        for (PreguntaTrivia pregunta : preguntasNivel) {
            String respuestaEnviada = params.get("pregunta_" + pregunta.getIdPregunta());
            if (respuestaEnviada != null && Integer.parseInt(respuestaEnviada) == pregunta.getRespuestaCorrectaIndex()) {
                correctas++;
                feedback.add("Pregunta #" + pregunta.getIdPregunta() + ": [CORRECTA] -> " + pregunta.getJustificacion());
            } else {
                feedback.add("Pregunta #" + pregunta.getIdPregunta() + ": [INCORRECTA] -> " + pregunta.getJustificacion());
            }
        }

        boolean aprobado = (correctas == preguntasNivel.size());
        int expGanada = aprobado ? 10 : 0; // Ajustado a Progresión de 10 en 10 EXP
        int responsabilidadGanada = aprobado ? 10 : 0;

        if (aprobado) {
            // Asignación de Experiencia y Puntos Institucionales
            jugador.setExperiencia((jugador.getExperiencia() != null ? jugador.getExperiencia() : 0) + expGanada);
            jugador.setPtosResponsabilidad((jugador.getPtosResponsabilidad() != null ? jugador.getPtosResponsabilidad() : 0) + responsabilidadGanada);

            // Soporte para los saldos específicos de las primeras áreas en caso de existir en el modelo
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
        model.addAttribute("responsabilidadGanada", responsabilidadGanada);
        model.addAttribute("correctas", correctas);
        model.addAttribute("total", preguntasNivel.size());
        model.addAttribute("feedbacks", feedback);
        model.addAttribute("areaId", idArea);

        return "game/resultado_trivia";
    }

    /* Apartado progresivo de la historia con nuevos registros de la BD y escala lineal de Laboriosidad (10-100) */
    @GetMapping("/historia")
    public String verHistoria(HttpSession session, Model model) {
        Jugador jugadorSesion = (Jugador) session.getAttribute("usuarioLogueado");
        if (jugadorSesion == null) return "redirect:/auth/login";

        Jugador jugador = jugadorService.buscarPorId(jugadorSesion.getIdJugador());
        int expActual = (jugador.getExperiencia() != null) ? jugador.getExperiencia() : 0;
        int laboriosidadActual = (jugador.getSaldoLaboriosidad() != null) ? jugador.getSaldoLaboriosidad() : 0;

        List<CapituloHistoria> capitulos = new ArrayList<>();

        // Estructura: crearCapitulo(id, titulo, epoca, expReq, textoNarrativo, expActual, rutaImg, labReq, labActual, bonusRecompensa)
        capitulos.add(crearCapitulo(1, "El Cimiento del Núcleo", "1961", 0,
                "El Centro Educativo Técnico Laboral Kinal nació en 1961 en Guatemala. Su meta fundamental desde su origen ha sido brindar oportunidades de superación técnica y humana a la clase trabajadora.",
                expActual, "/img/historia/logo-Kinal.jpg", 0, laboriosidadActual, null)); // Reducido a 0

        capitulos.add(crearCapitulo(2, "Archivo Visual: Los Primeros Pasos", "1965", 10,
                "Fichero recuperado de los archivos confidenciales. Las primeras clases de dibujo técnico y electricidad se estructuraron con recursos limitados pero con un alto estándar de excelencia.",
                expActual, "/img/aniversarios/61-aniversario.jpg", 10, laboriosidadActual, "[+10 Ptos Prestigio Logrado]")); // Reducido a 10

        capitulos.add(crearCapitulo(3, "La Consolidación del Servidor", "1989", 20,
                "En 1989 se concreta el traslado definitivo a la sede actual en la Zona 7 de la Ciudad de Guatemala, expandiendo radicalmente la infraestructura de los laboratorios y talleres tecnológicos.",
                expActual, "/img/historia/Construccion-sede-1989.jpg", 20, laboriosidadActual, null)); // Reducido a 20

        capitulos.add(crearCapitulo(4, "Código de Honor: Laboriosidad", "1975", 30,
                "Protocolo ético: El trabajo diario no es solo una carga, sino un medio para alcanzar la excelencia humana, santificar el entorno y desarrollar la infraestructura de Guatemala.",
                expActual, "/img/historia/Construccion-basicos-2005.jpg", 30, laboriosidadActual, "[Análisis de Integridad Completado]")); // Reducido a 30

        capitulos.add(crearCapitulo(5, "Planos de Infraestructura: Edificio C", "1992", 40,
                "Planos estructurales recuperados de los servidores centrales. Captura histórica que documenta las fases de cimentación y levantamiento del icónico Edificio C.",
                expActual, "/img/historia/Construccion-edificio-C-1992.jpg", 40, laboriosidadActual, "[Módulo de Hardware Desbloqueado]")); // Reducido a 40

        capitulos.add(crearCapitulo(6, "Registro de Campo: Bloque de Básicos", "2005", 50,
                "Compilación de capturas del área de educación básica técnica del año 2005, showing la evolución de los entornos académicos interactivos.",
                expActual, "/img/historia/Construccion-basicos-2-2005.jpg", 50, laboriosidadActual, "[+5 Ptos Cooperación Colectiva]")); // Reducido a 50

        capitulos.add(crearCapitulo(7, "La Era Moderna del Core", "2026", 60,
                "Kinal se transforma en un referente de innovación digital y tecnológica en la región, integrando desarrollo de software de vanguardia y metodologías ágiles avanzadas.",
                expActual, "/img/historia/Entrada-Kinal-actual.jpg", 60, laboriosidadActual, "[+20 Ptos Sistema de Vanguardia]")); // Reducido a 60

        capitulos.add(crearCapitulo(8, "Arquitectura Orientada a Objetos", "POO", 70,
                "El software moderno demanda abstracción. Kinal adoptó metodologías avanzadas de desarrollo, enseñando que cada entidad del mundo real puede ser modelada como un objeto con responsabilidades específicas.",
                expActual, "/img/historia/Construccion-basicos-3-2005.jpg", 70, laboriosidadActual, "[Compilador POO Activado]")); // Reducido a 70

        capitulos.add(crearCapitulo(9, "Tolerancia a Fallos", "EXCEPCIONES", 80,
                "Los sistemas reales fallan, pero un ingeniero de Kinal escribe software preparado para mitigar el caos. El control estructurado de excepciones captura los imprevistos de ejecución y previene el colapso total de la infraestructura.",
                expActual, "/img/aniversarios/63-aniversario.jpg", 80, laboriosidadActual, "[Manejo de Excepciones Ok]")); // Reducido a 80

        capitulos.add(crearCapitulo(10, "Persistencia del Núcleo", "JDBC", 90,
                "Los archivos pasan, pero las conexiones estables a base de datos (JDBC) trascienden las sesiones activas de memoria. De igual manera, el impacto formativo y social de Kinal queda grabado de forma persistente en la historia.",
                expActual, "/img/aniversarios/65-aniversario.jpg", 90, laboriosidadActual, "[Persistencia de Datos Completa]")); // Reducido a 90

        model.addAttribute("jugador", jugador);
        model.addAttribute("capitulos", capitulos);

        return "game/historia";
    }

    /* Función de soporte mapeada con soporte para bonus estéticos de recompensa */
    /* Función de soporte mapeada con soporte para bonus estéticos de recompensa */
    private CapituloHistoria crearCapitulo(int id, String titulo, String epoca, int exp, String contenido, int expActual, String rutaImagen, int costoLaboriosidad, int laboriosidadActual, String bonusRecompensa) {
        CapituloHistoria cap = new CapituloHistoria();
        cap.setIdCapitulo(id);
        cap.setTitulo(titulo);
        cap.setEpoca(epoca);
        cap.setCostoCantidad(exp);
        cap.setTipoMoneda("EXP");
        cap.setTipoContenido("PRINCIPAL");
        cap.setContenidoNarrativo(contenido);
        cap.setRutaImagen(rutaImagen);
        cap.setBonusRecompensa(bonusRecompensa);

        // Evaluación de Desbloqueo de TEXTO (Por Puntos de Experiencia)
        cap.setDesbloqueado(expActual >= exp);

        // Evaluación de Desbloqueo de IMAGEN (Por Puntos de Laboriosidad)
        cap.setCostoLaboriosidad(costoLaboriosidad);
        cap.setImagenDesbloqueada(laboriosidadActual >= costoLaboriosidad);

        return cap;
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

    /* Despliega la bitácora completa de misiones generales disponibles */
    @GetMapping("/misiones")
    public String misionesGenerales(HttpSession session, Model model) {
        Jugador jugador = (Jugador) session.getAttribute("usuarioLogueado");
        if (jugador == null) return "redirect:/auth/login";

        List<Mision> misiones = misionesService.listarTodas();
        Map<Integer, Boolean> misionesCompletadas = new HashMap<>();
        
        // Crear mapa de misiones completadas
        for (Mision mision : misiones) {
            ProgresoJugador progreso = progresoRepository.findByJugadorAndMision(jugador, mision).orElse(null);
            misionesCompletadas.put(mision.getIdMision(), progreso != null && progreso.getCompletada());
        }

        model.addAttribute("jugador", jugador);
        model.addAttribute("misiones", misiones);
        model.addAttribute("misionesCompletadas", misionesCompletadas);
        
        // Añadimos ejercicios guía para mostrarlos en la página de misiones
        try {
            model.addAttribute("listaEjercicios", ejercicioGuiaService.listarTodosActivos());
        } catch (Exception e) {
            model.addAttribute("listaEjercicios", null);
        }
        // Pasar lista de ejercicios completados para renderizar botones
        try {
            model.addAttribute("ejerciciosCompletados", ejercicioGuiaService.ejerciciosCompletadosIds(jugador));
        } catch (Exception e) {
            model.addAttribute("ejerciciosCompletados", java.util.Collections.emptyList());
        }
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
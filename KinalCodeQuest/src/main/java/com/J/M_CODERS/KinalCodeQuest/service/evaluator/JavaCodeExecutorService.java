
package com.J.M_CODERS.KinalCodeQuest.service.evaluator;

import org.springframework.stereotype.Service;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Servicio para compilar y ejecutar código Java de forma segura.
 * Proporciona ejecución de código dinámico con manejo de Scanner y captura de salida.
 */
@Service
public class JavaCodeExecutorService {

    private static final long EXECUTION_TIMEOUT_SECONDS = 10;
    private static final int MAX_OUTPUT_CHARS = 5000;
    private static final String TEMP_DIR = System.getProperty("java.io.tmpdir") + "/kinal_executor/";

    public JavaCodeExecutorService() {
        // Crear directorio temporal
        try {
            Files.createDirectories(Paths.get(TEMP_DIR));
        } catch (IOException e) {
            System.err.println("Error creando directorio temporal: " + e.getMessage());
        }
    }

    /**
     * Ejecuta código Java y devuelve el resultado
     */
    public ExecutionResult ejecutarCodigo(String codigoFuente) {
        ExecutionResult resultado = new ExecutionResult();
        long tiempoInicio = System.currentTimeMillis();

        try {
            // Validar que el código sea Java válido
            String codigoNormalizado = normalizarCodigo(codigoFuente);
            resultado.codigoNormalizado = codigoNormalizado;

            // Generar clase temporal
            String nombreClase = "KinalExecutor_" + System.currentTimeMillis();
            String codigoWrapped = wrapearCodigo(codigoNormalizado, nombreClase);

            // Ruta de archivos temporales
            Path dirTemp = Paths.get(TEMP_DIR, nombreClase);
            Files.createDirectories(dirTemp);

            Path archivoJava = dirTemp.resolve(nombreClase + ".java");
            Path archivoClass = dirTemp.resolve(nombreClase + ".class");

            // Escribir archivo .java
            Files.write(archivoJava, codigoWrapped.getBytes(StandardCharsets.UTF_8));

            // Compilar
            StringBuilder compileOutput = new StringBuilder();
            compileOutput.append("C:\\KinalCodeQuest\\compiler> javac ").append(nombreClase).append(".java\n");

            ProcessBuilder compileBuilder = new ProcessBuilder(
                    "javac",
                    "-encoding", "UTF-8",
                    archivoJava.toAbsolutePath().toString()
            );
            compileBuilder.redirectErrorStream(true);

            Process compileProcess = compileBuilder.start();
            boolean compiloOk = compileProcess.waitFor(EXECUTION_TIMEOUT_SECONDS, TimeUnit.SECONDS);

            if (!compiloOk) {
                compileProcess.destroyForcibly();
                resultado.success = false;
                resultado.error = "Timeout en la compilación";
                compileOutput.append("[ERROR] Compilación excedió el tiempo límite\n");
                resultado.consolaOutput = compileOutput.toString();
                return resultado;
            }

            // Leer errores de compilación
            String compileErrors = leerOutputProcess(compileProcess);
            if (compileProcess.exitValue() != 0) {
                resultado.success = false;
                resultado.error = "Error de compilación";
                compileOutput.append(compileErrors.isEmpty() ? 
                    "[ERROR] Fallo en la compilación. Verifica la sintaxis de tu código.\n" : 
                    compileErrors);
                resultado.consolaOutput = compileOutput.toString();
                return resultado;
            }

            compileOutput.append("C:\\KinalCodeQuest\\compiler> java ").append(nombreClase).append("\n");
            compileOutput.append("[INFO] Java Virtual Machine inicializada de manera exitosa.\n");
            compileOutput.append("--------------------------------------------------\n");

            // Ejecutar
            ProcessBuilder runBuilder = new ProcessBuilder(
                    "java",
                    "-cp", dirTemp.toAbsolutePath().toString(),
                    nombreClase
            );
            runBuilder.redirectErrorStream(true);
            Process runProcess = runBuilder.start();

            // Ejecutar en un thread separado con timeout
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Future<String> future = executor.submit(() -> leerOutputProcess(runProcess));

            boolean executedOk = runProcess.waitFor(EXECUTION_TIMEOUT_SECONDS, TimeUnit.SECONDS);

            String output = "";
            if (executedOk) {
                try {
                    output = future.get(1, TimeUnit.SECONDS);
                } catch (TimeoutException e) {
                    output = "[TIMEOUT] La ejecución tardó demasiado tiempo.\n";
                }
                int exitCode = runProcess.exitValue();
                if (exitCode != 0 && output.isEmpty()) {
                    resultado.success = false;
                    resultado.error = "Error de ejecución";
                    compileOutput.append("[ERROR] La aplicación terminó con código de error: ").append(exitCode).append("\n");
                } else {
                    resultado.success = true;
                }
            } else {
                runProcess.destroyForcibly();
                future.cancel(true);
                resultado.success = false;
                resultado.error = "Timeout en la ejecución";
                output = "[TIMEOUT] La ejecución excedió el tiempo límite de " + EXECUTION_TIMEOUT_SECONDS + " segundos.\n";
            }

            executor.shutdown();

            // Limitar el tamaño del output
            if (output.length() > MAX_OUTPUT_CHARS) {
                output = output.substring(0, MAX_OUTPUT_CHARS) + "\n... [SALIDA TRUNCADA] ...\n";
            }

            compileOutput.append(output);
            compileOutput.append("--------------------------------------------------\n");
            
            if (resultado.success) {
                compileOutput.append("\n[SUCCESS] Compilación y ejecución aprobadas.\n");
            }

            resultado.consolaOutput = compileOutput.toString();

            // Limpiar archivos temporales
            limpiarTemporales(dirTemp);

        } catch (Exception e) {
            resultado.success = false;
            resultado.error = "Error interno: " + e.getMessage();
            resultado.consolaOutput = "[CRITICAL ERROR] " + e.getMessage() + "\n";
            e.printStackTrace();
        }

        resultado.tiempoEjecucion = System.currentTimeMillis() - tiempoInicio;
        return resultado;
    }

    /**
     * Valida si el código contiene un criterio específico (usando regex)
     */
    public boolean validarConCriterio(String codigoFuente, String regexCriterio) {
        try {
            // Eliminar comentarios y reducir múltiples espacios para una comparación más tolerante
            String sinComentarios = removeComments(codigoFuente);
            String normalizado = sinComentarios.replaceAll("\\s+", " ").trim();
            return Pattern.compile(regexCriterio, Pattern.CASE_INSENSITIVE | Pattern.DOTALL)
                    .matcher(normalizado)
                    .find(); // usar find en vez de matches para mayor flexibilidad
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Compara dos fragmentos de código de forma tolerant: elimina comentarios y normaliza espacios
     */
    public boolean compararCodigo(String codigoA, String codigoB) {
        if (codigoA == null) codigoA = "";
        if (codigoB == null) codigoB = "";
        String na = normalizeForComparison(codigoA);
        String nb = normalizeForComparison(codigoB);

        // Igualdad rápida
        if (na.equals(nb)) return true;

        int maxLen = Math.max(na.length(), nb.length());
        if (maxLen == 0) return true;

        int dist = levenshteinDistance(na, nb);
        double similarity = 1.0 - ((double) dist / (double) maxLen);
        double threshold = 0.85; // tolerancia por defecto (85%)
        return similarity >= threshold;
    }

    private int levenshteinDistance(String s1, String s2) {
        int n = s1.length();
        int m = s2.length();
        if (n == 0) return m;
        if (m == 0) return n;

        int[] prev = new int[m + 1];
        int[] curr = new int[m + 1];

        for (int j = 0; j <= m; j++) prev[j] = j;

        for (int i = 1; i <= n; i++) {
            curr[0] = i;
            char c1 = s1.charAt(i - 1);
            for (int j = 1; j <= m; j++) {
                int cost = (c1 == s2.charAt(j - 1)) ? 0 : 1;
                curr[j] = Math.min(Math.min(curr[j - 1] + 1, prev[j] + 1), prev[j - 1] + cost);
            }
            int[] tmp = prev; prev = curr; curr = tmp;
        }
        return prev[m];
    }

    private String normalizeForComparison(String codigo) {
        String sinComentarios = removeComments(codigo);
        // Colapsar espacios y eliminar saltos de línea, mantener cadenas literales intactas
        return sinComentarios.replaceAll("\\s+", " ").trim();
    }

    /**
     * Elimina comentarios de línea y de bloque del código fuente
     */
    private String removeComments(String codigo) {
        if (codigo == null) return "";
        // Eliminar comentarios multi-línea /* ... */ usando DOTALL
        String sinBloques = codigo.replaceAll("(?s)/\\*.*?\\*/", "");
        // Eliminar comentarios de una sola línea //...
        String sinLineas = sinBloques.replaceAll("//.*", "");
        return sinLineas;
    }

    /**
     * Extrae los System.out.println del código
     */
    public List<String> extraerPrintlns(String codigoFuente) {
        List<String> printlns = new ArrayList<>();
        Pattern p = Pattern.compile("System\\.out\\.println\\s*\\(\\s*\"([^\"]*)\"\\s*\\)");
        Matcher m = p.matcher(codigoFuente);
        
        while (m.find()) {
            printlns.add(m.group(1));
        }
        
        return printlns;
    }

    /**
     * Normaliza el código fuente
     */
    private String normalizarCodigo(String codigo) {
        return codigo.trim();
    }

    /**
     * Envuelve el código en una clase ejecutable
     */
    private String wrapearCodigo(String codigo, String nombreClase) {
        // Si ya tiene método main, usar como está
        if (codigo.contains("public static void main")) {
            // Reemplazar nombre de clase si es necesario
            return codigo.replaceFirst("class\\s+\\w+", "class " + nombreClase);
        }

        // Si tiene clase pero no main, agregar main
        if (codigo.contains("class ")) {
            return codigo.replaceFirst("class\\s+\\w+", "class " + nombreClase)
                    .replaceFirst("\\{", "{\n    public static void main(String[] args) throws Exception {\n")
                    .replaceFirst("\\}(?!\\})", "\n    }\n}");
        }

        // Código suelto, envolver en clase con main
        return "public class " + nombreClase + " {\n" +
                "    public static void main(String[] args) throws Exception {\n" +
                "        " + codigo.replaceAll("\n", "\n        ") + "\n" +
                "    }\n" +
                "}\n";
    }

    /**
     * Lee el output de un proceso
     */
    private String leerOutputProcess(Process process) throws IOException {
        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                output.append(linea).append("\n");
            }
        }
        return output.toString();
    }

    /**
     * Limpia archivos temporales
     */
    private void limpiarTemporales(Path dirPath) {
        try {
            if (Files.exists(dirPath)) {
                Files.walk(dirPath)
                        .sorted(Comparator.reverseOrder())
                        .forEach(path -> {
                            try {
                                Files.delete(path);
                            } catch (IOException e) {
                                // Ignorar
                            }
                        });
            }
        } catch (IOException e) {
            System.err.println("Error limpiando temporales: " + e.getMessage());
        }
    }

    /**
     * Clase interna para el resultado de ejecución
     */
    public static class ExecutionResult {
        public boolean success = false;
        public String consolaOutput = "";
        public String error = "";
        public String codigoNormalizado = "";
        public long tiempoEjecucion = 0;

        @Override
        public String toString() {
            return "ExecutionResult{" +
                    "success=" + success +
                    ", error='" + error + '\'' +
                    ", tiempoEjecucion=" + tiempoEjecucion + "ms" +
                    '}';
        }
    }
}


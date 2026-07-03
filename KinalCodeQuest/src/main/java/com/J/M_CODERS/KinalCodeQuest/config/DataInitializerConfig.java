package com.J.M_CODERS.KinalCodeQuest.config;

import com.J.M_CODERS.KinalCodeQuest.model.entity.AreaTecnica;
import com.J.M_CODERS.KinalCodeQuest.model.entity.Mision;
import com.J.M_CODERS.KinalCodeQuest.model.entity.EjercicioGuia;
import com.J.M_CODERS.KinalCodeQuest.repository.AreaTecnicaRepository;
import com.J.M_CODERS.KinalCodeQuest.repository.MisionRepository;
import com.J.M_CODERS.KinalCodeQuest.repository.EjercicioGuiaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializerConfig {

    @Bean
    CommandLineRunner verificarYpoblarBaseDatos(
            AreaTecnicaRepository areaRepo,
            MisionRepository misionRepo,
            EjercicioGuiaRepository ejercicioRepo) {

        return args -> {
            // Verificar si el sistema ya tiene datos para no duplicar
            if (areaRepo.count() == 0) {
                System.out.println(">>> [CONFIG-CORE] Base de datos vacía. Iniciando protocolo de inyección...");

                // 1. Crear Entorno Técnico Base
                AreaTecnica areaJava = new AreaTecnica();
                areaJava.setNombre("Programación Java");
                areaJava.setDescripcionTematica("Dominio de la sintaxis estructural, lógica de objetos y control de flujos.");
                areaRepo.save(areaJava);

                // 2. Crear Misión de Hackeo Inicial (Nivel 1)
                Mision m1 = new Mision();
                m1.setArea(areaJava);
                m1.setTitulo("Protocolo: Hola Kinal");
                m1.setDescripcionNarrativa("Recluta, el núcleo del sistema requiere una señal de vida. Desarrolla la secuencia estándar para imprimir en la consola virtual el texto exacto: 'HOLA KINAL'.");
                m1.setCodigoBase("public class Main {\n    public static void main(String[] args) {\n        // Escribe tu código de transmisión aquí\n    }\n}");

                // Expresión regular robusta para validar el System.out.println
                m1.setCriterioEvaluacion(".*System\\.out\\.println\\s*\\(\\s*\"HOLA KINAL\"\\s*\\)\\s*;.*");
                m1.setExpRecompensa(250); // Buena EXP para empezar

                misionRepo.save(m1);

                // Agregar 10 ejercicios guía para principiantes
                if (ejercicioRepo.count() == 0) {
                    System.out.println(">>> [CONFIG-CORE] Inyectando ejercicios guía básicos...");

                    EjercicioGuia e1 = EjercicioGuia.builder()
                            .titulo("Hello World")
                            .descripcion("Imprime en consola el texto: HOLA KINAL")
                            .enunciado("Usa System.out.println para muestra el mensaje exacto: HOLA KINAL")
                            .codigoTemplate("public class Main {\n    public static void main(String[] args) {\n        // Escribe aquí\n    }\n}")
                            .criterioEvaluacion(".*System\\.out\\.println\\s*\\(\\s*\"HOLA KINAL\"\\s*\\)\\s*;.*")
                            .dificultad("FACIL")
                            .puntosRecompensa(10)
                            .orden(1)
                            .activo(true)
                            .pistas("Pista: Usa System.out.println(\"HOLA KINAL\");")
                            .build();

                    EjercicioGuia e2 = EjercicioGuia.builder()
                            .titulo("Suma básica")
                            .descripcion("Suma dos números y muestra el resultado")
                            .enunciado("Declara dos variables int y muestra la suma con System.out.println")
                            .codigoTemplate("public class Main {\n    public static void main(String[] args) {\n        int a = 2;\n        int b = 3;\n        // Imprime la suma\n    }\n}")
                            .criterioEvaluacion(".*\\+.*")
                            .dificultad("FACIL")
                            .puntosRecompensa(10)
                            .orden(2)
                            .activo(true)
                            .pistas("Pista: Usa System.out.println(a + b);")
                            .build();

                    EjercicioGuia e3 = EjercicioGuia.builder()
                            .titulo("Bucle For")
                            .descripcion("Imprime números del 1 al 5 usando un for")
                            .enunciado("Usa un bucle for para imprimir los números del 1 al 5")
                            .codigoTemplate("public class Main {\n    public static void main(String[] args) {\n        // for (int i = 1; i <= 5; i++) { ... }\n    }\n}")
                            .criterioEvaluacion(".*for\\s*\\(.*;.*;.*\\).*")
                            .dificultad("FACIL")
                            .puntosRecompensa(10)
                            .orden(3)
                            .activo(true)
                            .pistas("Pista: for (int i = 1; i <= 5; i++) { System.out.println(i); }")
                            .build();

                    EjercicioGuia e4 = EjercicioGuia.builder()
                            .titulo("If-Else")
                            .descripcion("Verifica si un número es par o impar")
                            .enunciado("Declara una variable int y muestra si es par o impar usando if/else")
                            .codigoTemplate("public class Main {\n    public static void main(String[] args) {\n        int n = 4;\n        // if/else\n    }\n}")
                            .criterioEvaluacion(".*if\\s*\\(.*\\).*|.*else.*")
                            .dificultad("FACIL")
                            .puntosRecompensa(10)
                            .orden(4)
                            .activo(true)
                            .pistas("Pista: if (n % 2 == 0) { ... } else { ... }")
                            .build();

                    EjercicioGuia e5 = EjercicioGuia.builder()
                            .titulo("Array básico")
                            .descripcion("Crea un array de 3 elementos y muéstralos")
                            .enunciado("Declara un array int[] y recórrelo con un for para imprimir sus elementos")
                            .codigoTemplate("public class Main {\n    public static void main(String[] args) {\n        int[] arr = {1,2,3};\n        // recorrer e imprimir\n    }\n}")
                            .criterioEvaluacion(".*\\{.*\\}.*")
                            .dificultad("FACIL")
                            .puntosRecompensa(10)
                            .orden(5)
                            .activo(true)
                            .pistas("Pista: for (int i=0; i < arr.length; i++) { System.out.println(arr[i]); }")
                            .build();

                    EjercicioGuia e6 = EjercicioGuia.builder()
                            .titulo("Método simple")
                            .descripcion("Crea un método que sume dos números y lo llame desde main")
                            .enunciado("Define un método static int suma(int a, int b) y usa System.out.println para mostrar el resultado")
                            .codigoTemplate("public class Main {\n    static int suma(int a, int b) { return 0; }\n    public static void main(String[] args) {\n        // llamar suma\n    }\n}")
                            .criterioEvaluacion(".*static.*int.*suma.*\\(.*\\).*")
                            .dificultad("FACIL")
                            .puntosRecompensa(10)
                            .orden(6)
                            .activo(true)
                            .pistas("Pista: static int suma(int a, int b) { return a + b; }")
                            .build();

                    EjercicioGuia e7 = EjercicioGuia.builder()
                            .titulo("ArrayList básico")
                            .descripcion("Usa ArrayList para almacenar y mostrar elementos")
                            .enunciado("Importa java.util.ArrayList, crea una lista, añade 2 elementos y muéstralos")
                            .codigoTemplate("import java.util.ArrayList;\npublic class Main {\n    public static void main(String[] args) {\n        // ArrayList<String> lista = new ArrayList<>();\n    }\n}")
                            .criterioEvaluacion(".*ArrayList.*")
                            .dificultad("FACIL")
                            .puntosRecompensa(10)
                            .orden(7)
                            .activo(true)
                            .pistas("Pista: ArrayList<String> l = new ArrayList<>(); l.add(\"a\"); System.out.println(l);")
                            .build();

                    EjercicioGuia e8 = EjercicioGuia.builder()
                            .titulo("While Loop")
                            .descripcion("Usa while para contar hasta 3")
                            .enunciado("Implementa un while que imprima 0,1,2,3 en consola")
                            .codigoTemplate("public class Main {\n    public static void main(String[] args) {\n        int i = 0;\n        // while (i <= 3) { ... }\n    }\n}")
                            .criterioEvaluacion(".*while\\s*\\(.*\\).*")
                            .dificultad("FACIL")
                            .puntosRecompensa(10)
                            .orden(8)
                            .activo(true)
                            .pistas("Pista: while (i <= 3) { System.out.println(i); i++; }")
                            .build();

                    EjercicioGuia e9 = EjercicioGuia.builder()
                            .titulo("Try-Catch básico")
                            .descripcion("Captura una excepción de conversión de String a int")
                            .enunciado("Usa try-catch para convertir una cadena no numérica y manejar NumberFormatException")
                            .codigoTemplate("public class Main {\n    public static void main(String[] args) {\n        String s = \"abc\";\n        // try-catch Integer.parseInt(s)\n    }\n}")
                            .criterioEvaluacion(".*try\\s*\\{.*catch\\s*\\(.*\\).*\\}.*")
                            .dificultad("FACIL")
                            .puntosRecompensa(10)
                            .orden(9)
                            .activo(true)
                            .pistas("Pista: try { Integer.parseInt(s); } catch (NumberFormatException e) { System.out.println(\"Error\"); }")
                            .build();

                    EjercicioGuia e10 = EjercicioGuia.builder()
                            .titulo("Recursión (factorial)")
                            .descripcion("Calcula el factorial de un número usando recursión")
                            .enunciado("Implementa un método recursivo factorial(int n) y muestra factorial(5)")
                            .codigoTemplate("public class Main {\n    static int factorial(int n) { return 1; }\n    public static void main(String[] args) {\n        // System.out.println(factorial(5));\n    }\n}")
                            .criterioEvaluacion(".*factorial\\s*\\(.*\\).*|.*recursive.*")
                            .dificultad("FACIL")
                            .puntosRecompensa(10)
                            .orden(10)
                            .activo(true)
                            .pistas("Pista: factorial(n) = n * factorial(n-1) con caso base n<=1")
                            .build();

                    ejercicioRepo.save(e1);
                    ejercicioRepo.save(e2);
                    ejercicioRepo.save(e3);
                    ejercicioRepo.save(e4);
                    ejercicioRepo.save(e5);
                    ejercicioRepo.save(e6);
                    ejercicioRepo.save(e7);
                    ejercicioRepo.save(e8);
                    ejercicioRepo.save(e9);
                    ejercicioRepo.save(e10);
                }

                System.out.println(">>> [CONFIG-CORE] Estructuras de misiones y ejercicios cargadas con éxito.");
            } else {
                System.out.println(">>> [CONFIG-CORE] Archivos del Data Kernel verificados. Sistema listo.");
            }
        };
    }
}
package com.J.M_CODERS.KinalCodeQuest.config;

import com.J.M_CODERS.KinalCodeQuest.model.entity.AreaTecnica;
import com.J.M_CODERS.KinalCodeQuest.model.entity.Mision;
import com.J.M_CODERS.KinalCodeQuest.repository.AreaTecnicaRepository;
import com.J.M_CODERS.KinalCodeQuest.repository.MisionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializerConfig {

    @Bean
    CommandLineRunner verificarYpoblarBaseDatos(
            AreaTecnicaRepository areaRepo,
            MisionRepository misionRepo) {

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
                System.out.println(">>> [CONFIG-CORE] Estructuras de misiones cargadas con éxito.");
            } else {
                System.out.println(">>> [CONFIG-CORE] Archivos del Data Kernel verificados. Sistema listo.");
            }
        };
    }
}
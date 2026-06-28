CREATE DATABASE IF NOT EXISTS kinal_code_quest;
USE kinal_code_quest;

-- ENTIDADES --

-- 1. Tabla de Jugadores 
CREATE TABLE jugador (
    id_jugador INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,     -- Usado para el login
    password VARCHAR(255) NOT NULL,           -- Hash generado por BCrypt
    nombre_avatar VARCHAR(50) NOT NULL,       -- Nombre visible en la consola
    rol VARCHAR(30) DEFAULT 'ROLE_ESTUDIANTE',-- Para permisos de Spring Security
    experiencia INT DEFAULT 0,
    ptos_responsabilidad INT DEFAULT 0,       -- Recompensa institucional
    ptos_solidaridad INT DEFAULT 0,           -- Recompensa institucional
    ptos_laboriosidad INT DEFAULT 0,          -- Recompensa institucional
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Tabla de Áreas del Campus
CREATE TABLE area_tecnica (
    id_area INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,              -- Ej: "Mecánica", "Informática"
    descripcion_tematica TEXT                 -- Ej: "Taller de motores de combustión interna"
);

-- 3. Tabla de Misiones (El núcleo del juego)
CREATE TABLE mision (
    id_mision INT AUTO_INCREMENT PRIMARY KEY,
    id_area INT NOT NULL,
    titulo VARCHAR(100) NOT NULL,
    descripcion_narrativa TEXT NOT NULL,      -- Ej: "El motor principal falló. Arregla el ciclo FOR."
    codigo_base TEXT NOT NULL,                -- El código incompleto que se carga en la vista
    criterio_evaluacion TEXT NOT NULL,        -- Regex o palabra clave para validar la respuesta
    exp_recompensa INT NOT NULL DEFAULT 100,  -- Cuánta XP da completar esta misión
    FOREIGN KEY (id_area) REFERENCES area_tecnica(id_area)
);

-- 4. Tabla de Progreso
CREATE TABLE progreso_jugador (
    id_progreso INT AUTO_INCREMENT PRIMARY KEY,
    id_jugador INT NOT NULL,
    id_mision INT NOT NULL,
    completada BOOLEAN DEFAULT FALSE,
    intentos INT DEFAULT 0,                   -- Útil para medir la "Laboriosidad"
    ultimo_codigo_enviado TEXT,               -- Guarda el último intento del jugador
    fecha_completado TIMESTAMP NULL,
    FOREIGN KEY (id_jugador) REFERENCES jugador(id_jugador),
    FOREIGN KEY (id_mision) REFERENCES mision(id_mision),
    UNIQUE(id_jugador, id_mision)             -- Evita que el jugador tenga el progreso duplicado
);

-- INSERCIÓN DE DATOS INICIALES PARA LAS ÁREAS TÉCNICAS (NIVELES 1 AL 4) --

INSERT INTO area_tecnica (id_area, nombre, descripcion_tematica) VALUES
(1, 'Sintaxis y Variables Básicas', 'Fundamentos del compilador y manejo de memoria en Stack.'),
(2, 'Estructuras de Control', 'Bifurcaciones lógicas complejas y bucles estructurados.'),
(3, 'Colecciones e Inventarios', 'Manejo dinámico de estructuras ArrayList y HashMap para la gestión del Pañol de Kinal.'),
(4, 'Estructura de Métodos', 'Modularización, firmas de retorno y encapsulamiento de lógica de sistemas.'),
(5, 'Programación Orientada a Objetos', 'Conceptos de Clases, Objetos, Instanciación y Constructores en Java.'),
(6, 'Pilares POO: Herencia y Polimorfismo', 'Reutilización de código mediante herencia (extends) y sobreescritura de métodos (@Override).'),
(7, 'Encapsulamiento y Abstracción', 'Modificadores de acceso (private, public, protected), Getters/Setters, Interfaces y Clases Abstractas.'),
(8, 'Manejo de Excepciones', 'Control de errores en tiempo de ejecución utilizando bloques try-catch-finally y throws.'),
(9, 'Manejo de Archivos e I/O', 'Lectura y escritura de flujos de datos y archivos de texto plano con BufferedReader y PrintWriter.'),
(10, 'Introducción a JDBC y Persistencia', 'Conexión del ecosistema Java hacia bases de datos relacionales mediante controladores JDBC.')
ON DUPLICATE KEY UPDATE 
    nombre = VALUES(nombre), 
    descripcion_tematica = VALUES(descripcion_tematica);
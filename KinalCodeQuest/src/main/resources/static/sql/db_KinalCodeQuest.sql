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
    saldo_responsabilidad INT DEFAULT 0,
    saldo_solidaridad INT DEFAULT 0,
    saldo_laboriosidad INT DEFAULT 0,
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

CREATE TABLE capitulo_historia (
    id_capitulo INT AUTO_INCREMENT PRIMARY KEY,
    id_capitulo_padre INT NULL,
    titulo VARCHAR(150) NOT NULL,
    epoca VARCHAR(50) NULL,
    tipo_contenido VARCHAR(30) NULL,
    contenido_narrativo TEXT NOT NULL,
    ruta_imagen VARCHAR(255) NULL,
    costo_cantidad INT NOT NULL DEFAULT 0,
    tipo_moneda VARCHAR(30) NOT NULL,
    bonus_recompensa VARCHAR(100) NULL,
    FOREIGN KEY (id_capitulo_padre) REFERENCES capitulo_historia(id_capitulo) ON DELETE CASCADE
);

-- 5. Tabla de Progreso de Ejercicios Guía (registro de intentos y completados)
CREATE TABLE IF NOT EXISTS progreso_ejercicio (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_jugador INT NOT NULL,
    id_ejercicio INT NOT NULL,
    completada BOOLEAN DEFAULT FALSE,
    intentos INT DEFAULT 0,
    ultimo_codigo_enviado TEXT,
    fecha_completado TIMESTAMP NULL,
    FOREIGN KEY (id_jugador) REFERENCES jugador(id_jugador) ON DELETE CASCADE
);

CREATE TABLE compra_historia (
    id_compra INT AUTO_INCREMENT PRIMARY KEY,
    id_jugador INT NOT NULL,
    id_capitulo INT NOT NULL,
    fecha_compra DATETIME NOT NULL,

    -- Restricciones de Claves Foráneas para asegurar la integridad lógica
    FOREIGN KEY (id_jugador) REFERENCES jugador(id_jugador) ON DELETE CASCADE,
    FOREIGN KEY (id_capitulo) REFERENCES capitulo_historia(id_capitulo) ON DELETE CASCADE
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

INSERT INTO capitulo_historia (id_capitulo, id_capitulo_padre, titulo, epoca, tipo_contenido, contenido_narrativo, ruta_imagen, costo_cantidad, tipo_moneda, bonus_recompensa) VALUES 
(1, NULL, 'El Cimiento del Núcleo', '1961', 'PRINCIPAL', 'El Centro Educativo Técnico Laboral Kinal nació en 1961 en Guatemala. Su meta fundamental desde su origen ha sido brindar oportunidades de superación técnica y humana a la clase trabajadora.', '/img/historia/Construccion-sede-1989.jpg', 0, 'EXP', NULL),

(2, 1, 'Archivo Visual: Los Primeros Pasos', '1965', 'FOTO_ANTIGUA', 'Fichero recuperado de los archivos confidenciales. Las primeras clases de dibujo técnico y electricidad se estructuraron con recursos limitados pero con un alto estándar de excelencia.', '/img/aniversarios/61-aniversario.jpg', 20, 'RESPONSABILIDAD', '[+10 Ptos Prestigio Logrado]'),

(3, NULL, 'La Consolidación del Servidor', '1989', 'PRINCIPAL', 'En 1989 se concreta el traslado definitivo a la sede actual en la Zona 7 de la Ciudad de Guatemala, expandiendo radicalmente la infraestructura de los laboratorios y talleres tecnológicos.', '/img/historia/Construccion-sede-1989.jpg', 100, 'EXP', NULL),

(4, 3, 'Planos de Infraestructura: Edificio C', '1992', 'FOTO_ANTIGUA', 'Planos estructurales recuperados de los servidores centrales. Captura histórica que documenta las fases de cimentación y levantamiento del icónico Edificio C.', '/img/historia/Construccion-edificio-C-1992.jpg', 40, 'LABORIOSIDAD', '[Módulo de Hardware Desbloqueado]'),

(5, 3, 'Registro de Campo: Bloque de Básicos', '2005', 'FOTO_ANTIGUA', 'Compilación de capturas del área de educación básica técnica del año 2005, mostrando la evolución de los entornos académicos interactivos.', '/img/historia/Construccion-basicos-2005.jpg', 30, 'SOLIDARIDAD', '[+5 Ptos Cooperación Colectiva]'),

(6, NULL, 'La Era Moderna del Core', '2026', 'PRINCIPAL', 'Kinal se transforma en un referente de innovación digital y tecnológica en la región, integrando desarrollo de software de vanguardia y metodologías ágiles avanzadas.', '/img/historia/Entrada-Kinal-actual.jpg', 250, 'EXP', NULL);
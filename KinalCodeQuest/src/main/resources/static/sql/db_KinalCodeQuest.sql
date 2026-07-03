DROP DATABASE IF EXISTS kinal_code_quest;
CREATE DATABASE IF NOT EXISTS kinal_code_quest;
USE kinal_code_quest;

-- =========================================================================
-- ESTRUCTURA DE ENTIDADES
-- =========================================================================

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

-- 4. Tabla de Progreso de Misiones
CREATE TABLE progreso_jugador (
    id_progreso INT AUTO_INCREMENT PRIMARY KEY,
    id_jugador INT NOT NULL,
    id_mision INT NOT NULL,
    completada BOOLEAN DEFAULT FALSE,
    intentos INT DEFAULT 0,                   -- Útil para medir la "Laboriosidad"
    ultimo_codigo_enviado TEXT,               -- Guarda el último intento del jugador
    fecha_completado TIMESTAMP NULL,
    FOREIGN KEY (id_jugador) REFERENCES jugador(id_jugador) ON DELETE CASCADE,
    FOREIGN KEY (id_mision) REFERENCES mision(id_mision) ON DELETE CASCADE,
    UNIQUE(id_jugador, id_mision)             -- Evita duplicados
);

-- 5. Tabla de Capítulos de Historia
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

-- 6. Tabla Maestra de Ejercicios Guía
CREATE TABLE ejercicio_guia (
    id_ejercicio INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(50) NOT NULL,
    descripcion TEXT NOT NULL,
    enunciado TEXT NOT NULL,
    codigo_template TEXT NOT NULL,
    criterio_evaluacion TEXT NOT NULL,
    dificultad VARCHAR(20) NOT NULL,          -- FACIL, INTERMEDIO
    puntos_recompensa INT NOT NULL DEFAULT 10,
    orden INT NOT NULL,                       -- Orden de presentación (1-10)
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    pistas TEXT
);

-- 7. Tabla de Progreso de Ejercicios Guía
CREATE TABLE progreso_ejercicio (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_jugador INT NOT NULL,
    id_ejercicio INT NOT NULL,
    completada BOOLEAN DEFAULT FALSE,
    intentos INT DEFAULT 0,
    ultimo_codigo_enviado TEXT,
    fecha_completado TIMESTAMP NULL,
    FOREIGN KEY (id_jugador) REFERENCES jugador(id_jugador) ON DELETE CASCADE,
    FOREIGN KEY (id_ejercicio) REFERENCES ejercicio_guia(id_ejercicio) ON DELETE CASCADE,
    UNIQUE(id_jugador, id_ejercicio)
);

-- 8. Tabla de Compras de Historia
CREATE TABLE compra_historia (
    id_compra INT AUTO_INCREMENT PRIMARY KEY,
    id_jugador INT NOT NULL,
    id_capitulo INT NOT NULL,
    fecha_compra DATETIME NOT NULL,
    FOREIGN KEY (id_jugador) REFERENCES jugador(id_jugador) ON DELETE CASCADE,
    FOREIGN KEY (id_capitulo) REFERENCES capitulo_historia(id_capitulo) ON DELETE CASCADE
);


-- =========================================================================
-- INSERCIÓN DE DATOS INICIALES
-- =========================================================================

-- Áreas Técnicas (Niveles del 1 al 10)
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
ON DUPLICATE KEY UPDATE nombre = VALUES(nombre), descripcion_tematica = VALUES(descripcion_tematica);

-- Misión Inicial por Defecto (Evita el crash del compilador standalone)
INSERT INTO mision (id_mision, id_area, titulo, descripcion_narrativa, codigo_base, criterio_evaluacion, exp_recompensa) VALUES
(1, 1, 'Consola Standalone Activa', 'Consola de compilación general lista para pruebas.', '// Escribe tu código aquí', '.*', 0)
ON DUPLICATE KEY UPDATE titulo=VALUES(titulo);

-- Fusión Cronológica de Capítulos de Historia (Ordenados y con IDs únicos)
INSERT INTO capitulo_historia (id_capitulo, id_capitulo_padre, titulo, epoca, tipo_contenido, contenido_narrativo, ruta_imagen, costo_cantidad, tipo_moneda, bonus_recompensa) VALUES 
(1, NULL, 'El Cimiento del Núcleo', '1961', 'PRINCIPAL', 'El Centro Educativo Técnico Laboral Kinal nació en 1961 en Guatemala, gracias a la iniciativa de profesionales motivados por las enseñanzas de San Josemaría Escrivá de Balaguer. Su meta fundamental desde su origen ha sido brindar oportunidades de superación técnica y humana a la clase trabajadora.', '/img/historia/Construccion-sede-1989.jpg', 0, 'EXP', NULL),
(2, 1, 'Archivo Visual: Los Primeros Pasos', '1965', 'FOTO_ANTIGUA', 'Fichero recuperado de los archivos confidenciales. Las primeras clases de dibujo técnico y electricidad se estructuraron con recursos limitados pero con un alto estándar de excelencia.', '/img/aniversarios/61-aniversario.jpg', 20, 'RESPONSABILIDAD', '[+10 Ptos Prestigio Logrado]'),
(3, NULL, 'Migración de Datos y Expansión', '1970', 'PRINCIPAL', 'Durante las décadas de los 70 y 80, la Fundación expandió sus programas de formación acelerada para adultos trabajadores. Fue en esta era donde se consolidaron los pilares formativos tradicionales.', NULL, 100, 'EXP', NULL),
(4, 3, 'Código de Honor: Laboriosidad', '1975', 'VALOR_INSTITUCIONAL', 'Protocolo ético: El trabajo diario no es solo una carga, sino un medio para alcanzar la excelencia humana, santificar el entorno y desarrollar la infraestructura de Guatemala.', NULL, 25, 'LABORIOSIDAD', '[Análisis de Integridad Completado]'),
(5, NULL, 'La Consolidación del Servidor', '1989', 'PRINCIPAL', 'En 1989 se concreta el traslado definitivo a la sede actual en la Zona 7 de la Ciudad de Guatemala, expandiendo radicalmente la infraestructura de los laboratorios y talleres tecnológicos.', '/img/historia/Construccion-sede-1989.jpg', 100, 'EXP', NULL),
(6, 5, 'Planos de Infraestructura: Edificio C', '1992', 'FOTO_ANTIGUA', 'Planos estructurales recuperados de los servidores centrales. Captura histórica que documenta las fases de cimentación y levantamiento del icónico Edificio C.', '/img/historia/Construccion-edificio-C-1992.jpg', 40, 'LABORIOSIDAD', '[Módulo de Hardware Desbloqueado]'),
(7, 5, 'Registro de Campo: Bloque de Básicos', '2005', 'FOTO_ANTIGUA', 'Compilación de capturas del área de educación básica técnica del año 2005, mostrando la evolución de los entornos académicos interactivos.', '/img/historia/Construccion-basicos-2005.jpg', 30, 'SOLIDARIDAD', '[+5 Ptos Cooperación Colectiva]'),
(8, NULL, 'La Era Moderna del Core', '2026', 'PRINCIPAL', 'Kinal se transforma en un referente de innovación digital y tecnológica en la región, integrando desarrollo de software de vanguardia y metodologías ágiles avanzadas.', '/img/historia/Entrada-Kinal-actual.jpg', 250, 'EXP', NULL);

-- Ejercicios Guía de Java
INSERT INTO ejercicio_guia (id_ejercicio, orden, titulo, descripcion, enunciado, codigo_template, criterio_evaluacion, dificultad, puntos_recompensa, activo, pistas) VALUES
(1, 1, 'Hello World', 'Imprime en consola', 'Imprime el texto HOLA KINAL', 'public class Main {\n    public static void main(String[] args) {\n        System.out.println(\"HOLA KINAL\");\n    }\n}', 'HOLA KINAL', 'FACIL', 10, TRUE, 'Usa System.out.println'),
(2, 2, 'Suma básica', 'Operaciones aritméticas', 'Suma dos números fijos', 'public class Calculator {\n    public static void main(String[] args) {\n        int num1 = 15;\n        int num2 = 7;\n        System.out.println(\"Suma: \" + (num1 + num2));\n    }\n}', 'Suma: 22', 'FACIL', 10, TRUE, 'Suma num1 y num2'),
(3, 3, 'Bucle For', 'Ciclos estructurados', 'Imprime del 1 al 5', 'public class ForLoop {\n    public static void main(String[] args) {\n        for (int i = 1; i <= 5; i++) {\n            System.out.println(i);\n        }\n    }\n}', '1.*2.*3.*4.*5', 'FACIL', 10, TRUE, 'Usa un iterador int i = 1'),
(4, 4, 'If-Else', 'Condicionales', 'Verifica si un número es par', 'public class Conditional {\n    public static void main(String[] args) {\n        int n = 4;\n        if (n % 2 == 0) {\n            System.out.println(\"Par\");\n        } else {\n            System.out.println(\"Impar\");\n        }\n    }\n}', 'Par', 'FACIL', 10, TRUE, 'El operador % calcula el residuo'),
(5, 5, 'Array básico', 'Estructuras indexadas', 'Recorre un array de números', 'public class ArrayExample {\n    public static void main(String[] args) {\n        int[] numeros = {10,20,30};\n        for (int num : numeros) System.out.println(num);\n    }\n}', '10.*20.*30', 'FACIL', 10, TRUE, 'Usa un ciclo for-each'),
(6, 6, 'Método simple', 'Modularización', 'Invoca una función de suma', 'public class Methods {\n    static int sumar(int a, int b) { return a + b; }\n    public static void main(String[] args) {\n        System.out.println(sumar(5,3));\n    }\n}', '8', 'INTERMEDIO', 10, TRUE, 'Llama al método pasándole parámetros'),
(7, 7, 'ArrayList básico', 'Colecciones dinámicas', 'Agrega frutas a una lista', 'import java.util.ArrayList;\npublic class ListExample {\n    public static void main(String[] args) {\n        ArrayList<String> frutas = new ArrayList<>();\n        frutas.add(\"Manzana\");\n        frutas.add(\"Banana\");\n        System.out.println(frutas);\n    }\n}', 'Manzana.*Banana', 'INTERMEDIO', 10, TRUE, 'Las colecciones dinámicas usan el método .add()'),
(8, 8, 'While Loop', 'Ciclos condicionales', 'Cuenta hasta 3 con while', 'public class WhileLoop {\n    public static void main(String[] args) {\n        int i = 0;\n        while (i <= 3) {\n            System.out.println(i); i++;\n        }\n    }\n}', '0.*1.*2.*3', 'FACIL', 10, TRUE, 'Asegúrate de incrementar la variable de control'),
(9, 9, 'Try-Catch básico', 'Manejo de excepciones', 'Captura un error de formato', 'public class ErrorHandling {\n    public static void main(String[] args) {\n        try { Integer.parseInt(\"abc\"); } catch (NumberFormatException e) { System.out.println(\"Error de conversión\"); }\n    }\n}', 'Error de conversión', 'INTERMEDIO', 10, TRUE, 'El bloque catch atrapa errores en tiempo de ejecución'),
(10, 10, 'Recursión (factorial)', 'Algoritmia avanzada', 'Calcula el factorial de 5', 'public class Recursion {\n    static int factorial(int n) { if (n <= 1) return 1; return n * factorial(n-1); }\n    public static void main(String[] args) { System.out.println(factorial(5)); }\n}', '120', 'INTERMEDIO', 10, TRUE, 'La recursión requiere un caso base estructural');

-- Ajustes de Criterios de Evaluación Específicos
UPDATE ejercicio_guia SET criterio_evaluacion = 'Suma:' WHERE id_ejercicio = 2;
UPDATE ejercicio_guia SET criterio_evaluacion = 'for' WHERE id_ejercicio = 3;
UPDATE ejercicio_guia SET criterio_evaluacion = 'sumar' WHERE id_ejercicio = 6;
UPDATE ejercicio_guia SET criterio_evaluacion = 'while' WHERE id_ejercicio = 8;
UPDATE ejercicio_guia SET criterio_evaluacion = 'factorial' WHERE id_ejercicio = 10;

-- cuenta administrador:
INSERT INTO jugador (
    username, 
    password, 
    nombre_avatar, 
    rol, 
    ptos_laboriosidad, 
    ptos_responsabilidad, 
    ptos_solidaridad, 
    saldo_laboriosidad, 
    saldo_responsabilidad, 
    saldo_solidaridad,
    experiencia,
    fecha_registro
) VALUES (
    'admin', 
    'admin123', -- Contraseña encriptada: admin123
    'Cyborg_Inft', 
    'ADMIN', -- Rol con privilegios elevados en el sistema
    9999,    -- Puntos de Laboriosidad al máximo
    9999,    -- Puntos de Responsabilidad al máximo
    9999,    -- Puntos de Solidaridad al máximo
    9999,    -- Saldos iniciales en caso de uso residual
    9999, 
    9999,
    9999,    -- Experiencia máxima
    NOW()
);

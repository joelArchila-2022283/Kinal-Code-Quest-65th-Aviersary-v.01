-- Flyway migration: crear tabla progreso_ejercicio
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


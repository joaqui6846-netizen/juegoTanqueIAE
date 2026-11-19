-- Script de configuración para el Juego de Tanques (IAE)
-- Ejecutar con: sudo mariadb < setup_db.sql

-- 1. Crear base de datos
CREATE DATABASE IF NOT EXISTS prueba_java CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE prueba_java;

-- 2. Crear tabla de usuarios con soporte para GestorPuntajes y LectorBaseDeDatos
CREATE TABLE IF NOT EXISTS usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    correo VARCHAR(100) DEFAULT '',
    puntaje INT DEFAULT 0
);

-- 3. Limpiar e insertar datos de ejemplo para visualizar en el ranking
DELETE FROM usuarios;
INSERT INTO usuarios (nombre, correo, puntaje) VALUES 
('Mario', 'mario@liceo.edu.uy', 15),
('Jugador 1', 'j1@iae.edu.uy', 10),
('IAE Pro', 'bot@iae.edu.uy', 8),
('TanqueX', 'tanque@iae.edu.uy', 5);

-- 4. Habilitar permisos para que la aplicación Java pueda conectar sin contraseña vía TCP (localhost y 127.0.0.1)
GRANT ALL PRIVILEGES ON prueba_java.* TO 'root'@'localhost' IDENTIFIED BY '';
GRANT ALL PRIVILEGES ON prueba_java.* TO 'root'@'127.0.0.1' IDENTIFIED BY '';
FLUSH PRIVILEGES;

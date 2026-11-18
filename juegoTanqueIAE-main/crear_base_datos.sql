-- ============================================================================
-- SCRIPT DE CREACIÓN Y CONFIGURACIÓN DE BASE DE DATOS
-- Proyecto: Combate de Tanques Java (IAE / UTU / Secundaria)
-- Motor compatible: MariaDB 10.x / 11.x / 12.x / 13.x o MySQL 8.x
-- ============================================================================

-- 1. Crear la base de datos si no existe y seleccionarla
CREATE DATABASE IF NOT EXISTS prueba_java 
    CHARACTER SET utf8mb4 
    COLLATE utf8mb4_unicode_ci;

USE prueba_java;

-- 2. Crear la tabla 'usuarios' requerida por GestorPuntajes y LectorBaseDeDatos
CREATE TABLE IF NOT EXISTS usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    correo VARCHAR(100) DEFAULT '',
    puntaje INT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 3. Cargar datos de prueba para visualizar en el ranking del menú
-- (Si la tabla ya tiene datos, no los duplica)
INSERT INTO usuarios (nombre, correo, puntaje)
SELECT 'Mario', 'mario@liceo.edu.uy', 15
WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE nombre = 'Mario');

INSERT INTO usuarios (nombre, correo, puntaje)
SELECT 'Jugador 1', 'j1@iae.edu.uy', 10
WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE nombre = 'Jugador 1');

INSERT INTO usuarios (nombre, correo, puntaje)
SELECT 'IAE Pro', 'bot@iae.edu.uy', 8
WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE nombre = 'IAE Pro');

INSERT INTO usuarios (nombre, correo, puntaje)
SELECT 'TanqueX', 'tanque@iae.edu.uy', 5
WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE nombre = 'TanqueX');

-- 4. Permisos de conexión para la aplicación Java (usuario: root sin contraseña)
-- Nota: En XAMPP (Windows) esto viene por defecto. En Linux asegura el acceso TCP.
GRANT ALL PRIVILEGES ON prueba_java.* TO 'root'@'localhost' IDENTIFIED BY '';
GRANT ALL PRIVILEGES ON prueba_java.* TO 'root'@'127.0.0.1' IDENTIFIED BY '';
FLUSH PRIVILEGES;

#!/usr/bin/env bash
# Script para compilar y ejecutar el Juego de Tanques
DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$DIR"

JAR_PATH="../mysql-connector-j-26.10.0.jar"

echo "Compilando clases Java..."
javac -cp "$JAR_PATH:." *.java

if [ $? -eq 0 ]; then
    echo "Iniciando Combate de Tanques..."
    java -cp "$JAR_PATH:." JuegoTanques
else
    echo "Error al compilar el proyecto."
fi

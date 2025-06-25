#!/bin/bash

echo "🔄 Reorganizando estructura de archivos Java..."

mkdir -p src/controlador
mkdir -p src/modelo
mkdir -p src/vista

# Mover archivos según función
mv src/Main.java src/controlador/
mv src/UI.java src/controlador/

mv src/Puzzle.java src/modelo/
mv src/Player.java src/modelo/
mv src/ScoreManager.java src/modelo/
mv src/MoveSuggester.java src/modelo/
mv src/PuzzleSolver.java src/modelo/

mv src/PuzzleGUI.java src/vista/

echo "✅ Archivos organizados:"
tree src
PROYECTO FINAL – 8-PUZZLE
Lenguaje: Java
Desarrollado por: v1000

===============================
 DESCRIPCIÓN GENERAL
===============================

Este proyecto implementa el clásico juego del 8-Puzzle con menú por consola y ejecución en modo gráfico o terminal.

Modos de juego disponibles:

1. MODO MANUAL (Gráfico)
   - El usuario mueve fichas haciendo clic.
   - Genera tableros aleatorios.
   - Valida movimientos según las reglas del juego.

2. MODO INTELIGENTE (Gráfico)
   - El usuario ingresa el estado inicial y el estado meta.
   - Usa el algoritmo A* para resolver el puzzle.
   - Muestra paso a paso la solución mediante animación.

3. MODO MANUAL (Terminal)
   - El jugador mueve fichas escribiendo comandos (`arriba`, `abajo`, etc.).
   - El tablero se muestra en consola.
   - Soporte para movimientos válidos y finalización cuando se resuelve.

4. MODO INTELIGENTE (Terminal)
   - Entrada del estado inicial y meta por consola.
   - El sistema resuelve el puzzle con A* y muestra los pasos por texto.


===============================
 CARACTERÍSTICAS TÉCNICAS
===============================

- Programación Orientada a Objetos (POO): encapsulación, herencia, modularidad.
- Algoritmo de búsqueda A* (heurística de Manhattan).
- Visualización gráfica usando Java Swing.
- Uso de estructuras de datos: `ArrayList`, `HashSet`, `PriorityQueue`.
- Registro de puntuaciones en archivo (`scores.txt`) con alias, puntaje y fecha.
- Ranking ordenado automáticamente (de mayor a menor).
- Interfaz amigable vía menú por consola.

===============================
 ESTRUCTURA DE CARPETAS
===============================

PERSONA8/
│
├── src/                    → Código fuente en Java
│   ├── Main.java           → Punto de entrada del programa
│   ├── UI.java             → Menú principal y controlador de modos
│   ├── Puzzle.java         → Lógica del juego en modo manual
│   ├── PuzzleSolver.java   → Lógica del modo inteligente (A*)
│   ├── PuzzleGUI.java      → Interfaz gráfica para mostrar el tablero
│   ├── MoveSuggester.java  → Heurística para sugerir movimientos en modo manual
│   ├── Player.java         → Clase para representar jugadores
│   ├── ScoreManager.java   → Carga/guarda el ranking en archivo
│
├── data/
│   └── scores.txt          → Archivo de texto con historial de puntuaciones
│
└── Readme.txt              → Este archivo con las instrucciones

===============================
 INSTRUCCIONES DE COMPILACIÓN
===============================

1. Asegúrate de tener Java instalado (JDK 8 o superior).
2. Abre una terminal y compila el proyecto:

   > javac -d bin src/*.java

3. Ejecuta el programa con:

   > java -cp bin Main

===============================
 REQUISITOS DEL SISTEMA
===============================

- Sistema operativo: Windows, Linux o macOS
- Java Development Kit (JDK)
- Terminal/Consola para ejecutar el programa
- No requiere conexión a internet

===============================
 OBSERVACIONES
===============================

- La solución automática puede tardar más si el tablero inicial está muy desordenado.
- El archivo `scores.txt` se actualizará automáticamente tras cada partida con el alias del jugador.
- Las opciones gráficas utilizan Swing; si usas WSL o un entorno sin soporte de ventanas, usa las opciones por terminal.
- El programa valida la entrada del usuario para evitar configuraciones inválidas del tablero.
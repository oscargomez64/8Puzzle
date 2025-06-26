// UI.java
// Interfaz del juego 8-Puzzle con menú por terminal y ejecución gráfica
// Proyecto por Oscar Gomez y Uriel Ortiz

import java.util.Scanner;
import javax.swing.*;

public class UI {
    private Scanner scanner;             // Para leer entrada del usuario desde consola
    private ScoreManager scoreManager;  // Gestión de puntuaciones

    // Constructor que inicializa el scanner y el manejador de puntuaciones
    public UI() {
        scanner = new Scanner(System.in);
        scoreManager = new ScoreManager("data/scores.txt"); // Archivo para guardar puntuaciones
    }

    // Muestra el menú principal y controla el flujo del juego por terminal
    public void mostrarMenuPrincipal() {
        int opcion;

        do {
            System.out.println("\n=== Juego 8-Puzzle ===");
            System.out.println("1. Jugar en Modo Manual (Gráfico)");
            System.out.println("2. Jugar en Modo Inteligente (Gráfico)");
            System.out.println("3. Ver puntuaciones");
            System.out.println("4. Salir");
            System.out.print("Selecciona una opción: ");

            // Intenta leer y convertir la opción a entero, si falla asigna -1
            try {
                opcion = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                opcion = -1;
            }

            // Decide acción según la opción seleccionada
            switch (opcion) {
                case 1:
                    iniciarModoManualGrafico();
                    break;
                case 2:
                    iniciarModoInteligenteGrafico();
                    break;
                case 3:
                    mostrarPuntuaciones();
                    break;
                case 4:
                    System.out.println("Gracias por jugar. ¡Hasta luego!");
                    break;
                default:
                    System.out.println("Opción inválida. Intenta de nuevo.");
            }

        } while (opcion != 4); // Repetir hasta que el usuario decida salir
    }

    // Método para iniciar el modo manual con interfaz gráfica Swing
    private void iniciarModoManualGrafico() {
        System.out.println("\n--- Modo Manual (Gráfico) ---");

        int nivel = 1; // Nivel de dificultad por defecto
        try {
            // Solicita al usuario seleccionar nivel de dificultad
            System.out.println("Selecciona nivel de dificultad:");
            System.out.println("1. Fácil");
            System.out.println("2. Medio");
            System.out.println("3. Difícil");
            System.out.print("Opción: ");
            nivel = Integer.parseInt(scanner.nextLine());

            // Si la opción no es válida, se usa nivel fácil por defecto
            if (nivel < 1 || nivel > 3) nivel = 1;
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. Se usará dificultad Fácil.");
            nivel = 1;
        }

        // Crea el puzzle y genera tablero aleatorio según nivel de dificultad
        Puzzle puzzle = new Puzzle();
        puzzle.generarTableroAleatorio(nivel);

        // Ejecuta la interfaz gráfica en el hilo de eventos Swing
        SwingUtilities.invokeLater(() -> {
            JFrame ventana = new JFrame("8-Puzzle - Modo Manual por Oscar Gomez y Uriel Ortiz");
            ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            ventana.setResizable(false);
            ventana.add(new PuzzleGUI(puzzle)); // Añade el panel gráfico con el puzzle
            ventana.pack();
            ventana.setLocationRelativeTo(null); // Centrar ventana en pantalla
            ventana.setVisible(true);
        });
    }

    // Método para iniciar el modo inteligente con interfaz gráfica Swing
    private void iniciarModoInteligenteGrafico() {
        System.out.println("\n--- Modo Inteligente (Gráfico) ---");

        PuzzleSolver solver = new PuzzleSolver();
        solver.configurar(scanner);  // El usuario ingresa estados inicial y meta por consola

        // Se crea un puzzle con el estado inicial configurado
        Puzzle puzzle = new Puzzle(solver.getEstadoInicialClonado());

        // Ejecuta la interfaz gráfica en el hilo de eventos Swing
        SwingUtilities.invokeLater(() -> {
            PuzzleGUI panel = new PuzzleGUI(puzzle);
            JFrame ventana = new JFrame("8-Puzzle - Modo Inteligente por Oscar Gomez y Uriel Ortiz");
            ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            ventana.setResizable(false);
            ventana.add(panel);
            ventana.pack();
            ventana.setLocationRelativeTo(null);
            ventana.setVisible(true);

            solver.setEstadoMeta(puzzle.generarMeta()); // Define el estado meta estándar (ordenado)
            solver.resolverAutomaticamente(puzzle, panel); // Resuelve y anima la solución
        });
    }

    // Método para mostrar las puntuaciones guardadas en consola
    private void mostrarPuntuaciones() {
        System.out.println("\n--- Puntuaciones ---");
        scoreManager.mostrarPuntuaciones();
    }
}

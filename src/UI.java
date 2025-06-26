// UI.java
// Interfaz del juego 8-Puzzle con menú por terminal y ejecución gráfica
// Proyecto por v1000

import java.util.Scanner;
import javax.swing.*;

public class UI {
    private Scanner scanner;
    private ScoreManager scoreManager;

    public UI() {
        scanner = new Scanner(System.in);
        scoreManager = new ScoreManager("data/scores.txt");
    }

    public void mostrarMenuPrincipal() {
        int opcion;

        do {
            System.out.println("\n=== Juego 8-Puzzle ===");
            System.out.println("1. Jugar en Modo Manual (Gráfico)");
            System.out.println("2. Jugar en Modo Inteligente (Gráfico)");
            System.out.println("3. Ver puntuaciones");
            System.out.println("4. Salir");
            System.out.print("Selecciona una opción: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                opcion = -1;
            }

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

        } while (opcion != 4);
    }

    private void iniciarModoManualGrafico() {
    System.out.println("\n--- Modo Manual (Gráfico) ---");

    int nivel = 1;
    try {
        System.out.println("Selecciona nivel de dificultad:");
        System.out.println("1. Fácil");
        System.out.println("2. Medio");
        System.out.println("3. Difícil");
        System.out.print("Opción: ");
        nivel = Integer.parseInt(scanner.nextLine());
        if (nivel < 1 || nivel > 3) nivel = 1;
    } catch (NumberFormatException e) {
        System.out.println("Entrada inválida. Se usará dificultad Fácil.");
        nivel = 1;
    }

    Puzzle puzzle = new Puzzle();
    puzzle.generarTableroAleatorio(nivel); // método sobrecargado con dificultad

    SwingUtilities.invokeLater(() -> {
        JFrame ventana = new JFrame("8-Puzzle - Modo Manual por v1000");
        ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ventana.setResizable(false);
        ventana.add(new PuzzleGUI(puzzle));
        ventana.pack();
        ventana.setLocationRelativeTo(null);
        ventana.setVisible(true);
    });
}


    private void iniciarModoInteligenteGrafico() {
        System.out.println("\n--- Modo Inteligente (Gráfico) ---");

        PuzzleSolver solver = new PuzzleSolver();
        solver.configurar(scanner);  // el usuario define los estados

        Puzzle puzzle = new Puzzle(solver.getEstadoInicialClonado());  // usamos el estado inicial configurado

        SwingUtilities.invokeLater(() -> {
            PuzzleGUI panel = new PuzzleGUI(puzzle);
            JFrame ventana = new JFrame("8-Puzzle - Modo Inteligente por v1000");
            ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            ventana.setResizable(false);
            ventana.add(panel);
            ventana.pack();
            ventana.setLocationRelativeTo(null);
            ventana.setVisible(true);

            solver.setEstadoMeta(puzzle.generarMeta()); // usamos el estado meta estándar
            solver.resolverAutomaticamente(puzzle, panel);
        });
    }

    private void mostrarPuntuaciones() {
        System.out.println("\n--- Puntuaciones ---");
        scoreManager.mostrarPuntuaciones();
    }
}
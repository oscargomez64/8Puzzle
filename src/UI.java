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
            System.out.println("3. Jugar en Modo Manual (Terminal)");
            System.out.println("4. Jugar en Modo Inteligente (Terminal)");
            System.out.println("5. Ver puntuaciones");
            System.out.println("6. Salir");
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
                    iniciarModoManualTerminal();
                    break;
                case 4:
                    iniciarModoInteligenteTerminal();
                    break;
                case 5:
                    mostrarPuntuaciones();
                    break;
                case 6:
                    System.out.println("Gracias por jugar. ¡Hasta luego!");
                    break;
                default:
                    System.out.println("Opción inválida. Intenta de nuevo.");
            }

        } while (opcion != 6);
    }

    private void iniciarModoManualGrafico() {
        System.out.println("\n--- Modo Manual (Gráfico) ---");
        Puzzle puzzle = new Puzzle();
        puzzle.generarTableroAleatorio();

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
            solver.resolverAutomaticamente(puzzle, panel); // ✅ ahora enviamos ambos: puzzle y panel
        });
    }

    private void iniciarModoManualTerminal() {
        System.out.println("\n--- Modo Manual (Terminal) ---");
        Puzzle puzzle = new Puzzle();
        puzzle.generarTableroAleatorio();
        puzzle.jugarConUsuario(scanner);
    }

    private void iniciarModoInteligenteTerminal() {
        System.out.println("\n--- Modo Inteligente (Terminal) ---");
        PuzzleSolver solver = new PuzzleSolver();
        solver.configurar(scanner);
        solver.resolverPuzzle();
    }

    private void mostrarPuntuaciones() {
        System.out.println("\n--- Puntuaciones ---");
        scoreManager.mostrarPuntuaciones();
    }
}

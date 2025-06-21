// Puzzle.java
// Lógica del juego 8-Puzzle en modo manual
// Proyecto por v1000

import java.util.*;

public class Puzzle {
    private int[][] tablero;
    private final int TAM = 3;
    private int filaVacia, colVacia;

    public Puzzle() {
        tablero = new int[TAM][TAM];
    }

    public Puzzle(int[][] estadoInicial) {
        tablero = new int[TAM][TAM];
        for (int i = 0; i < TAM; i++) {
            tablero[i] = Arrays.copyOf(estadoInicial[i], TAM);
            for (int j = 0; j < TAM; j++) {
                if (tablero[i][j] == 0) {
                    filaVacia = i;
                    colVacia = j;
                }
            }
        }
    }

    public void setTablero(int[][] nuevoTablero) {
        for (int i = 0; i < TAM; i++) {
            tablero[i] = Arrays.copyOf(nuevoTablero[i], TAM);
            for (int j = 0; j < TAM; j++) {
                if (tablero[i][j] == 0) {
                    filaVacia = i;
                    colVacia = j;
                }
            }
        }
    }

    public void generarTableroAleatorio() {
        List<Integer> numeros = new ArrayList<>();
        for (int i = 0; i < TAM * TAM; i++) numeros.add(i);
        do {
            Collections.shuffle(numeros);
        } while (!esSolucionable(numeros));

        int index = 0;
        for (int i = 0; i < TAM; i++) {
            for (int j = 0; j < TAM; j++) {
                tablero[i][j] = numeros.get(index++);
                if (tablero[i][j] == 0) {
                    filaVacia = i;
                    colVacia = j;
                }
            }
        }
        mostrarTablero();
    }

    public void jugarConUsuario(Scanner scanner) {
        String input;
        MoveSuggester sugeridor = new MoveSuggester();

        do {
            System.out.print("Mover ficha (arriba, abajo, izquierda, derecha, sugerencia, resolver o salir): ");
            input = scanner.nextLine().toLowerCase();

            switch (input) {
                case "sugerencia":
                    String sugerido = sugeridor.sugerirMovimiento(getTablero());
                    if (sugerido != null) {
                        System.out.println("➡️  Sugerencia: mueve " + sugerido);
                    } else {
                        System.out.println("No se puede sugerir un movimiento.");
                    }
                    break;

                case "resolver":
                    PuzzleSolver solver = new PuzzleSolver();
                    solver.setEstadoInicial(getTablero());
                    solver.setEstadoMeta(generarMeta());

                    // GUI animada (desde consola)
                    javax.swing.SwingUtilities.invokeLater(() -> {
                        PuzzleGUI panel = new PuzzleGUI(this);
                        javax.swing.JFrame ventana = new javax.swing.JFrame("Resolviendo...");
                        ventana.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);
                        ventana.setResizable(false);
                        ventana.add(panel);
                        ventana.pack();
                        ventana.setLocationRelativeTo(null);
                        ventana.setVisible(true);
                        solver.resolverAutomaticamente(this, panel); // animación
                    });
                    return; // Salir del bucle para evitar múltiples ejecuciones

                case "arriba":
                case "abajo":
                case "izquierda":
                case "derecha":
                    if (mover(input)) {
                        mostrarTablero();
                        if (esMeta()) {
                            System.out.println("🎉 ¡Felicidades, resolviste el puzzle!");
                            System.out.print("Introduce tu alias: ");
                            String alias = scanner.nextLine();
                            ScoreManager scoreManager = new ScoreManager("data/scores.txt");
                            scoreManager.registrarPuntuacion(alias, 100);
                            return;
                        }
                    } else {
                        System.out.println("Movimiento inválido.");
                    }
                    break;

                case "salir":
                    break;

                default:
                    System.out.println("Comando no reconocido.");
            }
        } while (!input.equals("salir"));
    }

    public boolean mover(String direccion) {
        int nuevaFila = filaVacia;
        int nuevaCol = colVacia;

        switch (direccion) {
            case "arriba":    nuevaFila++; break;
            case "abajo":     nuevaFila--; break;
            case "izquierda": nuevaCol++; break;
            case "derecha":   nuevaCol--; break;
            default: return false;
        }

        if (esPosicionValida(nuevaFila, nuevaCol)) {
            tablero[filaVacia][colVacia] = tablero[nuevaFila][nuevaCol];
            tablero[nuevaFila][nuevaCol] = 0;
            filaVacia = nuevaFila;
            colVacia = nuevaCol;
            return true;
        }
        return false;
    }

    private boolean esPosicionValida(int fila, int col) {
        return fila >= 0 && fila < TAM && col >= 0 && col < TAM;
    }

    public void mostrarTablero() {
        System.out.println("-------------");
        for (int i = 0; i < TAM; i++) {
            for (int j = 0; j < TAM; j++) {
                System.out.print("| ");
                if (tablero[i][j] == 0) {
                    System.out.print("  ");
                } else {
                    System.out.print(tablero[i][j] + " ");
                }
            }
            System.out.println("|");
        }
        System.out.println("-------------");
    }

    public boolean esMeta() {
        int contador = 1;
        for (int i = 0; i < TAM; i++) {
            for (int j = 0; j < TAM; j++) {
                if (i == TAM - 1 && j == TAM - 1) {
                    return tablero[i][j] == 0;
                }
                if (tablero[i][j] != contador++) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean esSolucionable(List<Integer> lista) {
        int inversions = 0;
        for (int i = 0; i < lista.size(); i++) {
            for (int j = i + 1; j < lista.size(); j++) {
                int a = lista.get(i);
                int b = lista.get(j);
                if (a != 0 && b != 0 && a > b) inversions++;
            }
        }
        return inversions % 2 == 0;
    }

    public int[][] getTablero() {
        return tablero;
    }

    public int[][] generarMeta() {
        int[][] meta = new int[TAM][TAM];
        int contador = 1;
        for (int i = 0; i < TAM; i++) {
            for (int j = 0; j < TAM; j++) {
                if (i == TAM - 1 && j == TAM - 1)
                    meta[i][j] = 0;
                else
                    meta[i][j] = contador++;
            }
        }
        return meta;
    }
}

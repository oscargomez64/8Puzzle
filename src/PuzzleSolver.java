// PuzzleSolver.java
// Modo inteligente del juego 8-Puzzle con A*
// Proyecto por v1000

import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.Timer;

public class PuzzleSolver {
    private final int TAM = 3;

    private int[][] estadoInicial;
    private int[][] estadoMeta;

    public void configurar(Scanner scanner) {
        System.out.println("Introduce el estado inicial (9 números del 0 al 8 separados por espacio):");
        estadoInicial = leerEstadoDesdeUsuario(scanner);

        System.out.println("Introduce el estado meta (9 números del 0 al 8 separados por espacio):");
        estadoMeta = leerEstadoDesdeUsuario(scanner);
    }

    public void resolverPuzzle() {
        Nodo inicio = new Nodo(estadoInicial, null, 0, calcularHeuristica(estadoInicial));
        PriorityQueue<Nodo> abierta = new PriorityQueue<>(Comparator.comparingInt(n -> n.costoTotal));
        Set<String> visitados = new HashSet<>();

        abierta.add(inicio);

        while (!abierta.isEmpty()) {
            Nodo actual = abierta.poll();

            if (Arrays.deepEquals(actual.estado, estadoMeta)) {
                mostrarSolucion(actual);
                return;
            }

            String estadoClave = Arrays.deepToString(actual.estado);
            if (visitados.contains(estadoClave)) continue;
            visitados.add(estadoClave);

            for (Nodo sucesor : generarSucesores(actual)) {
                if (!visitados.contains(Arrays.deepToString(sucesor.estado))) {
                    abierta.add(sucesor);
                }
            }
        }

        System.out.println("No se encontró solución.");
    }

    public int[][] getEstadoInicialClonado() {
        return copiarMatriz(estadoInicial);
    }

    public void setEstadoInicial(int[][] estado) {
        this.estadoInicial = copiarMatriz(estado);
    }

    public void setEstadoMeta(int[][] meta) {
        this.estadoMeta = copiarMatriz(meta);
    }

    public void resolverAutomaticamente(Puzzle puzzle, JPanel panel) {
        Nodo inicio = new Nodo(copiarMatriz(puzzle.getTablero()), null, 0, calcularHeuristica(puzzle.getTablero()));
        PriorityQueue<Nodo> abierta = new PriorityQueue<>(Comparator.comparingInt(n -> n.costoTotal));
        Set<String> visitados = new HashSet<>();
        abierta.add(inicio);

        while (!abierta.isEmpty()) {
            Nodo actual = abierta.poll();
            if (Arrays.deepEquals(actual.estado, estadoMeta)) {
                animarSolucion(actual, puzzle, panel);
                return;
            }

            String clave = Arrays.deepToString(actual.estado);
            if (visitados.contains(clave)) continue;
            visitados.add(clave);

            for (Nodo sucesor : generarSucesores(actual)) {
                if (!visitados.contains(Arrays.deepToString(sucesor.estado))) {
                    abierta.add(sucesor);
                }
            }
        }

        JOptionPane.showMessageDialog(panel, "No se encontró solución.");
    }

private void animarSolucion(Nodo nodo, Puzzle puzzle, JPanel panel) {
    List<Nodo> camino = new ArrayList<>();
    while (nodo != null) {
        camino.add(nodo);
        nodo = nodo.padre;
    }
    Collections.reverse(camino);

    Timer timer = new Timer(500, new AbstractAction() {
        int i = 0;
        @Override
        public void actionPerformed(ActionEvent e) {
            if (i < camino.size()) {
                puzzle.setTablero(camino.get(i++).estado);
                panel.repaint();
            } else {
                ((Timer) e.getSource()).stop();
                JOptionPane.showMessageDialog(panel, "🎉 Puzzle resuelto automáticamente.");

                // Pedir alias y guardar puntuación
                String alias = JOptionPane.showInputDialog(panel, "Introduce tu alias:");
                if (alias != null && !alias.trim().isEmpty()) {
                    ScoreManager scoreManager = new ScoreManager("data/scores.txt");
                    scoreManager.registrarPuntuacion(alias.trim(), 50); // Puntaje para resolución automática
                }
            }
        }
    });
    timer.start();
}


    private void mostrarSolucion(Nodo nodo) {
        List<Nodo> camino = new ArrayList<>();
        while (nodo != null) {
            camino.add(nodo);
            nodo = nodo.padre;
        }
        Collections.reverse(camino);
        System.out.println("\n--- Solución encontrada ---");
        int paso = 0;
        for (Nodo n : camino) {
            System.out.println("Paso " + paso++);
            imprimirEstado(n.estado);
        }
    }

    private List<Nodo> generarSucesores(Nodo nodo) {
        List<Nodo> sucesores = new ArrayList<>();
        int[][] dir = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        int[] vacia = encontrarPosicionVacia(nodo.estado);
        int fila = vacia[0], col = vacia[1];

        for (int[] d : dir) {
            int nf = fila + d[0];
            int nc = col + d[1];

            if (nf >= 0 && nf < TAM && nc >= 0 && nc < TAM) {
                int[][] nuevoEstado = copiarMatriz(nodo.estado);
                nuevoEstado[fila][col] = nuevoEstado[nf][nc];
                nuevoEstado[nf][nc] = 0;

                int nuevoCosto = nodo.costo + 1;
                int heuristica = calcularHeuristica(nuevoEstado);
                sucesores.add(new Nodo(nuevoEstado, nodo, nuevoCosto, heuristica));
            }
        }

        return sucesores;
    }

    private int[][] leerEstadoDesdeUsuario(Scanner scanner) {
        int[][] estado = new int[TAM][TAM];
        while (true) {
            try {
                String[] tokens = scanner.nextLine().trim().split("\\s+");
                if (tokens.length != 9) throw new Exception();
                Set<Integer> numeros = new HashSet<>();
                for (int i = 0; i < 9; i++) numeros.add(Integer.parseInt(tokens[i]));
                if (numeros.size() != 9 || !numeros.contains(0)) throw new Exception();

                int idx = 0;
                for (int i = 0; i < TAM; i++) {
                    for (int j = 0; j < TAM; j++) {
                        estado[i][j] = Integer.parseInt(tokens[idx++]);
                    }
                }
                break;
            } catch (Exception e) {
                System.out.println("Entrada inválida. Intenta de nuevo:");
            }
        }
        return estado;
    }

    private int[] encontrarPosicionVacia(int[][] estado) {
        for (int i = 0; i < TAM; i++)
            for (int j = 0; j < TAM; j++)
                if (estado[i][j] == 0)
                    return new int[]{i, j};
        return null;
    }

    private int[][] copiarMatriz(int[][] original) {
        int[][] copia = new int[TAM][TAM];
        for (int i = 0; i < TAM; i++)
            copia[i] = Arrays.copyOf(original[i], TAM);
        return copia;
    }

    private int calcularHeuristica(int[][] estado) {
        int h = 0;
        for (int i = 0; i < TAM; i++) {
            for (int j = 0; j < TAM; j++) {
                int val = estado[i][j];
                if (val != 0) {
                    int filaCorrecta = (val - 1) / TAM;
                    int colCorrecta = (val - 1) % TAM;
                    h += Math.abs(i - filaCorrecta) + Math.abs(j - colCorrecta);
                }
            }
        }
        return h;
    }

    private void imprimirEstado(int[][] estado) {
        System.out.println("-------------");
        for (int i = 0; i < TAM; i++) {
            for (int j = 0; j < TAM; j++) {
                System.out.print("| ");
                if (estado[i][j] == 0)
                    System.out.print("  ");
                else
                    System.out.print(estado[i][j] + " ");
            }
            System.out.println("|");
        }
        System.out.println("-------------\n");
    }

    private class Nodo {
        int[][] estado;
        Nodo padre;
        int costo;
        int costoTotal;

        Nodo(int[][] estado, Nodo padre, int costo, int heuristica) {
            this.estado = estado;
            this.padre = padre;
            this.costo = costo;
            this.costoTotal = costo + heuristica;
        }
    }
}
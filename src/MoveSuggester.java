// MoveSuggester.java
// Sugerencia inteligente usando A*
// Proyecto por v1000

import java.util.*;

public class MoveSuggester {
    private final int TAM = 3;

    // Estado meta por defecto
    private final int[][] objetivo = {
        {1, 2, 3},
        {4, 5, 6},
        {7, 8, 0}
    };

    public String sugerirMovimiento(int[][] estadoInicial) {
        PriorityQueue<Nodo> abierta = new PriorityQueue<>(Comparator.comparingInt(n -> n.costoTotal));
        Set<String> visitados = new HashSet<>();

        abierta.add(new Nodo(estadoInicial, null, 0, calcularHeuristica(estadoInicial), null));

        while (!abierta.isEmpty()) {
            Nodo actual = abierta.poll();

            if (Arrays.deepEquals(actual.estado, objetivo)) {
                return reconstruirPrimerMovimiento(actual);
            }

            String clave = Arrays.deepToString(actual.estado);
            if (visitados.contains(clave)) continue;
            visitados.add(clave);

            for (Nodo sucesor : generarSucesores(actual)) {
                String claveSucesor = Arrays.deepToString(sucesor.estado);
                if (!visitados.contains(claveSucesor)) {
                    abierta.add(sucesor);
                }
            }
        }

        return null; // no hay solución
    }

    private String reconstruirPrimerMovimiento(Nodo nodo) {
        while (nodo.padre != null && nodo.padre.padre != null) {
            nodo = nodo.padre;
        }
        return nodo.movimiento;
    }

    private List<Nodo> generarSucesores(Nodo nodo) {
        List<Nodo> sucesores = new ArrayList<>();
        int[][] estado = nodo.estado;
        int[] vacia = encontrarVacia(estado);
        int fila = vacia[0], col = vacia[1];

        int[][] movimientos = {
            {-1, 0, 'b'}, // arriba
            {1, 0, 'a'},  // abajo
            {0, -1, 'd'}, // izquierda
            {0, 1, 'i'}   // derecha
        };

        for (int[] mov : movimientos) {
            int nf = fila + mov[0];
            int nc = col + mov[1];
            String dir = direccionTexto(mov[0], mov[1]);

            if (nf >= 0 && nf < TAM && nc >= 0 && nc < TAM) {
                int[][] nuevoEstado = copiar(estado);
                nuevoEstado[fila][col] = nuevoEstado[nf][nc];
                nuevoEstado[nf][nc] = 0;
                int nuevoCosto = nodo.costo + 1;
                int heuristica = calcularHeuristica(nuevoEstado);
                sucesores.add(new Nodo(nuevoEstado, nodo, nuevoCosto, heuristica, dir));
            }
        }

        return sucesores;
    }

    private String direccionTexto(int df, int dc) {
        if (df == -1 && dc == 0) return "arriba";
        if (df == 1 && dc == 0) return "abajo";
        if (df == 0 && dc == -1) return "izquierda";
        if (df == 0 && dc == 1) return "derecha";
        return "";
    }

    private int[] encontrarVacia(int[][] estado) {
        for (int i = 0; i < TAM; i++)
            for (int j = 0; j < TAM; j++)
                if (estado[i][j] == 0)
                    return new int[]{i, j};
        return null;
    }

    private int[][] copiar(int[][] original) {
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
                    int fi = (val - 1) / TAM;
                    int ci = (val - 1) % TAM;
                    h += Math.abs(i - fi) + Math.abs(j - ci);
                }
            }
        }
        return h;
    }

    // Clase interna para nodos A*
    private class Nodo {
        int[][] estado;
        Nodo padre;
        int costo;
        int costoTotal;
        String movimiento;

        Nodo(int[][] estado, Nodo padre, int costo, int heuristica, String movimiento) {
            this.estado = estado;
            this.padre = padre;
            this.costo = costo;
            this.movimiento = movimiento;
            this.costoTotal = costo + heuristica;
        }
    }
}

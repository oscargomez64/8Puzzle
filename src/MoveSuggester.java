// MoveSuggester.java
// Sugerencia inteligente usando A*
// Proyecto por Oscar Gomez y Uriel Ortiz

import java.util.*;

public class MoveSuggester {
    private final int TAM = 3; // Tamaño del tablero (3x3)

    // Estado objetivo del puzzle
    private final int[][] objetivo = {
        {1, 2, 3},
        {4, 5, 6},
        {7, 8, 0}
    };

    // Método principal para sugerir el siguiente movimiento óptimo
    public String sugerirMovimiento(int[][] estadoInicial) {
        // Cola de prioridad ordenada por costo total estimado (A*)
        PriorityQueue<Nodo> abierta = new PriorityQueue<>(Comparator.comparingInt(n -> n.costoTotal));
        Set<String> visitados = new HashSet<>();

        // Se agrega el nodo inicial
        abierta.add(new Nodo(estadoInicial, null, 0, calcularHeuristica(estadoInicial), null));

        // Bucle principal del algoritmo A*
        while (!abierta.isEmpty()) {
            Nodo actual = abierta.poll();

            // Si se alcanza el objetivo, reconstruir el camino
            if (Arrays.deepEquals(actual.estado, objetivo)) {
                return reconstruirMovimientoDetallado(actual);
            }

            // Se evita visitar estados ya explorados
            String clave = Arrays.deepToString(actual.estado);
            if (visitados.contains(clave)) continue;
            visitados.add(clave);

            // Generar sucesores del estado actual
            for (Nodo sucesor : generarSucesores(actual)) {
                String claveSucesor = Arrays.deepToString(sucesor.estado);
                if (!visitados.contains(claveSucesor)) {
                    abierta.add(sucesor);
                }
            }
        }

        // Si no se encuentra solución
        return null;
    }

    // Reconstruye el primer movimiento desde el estado inicial hacia la solución
    private String reconstruirMovimientoDetallado(Nodo nodo) {
        // Retroceder hasta el segundo nodo para obtener el primer movimiento
        while (nodo.padre != null && nodo.padre.padre != null) {
            nodo = nodo.padre;
        }

        int[][] antes = nodo.padre.estado;
        int[][] despues = nodo.estado;

        // Identificar la pieza que se movió hacia la posición vacía
        for (int i = 0; i < TAM; i++) {
            for (int j = 0; j < TAM; j++) {
                if (antes[i][j] != despues[i][j] && despues[i][j] == 0) {
                    int numero = antes[i][j];
                    return numero + " hacia " + nodo.movimiento;
                }
            }
        }

        return nodo.movimiento; // Respaldo por si falla el análisis anterior
    }

    // Genera todos los movimientos válidos desde el nodo actual
    private List<Nodo> generarSucesores(Nodo nodo) {
        List<Nodo> sucesores = new ArrayList<>();
        int[][] estado = nodo.estado;
        int[] vacia = encontrarVacia(estado);
        int fila = vacia[0], col = vacia[1];

        // Posibles direcciones de movimiento
        int[][] movimientos = {
            {-1, 0}, // arriba
            {1, 0},  // abajo
            {0, -1}, // izquierda
            {0, 1}   // derecha
        };

        for (int[] mov : movimientos) {
            int nf = fila + mov[0];
            int nc = col + mov[1];
            String dir = direccionTexto(mov[0], mov[1]);

            // Verifica si el movimiento es válido
            if (nf >= 0 && nf < TAM && nc >= 0 && nc < TAM) {
                int[][] nuevoEstado = copiar(estado);
                // Intercambiar la casilla vacía con la casilla vecina
                nuevoEstado[fila][col] = nuevoEstado[nf][nc];
                nuevoEstado[nf][nc] = 0;

                int nuevoCosto = nodo.costo + 1;
                int heuristica = calcularHeuristica(nuevoEstado);
                sucesores.add(new Nodo(nuevoEstado, nodo, nuevoCosto, heuristica, dir));
            }
        }

        return sucesores;
    }

    // Convierte un movimiento en texto (para mostrar dirección)
    private String direccionTexto(int df, int dc) {
        if (df == -1 && dc == 0) return "abajo";     // Movimiento hacia arriba = vacío baja
        if (df == 1 && dc == 0) return "arriba";     // Movimiento hacia abajo = vacío sube
        if (df == 0 && dc == -1) return "derecha";   // Movimiento a la izquierda = vacío va a la derecha
        if (df == 0 && dc == 1) return "izquierda";  // Movimiento a la derecha = vacío va a la izquierda
        return "";
    }

    // Busca la posición de la casilla vacía (0)
    private int[] encontrarVacia(int[][] estado) {
        for (int i = 0; i < TAM; i++)
            for (int j = 0; j < TAM; j++)
                if (estado[i][j] == 0)
                    return new int[]{i, j};
        return null;
    }

    // Realiza una copia profunda de la matriz del estado
    private int[][] copiar(int[][] original) {
        int[][] copia = new int[TAM][TAM];
        for (int i = 0; i < TAM; i++)
            copia[i] = Arrays.copyOf(original[i], TAM);
        return copia;
    }

    // Calcula la heurística: distancia de Manhattan de cada ficha a su posición objetivo
    private int calcularHeuristica(int[][] estado) {
        int h = 0;
        for (int i = 0; i < TAM; i++) {
            for (int j = 0; j < TAM; j++) {
                int val = estado[i][j];
                if (val != 0) {
                    int fi = (val - 1) / TAM; // Fila objetivo
                    int ci = (val - 1) % TAM; // Columna objetivo
                    h += Math.abs(i - fi) + Math.abs(j - ci);
                }
            }
        }
        return h;
    }

    // Clase interna para representar un nodo del árbol de búsqueda A*
    private class Nodo {
        int[][] estado;     // Estado del tablero
        Nodo padre;         // Nodo padre desde el cual se llegó aquí
        int costo;          // Costo acumulado desde el inicio
        int costoTotal;     // Costo + heurística
        String movimiento;  // Dirección del movimiento que generó este nodo

        Nodo(int[][] estado, Nodo padre, int costo, int heuristica, String movimiento) {
            this.estado = estado;
            this.padre = padre;
            this.costo = costo;
            this.movimiento = movimiento;
            this.costoTotal = costo + heuristica;
        }
    }
}

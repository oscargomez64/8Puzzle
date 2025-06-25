// Puzzle.java
// Lógica del juego 8-Puzzle en modo gráfico
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
    }

    public void generarTableroAleatorio(int nivelDificultad) {
        int movimientos;
        switch (nivelDificultad) {
            case 1:  movimientos = 10; break;  // Fácil
            case 2:  movimientos = 30; break;  // Medio
            case 3:  movimientos = 100; break; // Difícil
            default: movimientos = 30;
        }

        // Generamos estado meta y lo vamos desordenando
        int[][] meta = generarMeta();
        for (int i = 0; i < TAM; i++) {
            tablero[i] = Arrays.copyOf(meta[i], TAM);
            for (int j = 0; j < TAM; j++) {
                if (tablero[i][j] == 0) {
                    filaVacia = i;
                    colVacia = j;
                }
            }
        }

        Random rand = new Random();
        String[] direcciones = {"arriba", "abajo", "izquierda", "derecha"};
        for (int i = 0; i < movimientos; i++) {
            mover(direcciones[rand.nextInt(direcciones.length)]);
        }
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
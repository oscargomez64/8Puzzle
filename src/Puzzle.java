// Puzzle.java
// Lógica del juego 8-Puzzle en modo gráfico
// Proyecto por Oscar Gomez y Uriel Ortiz

import java.util.*;

public class Puzzle {
    private int[][] tablero;               // Matriz que representa el estado actual del tablero (3x3)
    private final int TAM = 3;             // Tamaño constante del tablero
    private int filaVacia, colVacia;       // Coordenadas actuales del espacio vacío (representado por 0)

    // Constructor por defecto: crea un tablero vacío (sin piezas asignadas aún)
    public Puzzle() {
        tablero = new int[TAM][TAM];
    }

    // Constructor con un estado inicial ya definido
    // Copia el estado dado y localiza la posición del espacio vacío (0)
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

    // Asigna manualmente un nuevo estado al tablero
    // También actualiza la posición del espacio vacío
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

    // Genera una configuración aleatoria del tablero
    // Se asegura de que la combinación generada sea solucionable
    public void generarTableroAleatorio() {
        List<Integer> numeros = new ArrayList<>();
        for (int i = 0; i < TAM * TAM; i++) numeros.add(i);

        // Mezcla hasta obtener una combinación válida según el número de inversiones
        do {
            Collections.shuffle(numeros);
        } while (!esSolucionable(numeros));

        // Asigna los valores mezclados al tablero
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

    // Genera un tablero a partir del estado meta y lo desordena aplicando una cantidad controlada de movimientos válidos
    // Cuanto mayor sea el número de movimientos, mayor será la dificultad
    public void generarTableroAleatorio(int nivelDificultad) {
        int movimientos;
        switch (nivelDificultad) {
            case 1:  movimientos = 10; break;  // Fácil
            case 2:  movimientos = 30; break;  // Medio
            case 3:  movimientos = 100; break; // Difícil
            default: movimientos = 30;        // Nivel medio por defecto
        }

        // Comenzamos desde el estado meta
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

        // Aplicamos movimientos aleatorios para desordenar el tablero
        Random rand = new Random();
        String[] direcciones = {"arriba", "abajo", "izquierda", "derecha"};
        for (int i = 0; i < movimientos; i++) {
            mover(direcciones[rand.nextInt(direcciones.length)]);
        }
    }

    // Realiza un movimiento del espacio vacío en la dirección especificada si es posible
    // Devuelve true si el movimiento se realizó correctamente
    public boolean mover(String direccion) {
        int nuevaFila = filaVacia;
        int nuevaCol = colVacia;

        // Determinar la dirección del movimiento y calcular nueva posición
        switch (direccion) {
            case "arriba":    nuevaFila++; break;
            case "abajo":     nuevaFila--; break;
            case "izquierda": nuevaCol++; break;
            case "derecha":   nuevaCol--; break;
            default: return false; // Dirección inválida
        }

        // Verifica si la posición destino es válida
        if (esPosicionValida(nuevaFila, nuevaCol)) {
            // Intercambia los valores entre la celda vacía y la celda vecina
            tablero[filaVacia][colVacia] = tablero[nuevaFila][nuevaCol];
            tablero[nuevaFila][nuevaCol] = 0;
            filaVacia = nuevaFila;
            colVacia = nuevaCol;
            return true;
        }
        return false;
    }

    // Verifica si una posición está dentro de los límites del tablero
    private boolean esPosicionValida(int fila, int col) {
        return fila >= 0 && fila < TAM && col >= 0 && col < TAM;
    }

    // Verifica si el estado actual del tablero corresponde al estado objetivo
    public boolean esMeta() {
        int contador = 1;
        for (int i = 0; i < TAM; i++) {
            for (int j = 0; j < TAM; j++) {
                if (i == TAM - 1 && j == TAM - 1) {
                    return tablero[i][j] == 0; // La última celda debe estar vacía
                }
                if (tablero[i][j] != contador++) {
                    return false;
                }
            }
        }
        return true;
    }

    // Determina si una combinación de números es solucionable
    // Basado en el número de inversiones (pares en orden incorrecto)
    private boolean esSolucionable(List<Integer> lista) {
        int inversions = 0;
        for (int i = 0; i < lista.size(); i++) {
            for (int j = i + 1; j < lista.size(); j++) {
                int a = lista.get(i);
                int b = lista.get(j);
                if (a != 0 && b != 0 && a > b) inversions++;
            }
        }
        return inversions % 2 == 0; // Debe haber un número par de inversiones
    }

    // Devuelve el estado actual del tablero
    public int[][] getTablero() {
        return tablero;
    }

    // Genera el estado meta (orden correcto del puzzle)
    // De 1 a 8, con el 0 en la última celda
    public int[][] generarMeta() {
        int[][] meta = new int[TAM][TAM];
        int contador = 1;
        for (int i = 0; i < TAM; i++) {
            for (int j = 0; j < TAM; j++) {
                if (i == TAM - 1 && j == TAM - 1)
                    meta[i][j] = 0; // Última celda vacía
                else
                    meta[i][j] = contador++;
            }
        }
        return meta;
    }
}

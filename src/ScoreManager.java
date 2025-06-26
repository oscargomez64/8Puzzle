// ScoreManager.java
// Gestión de puntuaciones para el juego 8-Puzzle
// Proyecto por Oscar Gomez y Uriel Ortiz

import java.io.*;
import java.util.*;

public class ScoreManager {
    private String archivo;            // Ruta del archivo donde se almacenan las puntuaciones
    private List<Player> jugadores;   // Lista de jugadores con sus puntuaciones

    // Constructor que recibe el archivo donde se almacenarán las puntuaciones
    public ScoreManager(String archivo) {
        this.archivo = archivo;
        this.jugadores = new ArrayList<>();
        cargarPuntuaciones();        // Carga las puntuaciones existentes desde archivo
    }

    // Método privado para cargar puntuaciones desde el archivo
    private void cargarPuntuaciones() {
        File f = new File(archivo);
        if (!f.exists()) return;     // Si el archivo no existe, no hacer nada

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                // Convierte cada línea en un objeto Player y lo añade a la lista
                Player p = Player.desdeLineaTexto(linea);
                if (p != null) {
                    jugadores.add(p);
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo de puntuaciones.");
        }
    }

    // Método privado para guardar todas las puntuaciones actuales en el archivo
    private void guardarPuntuaciones() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
            for (Player p : jugadores) {
                bw.write(p.toString()); // Escribe cada jugador en una línea
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error al guardar el archivo de puntuaciones.");
        }
    }

    // Muestra el ranking ordenado de jugadores con alias, puntos y fecha
    public void mostrarPuntuaciones() {
        if (jugadores.isEmpty()) {
            System.out.println("No hay puntuaciones registradas.");
            return;
        }

        Collections.sort(jugadores); // Ordena la lista en orden descendente por puntuación
        System.out.println("Alias\tPuntuación\tFecha");
        for (Player p : jugadores) {
            System.out.println(p.getAlias() + "\t" + p.getPuntuacion() + "\t\t" + p.getFechaTexto());
        }
    }

    // Registra o actualiza la puntuación de un jugador por alias
    public void registrarPuntuacion(String alias, int puntosGanados) {
        Player existente = null;
        // Busca si el alias ya existe en la lista
        for (Player p : jugadores) {
            if (p.getAlias().equalsIgnoreCase(alias)) {
                existente = p;
                break;
            }
        }

        if (existente != null) {
            existente.sumarPuntos(puntosGanados); // Si existe, suma los puntos nuevos
        } else {
            jugadores.add(new Player(alias, puntosGanados)); // Si no existe, crea un nuevo jugador
        }

        guardarPuntuaciones(); // Guarda los cambios en el archivo
    }
}

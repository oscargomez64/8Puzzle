// ScoreManager.java
// Gestión de puntuaciones para el juego 8-Puzzle
// Proyecto por v1000

import java.io.*;
import java.util.*;

public class ScoreManager {
    private String archivo;
    private List<Player> jugadores;

    public ScoreManager(String archivo) {
        this.archivo = archivo;
        this.jugadores = new ArrayList<>();
        cargarPuntuaciones();
    }

    // Cargar desde archivo
    private void cargarPuntuaciones() {
        File f = new File(archivo);
        if (!f.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                Player p = Player.desdeLineaTexto(linea);
                if (p != null) {
                    jugadores.add(p);
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo de puntuaciones.");
        }
    }

    // Guardar en archivo
    private void guardarPuntuaciones() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
            for (Player p : jugadores) {
                bw.write(p.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error al guardar el archivo de puntuaciones.");
        }
    }

    // Mostrar ranking
    public void mostrarPuntuaciones() {
        if (jugadores.isEmpty()) {
            System.out.println("No hay puntuaciones registradas.");
            return;
        }

        Collections.sort(jugadores); // Orden descendente
        System.out.println("Alias\tPuntuación\tFecha");
        for (Player p : jugadores) {
            System.out.println(p.getAlias() + "\t" + p.getPuntuacion() + "\t\t" + p.getFechaTexto());
        }
    }

    // Añadir o actualizar puntuación
    public void registrarPuntuacion(String alias, int puntosGanados) {
        Player existente = null;
        for (Player p : jugadores) {
            if (p.getAlias().equalsIgnoreCase(alias)) {
                existente = p;
                break;
            }
        }

        if (existente != null) {
            existente.sumarPuntos(puntosGanados);
        } else {
            jugadores.add(new Player(alias, puntosGanados));
        }

        guardarPuntuaciones();
    }
}

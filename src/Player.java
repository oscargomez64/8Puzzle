// Player.java
// Representa a un jugador y su puntuación
// Proyecto por Oscar Gomez y Uriel Ortiz

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Player implements Comparable<Player> {
    private String alias;           // Nombre o apodo del jugador
    private int puntuacion;         // Puntos obtenidos por el jugador
    private LocalDate fecha;        // Fecha en que se registró la puntuación

    // Constructor que establece alias y puntuación, y usa la fecha actual
    public Player(String alias, int puntuacion) {
        this.alias = alias;
        this.puntuacion = puntuacion;
        this.fecha = LocalDate.now();
    }

    // Constructor que además permite establecer una fecha específica
    public Player(String alias, int puntuacion, String fechaTexto) {
        this.alias = alias;
        this.puntuacion = puntuacion;
        this.fecha = LocalDate.parse(fechaTexto, DateTimeFormatter.ISO_LOCAL_DATE);
    }

    // Devuelve el alias del jugador
    public String getAlias() {
        return alias;
    }

    // Devuelve la puntuación actual del jugador
    public int getPuntuacion() {
        return puntuacion;
    }

    // Devuelve la fecha en formato texto ISO (aaaa-mm-dd)
    public String getFechaTexto() {
        return fecha.toString();
    }

    // Suma puntos a la puntuación actual
    public void sumarPuntos(int puntos) {
        this.puntuacion += puntos;
    }

    // Permite comparar jugadores por puntuación descendente (para ordenar rankings)
    @Override
    public int compareTo(Player otro) {
        return Integer.compare(otro.puntuacion, this.puntuacion); // Mayor puntuación primero
    }

    // Representación en texto para guardar en archivo o mostrar
    @Override
    public String toString() {
        return alias + "," + puntuacion + "," + getFechaTexto();
    }

    // Crea un objeto Player a partir de una línea de texto con formato CSV
    public static Player desdeLineaTexto(String linea) {
        String[] partes = linea.split(",");
        if (partes.length != 3) return null;
        String alias = partes[0];
        int puntos = Integer.parseInt(partes[1]);
        String fecha = partes[2];
        return new Player(alias, puntos, fecha);
    }
}

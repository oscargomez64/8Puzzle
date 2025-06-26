// Player.java
// Representa a un jugador y su puntuación
// Proyecto por v1000

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Player implements Comparable<Player> {
    private String alias;
    private int puntuacion;
    private LocalDate fecha;

    public Player(String alias, int puntuacion) {
        this.alias = alias;
        this.puntuacion = puntuacion;
        this.fecha = LocalDate.now();
    }

    public Player(String alias, int puntuacion, String fechaTexto) {
        this.alias = alias;
        this.puntuacion = puntuacion;
        this.fecha = LocalDate.parse(fechaTexto, DateTimeFormatter.ISO_LOCAL_DATE);
    }

    public String getAlias() {
        return alias;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public String getFechaTexto() {
        return fecha.toString();
    }

    public void sumarPuntos(int puntos) {
        this.puntuacion += puntos;
    }

    @Override
    public int compareTo(Player otro) {
        return Integer.compare(otro.puntuacion, this.puntuacion); // Descendente
    }

    @Override
    public String toString() {
        return alias + "," + puntuacion + "," + getFechaTexto();
    }

    public static Player desdeLineaTexto(String linea) {
        String[] partes = linea.split(",");
        if (partes.length != 3) return null;
        String alias = partes[0];
        int puntos = Integer.parseInt(partes[1]);
        String fecha = partes[2];
        return new Player(alias, puntos, fecha);
    }
}

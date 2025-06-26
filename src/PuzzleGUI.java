// PuzzleGUI.java
// Interfaz gráfica 2D para el juego 8-Puzzle con botones extra
// Proyecto por v1000

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PuzzleGUI extends JPanel {
    private Puzzle puzzle;
    private final int TAM = 3;
    private final int TILE_SIZE = 100;

    public PuzzleGUI(Puzzle puzzle) {
        this.puzzle = puzzle;
        this.setLayout(null); // Para posicionar botones manualmente
        this.setPreferredSize(new Dimension(TAM * TILE_SIZE, TAM * TILE_SIZE + 50)); // espacio extra para botones

        // Botón: Sugerencia
        JButton btnSugerencia = new JButton("Sugerencia");
        btnSugerencia.setBounds(10, TAM * TILE_SIZE + 5, 120, 30);
        btnSugerencia.addActionListener(_ -> {
            MoveSuggester sugeridor = new MoveSuggester();
            String mov = sugeridor.sugerirMovimiento(puzzle.getTablero());
            if (mov != null) {
                JOptionPane.showMessageDialog(this, "➡️ Sugerencia: mueve " + mov);
            } else {
                JOptionPane.showMessageDialog(this, "No se puede sugerir un movimiento.");
            }
        });
        this.add(btnSugerencia);

        // Botón: Resolver
        JButton btnResolver = new JButton("Resolver");
        btnResolver.setBounds(140, TAM * TILE_SIZE + 5, 120, 30);
        btnResolver.addActionListener(_ -> {
            PuzzleSolver solver = new PuzzleSolver();
            solver.setEstadoInicial(puzzle.getTablero());
            solver.setEstadoMeta(puzzle.generarMeta()); // meta por defecto
            solver.resolverAutomaticamente(puzzle, this); // ✅ pasamos Puzzle y JPanel
        });
        this.add(btnResolver);
        

        // Clic para mover fichas
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getY() < TAM * TILE_SIZE) {
                    int fila = e.getY() / TILE_SIZE;
                    int col = e.getX() / TILE_SIZE;
                    intentarMover(fila, col);
                    repaint();

                    if (puzzle.esMeta()) {
                        JOptionPane.showMessageDialog(null, "🎉 ¡Felicidades! Has resuelto el puzzle.");

                        String alias = JOptionPane.showInputDialog(null, "Introduce tu nombre o alias:");
                        if (alias != null && !alias.trim().isEmpty()) {
                            int puntos = 100;

                            // Asegura que el directorio exista
                            new java.io.File("data").mkdirs();

                            ScoreManager scoreManager = new ScoreManager("data/scores.txt");
                            scoreManager.registrarPuntuacion(alias.trim(), puntos);

                            JOptionPane.showMessageDialog(null, "✅ Puntuación guardada.");
                        }
                    }
                }
            }
        });
    }

    // Constructor alternativo: acepta una matriz de estado inicial
    public PuzzleGUI(int[][] estadoInicial) {
        this(new Puzzle(estadoInicial));
    }

    private void intentarMover(int fila, int col) {
        int[][] tablero = puzzle.getTablero();
        int[] vacia = encontrarVacia(tablero);
        int vf = vacia[0];
        int vc = vacia[1];

        if ((Math.abs(fila - vf) + Math.abs(col - vc)) == 1) {
            tablero[vf][vc] = tablero[fila][col];
            tablero[fila][col] = 0;
        }
    }

    private int[] encontrarVacia(int[][] estado) {
        for (int i = 0; i < TAM; i++) {
            for (int j = 0; j < TAM; j++) {
                if (estado[i][j] == 0) return new int[]{i, j};
            }
        }
        return null;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int[][] tablero = puzzle.getTablero();

        for (int i = 0; i < TAM; i++) {
            for (int j = 0; j < TAM; j++) {
                int val = tablero[i][j];

                // Color de fondo
                if (val != 0) {
                    g.setColor(new Color(0, 180, 255)); // celda normal
                    g.fillRect(j * TILE_SIZE, i * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                    g.setColor(Color.BLACK);
                    g.setFont(new Font("Arial", Font.BOLD, 28));
                    g.drawString(String.valueOf(val), j * TILE_SIZE + 38, i * TILE_SIZE + 62);
                } else {
                    g.setColor(Color.LIGHT_GRAY); // celda vacía
                    g.fillRect(j * TILE_SIZE, i * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }

                // Bordes
                g.setColor(Color.BLACK);
                g.drawRect(j * TILE_SIZE, i * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }
    }
}

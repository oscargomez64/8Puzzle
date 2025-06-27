// PuzzleGUI.java
// Interfaz gráfica 2D para el juego 8-Puzzle con botones extra
// Proyecto por Oscar Gomez y Uriel Ortiz

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PuzzleGUI extends JPanel {
    private Puzzle puzzle;                  // Objeto que contiene la lógica del tablero
    private final int TAM = 3;              // Tamaño del tablero (3x3)
    private final int TILE_SIZE = 100;      // Tamaño en píxeles de cada ficha (tile)

    // Constructor principal que recibe un objeto Puzzle
    public PuzzleGUI(Puzzle puzzle) {
        this.puzzle = puzzle;
        this.setLayout(null); // Posicionamiento manual (absoluto)
        this.setPreferredSize(new Dimension(TAM * TILE_SIZE, TAM * TILE_SIZE + 50)); // Espacio adicional para botones

        // Botón: Sugerencia de movimiento usando inteligencia artificial (A*)
        JButton btnSugerencia = new JButton("Sugerencia");
        btnSugerencia.setBounds(10, TAM * TILE_SIZE + 5, 120, 30);
        btnSugerencia.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MoveSuggester sugeridor = new MoveSuggester(); // Instancia del sugeridor
                String mov = sugeridor.sugerirMovimiento(puzzle.getTablero());
                if (mov != null) {
                    JOptionPane.showMessageDialog(PuzzleGUI.this, "➡️ Sugerencia: mueve " + mov);
                } else {
                    JOptionPane.showMessageDialog(PuzzleGUI.this, "No se puede sugerir un movimiento.");
                }
            }
        });
        this.add(btnSugerencia);

        // Botón: Resolver automáticamente el puzzle paso a paso
        JButton btnResolver = new JButton("Resolver");
        btnResolver.setBounds(140, TAM * TILE_SIZE + 5, 120, 30);
        btnResolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PuzzleSolver solver = new PuzzleSolver();
                solver.setEstadoInicial(puzzle.getTablero());
                solver.setEstadoMeta(puzzle.generarMeta()); // Estado meta por defecto
                solver.resolverAutomaticamente(puzzle, PuzzleGUI.this); // Se pasa el tablero y el panel gráfico
            }
        });
        this.add(btnResolver);

        // Lógica para mover piezas con clic del ratón
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Solo se permite mover si el clic fue dentro del área del tablero
                if (e.getY() < TAM * TILE_SIZE) {
                    int fila = e.getY() / TILE_SIZE;
                    int col = e.getX() / TILE_SIZE;

                    // Intenta mover la ficha clickeada si es adyacente al espacio vacío
                    intentarMover(fila, col);
                    repaint(); // Redibuja el tablero

                    // Verifica si el puzzle fue resuelto
                    if (puzzle.esMeta()) {
                        JOptionPane.showMessageDialog(null, "🎉 ¡Felicidades! Has resuelto el puzzle.");

                        // Solicita el nombre del jugador para guardar puntuación
                        String alias = JOptionPane.showInputDialog(null, "Introduce tu nombre o alias:");
                        if (alias != null && !alias.trim().isEmpty()) {
                            int puntos = 100;

                            // Asegura que exista el directorio para guardar los datos
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

    // Constructor alternativo que permite inicializar directamente desde una matriz
    public PuzzleGUI(int[][] estadoInicial) {
        this(new Puzzle(estadoInicial));
    }

    // Intenta mover la ficha seleccionada si es adyacente a la ficha vacía
    private void intentarMover(int fila, int col) {
        int[][] tablero = puzzle.getTablero();
        int[] vacia = encontrarVacia(tablero);
        int vf = vacia[0];
        int vc = vacia[1];

        // Solo permite el movimiento si está exactamente a una casilla de distancia
        if ((Math.abs(fila - vf) + Math.abs(col - vc)) == 1) {
            tablero[vf][vc] = tablero[fila][col];
            tablero[fila][col] = 0;
        }
    }

    // Encuentra la posición (fila, columna) de la ficha vacía (valor 0)
    private int[] encontrarVacia(int[][] estado) {
        for (int i = 0; i < TAM; i++) {
            for (int j = 0; j < TAM; j++) {
                if (estado[i][j] == 0) return new int[]{i, j};
            }
        }
        return null;
    }

    // Método que dibuja el tablero gráficamente
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int[][] tablero = puzzle.getTablero();

        for (int i = 0; i < TAM; i++) {
            for (int j = 0; j < TAM; j++) {
                int val = tablero[i][j];

                // Dibuja ficha con número
                if (val != 0) {
                    g.setColor(new Color(0, 180, 255)); // Color de ficha
                    g.fillRect(j * TILE_SIZE, i * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                    g.setColor(Color.BLACK);           // Texto en negro
                    g.setFont(new Font("Arial", Font.BOLD, 28));
                    g.drawString(String.valueOf(val), j * TILE_SIZE + 38, i * TILE_SIZE + 62);
                } else {
                    // Dibuja la celda vacía
                    g.setColor(Color.LIGHT_GRAY);
                    g.fillRect(j * TILE_SIZE, i * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }

                // Dibuja el borde de cada celda
                g.setColor(Color.BLACK);
                g.drawRect(j * TILE_SIZE, i * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }
    }
}

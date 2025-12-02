package view;

import javax.swing.*;
import java.awt.*;

public class TableroPanel extends JPanel {

    private static final int TAM_CELDA = 30; // Tamaño de cada cuadrito
    private static final int FILAS = 20;
    private static final int COLUMNAS = 10;

    private int[][] grid; 

    public TableroPanel() {
        // Establecemos un tamaño preferido basado en las celdas
        setPreferredSize(new Dimension(COLUMNAS * TAM_CELDA, FILAS * TAM_CELDA));
        setBackground(Color.BLACK);
        // Inicializamos el grid vacío para evitar NullPointerException al inicio
        grid = new int[FILAS][COLUMNAS];
    }

    // Método para recibir el estado actual del juego desde el Controlador
    public void setGrid(int[][] nuevoGrid) {
        this.grid = nuevoGrid;
        repaint(); // Solicita a Swing que vuelva a pintar el panel
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (grid == null) return;

        for (int fila = 0; fila < FILAS; fila++) {
            for (int col = 0; col < COLUMNAS; col++) {
                int valorCelda = grid[fila][col];
                
                if (valorCelda != 0) {
                    // Dibujar el bloque
                    g.setColor(obtenerColor(valorCelda));
                    g.fillRect(col * TAM_CELDA, fila * TAM_CELDA, TAM_CELDA, TAM_CELDA);
                    
                    // Dibujar borde para que se vean separados
                    g.setColor(Color.DARK_GRAY);
                    g.drawRect(col * TAM_CELDA, fila * TAM_CELDA, TAM_CELDA, TAM_CELDA);
                }
            }
        }
    }

    private Color obtenerColor(int valor) {
        switch (valor) {
            case 1: return Color.CYAN;    // I
            case 2: return Color.YELLOW;  // O
            case 3: return Color.MAGENTA; // T
            case 4: return Color.ORANGE;  // L
            case 5: return Color.BLUE;    // J
            case 6: return Color.GREEN;   // S
            case 7: return Color.RED;     // Z
            default: return Color.WHITE;
        }
    }
}
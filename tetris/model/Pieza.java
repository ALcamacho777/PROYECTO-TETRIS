package model;

import java.util.Random;

public class Pieza {

    private int[][] forma; // 4x4
    private int idColor;

    private Pieza(int[][] forma, int idColor) {
        this.forma = forma;
        this.idColor = idColor;
    }

    public static Pieza randomPieza(Random random) {
        int tipo = random.nextInt(7); // 7 tetrominos clásicos
        int[][] f;

        switch (tipo) {
            case 0: // I
                f = new int[][] {
                        { 0, 0, 0, 0 },
                        { 1, 1, 1, 1 },
                        { 0, 0, 0, 0 },
                        { 0, 0, 0, 0 }
                };
                break;
            case 1: // O
                f = new int[][] {
                        { 0, 2, 2, 0 },
                        { 0, 2, 2, 0 },
                        { 0, 0, 0, 0 },
                        { 0, 0, 0, 0 }
                };
                break;
            case 2: // T
                f = new int[][] {
                        { 0, 3, 0, 0 },
                        { 3, 3, 3, 0 },
                        { 0, 0, 0, 0 },
                        { 0, 0, 0, 0 }
                };
                break;
            case 3: // L
                f = new int[][] {
                        { 0, 0, 4, 0 },
                        { 4, 4, 4, 0 },
                        { 0, 0, 0, 0 },
                        { 0, 0, 0, 0 }
                };
                break;
            case 4: // J
                f = new int[][] {
                        { 5, 0, 0, 0 },
                        { 5, 5, 5, 0 },
                        { 0, 0, 0, 0 },
                        { 0, 0, 0, 0 }
                };
                break;
            case 5: // S
                f = new int[][] {
                        { 0, 6, 6, 0 },
                        { 6, 6, 0, 0 },
                        { 0, 0, 0, 0 },
                        { 0, 0, 0, 0 }
                };
                break;
            default: // Z
                f = new int[][] {
                        { 7, 7, 0, 0 },
                        { 0, 7, 7, 0 },
                        { 0, 0, 0, 0 },
                        { 0, 0, 0, 0 }
                };
                break;
        }

        return new Pieza(f, tipo + 1);
    }

    public int[][] getForma() {
        return forma;
    }

    public void setForma(int[][] forma) {
        this.forma = forma;
    }

    public int getIdColor() {
        return idColor;
    }

    public int[][] getRotada() {
        int n = forma.length;
        int[][] rotada = new int[n][n];

        // rotación 90° horario
        for (int f = 0; f < n; f++) {
            for (int c = 0; c < n; c++) {
                rotada[c][n - 1 - f] = forma[f][c];
            }
        }
        return rotada;
    }
}

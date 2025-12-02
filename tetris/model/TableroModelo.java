package model;

import java.util.Random;

public class TableroModelo {

    public static final int FILAS = 20;
    public static final int COLUMNAS = 10;
    public static final int TAM_PIEZA = 4; // matrices 4x4

    private int[][] grid = new int[FILAS][COLUMNAS];

    private Pieza piezaActual;
    private Pieza piezaSiguiente; 
    private int filaPieza;
    private int colPieza;

    private Random random = new Random();

    // Líneas eliminadas en la última jugada
    private int lineasEliminadasUltimaJugada = 0;

    public TableroModelo() {
        // Inicializamos la siguiente pieza antes de comenzar
        piezaSiguiente = Pieza.randomPieza(random);
        nuevaPieza();
    }

    public int[][] getGridConPieza() {
        int[][] copia = new int[FILAS][COLUMNAS];

        // Copiar grid base
        for (int f = 0; f < FILAS; f++) {
            System.arraycopy(grid[f], 0, copia[f], 0, COLUMNAS);
        }

        // “dibujar” pieza actual encima
        int[][] forma = piezaActual.getForma();
        for (int f = 0; f < TAM_PIEZA; f++) {
            for (int c = 0; c < TAM_PIEZA; c++) {
                if (forma[f][c] != 0) {
                    int fila = filaPieza + f;
                    int col = colPieza + c;
                    if (fila >= 0 && fila < FILAS && col >= 0 && col < COLUMNAS) {
                        copia[fila][col] = piezaActual.getIdColor();
                    }
                }
            }
        }
        return copia;
    }

    public void nuevaPieza() {
        // La pieza actual pasa a ser la que estaba en espera
        piezaActual = piezaSiguiente;
        
        // Generamos una nueva pieza para la espera
        piezaSiguiente = Pieza.randomPieza(random);
        
        filaPieza = 0;
        colPieza = COLUMNAS / 2 - 2;
    }
    
    // Método necesario para que la Vista sepa qué dibujar en el recuadro "Siguiente"
    public Pieza getPiezaSiguiente() {
        return piezaSiguiente;
    }

    // ===== movimientos =====
    public boolean bajar() {
        if (puedeMover(filaPieza + 1, colPieza, piezaActual.getForma())) {
            filaPieza++;
            // si solo se movió, no se eliminaron líneas
            lineasEliminadasUltimaJugada = 0;
            return true;
        } else {
            fijarPieza();
            // calcular y guardar cuántas líneas se eliminaron
            int lineas = limpiarLineasCompletas();
            lineasEliminadasUltimaJugada = lineas;
            nuevaPieza();
            return false;
        }
    }

    public void moverIzquierda() {
        if (puedeMover(filaPieza, colPieza - 1, piezaActual.getForma())) {
            colPieza--;
        }
    }

    public void moverDerecha() {
        if (puedeMover(filaPieza, colPieza + 1, piezaActual.getForma())) {
            colPieza++;
        }
    }

    public void rotar() {
        int[][] rotada = piezaActual.getRotada();
        if (puedeMover(filaPieza, colPieza, rotada)) {
            piezaActual.setForma(rotada);
        }
    }

    // ===== lógica interna =====
    private boolean puedeMover(int nuevaFila, int nuevaCol, int[][] forma) {
        for (int f = 0; f < TAM_PIEZA; f++) {
            for (int c = 0; c < TAM_PIEZA; c++) {
                if (forma[f][c] != 0) {
                    int fila = nuevaFila + f;
                    int col = nuevaCol + c;

                    // fuera del tablero
                    if (fila < 0 || fila >= FILAS || col < 0 || col >= COLUMNAS) {
                        return false;
                    }

                    // colisión con bloques ya fijos
                    if (grid[fila][col] != 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void fijarPieza() {
        int[][] forma = piezaActual.getForma();
        for (int f = 0; f < TAM_PIEZA; f++) {
            for (int c = 0; c < TAM_PIEZA; c++) {
                if (forma[f][c] != 0) {
                    int fila = filaPieza + f;
                    int col = colPieza + c;
                    if (fila >= 0 && fila < FILAS && col >= 0 && col < COLUMNAS) {
                        grid[fila][col] = piezaActual.getIdColor();
                    }
                }
            }
        }
    }

    private int limpiarLineasCompletas() {
        int lineas = 0;
        for (int f = FILAS - 1; f >= 0; f--) {
            boolean llena = true;
            for (int c = 0; c < COLUMNAS; c++) {
                if (grid[f][c] == 0) {
                    llena = false;
                    break;
                }
            }
            if (llena) {
                lineas++;
                // bajar todas las filas superiores
                for (int fila = f; fila > 0; fila--) {
                    System.arraycopy(grid[fila - 1], 0, grid[fila], 0, COLUMNAS);
                }
                // fila superior en blanco
                for (int c = 0; c < COLUMNAS; c++) {
                    grid[0][c] = 0;
                }
                f++; // re-chequear misma fila
            }
        }
        return lineas;
    }

    public boolean hayColisionInicial() {
        return !puedeMover(filaPieza, colPieza, piezaActual.getForma());
    }

    public int getLineasEliminadasUltimaJugada() {
        return lineasEliminadasUltimaJugada;
    }
}
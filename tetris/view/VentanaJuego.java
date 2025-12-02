package view;

import javax.swing.*;
import java.awt.*;

public class VentanaJuego extends JFrame {

    private TableroPanel tableroPanel;

    public VentanaJuego() {
        setTitle("Tetris - Juego");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        inicializarComponentes();

        pack(); // Ajusta la ventana al tamaño del TableroPanel
        setLocationRelativeTo(null); // Centrar la ventana en la pantalla
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout());

        tableroPanel = new TableroPanel(); // Panel donde se dibuja el Tetris
        add(tableroPanel, BorderLayout.CENTER);
    }

    public void mostrar() {
        setVisible(true);
        tableroPanel.requestFocusInWindow(); // Importante para el teclado
    }
}

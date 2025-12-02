// Main.java (sin package)

import view.VentanaPrincipal;
import Controller.JuegoController;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal vista = new VentanaPrincipal();
            JuegoController juego = new JuegoController(vista, vista.getTableroPanel());
            vista.getBtnJugar().addActionListener(e -> juego.iniciarNuevoJuego());
            vista.setVisible(true);
        });
    }
}


/*CREATE DATABASE IF NOT EXISTS tetris_db;
USE tetris_db;

CREATE TABLE scores (
    id       INT AUTO_INCREMENT PRIMARY KEY,
    nombre   VARCHAR(100) NOT NULL,
    puntaje  INT NOT NULL,
    fecha    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

select * from scores;*/

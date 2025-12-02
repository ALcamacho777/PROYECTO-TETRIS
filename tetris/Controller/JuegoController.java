package Controller;

import dao.ScoreDAO;
import model.TableroModelo;
import view.GameOverDialog;
import view.TableroPanel;
import view.VentanaPrincipal;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class JuegoController {

    private TableroModelo modelo;
    private TableroPanel vistaTablero;
    private VentanaPrincipal vistaPrincipal;
    private ScoreDAO scoreDAO;

    private Timer timer;
    private int score;
    private boolean enJuego = false;
    private boolean pausado = false;

    public JuegoController(VentanaPrincipal vistaPrincipal, TableroPanel vistaTablero) {
        this.vistaPrincipal = vistaPrincipal;
        this.vistaTablero = vistaTablero;
        this.modelo = new TableroModelo();
        this.scoreDAO = new ScoreDAO(); 

        inicializar();
    }

    private void inicializar() {
        actualizarListaPuntajes();

        timer = new Timer(600, e -> tick());

        vistaPrincipal.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!enJuego) return;

                if (e.getKeyCode() == KeyEvent.VK_P) {
                    pausar();
                    return;
                }

                if (pausado) return;

                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        modelo.moverIzquierda();
                        break;
                    case KeyEvent.VK_RIGHT:
                        modelo.moverDerecha();
                        break;
                    case KeyEvent.VK_UP:
                        modelo.rotar();
                        reproducirSonido("Rotar.wav");
                        break;
                    case KeyEvent.VK_DOWN:
                        procesarBajada(); // <-- aquí se usa el nuevo método
                        return; // importante para no llamar actualizarVistaTablero otra vez
                }
                actualizarVistaTablero();
            }
        });
    }

    private void actualizarListaPuntajes() {
        DefaultListModel<String> model = vistaPrincipal.getModeloListaScores();
        if (model == null) return;
        model.clear();

        SwingWorker<List<String>, Void> worker = new SwingWorker<List<String>, Void>() {
            @Override
            protected List<String> doInBackground() {
                return scoreDAO.obtenerTopPuntajes();
            }

            @Override
            protected void done() {
                try {
                    List<String> scores = get();
                    for (String s : scores) {
                        model.addElement(s);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        worker.execute();
    }

    public void iniciarNuevoJuego() {
        modelo = new TableroModelo();
        score = 0;
        enJuego = true;
        pausado = false;

        vistaPrincipal.actualizarScore(score);
        vistaPrincipal.actualizarSiguientePieza(modelo.getPiezaSiguiente());
        actualizarVistaTablero();
        vistaPrincipal.requestFocusInWindow();

        if (timer.isRunning()) timer.stop();
        timer.start();
    }

    public void pausar() {
        if (!enJuego) return;
        pausado = !pausado;
        if (pausado) {
            timer.stop();
            JOptionPane.showMessageDialog(vistaPrincipal, "Juego Pausado");
            pausado = false;
            timer.start();
        }
    }

    private void tick() {
        if (!enJuego || pausado) return;
        procesarBajada(); // <-- usamos el mismo método centralizado
    }

    // NUEVO MÉTODO CENTRALIZADO PARA BAJADA, PUNTOS Y SONIDO
    private void procesarBajada() {
        boolean seMovio = modelo.bajar();

        if (!seMovio) {
            int lineas = modelo.getLineasEliminadasUltimaJugada();
            if (lineas > 0) {
                score += lineas * 100;
                vistaPrincipal.actualizarScore(score);
                reproducirSonido("Line.wav");
            }

            if (modelo.hayColisionInicial()) {
                reproducirSonido("GameOver.wav");
                gameOver();
                return;
            }

            vistaPrincipal.actualizarSiguientePieza(modelo.getPiezaSiguiente());
        }
        actualizarVistaTablero();
    }

    private void actualizarVistaTablero() {
        vistaTablero.setGrid(modelo.getGridConPieza());
    }

    private void gameOver() {
        enJuego = false;
        timer.stop();

        GameOverDialog dialog = new GameOverDialog(vistaPrincipal, score);
        dialog.setVisible(true);

        actualizarListaPuntajes();
    }

    private void reproducirSonido(String archivo) {
        try {
            File soundFile = new File("sonidos/" + archivo);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}

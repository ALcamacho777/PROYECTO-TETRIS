package view;

import dao.ScoreDAO;

import javax.swing.*;
import java.awt.*;

public class GameOverDialog extends JDialog {

    private JTextField txtNombre;
    private int score;

    private ScoreDAO scoreDAO = new ScoreDAO(); 

    public GameOverDialog(JFrame parent, int score) {
        super(parent, "Game Over", true); // true = modal
        this.score = score;

        initComponents();
        pack();
        setLocationRelativeTo(parent); // centrar sobre la ventana principal
    }

    private void initComponents() {
        // Panel principal
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Título
        JLabel lblTitulo = new JLabel("GAME OVER", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(lblTitulo, BorderLayout.NORTH);

        // Panel central con score + nombre
        JPanel centro = new JPanel();
        centro.setLayout(new GridLayout(3, 1, 5, 5));

        JLabel lblScore = new JLabel("Tu score: " + score, SwingConstants.CENTER);
        lblScore.setFont(new Font("Arial", Font.PLAIN, 18));

        JPanel panelNombre = new JPanel(new BorderLayout(5, 5));
        JLabel lblNombre = new JLabel("Nombre del jugador:");
        txtNombre = new JTextField(15);

        panelNombre.add(lblNombre, BorderLayout.WEST);
        panelNombre.add(txtNombre, BorderLayout.CENTER);

        centro.add(lblScore);
        centro.add(panelNombre);

        mainPanel.add(centro, BorderLayout.CENTER);

        // Botones
        JPanel botones = new JPanel();
        JButton btnGuardar = new JButton("Guardar score");
        JButton btnCerrar = new JButton("Cerrar");

        btnGuardar.addActionListener(e -> guardarScore());
        btnCerrar.addActionListener(e -> dispose());

        botones.add(btnGuardar);
        botones.add(btnCerrar);

        mainPanel.add(botones, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    private void guardarScore() {
        String nombre = txtNombre.getText().trim();
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Ingresa un nombre para guardar el score.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // GUARDAR EN LA BASE DE DATOS
        scoreDAO.guardarScore(nombre, score);

        JOptionPane.showMessageDialog(
                this,
                "Score guardado para " + nombre + " (puntos: " + score + ")",
                "Score guardado",
                JOptionPane.INFORMATION_MESSAGE
        );

        dispose(); // cerrar ventana
    }
}

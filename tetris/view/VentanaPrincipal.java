package view;

import model.Pieza;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class VentanaPrincipal extends JFrame {

    private static final int ANCHO_LATERAL = 220;
    private static final Color COLOR_FONDO_LATERAL = new Color(34, 45, 50);
    private static final Color COLOR_TEXTO_SCORE = Color.WHITE;
    private static final Color COLOR_FONDO_SCORE_BAR = new Color(51, 51, 51);

    private TableroPanel tableroPanel;
    private JButton btnJugar;
    private JLabel lblScore;
    
    
    private PanelProximaPieza panelProximaPieza;
    private DefaultListModel<String> modeloListaScores;
    private JList<String> listaScores;

    public VentanaPrincipal() {
        setTitle("Tetris UMM - Proyecto");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(0, 0));
        
        
        setMinimumSize(new Dimension(650, 760));
        setLocationRelativeTo(null);

        inicializarComponentes();
        pack();
    }

    private void inicializarComponentes() {
        add(createCenterPanel(), BorderLayout.CENTER);
        add(createEastPanel(), BorderLayout.EAST);
        add(createSouthPanel(), BorderLayout.SOUTH);
        
        // Importante: permite que la ventana capture teclas inicialmente
        this.setFocusable(true);
    }

    private JPanel createCenterPanel() {
        // CORRECCIÓN: Panel contenedor para que el borde no invada el dibujo
        JPanel panelContenedor = new JPanel(new GridBagLayout()); 
        panelContenedor.setBackground(Color.DARK_GRAY);
        // Borde solo a la derecha para separar del menú
        panelContenedor.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 2, Color.GRAY));

        tableroPanel = new TableroPanel();
        panelContenedor.add(tableroPanel);
        
        return panelContenedor;
    }

    private JPanel createSouthPanel() {
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panelInferior.setBackground(COLOR_FONDO_SCORE_BAR);

        JLabel lblTextoScore = new JLabel("PUNTUACIÓN:");
        lblTextoScore.setForeground(COLOR_TEXTO_SCORE);
        lblTextoScore.setFont(new Font("Arial", Font.BOLD, 14));

        lblScore = new JLabel("0");
        lblScore.setForeground(Color.YELLOW);
        lblScore.setFont(new Font("Arial", Font.BOLD, 18));

        panelInferior.add(lblTextoScore);
        panelInferior.add(lblScore);

        return panelInferior;
    }

    private JPanel createEastPanel() {
        JPanel panelLateral = new JPanel();
        panelLateral.setPreferredSize(new Dimension(ANCHO_LATERAL, 0));
        panelLateral.setLayout(new BoxLayout(panelLateral, BoxLayout.Y_AXIS));
        panelLateral.setBackground(COLOR_FONDO_LATERAL);
        panelLateral.setBorder(new EmptyBorder(20, 15, 20, 15));

        // Título
        JLabel lblTitulo = new JLabel("TETRIS UMM");
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));

        // Botón
        btnJugar = new JButton("NUEVO JUEGO");
        btnJugar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnJugar.setFocusable(false); // NO robar foco del teclado
        btnJugar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnJugar.setBackground(new Color(60, 141, 188));
        btnJugar.setForeground(Color.WHITE);
        btnJugar.setFont(new Font("Arial", Font.BOLD, 14));

        // Panel Siguiente Pieza
        JLabel lblTextoNext = new JLabel("SIGUIENTE");
        lblTextoNext.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTextoNext.setForeground(Color.LIGHT_GRAY);
        lblTextoNext.setFont(new Font("Arial", Font.PLAIN, 12));
        lblTextoNext.setBorder(BorderFactory.createEmptyBorder(20, 0, 5, 0));

        panelProximaPieza = new PanelProximaPieza();
        panelProximaPieza.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- LISTA DE MEJORES PUNTAJES ---
        JLabel lblRanking = new JLabel("MEJORES PUNTAJES");
        lblRanking.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblRanking.setForeground(Color.ORANGE);
        lblRanking.setFont(new Font("Arial", Font.BOLD, 14));
        lblRanking.setBorder(BorderFactory.createEmptyBorder(30, 0, 5, 0));

        modeloListaScores = new DefaultListModel<>();
        listaScores = new JList<>(modeloListaScores);
        listaScores.setBackground(new Color(44, 55, 60));
        listaScores.setForeground(Color.WHITE);
        listaScores.setFont(new Font("Consolas", Font.PLAIN, 12));
        listaScores.setFocusable(false); // NO robar foco
        
        JScrollPane scrollScores = new JScrollPane(listaScores);
        scrollScores.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        scrollScores.setPreferredSize(new Dimension(0, 200));
        scrollScores.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Agregar componentes en orden
        panelLateral.add(lblTitulo);
        panelLateral.add(Box.createVerticalStrut(20));
        panelLateral.add(btnJugar);
        panelLateral.add(lblTextoNext);
        panelLateral.add(panelProximaPieza);
        panelLateral.add(lblRanking);
        panelLateral.add(scrollScores);
        
        panelLateral.add(Box.createVerticalGlue());

        return panelLateral;
    }

    // ===== Getters y Métodos Públicos =====
    public JButton getBtnJugar() { return btnJugar; }
    public TableroPanel getTableroPanel() { return tableroPanel; }
    
    // Método necesario para que el controlador acceda a la lista
    public DefaultListModel<String> getModeloListaScores() { 
        return modeloListaScores; 
    }

    public void actualizarScore(int score) {
        lblScore.setText(String.valueOf(score));
    }

    public void actualizarSiguientePieza(Pieza pieza) {
        panelProximaPieza.setPieza(pieza);
    }

    // Clase interna para dibujar la miniatura
    public static class PanelProximaPieza extends JPanel {
        private Pieza pieza;
        private static final int CELDA_MINI = 20;

        public PanelProximaPieza() {
            setPreferredSize(new Dimension(100, 100));
            setMaximumSize(new Dimension(100, 100));
            setBackground(Color.BLACK);
            setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        }

        public void setPieza(Pieza p) {
            this.pieza = p;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (pieza == null) return;
            
            int[][] forma = pieza.getForma();
            int colorIndex = pieza.getIdColor();
            // Centrar (la pieza es 4x4, 80px. Panel es 100px. Margen = 10)
            int margen = 10;

            for (int f = 0; f < 4; f++) {
                for (int c = 0; c < 4; c++) {
                    if (forma[f][c] != 0) {
                        g.setColor(obtenerColor(colorIndex));
                        g.fillRect(margen + c * CELDA_MINI, margen + f * CELDA_MINI, CELDA_MINI, CELDA_MINI);
                        g.setColor(Color.WHITE);
                        g.drawRect(margen + c * CELDA_MINI, margen + f * CELDA_MINI, CELDA_MINI, CELDA_MINI);
                    }
                }
            }
        }
        
        private Color obtenerColor(int i) {
            switch (i) {
                case 1: return Color.CYAN;
                case 2: return Color.YELLOW;
                case 3: return Color.MAGENTA;
                case 4: return Color.ORANGE;
                case 5: return Color.BLUE;
                case 6: return Color.GREEN;
                case 7: return Color.RED;
                default: return Color.GRAY;
            }
        }
    }
}
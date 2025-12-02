package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VentanaJugadores extends JFrame {

    private JTextField txtId;
    private JTextField txtNombre;
    private JTextField txtPuntajeMax;

    private JButton btnNuevo;
    private JButton btnGuardar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnCerrar;

    private JTable tablaJugadores;
    private DefaultTableModel modeloTabla;

    public VentanaJugadores() {
        setTitle("Gestión de Jugadores");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout());

        // ---------- PANEL FORMULARIO (NORTE) ----------
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel lblId = new JLabel("ID:");
        JLabel lblNombre = new JLabel("Nombre:");
        JLabel lblPuntaje = new JLabel("Puntaje máximo:");

        txtId = new JTextField(5);
        txtId.setEditable(false); // ID se genera en BD
        txtNombre = new JTextField(15);
        txtPuntajeMax = new JTextField(6);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panelFormulario.add(lblId, gbc);

        gbc.gridx = 1;
        panelFormulario.add(txtId, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panelFormulario.add(lblNombre, gbc);

        gbc.gridx = 1;
        panelFormulario.add(txtNombre, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panelFormulario.add(lblPuntaje, gbc);

        gbc.gridx = 1;
        panelFormulario.add(txtPuntajeMax, gbc);

        add(panelFormulario, BorderLayout.NORTH);

        // ---------- TABLA (CENTRO) ----------
        modeloTabla = new DefaultTableModel(
                new Object[] { "ID", "Nombre", "Puntaje máximo" }, 0);
        tablaJugadores = new JTable(modeloTabla);
        JScrollPane scroll = new JScrollPane(tablaJugadores);

        add(scroll, BorderLayout.CENTER);

        // ---------- BOTONES (SUR) ----------
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        btnNuevo = new JButton("Nuevo");
        btnGuardar = new JButton("Guardar");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnCerrar = new JButton("Cerrar");

        panelBotones.add(btnNuevo);
        panelBotones.add(btnGuardar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnCerrar);

        add(panelBotones, BorderLayout.SOUTH);
    }

    // ===== Getters para que EL CONTROLLER maneje eventos y datos =====
    public JTextField getTxtId() {
        return txtId;
    }

    public JTextField getTxtNombre() {
        return txtNombre;
    }

    public JTextField getTxtPuntajeMax() {
        return txtPuntajeMax;
    }

    public JButton getBtnNuevo() {
        return btnNuevo;
    }

    public JButton getBtnGuardar() {
        return btnGuardar;
    }

    public JButton getBtnActualizar() {
        return btnActualizar;
    }

    public JButton getBtnEliminar() {
        return btnEliminar;
    }

    public JButton getBtnCerrar() {
        return btnCerrar;
    }

    public JTable getTablaJugadores() {
        return tablaJugadores;
    }

    public DefaultTableModel getModeloTabla() {
        return modeloTabla;
    }
}

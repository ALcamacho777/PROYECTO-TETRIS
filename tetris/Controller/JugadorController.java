package Controller;

import dao.JugadorDAO;
import model.Jugador;
import view.VentanaJugadores;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.util.List;

/**
 * Controlador encargado de la gestión de jugadores.
 * Implementa el patrón MVC mediando entre la VentanaJugadores y el JugadorDAO.
 */
public class JugadorController {

    // Referencias finales para asegurar que no cambien
    private final VentanaJugadores vista;
    private final JugadorDAO dao;

    // --- Constantes de Mensajes (Facilita cambios de texto o traducción) ---
    private static final String MSG_ERROR_CARGA = "Error crítico al cargar datos: ";
    private static final String MSG_ERROR_DB = "Error en la base de datos: ";
    private static final String MSG_ERROR_NUMERO = "El puntaje debe ser un número entero válido.";
    private static final String MSG_VALIDACION = "El nombre es obligatorio y el puntaje no puede estar vacío.";
    private static final String MSG_CONFIRM_ELIMINAR = "¿Está seguro de eliminar al jugador '%s' (ID: %d)?";
    private static final String TITLE_CONFIRM = "Confirmar eliminación";
    
    public JugadorController(VentanaJugadores vista, JugadorDAO dao) {
        this.vista = vista;
        this.dao = dao;
        initController();
    }

    /**
     * Configuración inicial de listeners y estado de la vista.
     */
    private void initController() {
        actualizarTabla();
        controlarEstadoBotones(false); // Inicia con botones de edición desactivados

        // --- Asignación de Eventos (Lambdas) ---
        vista.getBtnNuevo().addActionListener(e -> prepararNuevoRegistro());
        vista.getBtnGuardar().addActionListener(e -> guardarJugador());
        vista.getBtnActualizar().addActionListener(e -> actualizarJugador());
        vista.getBtnEliminar().addActionListener(e -> eliminarJugador());
        vista.getBtnCerrar().addActionListener(e -> vista.dispose());

        // Evento de Selección en Tabla
        vista.getTablaJugadores().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                boolean filaSeleccionada = vista.getTablaJugadores().getSelectedRow() != -1;
                controlarEstadoBotones(filaSeleccionada);
                
                if (filaSeleccionada) {
                    cargarDatosDeFila();
                }
            }
        });
    }

    // ================= LÓGICA DE NEGOCIO =================

    private void actualizarTabla() {
        DefaultTableModel modelo = vista.getModeloTabla();
        modelo.setRowCount(0);

        try {
            List<Jugador> lista = dao.listarTodos();
            for (Jugador j : lista) {
                modelo.addRow(new Object[]{ j.getId(), j.getNombre(), j.getPuntajeMax() });
            }
        } catch (SQLException ex) {
            mostrarError(MSG_ERROR_CARGA + ex.getMessage());
        }
    }

    private void prepararNuevoRegistro() {
        limpiarCampos();
        vista.getTablaJugadores().clearSelection(); // Esto disparará el listener para deshabilitar botones
        vista.getTxtNombre().requestFocus(); // Pone el cursor listo para escribir
    }

    private void guardarJugador() {
        if (!validarCampos()) return;

        try {
            String nombre = vista.getTxtNombre().getText().trim();
            int puntaje = Integer.parseInt(vista.getTxtPuntajeMax().getText().trim());

            Jugador nuevoJugador = new Jugador(nombre, puntaje);
            dao.insertar(nuevoJugador);

            mostrarInfo("Jugador guardado exitosamente.");
            prepararNuevoRegistro(); // Limpia y resetea
            actualizarTabla();

        } catch (NumberFormatException e) {
            mostrarError(MSG_ERROR_NUMERO);
        } catch (SQLException e) {
            mostrarError(MSG_ERROR_DB + e.getMessage());
        }
    }

    private void actualizarJugador() {
        if (!validarCampos()) return;
        
        // Validación extra: Asegurar que hay un ID (que viene de la selección)
        String idStr = vista.getTxtId().getText();
        if (idStr.isEmpty()) return;

        try {
            int id = Integer.parseInt(idStr);
            String nombre = vista.getTxtNombre().getText().trim();
            int puntaje = Integer.parseInt(vista.getTxtPuntajeMax().getText().trim());

            Jugador jugadorEditado = new Jugador(id, nombre, puntaje);
            dao.actualizar(jugadorEditado);

            mostrarInfo("Jugador actualizado correctamente.");
            prepararNuevoRegistro();
            actualizarTabla();

        } catch (NumberFormatException e) {
            mostrarError(MSG_ERROR_NUMERO);
        } catch (SQLException e) {
            mostrarError(MSG_ERROR_DB + e.getMessage());
        }
    }

    private void eliminarJugador() {
        String idStr = vista.getTxtId().getText();
        String nombre = vista.getTxtNombre().getText();
        
        if (idStr.isEmpty()) return;

        try {
            int id = Integer.parseInt(idStr);
            
            // Confirmación más detallada
            int resp = JOptionPane.showConfirmDialog(vista, 
                    String.format(MSG_CONFIRM_ELIMINAR, nombre, id), 
                    TITLE_CONFIRM, 
                    JOptionPane.YES_NO_OPTION, 
                    JOptionPane.WARNING_MESSAGE);

            if (resp == JOptionPane.YES_OPTION) {
                dao.eliminar(id);
                mostrarInfo("Jugador eliminado.");
                prepararNuevoRegistro();
                actualizarTabla();
            }

        } catch (NumberFormatException e) {
            mostrarError("ID inválido.");
        } catch (SQLException e) {
            mostrarError(MSG_ERROR_DB + e.getMessage());
        }
    }

    // ================= MÉTODOS AUXILIARES Y UI =================

    /**
     * Pasa los datos de la fila seleccionada a los campos de texto.
     */
    private void cargarDatosDeFila() {
        int fila = vista.getTablaJugadores().getSelectedRow();
        if (fila == -1) return;

        DefaultTableModel modelo = vista.getModeloTabla();
        vista.getTxtId().setText(String.valueOf(modelo.getValueAt(fila, 0)));
        vista.getTxtNombre().setText(String.valueOf(modelo.getValueAt(fila, 1)));
        vista.getTxtPuntajeMax().setText(String.valueOf(modelo.getValueAt(fila, 2)));
    }

    /**
     * Controla qué botones se pueden presionar según el estado.
     * @param haySeleccion true si el usuario seleccionó una fila.
     */
    private void controlarEstadoBotones(boolean haySeleccion) {
        vista.getBtnActualizar().setEnabled(haySeleccion);
        vista.getBtnEliminar().setEnabled(haySeleccion);
        // El botón guardar se suele deshabilitar si estamos editando, 
        // o se deja habilitado para crear uno nuevo a pesar de la selección.
        // Aquí lo dejamos habilitado, pero podrías cambiarlo según tu preferencia.
        vista.getBtnGuardar().setEnabled(!haySeleccion); 
    }

    private boolean validarCampos() {
        if (vista.getTxtNombre().getText().trim().isEmpty() || 
            vista.getTxtPuntajeMax().getText().trim().isEmpty()) {
            mostrarError(MSG_VALIDACION);
            return false;
        }
        return true;
    }

    private void limpiarCampos() {
        vista.getTxtId().setText("");
        vista.getTxtNombre().setText("");
        vista.getTxtPuntajeMax().setText("");
    }

    private void mostrarError(String msg) {
        JOptionPane.showMessageDialog(vista, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void mostrarInfo(String msg) {
        JOptionPane.showMessageDialog(vista, msg, "Información", JOptionPane.INFORMATION_MESSAGE);
    }
}
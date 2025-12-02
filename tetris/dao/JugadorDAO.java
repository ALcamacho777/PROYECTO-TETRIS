package dao;

import db.Conexion;
import model.Jugador;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JugadorDAO {

    // LISTAR TODOS
    public List<Jugador> listarTodos() throws SQLException {
        List<Jugador> lista = new ArrayList<>();

        String sql = "SELECT id, nombre, puntaje_max FROM jugador ORDER BY id";

        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Jugador j = new Jugador();
                j.setId(rs.getInt("id"));
                j.setNombre(rs.getString("nombre"));
                j.setPuntajeMax(rs.getInt("puntaje_max"));
                lista.add(j);
            }
        }

        return lista;
    }

    // INSERTAR
    public void insertar(Jugador j) throws SQLException {
        String sql = "INSERT INTO jugador (nombre, puntaje_max) VALUES (?, ?)";

        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, j.getNombre());
            ps.setInt(2, j.getPuntajeMax());
            ps.executeUpdate();

            // opcional: recuperar ID generado
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    j.setId(rs.getInt(1));
                }
            }
        }
    }

    // ACTUALIZAR
    public void actualizar(Jugador j) throws SQLException {
        String sql = "UPDATE jugador SET nombre = ?, puntaje_max = ? WHERE id = ?";

        try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, j.getNombre());
            ps.setInt(2, j.getPuntajeMax());
            ps.setInt(3, j.getId());

            ps.executeUpdate();
        }
    }

    // ELIMINAR
    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM jugador WHERE id = ?";

        try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    // OPCIONAL: BUSCAR POR ID (útil para tests o lógica extra)
    public Jugador buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, nombre, puntaje_max FROM jugador WHERE id = ?";

        try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Jugador(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getInt("puntaje_max"));
                }
            }
        }
        return null;
    }
}

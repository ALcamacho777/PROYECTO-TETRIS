package dao;

import db.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ScoreDAO {

    private static final String INSERT_SQL = "INSERT INTO scores (nombre, puntaje) VALUES (?, ?)";
    private static final String SELECT_TOP_SQL = "SELECT nombre, puntaje FROM scores ORDER BY puntaje DESC LIMIT 20";

    public void guardarScore(String nombre, int score) {
        try (Connection con = Conexion.getConexion(); 
             PreparedStatement ps = con.prepareStatement(INSERT_SQL)) {

            ps.setString(1, nombre);
            ps.setInt(2, score);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error al guardar el score en la BD");
            e.printStackTrace();
        }
    }

    // Método nuevo para llenar la lista en la interfaz
    public List<String> obtenerTopPuntajes() {
        List<String> lista = new ArrayList<>();
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(SELECT_TOP_SQL);
             ResultSet rs = ps.executeQuery()) {

            int ranking = 1;
            while (rs.next()) {
                String nombre = rs.getString("nombre");
                int puntaje = rs.getInt("puntaje");
                // Formato: "1. Pepe - 5000"
                lista.add(String.format("%d. %s - %d", ranking++, nombre, puntaje));
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener puntajes");
            e.printStackTrace();
        }
        return lista;
    }
}
package dao;

import controlador.ConexionBD;
import modelo.Repartidor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RepartidorDAO {
    //Create
    public boolean guardar(Repartidor repartidor) {
        String sql = "INSERT INTO repartidor (nombre) VALUES (?)";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)
        ) {
            sentencia.setString(1, repartidor.getNombre());
            sentencia.executeUpdate();

            try (ResultSet rs = sentencia.getGeneratedKeys()) {
                if(rs.next()) {
                    repartidor.setId(rs.getInt(1));
                }
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //Read
    public List<Repartidor> listarTodos(){
        List<Repartidor> repartidores = new ArrayList<>();
        String sql = "SELECT id, nombre FROM repartidor";

        try (Connection conexion = ConexionBD.obtenerConexion();
        PreparedStatement sentencia = conexion.prepareStatement(sql);
        ResultSet rs = sentencia.executeQuery()
        ) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                repartidores.add(new Repartidor(id, nombre));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return repartidores;
    }

    //Update
    public boolean actualizar(Repartidor repartidor) {
        String sql = "UPDATE repartidor SET nombre = ? WHERE id = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)
        ) {
            sentencia.setString(1, repartidor.getNombre());
            sentencia.setInt(2, repartidor.getId());
            return sentencia.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //Delete
    public boolean eliminar(int id) {
        String sql = "DELETE FROM repartidor WHERE id = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)
        ) {
            sentencia.setInt(1, id);
            return sentencia.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //Buscar repartidor por id
    public Repartidor buscarPorId(int id) {
        String sql = "SELECT id, nombre FROM repartidor WHERE id = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
        PreparedStatement sentencia = conexion.prepareStatement(sql)
        ) {
            sentencia.setInt(1, id);
            try (ResultSet rs = sentencia.executeQuery()) {
                if (rs.next()) {
                    return new Repartidor(rs.getInt("id"), rs.getString("nombre"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

package dao;

import controlador.ConexionBD;
import modelo.EstadoPedido;
import modelo.Pedido;
import modelo.TipoPedido;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {
    //Create
    public boolean guardar(Pedido pedido) {
        String sql = "INSERT INTO pedido (direccion,tipo, estado) VALUES (?, ?, ?)";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)
        ) {
            sentencia.setString(1,pedido.getDireccionEntrega());
            sentencia.setString(2,pedido.getTipo().name());
            sentencia.setString(3,pedido.getEstado().name());
            sentencia.executeUpdate();

            try (ResultSet rs = sentencia.getGeneratedKeys()) {
                if(rs.next()){
                    pedido.setId(rs.getInt(1));
                }
            }

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //Read
    public List<Pedido> listarTodos(){
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT id, direccion, tipo, estado FROM pedido";

        try (Connection conexion = ConexionBD.obtenerConexion();
        PreparedStatement sentencia = conexion.prepareStatement(sql);
        ResultSet rs = sentencia.executeQuery()
        ) {
            while(rs.next()) {
                int id = rs.getInt("id");
                String direccion = rs.getString("direccion");
                TipoPedido tipo = TipoPedido.valueOf(rs.getString("tipo"));
                EstadoPedido estado = EstadoPedido.valueOf(rs.getString("estado"));
                Pedido pedido = new Pedido(id, direccion, tipo, estado);
                pedidos.add(pedido);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pedidos;
    }

    //Update
    public boolean actualizar(Pedido pedido) {
        String sql = "UPDATE pedido SET direccion = ?, tipo = ?, estado = ? WHERE id = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
        PreparedStatement sentencia = conexion.prepareStatement(sql)
        ) {
            sentencia.setString(1, pedido.getDireccionEntrega());
            sentencia.setString(2, pedido.getTipo().name());
            sentencia.setString(3, pedido.getEstado().name());
            sentencia.setInt(4, pedido.getId());

            return sentencia.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //Delete
    public boolean eliminar(int id) {
        String sql = "DELETE FROM pedido WHERE id = ?";

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

    //Update solo del estado (para evitar el error de inconsistencia de la semana7
    public boolean actualizarEstado(int idPedido, EstadoPedido nuevoEstado) {
        String sql = "UPDATE pedido SET estado = ? WHERE id = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)
        ) {

            sentencia.setString(1, nuevoEstado.name());
            sentencia.setInt(2, idPedido);

            return sentencia.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //Buscar pedido por id
    public Pedido buscarPorId(int id) {
        String sql = "SELECT id, direccion, tipo estado FROM pedido WHERE id = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)
        ) {
            sentencia.setInt(1, id);

            try (ResultSet rs = sentencia.executeQuery()) {
                if (rs.next()) {
                    TipoPedido tipo = TipoPedido.valueOf(rs.getString("tipo"));
                    EstadoPedido estado = EstadoPedido.valueOf(rs.getString("estado"));
                    return new Pedido(rs.getInt("id"), rs.getString("direccion"), tipo, estado);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
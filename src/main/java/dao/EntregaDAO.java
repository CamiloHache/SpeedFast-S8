package dao;


import controlador.ConexionBD;
import modelo.Entrega;
import modelo.EstadoPedido;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class EntregaDAO {

    public boolean guardar(Entrega entrega) {

        String sqlEntrega = """
                INSERT INTO entrega
                (id_pedido, id_repartidor, fecha, hora)
                VALUES (?, ?, ?, ?)
                """;

        String sqlEstado = """
                UPDATE pedido
                SET estado = ?
                WHERE id = ?
                """;
        Connection conexion = null;

        try {
            conexion = ConexionBD.obtenerConexion();
            conexion.setAutoCommit(false);

            try (PreparedStatement sentenciaEntrega = conexion.prepareStatement(sqlEntrega, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement sentenciaEstado = conexion.prepareStatement(sqlEstado)
            ) {
                // 1. Registramos la entrega
                sentenciaEntrega.setInt(1, entrega.getIdPedido());
                sentenciaEntrega.setInt(2, entrega.getIdRepartidor());
                sentenciaEntrega.setObject(3, entrega.getFecha());
                sentenciaEntrega.setObject(4, entrega.getHora());
                sentenciaEntrega.executeUpdate();


                //Y para obtener el ID
                try (ResultSet rs = sentenciaEntrega.getGeneratedKeys()) {

                    if (rs.next()) {
                        entrega.setId(rs.getInt(1));
                    }
                }

                //2. Para cambiar le estado del pedido
                sentenciaEstado.setString(1, EstadoPedido.EN_REPARTO.name());
                sentenciaEstado.setInt(2, entrega.getIdPedido());
                sentenciaEstado.executeUpdate();

                //3. Para confirmar las dos operaciones
                conexion.commit();
                return true;
            }

        } catch (SQLException e) {

            if (conexion != null) {
                try {
                    conexion.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

            e.printStackTrace();
            return false;

        } finally {

            if (conexion != null) {
                try {
                    conexion.setAutoCommit(true);
                    conexion.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<Entrega> listarTodos() {

        List<Entrega> entregas = new ArrayList<>();

        String sql = """
                SELECT id, id_pedido, id_repartidor, fecha, hora
                FROM entrega
                """;

        try (
                Connection conexion = ConexionBD.obtenerConexion();
                PreparedStatement sentencia = conexion.prepareStatement(sql);
                ResultSet rs = sentencia.executeQuery()
        ) {

            while (rs.next()) {
                LocalDate fecha = rs.getObject("fecha", LocalDate.class);
                LocalTime hora = rs.getObject("hora", LocalTime.class);
                Entrega entrega = new Entrega(rs.getInt("id"), rs.getInt("id_pedido"), rs.getInt("id_repartidor"), fecha, hora);
                entregas.add(entrega);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return entregas;
    }
}

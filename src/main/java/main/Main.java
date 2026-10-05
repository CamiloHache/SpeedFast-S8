package main;

import dao.EntregaDAO;
import dao.PedidoDAO;
import dao.RepartidorDAO;
import modelo.Entrega;
import modelo.EstadoPedido;
import modelo.Pedido;
import modelo.Repartidor;
import modelo.TipoPedido;

import java.time.LocalDate;
import java.time.LocalTime;

public class Main {

    public static void main(String[] args) {

        System.out.println("=================================");
        System.out.println("PRUEBA COMPLETA DE ENTREGA");
        System.out.println("=================================");

        PedidoDAO pedidoDAO = new PedidoDAO();
        RepartidorDAO repartidorDAO = new RepartidorDAO();
        EntregaDAO entregaDAO = new EntregaDAO();

        // 1. Crear pedido
        System.out.println();
        System.out.println("===== 1. CREAR PEDIDO =====");

        Pedido pedido = new Pedido("Av. Providencia 123", TipoPedido.COMIDA, EstadoPedido.PENDIENTE);
        boolean pedidoGuardado = pedidoDAO.guardar(pedido);

        System.out.println("Pedido guardado: " + pedidoGuardado);
        System.out.println("ID pedido: " + pedido.getId());

        // 2. Crear repartidor
        System.out.println();
        System.out.println("===== 2. CREAR REPARTIDOR =====");

        Repartidor repartidor = new Repartidor("Carlos Soto");
        boolean repartidorGuardado = repartidorDAO.guardar(repartidor);

        System.out.println("Repartidor guardado: " + repartidorGuardado);
        System.out.println("ID repartidor: " + repartidor.getId());

        // 3. Crear entrega
        System.out.println();
        System.out.println("===== 3. CREAR ENTREGA =====");

        Entrega entrega = new Entrega(pedido.getId(), repartidor.getId(), LocalDate.now(), LocalTime.now());
        boolean entregaGuardada = entregaDAO.guardar(entrega);

        System.out.println("Entrega guardada: " + entregaGuardada);
        System.out.println("ID entrega: " + entrega.getId());

        // 4. Comprobar estado del pedido
        System.out.println();
        System.out.println("===== 4. COMPROBAR ESTADO =====");

        Pedido pedidoActualizado = pedidoDAO.buscarPorId(pedido.getId());

        if (pedidoActualizado != null) {
            System.out.println("Pedido ID: " + pedidoActualizado.getId());
            System.out.println("Estado: " + pedidoActualizado.getEstado());
        }

        // 5. Listar entregas
        System.out.println();
        System.out.println("===== 5. LISTAR ENTREGAS =====");

        for (Entrega e : entregaDAO.listarTodos()) {
            System.out.println("Entrega ID: " + e.getId() + " | Pedido: " + e.getIdPedido() + " | Repartidor: " + e.getIdRepartidor() + " | Fecha: " + e.getFecha() + " | Hora: " + e.getHora());
        }

        System.out.println();
        System.out.println("=================================");
        System.out.println("FIN DE LA PRUEBA");
        System.out.println("=================================");
    }
}



/*
Prueba 3
package main;

import dao.RepartidorDAO;
import modelo.Repartidor;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        RepartidorDAO repartidorDAO = new RepartidorDAO();

        System.out.println("===== PRUEBA GUARDAR REPARTIDOR =====");

        Repartidor repartidor =
                new Repartidor("Juan Pérez");

        boolean guardado = repartidorDAO.guardar(repartidor);

        System.out.println("Guardado: " + guardado);
        System.out.println("ID generado por MySQL: " + repartidor.getId());

        System.out.println();
        System.out.println("===== PRUEBA LISTAR REPARTIDORES =====");

        List<Repartidor> repartidores =
                repartidorDAO.listarTodos();

        for (Repartidor r : repartidores) {

            System.out.println(
                    "ID: " + r.getId()
                            + " | Nombre: " + r.getNombre()
            );
        }

        System.out.println();
        System.out.println("===== PRUEBA ACTUALIZAR =====");

        repartidor.setNombre("Juan Pérez Actualizado");

        boolean actualizado =
                repartidorDAO.actualizar(repartidor);

        System.out.println("Actualizado: " + actualizado);

        Repartidor buscado =
                repartidorDAO.buscarPorId(repartidor.getId());

        if (buscado != null) {

            System.out.println(
                    "Encontrado → ID: "
                            + buscado.getId()
                            + " | Nombre: "
                            + buscado.getNombre()
            );
        }

        System.out.println();
        System.out.println("===== PRUEBA ELIMINAR =====");

        boolean eliminado =
                repartidorDAO.eliminar(repartidor.getId());

        System.out.println("Eliminado: " + eliminado);
    }
}
*/


/*
Prueba 2
package main;

import dao.PedidoDAO;
import modelo.EstadoPedido;
import modelo.Pedido;
import modelo.TipoPedido;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        PedidoDAO pedidoDAO = new PedidoDAO();

        System.out.println("===== PRUEBA GUARDAR =====");

        Pedido pedido = new Pedido(
                "Av. Providencia 123",
                TipoPedido.COMIDA,
                EstadoPedido.PENDIENTE
        );

        boolean guardado = pedidoDAO.guardar(pedido);

        System.out.println("Guardado: " + guardado);
        System.out.println("ID generado por MySQL: " + pedido.getId());

        System.out.println();
        System.out.println("===== PRUEBA LISTAR =====");

        List<Pedido> pedidos = pedidoDAO.listarTodos();

        for (Pedido p : pedidos) {
            System.out.println(
                    "ID: " + p.getId()
                            + " | Dirección: " + p.getDireccionEntrega()
                            + " | Tipo: " + p.getTipo()
                            + " | Estado: " + p.getEstado()
            );
        }

        System.out.println();
        System.out.println("===== PRUEBA ACTUALIZAR ESTADO =====");

        boolean actualizado =
                pedidoDAO.actualizarEstado(
                        pedido.getId(),
                        EstadoPedido.EN_REPARTO
                );

        System.out.println("Estado actualizado: " + actualizado);

        Pedido pedidoBuscado = pedidoDAO.buscarPorId(pedido.getId());

        if (pedidoBuscado != null) {

            System.out.println(
                    "Pedido encontrado → ID: "
                            + pedidoBuscado.getId()
                            + " | Estado: "
                            + pedidoBuscado.getEstado()
            );
        }
    }
}

*/

/*
Prueba 1
package main;

import controlador.ConexionBD;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {

        try (Connection conexion = ConexionBD.obtenerConexion()) {

            System.out.println("================================");
            System.out.println("CONEXIÓN EXITOSA A MYSQL");
            System.out.println("Base de datos: " + conexion.getCatalog());
            System.out.println("================================");

        } catch (SQLException e) {

            System.out.println("ERROR DE CONEXIÓN");
            e.printStackTrace();
        }
    }
}

 */

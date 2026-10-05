package vista;

import dao.PedidoDAO;
import modelo.EstadoPedido;
import modelo.Pedido;
import modelo.TipoPedido;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VentanaListaPedidos extends JFrame {

    private JTable tablaPedidos;
    private DefaultTableModel modeloTabla;
    private PedidoDAO pedidoDAO;

    public VentanaListaPedidos() {

        pedidoDAO = new PedidoDAO();
        setTitle("SpeedFast - Pedidos");
        setSize(800, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        construirVentana();
    }

    private void construirVentana() {

        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        JLabel titulo = new JLabel("Pedidos Registrados", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        panelPrincipal.add(titulo, BorderLayout.NORTH);

        String[] columnas = {"ID", "Dirección", "Tipo", "Estado"};

        modeloTabla = new DefaultTableModel(columnas, 0) {

            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaPedidos = new JTable(modeloTabla);
        tablaPedidos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(tablaPedidos);
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel();
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");

        panelBotones.add(btnActualizar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);

        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        btnActualizar.addActionListener(e -> cargarTabla());
        btnEditar.addActionListener(e -> editarPedido());
        btnEliminar.addActionListener(e -> eliminarPedido());

        setContentPane(panelPrincipal);
        cargarTabla();
    }

    private void cargarTabla() {

        modeloTabla.setRowCount(0);

        List<Pedido> pedidos = pedidoDAO.readAll();

        for (Pedido pedido : pedidos) {

            modeloTabla.addRow(
                    new Object[]{
                            pedido.getId(),
                            pedido.getDireccionEntrega(),
                            pedido.getTipo(),
                            pedido.getEstado()
                    }
            );
        }
    }

    private void editarPedido() {

        int filaSeleccionada = tablaPedidos.getSelectedRow();

        if (filaSeleccionada == -1) {

            JOptionPane.showMessageDialog(this, "Debe seleccionar un pedido.", "Pedido no seleccionado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id =
                (int) modeloTabla.getValueAt(filaSeleccionada, 0);

        Pedido pedido = pedidoDAO.buscarPorId(id);

        if (pedido == null) {

            JOptionPane.showMessageDialog(this, "No se encontró el pedido.", "Error", JOptionPane.ERROR_MESSAGE);

            return;
        }

        JTextField txtDireccion = new JTextField(pedido.getDireccionEntrega());
        JComboBox<TipoPedido> cmbTipo = new JComboBox<>(TipoPedido.values());

        cmbTipo.setSelectedItem(pedido.getTipo());
        JComboBox<EstadoPedido> cmbEstado = new JComboBox<>(EstadoPedido.values());

        cmbEstado.setSelectedItem(pedido.getEstado());
        JPanel formulario = new JPanel(new GridLayout(3, 2, 10, 10));

        formulario.add(new JLabel("Dirección:"));
        formulario.add(txtDireccion);
        formulario.add(new JLabel("Tipo:"));
        formulario.add(cmbTipo);
        formulario.add(new JLabel("Estado:"));
        formulario.add(cmbEstado);

        int resultado = JOptionPane.showConfirmDialog(this, formulario, "Editar Pedido #" + id, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (resultado != JOptionPane.OK_OPTION) {
            return;
        }

        String direccion = txtDireccion.getText().trim();

        if (direccion.isEmpty()) {

            JOptionPane.showMessageDialog(this, "La dirección es obligatoria.", "Dato inválido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        TipoPedido tipo = (TipoPedido) cmbTipo.getSelectedItem();
        EstadoPedido estado = (EstadoPedido) cmbEstado.getSelectedItem();

        pedido.setDireccionEntrega(direccion);
        pedido.setTipo(tipo);
        pedido.setEstado(estado);

        boolean actualizado =
                pedidoDAO.update(pedido);

        if (actualizado) {

            JOptionPane.showMessageDialog(this, "Pedido actualizado correctamente.");
            cargarTabla();

        } else {

            JOptionPane.showMessageDialog(this, "No fue posible actualizar el pedido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarPedido() {

        int filaSeleccionada = tablaPedidos.getSelectedRow();

        if (filaSeleccionada == -1) {

            JOptionPane.showMessageDialog(this, "Debe seleccionar un pedido.", "Pedido no seleccionado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Desea eliminar el pedido #" + id + "?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        boolean eliminado = pedidoDAO.delete(id);

        if (eliminado) {

            JOptionPane.showMessageDialog(this, "Pedido eliminado correctamente.");
            cargarTabla();

        } else {
            JOptionPane.showMessageDialog(this, "No fue posible eliminar el pedido.\n" + "Puede que tenga una entrega asociada.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
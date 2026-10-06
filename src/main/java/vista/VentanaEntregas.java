package vista;

import dao.EntregaDAO;
import dao.PedidoDAO;
import dao.RepartidorDAO;
import modelo.Entrega;
import modelo.Pedido;
import modelo.Repartidor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class VentanaEntregas extends JFrame {

    private JComboBox<Pedido> cmbPedido;
    private JComboBox<Repartidor> cmbRepartidor;
    private JTextField txtFecha;
    private JTextField txtHora;

    private JTable tablaEntregas;
    private DefaultTableModel modeloTabla;

    private final EntregaDAO entregaDAO;
    private final PedidoDAO pedidoDAO;
    private final RepartidorDAO repartidorDAO;

    public VentanaEntregas() {

        entregaDAO = new EntregaDAO();
        pedidoDAO = new PedidoDAO();
        repartidorDAO = new RepartidorDAO();

        setTitle("SpeedFast - Entregas");
        setSize(850, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        construirVentana();
    }

    private void construirVentana() {

        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titulo = new JLabel("Gestión de Entregas", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        panelPrincipal.add(titulo, BorderLayout.NORTH);

        // TABLA

        String[] columnas = {
                "ID",
                "Pedido",
                "Repartidor",
                "Fecha",
                "Hora"
        };

        modeloTabla =
                new DefaultTableModel(columnas, 0) {

                    @Override
                    public boolean isCellEditable(int fila, int columna) {
                        return false;
                    }
                };

        tablaEntregas = new JTable(modeloTabla);
        tablaEntregas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panelPrincipal.add(new JScrollPane(tablaEntregas), BorderLayout.CENTER);

        // FORMULARIO

        JPanel panelFormulario = new JPanel(new GridLayout(4, 2, 10, 10));
        panelFormulario.add(new JLabel("Pedido:"));

        cmbPedido = new JComboBox<>();

        panelFormulario.add(cmbPedido);
        panelFormulario.add(new JLabel("Repartidor:"));

        cmbRepartidor = new JComboBox<>();

        panelFormulario.add(cmbRepartidor);
        panelFormulario.add(new JLabel("Fecha (AAAA-MM-DD):"));

        txtFecha = new JTextField();

        panelFormulario.add(txtFecha);
        panelFormulario.add(new JLabel("Hora (HH:MM):"));

        txtHora = new JTextField();

        panelFormulario.add(txtHora);
        panelPrincipal.add(panelFormulario, BorderLayout.WEST);

        // BOTONES

        JPanel panelBotones = new JPanel();
        JButton btnRegistrar = new JButton("Registrar");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");

        panelBotones.add(btnRegistrar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);

        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
        btnRegistrar.addActionListener(e -> registrarEntrega());
        btnActualizar.addActionListener(e -> cargarTabla());
        btnEditar.addActionListener(e -> editarEntrega());
        btnEliminar.addActionListener(e -> eliminarEntrega());

        setContentPane(panelPrincipal);

        cargarCombos();
        cargarTabla();

        txtFecha.setText(LocalDate.now().toString());
        txtHora.setText(LocalTime.now().withSecond(0).withNano(0).toString());
    }

    private void cargarCombos() {

        cmbPedido.removeAllItems();
        cmbRepartidor.removeAllItems();

        List<Pedido> pedidos = pedidoDAO.readAll();
        List<Repartidor> repartidores = repartidorDAO.readAll();

        for (Pedido pedido : pedidos) {
            cmbPedido.addItem(pedido);
        }

        for (Repartidor repartidor : repartidores) {
            cmbRepartidor.addItem(repartidor);
        }
    }

    private void cargarTabla() {

        modeloTabla.setRowCount(0);

        List<Entrega> entregas = entregaDAO.readAll();

        for (Entrega entrega : entregas) {

            modeloTabla.addRow(
                    new Object[]{
                            entrega.getId(),
                            entrega.getIdPedido(),
                            entrega.getIdRepartidor(),
                            entrega.getFecha(),
                            entrega.getHora()
                    }
            );
        }
    }

    private void registrarEntrega() {

        Pedido pedidoSeleccionado = (Pedido) cmbPedido.getSelectedItem();
        Repartidor repartidorSeleccionado = (Repartidor) cmbRepartidor.getSelectedItem();
        String fechaTexto = txtFecha.getText().trim();
        String horaTexto = txtHora.getText().trim();

        if (pedidoSeleccionado == null) {

            JOptionPane.showMessageDialog(this, "Debe seleccionar un pedido.", "Dato obligatorio", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (repartidorSeleccionado == null) {

            JOptionPane.showMessageDialog(this, "Debe seleccionar un repartidor.", "Dato obligatorio", JOptionPane.WARNING_MESSAGE);
            return;
        }

        LocalDate fecha;
        LocalTime hora;

        try {

            fecha = LocalDate.parse(fechaTexto);
            hora = LocalTime.parse(horaTexto);

        } catch (Exception e) {

            JOptionPane.showMessageDialog(this, "La fecha debe tener formato AAAA-MM-DD\n" + "y la hora HH:MM.", "Formato inválido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Entrega entrega = new Entrega(pedidoSeleccionado.getId(), repartidorSeleccionado.getId(), fecha, hora);
        boolean creada = entregaDAO.create(entrega);

        if (creada) {

            JOptionPane.showMessageDialog(this, "Entrega registrada correctamente.\n" + "ID generado: " + entrega.getId());
            cargarTabla();

        } else {

            JOptionPane.showMessageDialog(this, "No fue posible registrar la entrega.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editarEntrega() {

        int fila = tablaEntregas.getSelectedRow();

        if (fila == -1) {

            JOptionPane.showMessageDialog(this, "Seleccione una entrega.");
            return;
        }

        int id = (int) modeloTabla.getValueAt(fila, 0);
        Entrega entregaSeleccionada = null;

        for (Entrega entrega : entregaDAO.readAll()) {

            if (entrega.getId() == id) {
                entregaSeleccionada = entrega;
                break;
            }
        }

        if (entregaSeleccionada == null) {
            return;
        }

        seleccionarPedido(entregaSeleccionada.getIdPedido());
        seleccionarRepartidor(entregaSeleccionada.getIdRepartidor());

        txtFecha.setText(entregaSeleccionada.getFecha().toString());
        txtHora.setText(entregaSeleccionada.getHora().withSecond(0).withNano(0).toString());

        int respuesta =
                JOptionPane.showConfirmDialog(this, "Los datos quedaron cargados en el formulario.\n" + "¿Desea actualizar esta entrega?", "Editar entrega", JOptionPane.YES_NO_OPTION);

        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }

        Pedido pedido = (Pedido) cmbPedido.getSelectedItem();

        Repartidor repartidor = (Repartidor) cmbRepartidor.getSelectedItem();

        if (pedido == null || repartidor == null) {
            return;
        }

        try {

            LocalDate fecha = LocalDate.parse(txtFecha.getText().trim());
            LocalTime hora = LocalTime.parse(txtHora.getText().trim());
            Entrega nuevaEntrega = new Entrega(id, pedido.getId(), repartidor.getId(), fecha, hora);

            if (entregaDAO.update(nuevaEntrega)) {

                JOptionPane.showMessageDialog(this, "Entrega actualizada correctamente.");
                cargarTabla();

            } else {

                JOptionPane.showMessageDialog(this, "No fue posible actualizar la entrega.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(this, "Fecha u hora inválida.", "Dato inválido", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void seleccionarPedido(int idPedido) {

        for (int i = 0; i < cmbPedido.getItemCount(); i++) {

            Pedido pedido = cmbPedido.getItemAt(i);

            if (pedido.getId() == idPedido) {

                cmbPedido.setSelectedIndex(i);
                return;
            }
        }
    }

    private void seleccionarRepartidor(int idRepartidor) {

        for (int i = 0;
             i < cmbRepartidor.getItemCount();
             i++) {

            Repartidor repartidor = cmbRepartidor.getItemAt(i);

            if (repartidor.getId() == idRepartidor) {

                cmbRepartidor.setSelectedIndex(i);
                return;
            }
        }
    }

    private void eliminarEntrega() {

        int fila = tablaEntregas.getSelectedRow();

        if (fila == -1) {

            JOptionPane.showMessageDialog(this, "Seleccione una entrega.");
            return;
        }

        int id =
                (int) modeloTabla.getValueAt(fila, 0);

        int respuesta =
                JOptionPane.showConfirmDialog(this, "¿Desea eliminar la entrega #" + id + "?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }

        if (entregaDAO.delete(id)) {

            JOptionPane.showMessageDialog(this, "Entrega eliminada correctamente.");
            cargarTabla();

        } else {

            JOptionPane.showMessageDialog(this, "No fue posible eliminar la entrega.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
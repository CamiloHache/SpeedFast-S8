package vista;

import dao.PedidoDAO;
import modelo.EstadoPedido;
import modelo.Pedido;
import modelo.TipoPedido;

import javax.swing.*;
import java.awt.*;

public class VentanaRegistroPedido extends JFrame {

    private JTextField txtDireccion;
    private JComboBox<TipoPedido> cmbTipo;
    private final PedidoDAO pedidoDAO;

    public VentanaRegistroPedido() {

        pedidoDAO = new PedidoDAO();

        setTitle("SpeedFast - Registrar Pedido");
        setSize(450, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        construirVentana();
    }

    private void construirVentana() {

        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titulo = new JLabel("Registrar nuevo pedido", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        panelPrincipal.add(titulo, BorderLayout.NORTH);

        JPanel formulario = new JPanel(new GridLayout(2, 2, 10, 15));
        formulario.add(new JLabel("Dirección:"));
        txtDireccion = new JTextField();
        formulario.add(txtDireccion);
        formulario.add(new JLabel("Tipo de pedido:"));

        cmbTipo = new JComboBox<>(TipoPedido.values());
        formulario.add(cmbTipo);
        panelPrincipal.add(formulario, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel();
        JButton btnGuardar = new JButton("Guardar");
        JButton btnLimpiar = new JButton("Limpiar");

        panelBotones.add(btnGuardar);
        panelBotones.add(btnLimpiar);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        btnGuardar.addActionListener(e -> guardarPedido());
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        setContentPane(panelPrincipal);
    }

    private void guardarPedido() {

        String direccion = txtDireccion.getText().trim();

        if (direccion.isEmpty()) {

            JOptionPane.showMessageDialog(this, "La dirección es obligatoria.", "Campo obligatorio", JOptionPane.WARNING_MESSAGE);
            txtDireccion.requestFocus();
            return;
        }

        TipoPedido tipo = (TipoPedido) cmbTipo.getSelectedItem();

        if (tipo == null) {

            JOptionPane.showMessageDialog(this, "Debe seleccionar un tipo de pedido.", "Dato obligatorio", JOptionPane.WARNING_MESSAGE);
            return;
        }

        /*
         * El estado inicial siempre será PENDIENTE.
         * El ID NO se solicita al usuario.
         * MySQL lo genera automáticamente y así se evita el riesgo a redundancia o inconsistencia como en la S7.
         */

        Pedido pedido = new Pedido(direccion, tipo, EstadoPedido.PENDIENTE);
        boolean creado = pedidoDAO.create(pedido);

        if (creado) {

            JOptionPane.showMessageDialog(this, "Pedido registrado correctamente.\n" + "ID generado: " + pedido.getId(), "Pedido registrado", JOptionPane.INFORMATION_MESSAGE);
            limpiarFormulario();

        } else {

            JOptionPane.showMessageDialog(this, "No fue posible registrar el pedido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarFormulario() {

        txtDireccion.setText("");
        cmbTipo.setSelectedIndex(0);
        txtDireccion.requestFocus();
    }
}
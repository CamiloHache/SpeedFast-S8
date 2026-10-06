package vista;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {

    private JButton btnRegistrarPedido;
    private JButton btnListarPedidos;
    private JButton btnRepartidores;
    private JButton btnEntregas;
    private JButton btnSalir;

    public VentanaPrincipal() {

        setTitle("SpeedFast - Gestión de Entregas");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        construirVentana();
    }

    private void construirVentana() {

        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titulo = new JLabel("SpeedFast - Gestión de Entregas", SwingConstants.CENTER);

        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        panelPrincipal.add(titulo, BorderLayout.NORTH);
        JPanel panelBotones = new JPanel(new GridLayout(5, 1, 10, 10));

        btnRegistrarPedido = new JButton("Registrar Pedido");
        btnListarPedidos = new JButton("Listar Pedidos");
        btnRepartidores = new JButton("Gestionar Repartidores");
        btnEntregas = new JButton("Gestionar Entregas");
        btnSalir = new JButton("Salir");

        panelBotones.add(btnRegistrarPedido);
        panelBotones.add(btnListarPedidos);
        panelBotones.add(btnRepartidores);
        panelBotones.add(btnEntregas);
        panelBotones.add(btnSalir);

        panelPrincipal.add(panelBotones, BorderLayout.CENTER);

        // Registrar pedido
        btnRegistrarPedido.addActionListener(e -> {

            VentanaRegistroPedido ventana = new VentanaRegistroPedido();
            ventana.setVisible(true);
        });

        // Listar pedidos
        btnListarPedidos.addActionListener(e -> {

            VentanaListaPedidos ventana = new VentanaListaPedidos();
            ventana.setVisible(true);
        });

        btnRepartidores.addActionListener(e -> {

            VentanaRepartidores ventana = new VentanaRepartidores();
            ventana.setVisible(true);
        });

        btnEntregas.addActionListener(e -> {
            VentanaEntregas ventana = new VentanaEntregas();
            ventana.setVisible(true);
        });

        btnSalir.addActionListener(e -> System.exit(0));

        setContentPane(panelPrincipal);
    }
}
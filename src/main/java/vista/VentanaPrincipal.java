package vista;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {

    private JButton btnPedidos;
    private JButton btnRepartidores;
    private JButton btnEntregas;
    private JButton btnSalir;

    public VentanaPrincipal() {

        setTitle("SpeedFast - Gestión de Entregas");
        setSize(500, 350);
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
        JPanel panelBotones = new JPanel(new GridLayout(4, 1, 10, 10));

        btnPedidos = new JButton("Gestionar Pedidos");
        btnRepartidores = new JButton("Gestionar Repartidores");
        btnEntregas = new JButton("Gestionar Entregas");
        btnSalir = new JButton("Salir");

        panelBotones.add(btnPedidos);
        panelBotones.add(btnRepartidores);
        panelBotones.add(btnEntregas);
        panelBotones.add(btnSalir);

        panelPrincipal.add(panelBotones, BorderLayout.CENTER);

        btnSalir.addActionListener(e -> System.exit(0));

        setContentPane(panelPrincipal);
    }
}
package vista;

import dao.RepartidorDAO;
import modelo.Repartidor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VentanaRepartidores extends JFrame {

    private JTable tablaRepartidores;
    private DefaultTableModel modeloTabla;
    private JTextField txtNombre;

    private final RepartidorDAO repartidorDAO;

    public VentanaRepartidores() {

        repartidorDAO = new RepartidorDAO();

        setTitle("SpeedFast - Repartidores");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        construirVentana();
    }

    private void construirVentana() {

        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        JLabel titulo = new JLabel("Gestión de Repartidores", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        panelPrincipal.add(titulo, BorderLayout.NORTH);

        // Formulario
        JPanel panelFormulario = new JPanel(new FlowLayout());
        panelFormulario.add(new JLabel("Nombre:"));
        txtNombre = new JTextField(20);
        panelFormulario.add(txtNombre);

        JButton btnAgregar = new JButton("Agregar");
        panelFormulario.add(btnAgregar);
        panelPrincipal.add(panelFormulario, BorderLayout.SOUTH);

        // Tabla
        String[] columnas = {"ID", "Nombre"};

        modeloTabla = new DefaultTableModel(columnas, 0) {

                    @Override
                    public boolean isCellEditable(int fila, int columna) {
                        return false;
                    }
                };

        tablaRepartidores = new JTable(modeloTabla);
        tablaRepartidores.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panelPrincipal.add(new JScrollPane(tablaRepartidores), BorderLayout.CENTER);

        // Botones adicionales
        JPanel panelBotones = new JPanel();
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");

        panelBotones.add(btnActualizar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelPrincipal.add(panelBotones, BorderLayout.NORTH);

        btnAgregar.addActionListener(e -> agregarRepartidor());
        btnActualizar.addActionListener(e -> cargarTabla());
        btnEditar.addActionListener(e -> editarRepartidor());
        btnEliminar.addActionListener(e -> eliminarRepartidor());

        setContentPane(panelPrincipal);

        cargarTabla();
    }

    private void agregarRepartidor() {

        String nombre = txtNombre.getText().trim();

        if (nombre.isEmpty()) {

            JOptionPane.showMessageDialog(this, "Debe ingresar un nombre.", "Campo obligatorio", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Repartidor repartidor = new Repartidor(nombre);

        if (repartidorDAO.create(repartidor)) {

            JOptionPane.showMessageDialog(this, "Repartidor registrado.\nID generado: " + repartidor.getId());
            txtNombre.setText("");
            cargarTabla();

        } else {

            JOptionPane.showMessageDialog(this, "No fue posible registrar el repartidor.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarTabla() {

        modeloTabla.setRowCount(0);

        List<Repartidor> repartidores = repartidorDAO.readAll();

        for (Repartidor repartidor : repartidores) {

            modeloTabla.addRow(new Object[]{repartidor.getId(), repartidor.getNombre()});
        }
    }

    private void editarRepartidor() {

        int fila = tablaRepartidores.getSelectedRow();

        if (fila == -1) {

            JOptionPane.showMessageDialog(this, "Seleccione un repartidor.");
            return;
        }

        int id = (int) modeloTabla.getValueAt(fila, 0);

        Repartidor repartidor = repartidorDAO.buscarPorId(id);

        if (repartidor == null) {
            return;
        }

        String nuevoNombre =
                JOptionPane.showInputDialog(this, "Nuevo nombre:", repartidor.getNombre());

        if (nuevoNombre == null) {
            return;
        }

        nuevoNombre = nuevoNombre.trim();

        if (nuevoNombre.isEmpty()) {

            JOptionPane.showMessageDialog(this, "El nombre es obligatorio.");
            return;
        }

        repartidor.setNombre(nuevoNombre);

        if (repartidorDAO.update(repartidor)) {

            JOptionPane.showMessageDialog(this, "Repartidor actualizado.");
            cargarTabla();

        } else {

            JOptionPane.showMessageDialog(this, "No fue posible actualizar.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarRepartidor() {

        int fila = tablaRepartidores.getSelectedRow();

        if (fila == -1) {

            JOptionPane.showMessageDialog(this, "Seleccione un repartidor.");
            return;
        }

        int id = (int) modeloTabla.getValueAt(fila, 0);
        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Eliminar el repartidor seleccionado?", "Confirmar", JOptionPane.YES_NO_OPTION);

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        if (repartidorDAO.delete(id)) {

            JOptionPane.showMessageDialog(this, "Repartidor eliminado.");
            cargarTabla();

        } else {

            JOptionPane.showMessageDialog(this, "No fue posible eliminarlo.\n" + "Puede tener una entrega asociada.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
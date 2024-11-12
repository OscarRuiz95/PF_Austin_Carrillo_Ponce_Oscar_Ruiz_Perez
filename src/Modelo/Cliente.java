package Modelo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Cliente extends JFrame {

    private static final String url = "jdbc:mysql://localhost:3306/verdureria";
    private static final String usuario = "root";
    private static final String contrasena = "208240625";

    private JTextField cedulaField, nombre1Field, nombre2Field, apellido1Field, apellido2Field, telefonoField;
    private JTextArea resultadoArea;
    private JButton insertarButton, consultarButton, eliminarButton, actualizarButton;

    public Cliente() {
        setTitle("Gestión de Clientes");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel inputPanel = new JPanel(new GridLayout(7, 2));
        inputPanel.add(new JLabel("Cédula:"));
        cedulaField = new JTextField();
        inputPanel.add(cedulaField);

        inputPanel.add(new JLabel("Nombre 1:"));
        nombre1Field = new JTextField();
        inputPanel.add(nombre1Field);

        inputPanel.add(new JLabel("Nombre 2:"));
        nombre2Field = new JTextField();
        inputPanel.add(nombre2Field);

        inputPanel.add(new JLabel("Apellido 1:"));
        apellido1Field = new JTextField();
        inputPanel.add(apellido1Field);

        inputPanel.add(new JLabel("Apellido 2:"));
        apellido2Field = new JTextField();
        inputPanel.add(apellido2Field);

        inputPanel.add(new JLabel("Teléfono:"));
        telefonoField = new JTextField();
        inputPanel.add(telefonoField);

        insertarButton = new JButton("Insertar Cliente");
        consultarButton = new JButton("Consultar Cliente");
        eliminarButton = new JButton("Eliminar Cliente");
        actualizarButton = new JButton("Actualizar Cliente");

        JPanel buttonPanel = new JPanel(new GridLayout(1, 4));
        buttonPanel.add(insertarButton);
        buttonPanel.add(consultarButton);
        buttonPanel.add(eliminarButton);
        buttonPanel.add(actualizarButton);

        resultadoArea = new JTextArea(8, 30);
        resultadoArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultadoArea);

        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        // Action Listeners
        insertarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                insertarCliente();
            }
        });

        consultarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                consultarCliente();
            }
        });

        eliminarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                eliminarCliente();
            }
        });

        actualizarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actualizarCliente();
            }
        });
    }

    private void insertarCliente() {
        String cedula = cedulaField.getText();
        String nombre1 = nombre1Field.getText();
        String nombre2 = nombre2Field.getText();
        String apellido1 = apellido1Field.getText();
        String apellido2 = apellido2Field.getText();
        String telefono = telefonoField.getText();

        try (Connection conexion = DriverManager.getConnection(url, usuario, contrasena)) {
            String consultaSQL = "{CALL insertar_cliente(?, ?, ?, ?, ?, ?)}";
            CallableStatement llamada = conexion.prepareCall(consultaSQL);
            llamada.setString(1, cedula);
            llamada.setString(2, nombre1);
            llamada.setString(3, nombre2);
            llamada.setString(4, apellido1);
            llamada.setString(5, apellido2);
            llamada.setString(6, telefono);

            llamada.executeUpdate();
            resultadoArea.setText("Cliente insertado correctamente.");
            llamada.close();
        } catch (SQLException e) {
            resultadoArea.setText("Error al insertar el cliente: " + e.getMessage());
        }
    }

    private void consultarCliente() {
        String cedula = cedulaField.getText();

        try (Connection conexion = DriverManager.getConnection(url, usuario, contrasena)) {
            String consultaSQL = "{CALL consultar_cliente(?)}";
            CallableStatement llamada = conexion.prepareCall(consultaSQL);
            llamada.setString(1, cedula);

            ResultSet resultado = llamada.executeQuery();
            if (resultado.next()) {
                String mensaje = String.format(
                        "Cédula: %s\nNombre 1: %s\nNombre 2: %s\nApellido 1: %s\nApellido 2: %s\nTeléfono: %s",
                        resultado.getString("cedula"),
                        resultado.getString("nombre1"),
                        resultado.getString("nombre2"),
                        resultado.getString("apellido1"),
                        resultado.getString("apellido2"),
                        resultado.getString("telefono"));
                resultadoArea.setText(mensaje);
            } else {
                resultadoArea.setText("No se encontró un cliente con la cédula: " + cedula);
            }
            resultado.close();
            llamada.close();
        } catch (SQLException e) {
            resultadoArea.setText("Error al consultar el cliente: " + e.getMessage());
        }
    }

    private void eliminarCliente() {
        String cedula = cedulaField.getText();

        try (Connection conexion = DriverManager.getConnection(url, usuario, contrasena)) {
            String consultaSQL = "{CALL eliminar_cliente(?)}";
            CallableStatement llamada = conexion.prepareCall(consultaSQL);
            llamada.setString(1, cedula);

            int filasAfectadas = llamada.executeUpdate();
            if (filasAfectadas > 0) {
                resultadoArea.setText("Cliente con cédula " + cedula + " eliminado correctamente.");
            } else {
                resultadoArea.setText("No se encontró un cliente con la cédula: " + cedula);
            }
            llamada.close();
        } catch (SQLException e) {
            resultadoArea.setText("Error al eliminar el cliente: " + e.getMessage());
        }
    }

    private void actualizarCliente() {
        String cedula = cedulaField.getText();
        String nombre1 = nombre1Field.getText();
        String nombre2 = nombre2Field.getText();
        String apellido1 = apellido1Field.getText();
        String apellido2 = apellido2Field.getText();
        String telefono = telefonoField.getText();

        try (Connection conexion = DriverManager.getConnection(url, usuario, contrasena)) {
            String consultaSQL = "{CALL update_cliente(?, ?, ?, ?, ?, ?)}";
            CallableStatement llamada = conexion.prepareCall(consultaSQL);
            llamada.setString(1, cedula);
            llamada.setString(2, nombre1);
            llamada.setString(3, nombre2);
            llamada.setString(4, apellido1);
            llamada.setString(5, apellido2);
            llamada.setString(6, telefono);

            int filasAfectadas = llamada.executeUpdate();
            if (filasAfectadas > 0) {
                resultadoArea.setText("Cliente con cédula " + cedula + " actualizado correctamente.");
            } else {
                resultadoArea.setText("No se encontró un cliente con la cédula: " + cedula);
            }
            llamada.close();
        } catch (SQLException e) {
            resultadoArea.setText("Error al actualizar el cliente: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Cliente frame = new Cliente();
            frame.setVisible(true);
        });
    }
}

package Modelo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import Controlador.Panel1;

import java.awt.*;
import java.awt.event.ActionListener;
import java.security.Principal;
import java.sql.*;

public class Paquete extends JFrame {

    private static String url = "jdbc:mysql://localhost:3306/verdureria";
    private static String usuario = "root";
    private static String contrasena = "208240625";

    private JPanel panel;
    private JTextField descripcionField, idProductoField, idPaqueteField;
    private JTextArea resultadoArea;

    public Paquete() {
        setTitle("Gestión de Paquetes");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 500);

        panel = new JPanel();
        panel.setBackground(new Color(46, 46, 46));
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(panel);
        panel.setLayout(null);

        setLocationRelativeTo(null); // Centrar ventana

        // Título
        JLabel lblBienvenidos = new JLabel("Gestión de Paquetes");
        lblBienvenidos.setForeground(Color.WHITE);
        lblBienvenidos.setFont(new Font("Arial", Font.BOLD, 18));
        lblBienvenidos.setBounds(200, 10, 200, 30);
        panel.add(lblBienvenidos);

        // Campos de entrada
        descripcionField = new JTextField();
        idProductoField = new JTextField();
        idPaqueteField = new JTextField();

        agregarLabelYCampo("Descripción del Paquete:", descripcionField, 50);
        agregarLabelYCampo("ID Producto:", idProductoField, 90);
        agregarLabelYCampo("ID Paquete:", idPaqueteField, 130);

        // Botones de acción
        JButton btnCrear = crearBoton("Crear Paquete", 370, 50, e -> crearPaquete());
        JButton btnConsultar = crearBoton("Consultar Paquetes", 370, 90, e -> consultarPaquetes());
        JButton btnConsultarEspecifico = crearBoton("Consultar Paquete Específico", 370, 130, e -> consultarPaquete());
        JButton btnActualizar = crearBoton("Actualizar Paquete", 370, 170, e -> actualizarPaquete());
        JButton btnEliminar = crearBoton("Eliminar Paquete", 370, 210, e -> eliminarPaquete());

        panel.add(btnCrear);
        panel.add(btnConsultar);
        panel.add(btnConsultarEspecifico);
        panel.add(btnActualizar);
        panel.add(btnEliminar);

         // Botón para regresar al Panel1
        JButton btnRegresar = crearBoton("Regresar", 150, 210, e -> regresarPanel1());
        panel.add(btnRegresar);

        // Área de resultados
        resultadoArea = new JTextArea();
        resultadoArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultadoArea);
        scrollPane.setBounds(50, 250, 500, 200);
        panel.add(scrollPane);
    }

    private void agregarLabelYCampo(String labelText, JTextField textField, int y) {
        JLabel label = new JLabel(labelText);
        label.setForeground(Color.WHITE);
        label.setBounds(50, y, 160, 25);
        panel.add(label);

        textField.setBounds(210, y, 150, 25);
        panel.add(textField);
    }
    
    private JButton crearBoton(String texto, int x, int y, ActionListener action) {
        JButton boton = new JButton(texto);
        boton.setBackground(new Color(38, 81, 255));
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Arial", Font.BOLD, 12));
        boton.setBounds(x, y, 180, 30);
        boton.addActionListener(action);
        return boton;
    }
    // Método para regresar al Panel1
    private void regresarPanel1() {
        new Panel1().setVisible(true); // Muestra el Panel1
        this.dispose(); // Cierra el PanelCliente actual
    }
    private void crearPaquete() {
        String descripcion = descripcionField.getText();
        int idProducto = Integer.parseInt(idProductoField.getText());

        try (Connection conexion = DriverManager.getConnection(url, usuario, contrasena)) {
            String consultaSQL = "{CALL crear_paquete(?, ?)}";
            CallableStatement llamada = conexion.prepareCall(consultaSQL);
            llamada.setString(1, descripcion);
            llamada.setInt(2, idProducto);

            llamada.executeUpdate();
            resultadoArea.setText("Paquete creado correctamente.");
            llamada.close();
        } catch (SQLException e) {
            resultadoArea.setText("Error al crear el paquete: " + e.getMessage());
        }
    }

    private void consultarPaquetes() {
        try (Connection conexion = DriverManager.getConnection(url, usuario, contrasena)) {
            String consultaSQL = "{CALL consultar_paquetes()}";
            CallableStatement llamada = conexion.prepareCall(consultaSQL);

            ResultSet resultado = llamada.executeQuery();
            StringBuilder mensaje = new StringBuilder("Lista de Paquetes:\n");

            while (resultado.next()) {
                int idPaquete = resultado.getInt("id_paquete");
                String descripcion = resultado.getString("descripcion");
                int idProducto = resultado.getInt("id_producto");

                mensaje.append(String.format("ID Paquete: %d, Descripción: %s, ID Producto: %d\n", idPaquete, descripcion, idProducto));
            }

            resultadoArea.setText(mensaje.toString());
            resultado.close();
            llamada.close();
        } catch (SQLException e) {
            resultadoArea.setText("Error al consultar los paquetes: " + e.getMessage());
        }
    }

    private void consultarPaquete() {
        int idPaquete = Integer.parseInt(idPaqueteField.getText());

        try (Connection conexion = DriverManager.getConnection(url, usuario, contrasena)) {
            String consultaSQL = "{CALL consultar_paquetes(?)}";
            CallableStatement llamada = conexion.prepareCall(consultaSQL);
            llamada.setInt(1, idPaquete);

            ResultSet resultado = llamada.executeQuery();
            if (resultado.next()) {
                String mensaje = String.format(
                        "ID Paquete: %d\nDescripción: %s\nID Producto: %d",
                        resultado.getInt("id_paquete"),
                        resultado.getString("descripcion"),
                        resultado.getInt("id_producto"));
                resultadoArea.setText(mensaje);
            } else {
                resultadoArea.setText("No se encontró un paquete con el ID: " + idPaquete);
            }

            resultado.close();
            llamada.close();
        } catch (SQLException e) {
            resultadoArea.setText("Error al consultar el paquete: " + e.getMessage());
        }
    }

    private void actualizarPaquete() {
        int idPaquete = Integer.parseInt(idPaqueteField.getText());
        String descripcion = descripcionField.getText();
        int idProducto = Integer.parseInt(idProductoField.getText());

        try (Connection conexion = DriverManager.getConnection(url, usuario, contrasena)) {
            String consultaSQL = "{CALL update_paquete(?, ?, ?)}";
            CallableStatement llamada = conexion.prepareCall(consultaSQL);
            llamada.setInt(1, idPaquete);
            llamada.setString(2, descripcion);
            llamada.setInt(3, idProducto);

            int filasAfectadas = llamada.executeUpdate();
            if (filasAfectadas > 0) {
                resultadoArea.setText("Paquete actualizado correctamente.");
            } else {
                resultadoArea.setText("No se encontró un paquete con el ID: " + idPaquete);
            }
            llamada.close();
        } catch (SQLException e) {
            resultadoArea.setText("Error al actualizar el paquete: " + e.getMessage());
        }
    }

    private void eliminarPaquete() {
        int idPaquete = Integer.parseInt(idPaqueteField.getText());

        try (Connection conexion = DriverManager.getConnection(url, usuario, contrasena)) {
            String consultaSQL = "{CALL eliminar_paquete(?)}";
            CallableStatement llamada = conexion.prepareCall(consultaSQL);
            llamada.setInt(1, idPaquete);

            int filasAfectadas = llamada.executeUpdate();
            if (filasAfectadas > 0) {
                resultadoArea.setText("Paquete eliminado correctamente.");
            } else {
                resultadoArea.setText("No se encontró un paquete con el ID: " + idPaquete);
            }
            llamada.close();
        } catch (SQLException e) {
            resultadoArea.setText("Error al eliminar el paquete: " + e.getMessage());
        }
    }
}

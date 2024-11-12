package Modelo;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import Controlador.Panel1;

import java.awt.*;
import java.awt.event.ActionListener;

public class Cliente extends JFrame {

    private static String url = "jdbc:mysql://localhost:3306/verdureria"; // URL de la base de datos
    private static String usuario = "root"; // Usuario de la base de datos
    private static String contrasena = "208240625"; // Contraseña de la base de datos

    private JPanel Principal;
    private JTextField cedulaField, nombre1Field, nombre2Field, apellido1Field, apellido2Field, telefonoField;
    private JTextArea resultadoArea;

    // Constructor de la clase PanelCliente
    public Cliente() {
        setTitle("Gestión de Clientes");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 500);
        
        Principal = new JPanel();
        Principal.setBackground(new Color(46, 46, 46));
        Principal.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(Principal);
        Principal.setLayout(null);

        // Centrar la ventana en la pantalla
        setLocationRelativeTo(null);

        // Configuración de campos y etiquetas
        JLabel lblBienvenidos = new JLabel("Bienvenido al Registro de Clientes");
        lblBienvenidos.setBounds(180, 10, 400, 30);
        lblBienvenidos.setForeground(Color.WHITE);
        lblBienvenidos.setFont(new Font("Arial", Font.BOLD, 18));
        Principal.add(lblBienvenidos);

        // Campos de texto y etiquetas
        cedulaField = new JTextField();
        nombre1Field = new JTextField();
        nombre2Field = new JTextField();
        apellido1Field = new JTextField();
        apellido2Field = new JTextField();
        telefonoField = new JTextField();

        agregarLabelYCampo("Cédula:", cedulaField, 50);
        agregarLabelYCampo("Nombre 1:", nombre1Field, 90);
        agregarLabelYCampo("Nombre 2:", nombre2Field, 130);
        agregarLabelYCampo("Apellido 1:", apellido1Field, 170);
        agregarLabelYCampo("Apellido 2:", apellido2Field, 210);
        agregarLabelYCampo("Teléfono:", telefonoField, 250);

        // Botones de acción
        JButton btnIngresar = crearBoton("Ingresar Cliente", 370, 50, e -> insertarCliente());
        JButton btnConsultar = crearBoton("Consultar Cliente", 370, 90, e -> consultarCliente());
        JButton btnEliminar = crearBoton("Eliminar Cliente", 370, 130, e -> eliminarCliente());
        JButton btnActualizar = crearBoton("Actualizar Cliente", 370, 170, e -> actualizarCliente());
        JButton btnMostrar = crearBoton("Mostrar Clientes", 370, 210, e -> mostrarClientes());

        Principal.add(btnIngresar);
        Principal.add(btnConsultar);
        Principal.add(btnEliminar);
        Principal.add(btnActualizar);
        Principal.add(btnMostrar);

        // Botón para regresar al Panel1
        JButton btnRegresar = crearBoton("Regresar", 370, 250, e -> regresarPanel1());
        Principal.add(btnRegresar);

        // Área de resultados
        resultadoArea = new JTextArea();
        resultadoArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultadoArea);
        scrollPane.setBounds(50, 290, 500, 200);
        Principal.add(scrollPane);
    }

    private JButton crearBoton(String texto, int x, int y, ActionListener action) {
        JButton boton = new JButton(texto);
        boton.setBackground(new Color(38, 81, 255));
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Arial", Font.BOLD, 12));
        boton.setBounds(x, y, 150, 30);
        boton.addActionListener(action);
        return boton;
    }

    private void agregarLabelYCampo(String labelText, JTextField textField, int y) {
        JLabel label = new JLabel(labelText);
        label.setForeground(Color.WHITE);
        label.setBounds(50, y, 100, 25);
        Principal.add(label);

        textField.setBounds(150, y, 150, 25);
        Principal.add(textField);
    }

    // Método para regresar al Panel1
    private void regresarPanel1() {
        new Panel1().setVisible(true); // Muestra el Panel1
        this.dispose(); // Cierra el PanelCliente actual
    }

    private void insertarCliente() {
        String cedula = cedulaField.getText();
        String nombre1 = nombre1Field.getText();
        String nombre2 = nombre2Field.getText();
        String apellido1 = apellido1Field.getText();
        String apellido2 = apellido2Field.getText();
        String telefono = telefonoField.getText();

        try (Connection conexion = getConnection()) {
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

        try (Connection conexion = getConnection()) {
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

        try (Connection conexion = getConnection()) {
            String consultaSQL = "{CALL eliminar_cliente(?)}";
            CallableStatement llamada = conexion.prepareCall(consultaSQL);
            llamada.setString(1, cedula);

            int filasAfectadas = llamada.executeUpdate();
            if (filasAfectadas > 0) {
                resultadoArea.setText("Cliente eliminado correctamente.");
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

        try (Connection conexion = getConnection()) {
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
                resultadoArea.setText("Cliente actualizado correctamente.");
            } else {
                resultadoArea.setText("No se encontró un cliente con la cédula: " + cedula);
            }
            llamada.close();
        } catch (SQLException e) {
            resultadoArea.setText("Error al actualizar el cliente: " + e.getMessage());
        }
    }

    private void mostrarClientes() {
        try (Connection conexion = getConnection()) {
            String consultaSQL = "SELECT * FROM clientes";
            PreparedStatement consulta = conexion.prepareStatement(consultaSQL);
            ResultSet resultado = consulta.executeQuery();

            StringBuilder sb = new StringBuilder();
            while (resultado.next()) {
                sb.append(String.format("Cédula: %s | Nombre: %s %s | Apellido: %s %s | Teléfono: %s\n",
                        resultado.getString("cedula"),
                        resultado.getString("nombre1"),
                        resultado.getString("nombre2"),
                        resultado.getString("apellido1"),
                        resultado.getString("apellido2"),
                        resultado.getString("telefono")));
            }
            resultadoArea.setText(sb.toString());
            resultado.close();
            consulta.close();
        } catch (SQLException e) {
            resultadoArea.setText("Error al mostrar clientes: " + e.getMessage());
        }
    }
    
    // Método de conexión
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, usuario, contrasena);
    }

}

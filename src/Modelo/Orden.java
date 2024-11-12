package Modelo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import Controlador.Panel1;

import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.*;

public class Orden extends JFrame {

    private static String url = "jdbc:mysql://localhost:3306/verdureria"; // URL de la base de datos
    private static String usuario = "root"; // Usuario de la base de datos
    private static String contrasena = "208240625"; // Contraseña de la base de datos

    private JPanel Principal;
    private JTextField numeroOrdenField, cedulaField, codigoField, idPaqueteField, precioField;
    private JTextArea resultadoArea;

    public Orden() {
        setTitle("Gestión de Órdenes");
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
        JLabel lblBienvenidos = new JLabel("Bienvenido al Registro de Órdenes");
        lblBienvenidos.setBounds(180, 10, 300, 30);
        lblBienvenidos.setForeground(Color.WHITE);
        lblBienvenidos.setFont(new Font("Arial", Font.BOLD, 18));
        Principal.add(lblBienvenidos);

        // Campos de texto y etiquetas
        numeroOrdenField = new JTextField();
        cedulaField = new JTextField();
        codigoField = new JTextField();
        idPaqueteField = new JTextField();
        precioField = new JTextField();

        agregarLabelYCampo("Número Orden:", numeroOrdenField, 50);
        agregarLabelYCampo("Cédula:", cedulaField, 90);
        agregarLabelYCampo("Código Colaborador:", codigoField, 130);
        agregarLabelYCampo("ID Paquete:", idPaqueteField, 170);
        agregarLabelYCampo("Precio:", precioField, 210);

        // Botones de acción
        JButton btnIngresar = crearBoton("Ingresar Orden", 370, 50, e -> ingresarOrden());
        JButton btnConsultar = crearBoton("Consultar Orden", 370, 90, e -> consultarOrden());
        JButton btnEliminar = crearBoton("Eliminar Orden", 370, 130, e -> eliminarOrden());
        JButton btnActualizar = crearBoton("Actualizar Orden", 370, 170, e -> actualizarOrden());
        JButton btnMostrar = crearBoton("Mostrar Órdenes", 370, 210, e -> mostrarOrdenes());

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
        scrollPane.setBounds(50, 250, 500, 200);
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
        label.setBounds(50, y, 120, 25);
        Principal.add(label);

        textField.setBounds(180, y, 150, 25);
        Principal.add(textField);
    }
    // Método para regresar al Panel1
    private void regresarPanel1() {
        new Panel1().setVisible(true); // Muestra el Panel1
        this.dispose(); // Cierra el PanelCliente actual
    }
    private void ingresarOrden() {
        String cedula = cedulaField.getText();
        String codigo = codigoField.getText();
        int idPaquete = Integer.parseInt(idPaqueteField.getText());
        String precio = precioField.getText();

        try (Connection conexion = DriverManager.getConnection(url, usuario, contrasena)) {
            String consultaSQL = "{CALL ingresar_orden(?, ?, ?, ?)}";
            CallableStatement llamada = conexion.prepareCall(consultaSQL);
            llamada.setString(1, cedula);
            llamada.setString(2, codigo);
            llamada.setInt(3, idPaquete);
            llamada.setString(4, precio);

            llamada.executeUpdate();
            resultadoArea.setText("Orden ingresada correctamente.");
            llamada.close();
        } catch (SQLException e) {
            resultadoArea.setText("Error al ingresar la orden: " + e.getMessage());
        }
    }

    private void consultarOrden() {
        int numeroOrden = Integer.parseInt(numeroOrdenField.getText());

        try (Connection conexion = DriverManager.getConnection(url, usuario, contrasena)) {
            String consultaSQL = "{CALL consultar_orden(?)}";
            CallableStatement llamada = conexion.prepareCall(consultaSQL);
            llamada.setInt(1, numeroOrden);

            ResultSet resultado = llamada.executeQuery();
            if (resultado.next()) {
                String mensaje = String.format(
                        "Número Orden: %d\nCédula: %s\nCódigo: %s\nID Paquete: %d\nPrecio: %s\nFecha: %s\nHora: %s",
                        resultado.getInt("numero_orden"),
                        resultado.getString("cedula"),
                        resultado.getString("codigo"),
                        resultado.getInt("id_paquete"),
                        resultado.getString("precio"),
                        resultado.getString("fecha"),
                        resultado.getString("hora"));
                resultadoArea.setText(mensaje);
            } else {
                resultadoArea.setText("No se encontró una orden con el número: " + numeroOrden);
            }

            resultado.close();
            llamada.close();
        } catch (SQLException e) {
            resultadoArea.setText("Error al consultar la orden: " + e.getMessage());
        }
    }

    private void eliminarOrden() {
        int numeroOrden = Integer.parseInt(numeroOrdenField.getText());

        try (Connection conexion = DriverManager.getConnection(url, usuario, contrasena)) {
            String consultaSQL = "{CALL eliminar_orden(?)}";
            CallableStatement llamada = conexion.prepareCall(consultaSQL);
            llamada.setInt(1, numeroOrden);

            int filasAfectadas = llamada.executeUpdate();
            if (filasAfectadas > 0) {
                resultadoArea.setText("Orden con número " + numeroOrden + " eliminada correctamente.");
            } else {
                resultadoArea.setText("No se encontró una orden con el número: " + numeroOrden);
            }

            llamada.close();
        } catch (SQLException e) {
            resultadoArea.setText("Error al eliminar la orden: " + e.getMessage());
        }
    }

    private void actualizarOrden() {
        int numeroOrden = Integer.parseInt(numeroOrdenField.getText());
        String cedula = cedulaField.getText();
        String codigo = codigoField.getText();
        int idPaquete = Integer.parseInt(idPaqueteField.getText());
        String precio = precioField.getText();

        try (Connection conexion = DriverManager.getConnection(url, usuario, contrasena)) {
            String consultaSQL = "{CALL update_orden(?, ?, ?, ?, ?)}";
            CallableStatement llamada = conexion.prepareCall(consultaSQL);
            llamada.setInt(1, numeroOrden);
            llamada.setString(2, cedula);
            llamada.setString(3, codigo);
            llamada.setInt(4, idPaquete);
            llamada.setString(5, precio);

            int filasAfectadas = llamada.executeUpdate();
            if (filasAfectadas > 0) {
                resultadoArea.setText("Orden con número " + numeroOrden + " actualizada correctamente.");
            } else {
                resultadoArea.setText("No se encontró una orden con el número: " + numeroOrden);
            }

            llamada.close();
        } catch (SQLException e) {
            resultadoArea.setText("Error al actualizar la orden: " + e.getMessage());
        }
    }

    private void mostrarOrdenes() {
        try (Connection conexion = DriverManager.getConnection(url, usuario, contrasena)) {
            String consultaSQL = "{CALL mostrar_ordenes()}";
            CallableStatement llamada = conexion.prepareCall(consultaSQL);

            ResultSet resultado = llamada.executeQuery();
            StringBuilder sb = new StringBuilder();
            while (resultado.next()) {
                sb.append(String.format(
                        "Número Orden: %d | Cédula: %s | Código: %s | ID Paquete: %d | Precio: %s | Fecha: %s | Hora: %s\n",
                        resultado.getInt("numero_orden"),
                        resultado.getString("cedula"),
                        resultado.getString("codigo"),
                        resultado.getInt("id_paquete"),
                        resultado.getString("precio"),
                        resultado.getString("fecha"),
                        resultado.getString("hora")));
            }

            resultadoArea.setText(sb.toString());
            resultado.close();
            llamada.close();
        } catch (SQLException e) {
            resultadoArea.setText("Error al mostrar las órdenes: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Orden frame = new Orden();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}

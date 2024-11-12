package Modelo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import Controlador.Panel1;

import java.awt.*;
import java.awt.event.ActionListener;
import java.security.Principal;
import java.sql.*;

public class Producto extends JFrame {

    private static String url = "jdbc:mysql://localhost:3306/verdureria";
    private static String usuario = "root";
    private static String contrasena = "208240625";

    private JPanel panel;
    private JTextField idProductoField, nombreField, cantidadField;
    private JTextArea resultadoArea;

    public Producto() {
        setTitle("Gestión de Productos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 500);

        panel = new JPanel();
        panel.setBackground(new Color(46, 46, 46));
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(panel);
        panel.setLayout(null);

        setLocationRelativeTo(null); // Centrar ventana

        // Título
        JLabel lblBienvenidos = new JLabel("Gestión de Productos");
        lblBienvenidos.setForeground(Color.WHITE);
        lblBienvenidos.setFont(new Font("Arial", Font.BOLD, 18));
        lblBienvenidos.setBounds(200, 10, 200, 30);
        panel.add(lblBienvenidos);

        // Campos de entrada
        idProductoField = new JTextField();
        nombreField = new JTextField();
        cantidadField = new JTextField();

        agregarLabelYCampo("ID Producto:", idProductoField, 50);
        agregarLabelYCampo("Nombre del Producto:", nombreField, 90);
        agregarLabelYCampo("Cantidad:", cantidadField, 130);

        // Botones de acción
        JButton btnInsertar = crearBoton("Insertar Producto", 370, 50, e -> insertarProducto());
        JButton btnConsultar = crearBoton("Consultar Productos", 370, 90, e -> consultarProductos());
        JButton btnActualizar = crearBoton("Actualizar Producto", 370, 130, e -> actualizarProducto());
        JButton btnEliminar = crearBoton("Eliminar Producto", 370, 170, e -> eliminarProducto());

        panel.add(btnInsertar);
        panel.add(btnConsultar);
        panel.add(btnActualizar);
        panel.add(btnEliminar);
        
        // Botón para regresar al Panel1
        JButton btnRegresar = crearBoton("Regresar", 370, 250, e -> regresarPanel1());
        Principal.add(btnRegresar);

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
    private void insertarProducto() {
        String idProducto = idProductoField.getText();
        String nombre = nombreField.getText();
        int cantidad = Integer.parseInt(cantidadField.getText());

        try (Connection conexion = DriverManager.getConnection(url, usuario, contrasena)) {
            String consultaSQL = "{CALL insertar_producto(?, ?, ?)}";
            CallableStatement llamada = conexion.prepareCall(consultaSQL);
            llamada.setString(1, idProducto);
            llamada.setString(2, nombre);
            llamada.setInt(3, cantidad);

            llamada.executeUpdate();
            resultadoArea.setText("Producto insertado correctamente.");
            llamada.close();
        } catch (SQLException e) {
            resultadoArea.setText("Error al insertar el producto: " + e.getMessage());
        }
    }

    private void consultarProductos() {
        try (Connection conexion = DriverManager.getConnection(url, usuario, contrasena)) {
            String consultaSQL = "{CALL consultar_productos()}";
            CallableStatement llamada = conexion.prepareCall(consultaSQL);

            ResultSet resultado = llamada.executeQuery();
            StringBuilder mensaje = new StringBuilder("Lista de Productos:\n");

            while (resultado.next()) {
                String idProducto = resultado.getString("id_producto");
                String nombre = resultado.getString("nombre");
                int cantidad = resultado.getInt("cantidad");

                mensaje.append(String.format("ID: %s, Nombre: %s, Cantidad: %d\n", idProducto, nombre, cantidad));
            }

            resultadoArea.setText(mensaje.toString());
            resultado.close();
            llamada.close();
        } catch (SQLException e) {
            resultadoArea.setText("Error al consultar los productos: " + e.getMessage());
        }
    }

    private void eliminarProducto() {
        String idProducto = idProductoField.getText();

        try (Connection conexion = DriverManager.getConnection(url, usuario, contrasena)) {
            String consultaSQL = "{CALL eliminar_producto(?)}";
            CallableStatement llamada = conexion.prepareCall(consultaSQL);
            llamada.setString(1, idProducto);

            int filasAfectadas = llamada.executeUpdate();
            if (filasAfectadas > 0) {
                resultadoArea.setText("Producto con ID " + idProducto + " eliminado correctamente.");
            } else {
                resultadoArea.setText("No se encontró un producto con el ID: " + idProducto);
            }
            llamada.close();
        } catch (SQLException e) {
            resultadoArea.setText("Error al eliminar el producto: " + e.getMessage());
        }
    }

    private void actualizarProducto() {
        String idProducto = idProductoField.getText();
        String nombre = nombreField.getText();
        int cantidad = Integer.parseInt(cantidadField.getText());

        try (Connection conexion = DriverManager.getConnection(url, usuario, contrasena)) {
            String consultaSQL = "{CALL update_producto(?, ?, ?)}";
            CallableStatement llamada = conexion.prepareCall(consultaSQL);
            llamada.setString(1, idProducto);
            llamada.setString(2, nombre);
            llamada.setInt(3, cantidad);

            int filasAfectadas = llamada.executeUpdate();
            if (filasAfectadas > 0) {
                resultadoArea.setText("Producto con ID " + idProducto + " actualizado correctamente.");
            } else {
                resultadoArea.setText("No se encontró un producto con el ID: " + idProducto);
            }
            llamada.close();
        } catch (SQLException e) {
            resultadoArea.setText("Error al actualizar el producto: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Producto frame = new Producto();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}

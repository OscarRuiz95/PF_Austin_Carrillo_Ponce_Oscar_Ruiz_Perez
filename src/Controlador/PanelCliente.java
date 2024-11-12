package Controlador;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import Modelo.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PanelCliente extends JFrame {

    // Conexión a la base de datos
    private static String url = "jdbc:mysql://localhost:3306/verdureria"; // URL de la base de datos
    private static String usuario = "root"; // Usuario de la base de datos
    private static String contrasena = "Devastador95."; // Contraseña de la base de datos

    private JPanel Principal;

    // Método para obtener la conexión a la base de datos
    /**
     * @return
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, usuario, contrasena);

    // Constructor de la clase Panel1
    public PanelCliente() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 240);
        Principal = new JPanel();
        Principal.setBackground(new Color(46, 46, 46));
        Principal.setToolTipText("Registro de Cliente");
        Principal.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(Principal);
        Principal.setLayout(null);

        // Centrar la ventana en la pantalla
        setLocationRelativeTo(null);

        JLabel lblBienvenidosAlRegistro = new JLabel("Bienvenido");
        lblBienvenidosAlRegistro.setBounds(80, 11, 350, 23);
        lblBienvenidosAlRegistro.setForeground(Color.WHITE);
        lblBienvenidosAlRegistro.setFont(new Font("Arial", Font.BOLD, 18));
        Principal.add(lblBienvenidosAlRegistro);

        JLabel label = new JLabel("");
        label.setBounds(527, 331, 137, 164);
        Principal.add(label);

        // Botón para ingresar datos de papel
        JButton btnIngresar = new JButton("Ingresar cliente");
        btnIngresar.setBackground(new Color(38, 81, 255));
        btnIngresar.setForeground(Color.WHITE);
        btnIngresar.setBounds(42, 59, 138, 23);
        btnIngresar.setFont(new Font("Arial", Font.BOLD, 12));
        btnIngresar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                insertarCliente(); 
            }
        });
        Principal.add(btnIngresar);

        // Botón para mostrar datos
        JButton btnConsultar = new JButton("Consultar Cliente");
        btnConsultar.setBackground(new Color(38, 81, 255));
        btnConsultar.setForeground(Color.WHITE);
        btnConsultar.setFont(new Font("Arial", Font.BOLD, 12));
        btnConsultar.setBounds(261, 59, 138, 23);
        btnConsultar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                consultarCliente();
            }
        });
        Principal.add(btnConsultar);

        // Botón para eliminar datos
        JButton btnEliminarDatos = new JButton("Eliminar Cliente");
        btnEliminarDatos.setBackground(new Color(38, 81, 255));
        btnEliminarDatos.setForeground(Color.WHITE);
        btnEliminarDatos.setBounds(42, 104, 138, 23);
        btnEliminarDatos.setFont(new Font("Arial", Font.BOLD, 12));
        btnEliminarDatos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                eliminarCliente();
            }
        });
        Principal.add(btnEliminarDatos);

        // Botón para mostrar autores
        JButton btnMostrarDatos = new JButton("Mostrar clientes");
        btnMostrarDatos.setBackground(new Color(38, 81, 255));
        btnMostrarDatos.setForeground(Color.WHITE);
        btnMostrarDatos.setBounds(42, 149, 138, 23);
        btnMostrarDatos.setFont(new Font("Arial", Font.BOLD, 12));
        btnMostrarDatos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mostrarClientes();
            }
        });
        Principal.add(btnMostrarDatos);

        // Botón para buscar datos
        JButton btnActualizar= new JButton("Actualizar Cliente");
        btnActualizar.setBackground(new Color(38, 81, 255));
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.setBounds(261, 104, 138, 23);
        btnActualizar.setFont(new Font("Arial", Font.BOLD, 12));
        btnActualizar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actualizarCliente();
            }
        });
        Principal.add(btnActualizar);

        // Botón para salir
        JButton btnSalir = new JButton();
        ImageIcon icon = new ImageIcon(Cliente.class.getResource("/imagenes/salida.png"));
        btnSalir.setIcon(icon);
        btnSalir.setBounds(350, 150, 50, 50);
        // Eliminar el borde del botón
        btnSalir.setBorderPainted(false);
        btnSalir.setContentAreaFilled(false);
        btnSalir.setFocusPainted(false);
        btnSalir.setOpaque(false);
        btnSalir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int a = JOptionPane.showConfirmDialog(null, "¿Está seguro de que desea salir del registro?",
                        "Confirmación", JOptionPane.YES_NO_OPTION);
                if (a == JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(null, "Gracias por preferirnos", "Cerrando sistema",
                            JOptionPane.INFORMATION_MESSAGE);
                    System.exit(0);
                }
            }
        });
        Principal.add(btnSalir);

         {
            JOptionPane.showMessageDialog(null, "Error al consultar el cliente: " + e.getMessage());
        }

    private void insertarCliente() {

    }

    private void consultarCliente() {

    }

    private void eliminarCliente() {

    }

    private void actualizarCliente() {

    }

    private void mostrarClientes() {
        try (Connection conexion = DriverManager.getConnection(url, usuario, contrasena)) {
            String consultaSQL = "{CALL mostrar_clientes()}";
            CallableStatement llamada = conexion.prepareCall(consultaSQL);

            ResultSet resultado = llamada.executeQuery();
            StringBuilder mensaje = new StringBuilder("Lista de Clientes:\n");

            while (resultado.next()) {
                String cedula = resultado.getString("cedula");
                String nombre1 = resultado.getString("nombre1");
                String nombre2 = resultado.getString("nombre2");
                String apellido1 = resultado.getString("apellido1");
                String apellido2 = resultado.getString("apellido2");
                String telefono = resultado.getString("telefono");

                mensaje.append(String.format(
                        "Cédula: %s, Nombre 1: %s, Nombre 2: %s, Apellido 1: %s, Apellido 2: %s, Teléfono: %s\n",
                        cedula, nombre1, nombre2, apellido1, apellido2, telefono));
            }

            JOptionPane.showMessageDialog(null, mensaje.toString());
            resultado.close();
            llamada.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al mostrar los clientes: " + e.getMessage());
        }
    }
}

package Modelo;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import Controlador.Panel1;

import java.awt.*;
import java.awt.event.ActionListener;

public class Colaborador extends JFrame {

    private static String url = "jdbc:mysql://localhost:3306/verdureria";
    private static String usuario = "root";
    private static String contrasena = "208240625";

    private JPanel Principal;
    private JTextField codigoField, nombre1Field, apellido1Field, telefonoField;
    private JTextArea resultadoArea;

    public Colaborador() {
        setTitle("Gestión de Colaboradores");
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
        JLabel lblBienvenidos = new JLabel("Bienvenido al Registro de Colaboradores");
        lblBienvenidos.setBounds(150, 10, 400, 30);
        lblBienvenidos.setForeground(Color.WHITE);
        lblBienvenidos.setFont(new Font("Arial", Font.BOLD, 18));
        Principal.add(lblBienvenidos);

        // Campos de texto y etiquetas
        codigoField = new JTextField();
        nombre1Field = new JTextField();
        apellido1Field = new JTextField();
        telefonoField = new JTextField();

        agregarLabelYCampo("Código:", codigoField, 50);
        agregarLabelYCampo("Nombre 1:", nombre1Field, 90);
        agregarLabelYCampo("Apellido 1:", apellido1Field, 130);
        agregarLabelYCampo("Teléfono:", telefonoField, 170);

        // Botones de acción
        JButton btnIngresar = crearBoton("Ingresar Colaborador", 370, 50, e -> insertarColaborador());
        JButton btnConsultar = crearBoton("Consultar Colaborador", 370, 90, e -> consultarColaborador());
        JButton btnEliminar = crearBoton("Eliminar Colaborador", 370, 130, e -> eliminarColaborador());
        JButton btnActualizar = crearBoton("Actualizar Colaborador", 370, 170, e -> actualizarColaborador());
        JButton btnMostrar = crearBoton("Mostrar Colaboradores", 370, 210, e -> mostrarColaboradores());

        Principal.add(btnIngresar);
        Principal.add(btnConsultar);
        Principal.add(btnEliminar);
        Principal.add(btnActualizar);
        Principal.add(btnMostrar);

         // Botón para regresar al Panel1
         JButton btnRegresar = crearBoton("Regresar", 150, 210, e -> regresarPanel1());
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
        boton.setBounds(x, y, 180, 30);
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
    private void insertarColaborador() {
        String codigo = codigoField.getText();
        String nombre1 = nombre1Field.getText();
        String apellido1 = apellido1Field.getText();
        String telefono = telefonoField.getText();

        try (Connection conexion = getConnection()) {
            String consultaSQL = "{CALL insertar_colaborador(?, ?, ?, ?)}";
            CallableStatement llamada = conexion.prepareCall(consultaSQL);
            llamada.setString(1, codigo);
            llamada.setString(2, nombre1);
            llamada.setString(3, apellido1);
            llamada.setString(4, telefono);

            llamada.executeUpdate();
            resultadoArea.setText("Colaborador insertado correctamente.");
            llamada.close();
        } catch (SQLException e) {
            resultadoArea.setText("Error al insertar el colaborador: " + e.getMessage());
        }
    }

    private void consultarColaborador() {
        String codigo = codigoField.getText();

        try (Connection conexion = getConnection()) {
            String consultaSQL = "{CALL consultar_colaborador(?)}";
            CallableStatement llamada = conexion.prepareCall(consultaSQL);
            llamada.setString(1, codigo);

            ResultSet resultado = llamada.executeQuery();
            if (resultado.next()) {
                String mensaje = String.format(
                        "Código: %s\nNombre 1: %s\nApellido 1: %s\nTeléfono: %s",
                        codigo,
                        resultado.getString("nombre1"),
                        resultado.getString("apellido1"),
                        resultado.getString("telefono"));
                resultadoArea.setText(mensaje);
            } else {
                resultadoArea.setText("No se encontró un colaborador con el código: " + codigo);
            }
            resultado.close();
            llamada.close();
        } catch (SQLException e) {
            resultadoArea.setText("Error al consultar el colaborador: " + e.getMessage());
        }
    }

    private void eliminarColaborador() {
        String codigo = codigoField.getText();

        try (Connection conexion = getConnection()) {
            String consultaSQL = "{CALL eliminar_colaborador(?)}";
            CallableStatement llamada = conexion.prepareCall(consultaSQL);
            llamada.setString(1, codigo);

            int filasAfectadas = llamada.executeUpdate();
            if (filasAfectadas > 0) {
                resultadoArea.setText("Colaborador eliminado correctamente.");
            } else {
                resultadoArea.setText("No se encontró un colaborador con el código: " + codigo);
            }
            llamada.close();
        } catch (SQLException e) {
            resultadoArea.setText("Error al eliminar el colaborador: " + e.getMessage());
        }
    }

    private void actualizarColaborador() {
        String codigo = codigoField.getText();
        String nombre1 = nombre1Field.getText();
        String apellido1 = apellido1Field.getText();
        String telefono = telefonoField.getText();

        try (Connection conexion = getConnection()) {
            String consultaSQL = "{CALL update_colaborador(?, ?, ?, ?)}";
            CallableStatement llamada = conexion.prepareCall(consultaSQL);
            llamada.setString(1, codigo);
            llamada.setString(2, nombre1);
            llamada.setString(3, apellido1);
            llamada.setString(4, telefono);

            int filasAfectadas = llamada.executeUpdate();
            if (filasAfectadas > 0) {
                resultadoArea.setText("Colaborador actualizado correctamente.");
            } else {
                resultadoArea.setText("No se encontró un colaborador con el código: " + codigo);
            }
            llamada.close();
        } catch (SQLException e) {
            resultadoArea.setText("Error al actualizar el colaborador: " + e.getMessage());
        }
    }

    private void mostrarColaboradores() {
        try (Connection conexion = getConnection()) {
            String consultaSQL = "{CALL mostrar_colaboradores()}";
            CallableStatement llamada = conexion.prepareCall(consultaSQL);

            ResultSet resultado = llamada.executeQuery();
            StringBuilder mensaje = new StringBuilder();
            while (resultado.next()) {
                mensaje.append(String.format("Código: %s | Nombre 1: %s | Apellido 1: %s | Teléfono: %s\n",
                        resultado.getString("codigo"),
                        resultado.getString("nombre1"),
                        resultado.getString("apellido1"),
                        resultado.getString("telefono")));
            }
            resultadoArea.setText(mensaje.toString());
            resultado.close();
            llamada.close();
        } catch (SQLException e) {
            resultadoArea.setText("Error al mostrar los colaboradores: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, usuario, contrasena);
    }

}

package Modelo;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class Cliente {

    private static String url = "jdbc:mysql://localhost:3306/verdureria"; // URL de la base de datos
    private static String usuario = "root"; // Usuario de la base de datos
    private static String contrasena = "Devastador95."; // Contraseña de la base de datos

    

    // Se ingresa a el cliente a la base de datos
   public void insertarCliente() {
        String cedula = JOptionPane.showInputDialog("Ingrese cédula:");
        String nombre1 = JOptionPane.showInputDialog("Ingrese nombre 1:");
        String nombre2 = JOptionPane.showInputDialog("Ingrese nombre 2:");
        String apellido1 = JOptionPane.showInputDialog("Ingrese apellido 1:");
        String apellido2 = JOptionPane.showInputDialog("Ingrese apellido 2:");
        String telefono = JOptionPane.showInputDialog("Ingrese teléfono:");

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
            JOptionPane.showMessageDialog(null, "Cliente insertado correctamente.");
            llamada.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al insertar el cliente: " + e.getMessage());
        }
    }

    // Permite consultar un cliente de la base de datos por medio de la cedula
    private static void consultarCliente() {
        String cedula = JOptionPane.showInputDialog("Ingrese la cédula del cliente a consultar:");

        try (Connection conexion = DriverManager.getConnection(url, usuario, contrasena)) {
            String consultaSQL = "{CALL consultar_cliente(?)}";
            CallableStatement llamada = conexion.prepareCall(consultaSQL);
            llamada.setString(1, cedula);

            ResultSet resultado = llamada.executeQuery();
            if (resultado.next()) {
                String nombre1 = resultado.getString("nombre1");
                String nombre2 = resultado.getString("nombre2");
                String apellido1 = resultado.getString("apellido1");
                String apellido2 = resultado.getString("apellido2");
                String telefono = resultado.getString("telefono");

                String mensaje = String.format(
                        "Cédula: %s\nNombre 1: %s\nNombre 2: %s\nApellido 1: %s\nApellido 2: %s\nTeléfono: %s",
                        cedula, nombre1, nombre2, apellido1, apellido2, telefono);
                JOptionPane.showMessageDialog(null, mensaje);
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró un cliente con la cédula: " + cedula);
            }
            resultado.close();
            llamada.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al consultar el cliente: " + e.getMessage());
        }
    }

    // Permite eliminar un cliente de la base de datos por medio de la cedula
    private static void eliminarCliente() {
        String cedula = JOptionPane.showInputDialog("Ingrese la cédula del cliente a eliminar:");

        try (Connection conexion = DriverManager.getConnection(url, usuario, contrasena)) {
            String consultaSQL = "{CALL eliminar_cliente(?)}";
            CallableStatement llamada = conexion.prepareCall(consultaSQL);
            llamada.setString(1, cedula);

            int filasAfectadas = llamada.executeUpdate();
            if (filasAfectadas > 0) {
                JOptionPane.showMessageDialog(null, "Cliente con cédula " + cedula + " eliminado correctamente.");
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró un cliente con la cédula: " + cedula);
            }
            llamada.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar el cliente: " + e.getMessage());
        }
    }

    // Permite actualizar un cliente de la base de datos por medio de la cedula
    private static void actualizarCliente() {
        String cedula = JOptionPane.showInputDialog("Ingrese la cédula del cliente a actualizar:");
        String nombre1 = JOptionPane.showInputDialog("Ingrese el nuevo nombre 1:");
        String nombre2 = JOptionPane.showInputDialog("Ingrese el nuevo nombre 2:");
        String apellido1 = JOptionPane.showInputDialog("Ingrese el nuevo apellido 1:");
        String apellido2 = JOptionPane.showInputDialog("Ingrese el nuevo apellido 2:");
        String telefono = JOptionPane.showInputDialog("Ingrese el nuevo teléfono:");

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
                JOptionPane.showMessageDialog(null, "Cliente con cédula " + cedula + " actualizado correctamente.");
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró un cliente con la cédula: " + cedula);
            }
            llamada.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar el cliente: " + e.getMessage());
        }
    }

    // Permite mostrar a todos los  clientes de la base de datos
    private static void mostrarClientes() {
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

    public void setVisible(boolean b) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setVisible'");
    }

}

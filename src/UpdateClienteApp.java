import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.CallableStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class UpdateClienteApp {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/verdureria"; // URL de la base de datos
        String usuario = "root"; // Usuario de la base de datos
        String contrasena = "Devastador95."; // Contraseña de la base de datos

        // Solicita la cédula del cliente a actualizar usando JOptionPane
        String cedula = JOptionPane.showInputDialog("Ingrese la cédula del cliente a actualizar:");

        // Solicita los nuevos datos del cliente
        String nombre1 = JOptionPane.showInputDialog("Ingrese el nuevo nombre 1:");
        String nombre2 = JOptionPane.showInputDialog("Ingrese el nuevo nombre 2:");
        String apellido1 = JOptionPane.showInputDialog("Ingrese el nuevo apellido 1:");
        String apellido2 = JOptionPane.showInputDialog("Ingrese el nuevo apellido 2:");
        String telefono = JOptionPane.showInputDialog("Ingrese el nuevo teléfono:");

        // Llamada al procedimiento para actualizar el cliente
        try (Connection conexion = DriverManager.getConnection(url, usuario, contrasena)) {
            if (conexion != null) {
                System.out.println("Conexión a la base de datos verdureria establecida correctamente.");

                String consultaSQL = "{CALL update_cliente(?, ?, ?, ?, ?, ?)}";
                CallableStatement llamada = conexion.prepareCall(consultaSQL);

                // Establece los parámetros del procedimiento
                llamada.setString(1, cedula);
                llamada.setString(2, nombre1);
                llamada.setString(3, nombre2);
                llamada.setString(4, apellido1);
                llamada.setString(5, apellido2);
                llamada.setString(6, telefono);

                // Ejecuta la actualización
                int filasAfectadas = llamada.executeUpdate();

                // Verifica si se actualizó un cliente
                if (filasAfectadas > 0) {
                    JOptionPane.showMessageDialog(null, "Cliente con cédula " + cedula + " actualizado correctamente.");
                } else {
                    JOptionPane.showMessageDialog(null, "No se encontró un cliente con la cédula: " + cedula);
                }

                // Cerrar el CallableStatement
                llamada.close();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al conectar o ejecutar el procedimiento: " + e.getMessage());
        }
    }
}

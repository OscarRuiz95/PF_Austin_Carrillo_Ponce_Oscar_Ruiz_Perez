import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.CallableStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class EliminarClienteApp {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/verdureria"; // URL de la base de datos
        String usuario = "root"; // Usuario de la base de datos
        String contrasena = "Devastador95."; // Contraseña de la base de datos

        // Solicita la cédula del cliente a eliminar usando JOptionPane
        String cedula = JOptionPane.showInputDialog("Ingrese la cédula del cliente a eliminar:");

        // Llamada al procedimiento para eliminar el cliente
        try (Connection conexion = DriverManager.getConnection(url, usuario, contrasena)) {
            if (conexion != null) {
                System.out.println("Conexión a la base de datos verdureria establecida correctamente.");

                String consultaSQL = "{CALL eliminar_cliente(?)}";
                CallableStatement llamada = conexion.prepareCall(consultaSQL);

                // Establece el parámetro del procedimiento
                llamada.setString(1, cedula);

                // Ejecuta la eliminación
                int filasAfectadas = llamada.executeUpdate();
                
                // Verifica si se eliminó un cliente
                if (filasAfectadas > 0) {
                    JOptionPane.showMessageDialog(null, "Cliente con cédula " + cedula + " eliminado correctamente.");
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

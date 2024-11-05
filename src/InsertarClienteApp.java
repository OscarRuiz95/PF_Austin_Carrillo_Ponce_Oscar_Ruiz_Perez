import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.CallableStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class InsertarClienteApp {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/verdureria"; // URL de la base de datos
        String usuario = "root"; // Usuario de la base de datos
        String contrasena = "Devastador95."; // Contraseña de la base de datos

        // Solicita los datos del cliente
        String cedula = JOptionPane.showInputDialog("Ingrese cédula:");
        String nombre1 = JOptionPane.showInputDialog("Ingrese nombre 1:");
        String nombre2 = JOptionPane.showInputDialog("Ingrese nombre 2:");
        String apellido1 = JOptionPane.showInputDialog("Ingrese apellido 1:");
        String apellido2 = JOptionPane.showInputDialog("Ingrese apellido 2:");
        String telefono = JOptionPane.showInputDialog("Ingrese teléfono:");

        // Llamada al procedimiento para insertar el cliente
        try (Connection conexion = DriverManager.getConnection(url, usuario, contrasena)) {
            if (conexion != null) {
                System.out.println("Conexión a la base de datos verdureria establecida correctamente.");

                String consultaSQL = "{CALL insertar_cliente(?, ?, ?, ?, ?, ?)}";
                CallableStatement llamada = conexion.prepareCall(consultaSQL);

                // Parametros del procedimiento
                llamada.setString(1, cedula);
                llamada.setString(2, nombre1);
                llamada.setString(3, nombre2);
                llamada.setString(4, apellido1);
                llamada.setString(5, apellido2);
                llamada.setString(6, telefono);

                // Ejecuta el procedimiento
                llamada.executeUpdate();
                JOptionPane.showMessageDialog(null, "Cliente insertado correctamente.");

                // Cerrar el CallableStatement
                llamada.close();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al conectar o ejecutar el procedimiento: " + e.getMessage());
        }
    }
}

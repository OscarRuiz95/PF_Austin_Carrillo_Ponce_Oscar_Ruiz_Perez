import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class ConsultarClienteApp {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/verdureria"; // URL de la base de datos
        String usuario = "root"; // Usuario de la base de datos
        String contrasena = "Devastador95."; // Contraseña de la base de datos

        // Solicita la cédula a buscar usando JOptionPane
        String cedula = JOptionPane.showInputDialog("Ingrese la cédula del cliente a consultar:");

        // Llamada al procedimiento para consultar el cliente
        try (Connection conexion = DriverManager.getConnection(url, usuario, contrasena)) {
            if (conexion != null) {
                System.out.println("Conexión a la base de datos verdureria establecida correctamente.");

                String consultaSQL = "{CALL consultar_cliente(?)}";
                CallableStatement llamada = conexion.prepareCall(consultaSQL);

                // Establece el parámetro del procedimiento
                llamada.setString(1, cedula);

                // Ejecuta la consulta
                ResultSet resultado = llamada.executeQuery();

                // Verifica si se encontró un cliente
                if (resultado.next()) {
                    // Extrae los datos del cliente
                    String nombre1 = resultado.getString("nombre1");
                    String nombre2 = resultado.getString("nombre2");
                    String apellido1 = resultado.getString("apellido1");
                    String apellido2 = resultado.getString("apellido2");
                    String telefono = resultado.getString("telefono");

                    // Muestra los datos del cliente en un JOptionPane
                    String mensaje = String.format("Cédula: %s\nNombre 1: %s\nNombre 2: %s\nApellido 1: %s\nApellido 2: %s\nTeléfono: %s",
                            cedula, nombre1, nombre2, apellido1, apellido2, telefono);
                    JOptionPane.showMessageDialog(null, mensaje);
                } else {
                    JOptionPane.showMessageDialog(null, "No se encontró un cliente con la cédula: " + cedula);
                }

                // Cerrar el ResultSet y el CallableStatement
                resultado.close();
                llamada.close();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al conectar o ejecutar el procedimiento: " + e.getMessage());
        }
    }
}

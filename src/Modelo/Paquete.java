package Modelo;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class Paquete {

    private static String url = "jdbc:mysql://localhost:3306/verdureria";
    private static String usuario = "root";
    private static String contrasena = "Devastrador95.";

    public static void main(String[] args) {
        boolean continuar = true;

        while (continuar) {
            String[] opciones = { "Crear Paquete", "Consultar Paquetes", "Consultar Paquete Específico",
                    "Actualizar Paquete", "Eliminar Paquete", "Salir" };
            String seleccion = (String) JOptionPane.showInputDialog(null, "Seleccione una opción:",
                    "Menú Paquetes", JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);

            if (seleccion != null) {
                switch (seleccion) {
                    case "Crear Paquete":
                        crearPaquete();
                        break;
                    case "Consultar Paquetes":
                        consultarPaquetes();
                        break;
                    case "Consultar Paquete":
                        consultarPaquete();
                        break;
                    case "Actualizar Paquete":
                        actualizarPaquete();
                        break;
                    case "Eliminar Paquete":
                        eliminarPaquete();
                        break;
                    case "Salir":
                        continuar = false;
                        break;
                }
            } else {
                continuar = false;
            }
        }
    }

    // creacion de paquete
    private static void crearPaquete() {
        String descripcion = JOptionPane.showInputDialog("Ingrese la descripción del paquete:");
        int idProducto = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el ID del producto:"));

        try (Connection conexion = DriverManager.getConnection(url, usuario, contrasena)) {
            String consultaSQL = "{CALL crear_paquete(?, ?)}";
            CallableStatement llamada = conexion.prepareCall(consultaSQL);
            llamada.setString(1, descripcion);
            llamada.setInt(2, idProducto);

            llamada.executeUpdate();
            JOptionPane.showMessageDialog(null, "Paquete creado correctamente.");
            llamada.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al crear el paquete: " + e.getMessage());
        }
    }

    // consulta todos los paquetes
    private static void consultarPaquetes() {
        try (Connection conexion = DriverManager.getConnection(url, usuario, contrasena)) {
            String consultaSQL = "{CALL consultar_paquetes()}";
            CallableStatement llamada = conexion.prepareCall(consultaSQL);

            ResultSet resultado = llamada.executeQuery();
            StringBuilder mensaje = new StringBuilder("Lista de Paquetes:\n");

            while (resultado.next()) {
                int idPaquete = resultado.getInt("id_paquete");
                String descripcion = resultado.getString("descripcion");
                int idProducto = resultado.getInt("id_producto");

                mensaje.append(String.format("ID Paquete: %d, Descripción: %s, ID Producto: %d\n",
                        idPaquete, descripcion, idProducto));
            }

            JOptionPane.showMessageDialog(null, mensaje.toString());
            resultado.close();
            llamada.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al consultar los paquetes: " + e.getMessage());
        }
    }

    // busca un paquete y lo muestra
    private static void consultarPaquete() {
        int idPaquete = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el ID del paquete a consultar:"));

        try (Connection conexion = DriverManager.getConnection(url, usuario, contrasena)) {
            String consultaSQL = "{CALL consultar_paquetes(?)}";
            CallableStatement llamada = conexion.prepareCall(consultaSQL);
            llamada.setInt(1, idPaquete);

            ResultSet resultado = llamada.executeQuery();
            if (resultado.next()) {
                String mensaje = String.format(
                        "ID Paquete: %d\nDescripción: %s\nID Producto: %d",
                        resultado.getInt("id_paquete"),
                        resultado.getString("descripcion"),
                        resultado.getInt("id_producto"));
                JOptionPane.showMessageDialog(null, mensaje);
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró un paquete con el ID: " + idPaquete);
            }

            resultado.close();
            llamada.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al consultar el paquete: " + e.getMessage());
        }
    }

    // permite actualizar un paquete
    private static void actualizarPaquete() {
        int idPaquete = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el ID del paquete a actualizar:"));
        String descripcion = JOptionPane.showInputDialog("Ingrese la nueva descripción:");
        int idProducto = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el nuevo ID del producto:"));

        try (Connection conexion = DriverManager.getConnection(url, usuario, contrasena)) {
            String consultaSQL = "{CALL update_paquete(?, ?, ?)}";
            CallableStatement llamada = conexion.prepareCall(consultaSQL);
            llamada.setInt(1, idPaquete);
            llamada.setString(2, descripcion);
            llamada.setInt(3, idProducto);

            int filasAfectadas = llamada.executeUpdate();
            if (filasAfectadas > 0) {
                JOptionPane.showMessageDialog(null, "Paquete actualizado correctamente.");
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró un paquete con el ID: " + idPaquete);
            }
            llamada.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar el paquete: " + e.getMessage());
        }
    }

    // busca y elimina un paquete
    private static void eliminarPaquete() {
        int idPaquete = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el ID del paquete a eliminar:"));

        try (Connection conexion = DriverManager.getConnection(url, usuario, contrasena)) {
            String consultaSQL = "{CALL eliminar_paquete(?)}";
            CallableStatement llamada = conexion.prepareCall(consultaSQL);
            llamada.setInt(1, idPaquete);

            int filasAfectadas = llamada.executeUpdate();
            if (filasAfectadas > 0) {
                JOptionPane.showMessageDialog(null, "Paquete eliminado correctamente.");
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró un paquete con el ID: " + idPaquete);
            }
            llamada.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar el paquete: " + e.getMessage());
        }
    }

    public void setVisible(boolean b) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setVisible'");
    }
}

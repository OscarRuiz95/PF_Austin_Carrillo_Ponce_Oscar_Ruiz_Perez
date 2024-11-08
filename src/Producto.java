import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class Producto {

    private static String url = "jdbc:mysql://localhost:3306/verdureria";
    private static String usuario = "root";
    private static String contrasena = "Devastador95.";

    public static void main(String[] args) {
        boolean continuar = true;

        while (continuar) {
            String[] opciones = {"Insertar Producto", "Consultar Productos", "Eliminar Producto", "Actualizar Producto", "Salir"};
            String seleccion = (String) JOptionPane.showInputDialog(null, "Seleccione una opción:",
                    "Menu", JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);

            if (seleccion != null) {
                switch (seleccion) {
                    case "Insertar Producto":
                        insertarProducto();
                        break;
                    case "Consultar Productos":
                        consultarProductos();
                        break;
                    case "Eliminar Producto":
                        eliminarProducto();
                        break;
                    case "Actualizar Producto":
                        actualizarProducto();
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

    private static void insertarProducto() {
        String idProducto = JOptionPane.showInputDialog("Ingrese ID del producto:");
        String nombre = JOptionPane.showInputDialog("Ingrese nombre del producto:");
        int cantidad = Integer.parseInt(JOptionPane.showInputDialog("Ingrese cantidad del producto:"));

        try (Connection conexion = DriverManager.getConnection(url, usuario, contrasena)) {
            String consultaSQL = "{CALL insertar_producto(?, ?, ?)}";
            CallableStatement llamada = conexion.prepareCall(consultaSQL);
            llamada.setString(1, idProducto);
            llamada.setString(2, nombre);
            llamada.setInt(3, cantidad);

            llamada.executeUpdate();
            JOptionPane.showMessageDialog(null, "Producto insertado correctamente.");
            llamada.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al insertar el producto: " + e.getMessage());
        }
    }

    private static void consultarProductos() {
        try (Connection conexion = DriverManager.getConnection(url, usuario, contrasena)) {
            String consultaSQL = "{CALL consultar_productos()}";
            CallableStatement llamada = conexion.prepareCall(consultaSQL);

            ResultSet resultado = llamada.executeQuery();
            StringBuilder mensaje = new StringBuilder("Lista de Productos:\n");

            while (resultado.next()) {
                String idProducto = resultado.getString("id_producto");
                String nombre = resultado.getString("nombre");
                int cantidad = resultado.getInt("cantidad");

                mensaje.append(String.format("ID: %s, Nombre: %s, Cantidad: %d\n",
                        idProducto, nombre, cantidad));
            }

            JOptionPane.showMessageDialog(null, mensaje.toString());
            resultado.close();
            llamada.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al consultar los productos: " + e.getMessage());
        }
    }

    private static void eliminarProducto() {
        String idProducto = JOptionPane.showInputDialog("Ingrese el ID del producto a eliminar:");

        try (Connection conexion = DriverManager.getConnection(url, usuario, contrasena)) {
            String consultaSQL = "{CALL eliminar_producto(?)}";
            CallableStatement llamada = conexion.prepareCall(consultaSQL);
            llamada.setString(1, idProducto);

            int filasAfectadas = llamada.executeUpdate();
            if (filasAfectadas > 0) {
                JOptionPane.showMessageDialog(null, "Producto con ID " + idProducto + " eliminado correctamente.");
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró un producto con el ID: " + idProducto);
            }
            llamada.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar el producto: " + e.getMessage());
        }
    }

    private static void actualizarProducto() {
        String idProducto = JOptionPane.showInputDialog("Ingrese el ID del producto a actualizar:");
        String nombre = JOptionPane.showInputDialog("Ingrese el nuevo nombre del producto:");
        int cantidad = Integer.parseInt(JOptionPane.showInputDialog("Ingrese la nueva cantidad del producto:"));

        try (Connection conexion = DriverManager.getConnection(url, usuario, contrasena)) {
            String consultaSQL = "{CALL update_producto(?, ?, ?)}";
            CallableStatement llamada = conexion.prepareCall(consultaSQL);
            llamada.setString(1, idProducto);
            llamada.setString(2, nombre);
            llamada.setInt(3, cantidad);

            int filasAfectadas = llamada.executeUpdate();
            if (filasAfectadas > 0) {
                JOptionPane.showMessageDialog(null, "Producto con ID " + idProducto + " actualizado correctamente.");
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró un producto con el ID: " + idProducto);
            }
            llamada.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar el producto: " + e.getMessage());
        }
    }
}

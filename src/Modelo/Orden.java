package Modelo;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class Orden {

    private static String url = "jdbc:mysql://localhost:3306/verdureria";
    private static String usuario = "root";
    private static String contrasena = "Devastador95.";

    public static void main(String[] args) {
        boolean continuar = true;

        while (continuar) {
            String[] opciones = { "Ingresar Orden", "Consultar Órdenes", "Consultar Orden Específica", "Eliminar Orden",
                    "Actualizar Orden", "Salir" };
            String seleccion = (String) JOptionPane.showInputDialog(null, "Seleccione una opción:",
                    "Menú", JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);

            if (seleccion != null) {
                switch (seleccion) {
                    case "Ingresar Orden":
                        ingresarOrden();
                        break;
                    case "Consultar Órdenes":
                        consultarOrdenes();
                        break;
                    case "Buscar Orden":
                        consultarOrden();
                        break;
                    case "Eliminar Orden":
                        eliminarOrden();
                        break;
                    case "Actualizar Orden":
                        actualizarOrden();
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

    // permite ingresar una orden
    private static void ingresarOrden() {
        String cedula = JOptionPane.showInputDialog("Ingrese la cédula del cliente:");
        String codigo = JOptionPane.showInputDialog("Ingrese el código del colaborador:");
        int idPaquete = Integer.parseInt(JOptionPane.showInputDialog("Ingrese ID del paquete:"));
        String precio = JOptionPane.showInputDialog("Ingrese el precio:");

        try (Connection conexion = DriverManager.getConnection(url, usuario, contrasena)) {
            String consultaSQL = "{CALL ingresar_orden(?, ?, ?, ?)}";
            CallableStatement llamada = conexion.prepareCall(consultaSQL);
            llamada.setString(1, cedula);
            llamada.setString(2, codigo);
            llamada.setInt(3, idPaquete);
            llamada.setString(4, precio);

            llamada.executeUpdate();
            JOptionPane.showMessageDialog(null, "Orden ingresada correctamente.");
            llamada.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al ingresar la orden: " + e.getMessage());
        }
    }

    // muestra todas las ordenes
    private static void consultarOrdenes() {
        try (Connection conexion = DriverManager.getConnection(url, usuario, contrasena)) {
            String consultaSQL = "{CALL mostrar_ordenes()}";
            CallableStatement llamada = conexion.prepareCall(consultaSQL);

            ResultSet resultado = llamada.executeQuery();
            StringBuilder mensaje = new StringBuilder("Lista de Órdenes:\n");

            while (resultado.next()) {
                int numeroOrden = resultado.getInt("numero_orden");
                String cedula = resultado.getString("cedula");
                String codigo = resultado.getString("codigo");
                int idPaquete = resultado.getInt("id_paquete");
                String precio = resultado.getString("precio");
                String fecha = resultado.getString("fecha");
                String hora = resultado.getString("hora");

                mensaje.append(String.format(
                        "Número Orden: %d, Cédula: %s, Código: %s, ID Paquete: %d, Precio: %s, Fecha: %s, Hora: %s\n",
                        numeroOrden, cedula, codigo, idPaquete, precio, fecha, hora));
            }

            JOptionPane.showMessageDialog(null, mensaje.toString());
            resultado.close();
            llamada.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al consultar las órdenes: " + e.getMessage());
        }
    }

    // busca una orden en especifico
    private static void consultarOrden() {
        int numeroOrden = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el número de la orden a consultar:"));

        try (Connection conexion = DriverManager.getConnection(url, usuario, contrasena)) {
            String consultaSQL = "{CALL consultar_orden(?)}";
            CallableStatement llamada = conexion.prepareCall(consultaSQL);
            llamada.setInt(1, numeroOrden);

            ResultSet resultado = llamada.executeQuery();
            if (resultado.next()) {
                String mensaje = String.format(
                        "Número Orden: %d\nCédula: %s\nCódigo: %s\nID Paquete: %d\nPrecio: %s\nFecha: %s\nHora: %s",
                        resultado.getInt("numero_orden"),
                        resultado.getString("cedula"),
                        resultado.getString("codigo"),
                        resultado.getInt("id_paquete"),
                        resultado.getString("precio"),
                        resultado.getString("fecha"),
                        resultado.getString("hora"));
                JOptionPane.showMessageDialog(null, mensaje);
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró una orden con el número: " + numeroOrden);
            }

            resultado.close();
            llamada.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al consultar la orden: " + e.getMessage());
        }
    }

    // permite eliminar una orden
    private static void eliminarOrden() {
        int numeroOrden = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el número de orden a eliminar:"));

        try (Connection conexion = DriverManager.getConnection(url, usuario, contrasena)) {
            String consultaSQL = "{CALL eliminar_orden(?)}";
            CallableStatement llamada = conexion.prepareCall(consultaSQL);
            llamada.setInt(1, numeroOrden);

            int filasAfectadas = llamada.executeUpdate();
            if (filasAfectadas > 0) {
                JOptionPane.showMessageDialog(null, "Orden con número " + numeroOrden + " eliminada correctamente.");
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró una orden con el número: " + numeroOrden);
            }
            llamada.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar la orden: " + e.getMessage());
        }
    }

    private static void actualizarOrden() {
        int numeroOrden = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el número de la orden a actualizar:"));
        String cedula = JOptionPane.showInputDialog("Ingrese la nueva cédula:");
        String codigo = JOptionPane.showInputDialog("Ingrese el nuevo código:");
        int idPaquete = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el nuevo ID del paquete:"));
        String precio = JOptionPane.showInputDialog("Ingrese el nuevo precio:");

        try (Connection conexion = DriverManager.getConnection(url, usuario, contrasena)) {
            String consultaSQL = "{CALL update_orden(?, ?, ?, ?, ?)}";
            CallableStatement llamada = conexion.prepareCall(consultaSQL);
            llamada.setInt(1, numeroOrden);
            llamada.setString(2, cedula);
            llamada.setString(3, codigo);
            llamada.setInt(4, idPaquete);
            llamada.setString(5, precio);

            int filasAfectadas = llamada.executeUpdate();
            if (filasAfectadas > 0) {
                JOptionPane.showMessageDialog(null, "Orden con número " + numeroOrden + " actualizada correctamente.");
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró una orden con el número: " + numeroOrden);
            }
            llamada.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar la orden: " + e.getMessage());
        }
    }

    public void setVisible(boolean b) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setVisible'");
    }
}

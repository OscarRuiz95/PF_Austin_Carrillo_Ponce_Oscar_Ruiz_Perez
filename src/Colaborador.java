import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class Colaborador {

    private static String url = "jdbc:mysql://localhost:3306/verdureria";
    private static String usuario = "root";
    private static String contrasena = "Devastador95.";

    public static void main(String[] args) {
        boolean continuar = true;

        while (continuar) {
            String[] opciones = { "Insertar Colaborador", "Consultar Colaborador", "Eliminar Colaborador",
                    "Actualizar Colaborador", "Mostrar Todos los Colaboradores", "Salir" };
            String seleccion = (String) JOptionPane.showInputDialog(null, "Seleccione una opción:",
                    "Menu", JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);

            if (seleccion != null) {
                switch (seleccion) {
                    case "Insertar Colaborador":
                        insertarColaborador();
                        break;
                    case "Consultar Colaborador":
                        consultarColaborador();
                        break;
                    case "Eliminar Colaborador":
                        eliminarColaborador();
                        break;
                    case "Actualizar Colaborador":
                        actualizarColaborador();
                        break;
                    case "Mostrar Todos los Colaboradores":
                        mostrarColaboradores();
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

    // ingresa un colaborador
    private static void insertarColaborador() {
        String codigo = JOptionPane.showInputDialog("Ingrese código:");
        String nombre1 = JOptionPane.showInputDialog("Ingrese nombre 1:");
        String apellido1 = JOptionPane.showInputDialog("Ingrese apellido 1:");
        String telefono = JOptionPane.showInputDialog("Ingrese teléfono:");

        try (Connection conexion = DriverManager.getConnection(url, usuario, contrasena)) {
            String consultaSQL = "{CALL insertar_colaborador(?, ?, ?, ?)}";
            CallableStatement llamada = conexion.prepareCall(consultaSQL);
            llamada.setString(1, codigo);
            llamada.setString(2, nombre1);
            llamada.setString(3, apellido1);
            llamada.setString(4, telefono);

            llamada.executeUpdate();
            JOptionPane.showMessageDialog(null, "Colaborador insertado correctamente.");
            llamada.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al insertar el colaborador: " + e.getMessage());
        }
    }

    // consulta un colaborador por medio de su codigo
    private static void consultarColaborador() {
        String codigo = JOptionPane.showInputDialog("Ingrese el código del colaborador a consultar:");

        try (Connection conexion = DriverManager.getConnection(url, usuario, contrasena)) {
            String consultaSQL = "{CALL consultar_colaborador(?)}";
            CallableStatement llamada = conexion.prepareCall(consultaSQL);
            llamada.setString(1, codigo);

            ResultSet resultado = llamada.executeQuery();
            if (resultado.next()) {
                String nombre1 = resultado.getString("nombre1");
                String apellido1 = resultado.getString("apellido1");
                String telefono = resultado.getString("telefono");

                String mensaje = String.format("Código: %s\nNombre 1: %s\nApellido 1: %s\nTeléfono: %s",
                        codigo, nombre1, apellido1, telefono);
                JOptionPane.showMessageDialog(null, mensaje);
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró un colaborador con el código: " + codigo);
            }
            resultado.close();
            llamada.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al consultar el colaborador: " + e.getMessage());
        }
    }

    // busca y elimina un colaborador por medio de su codigo
    private static void eliminarColaborador() {
        String codigo = JOptionPane.showInputDialog("Ingrese el código del colaborador a eliminar:");

        try (Connection conexion = DriverManager.getConnection(url, usuario, contrasena)) {
            String consultaSQL = "{CALL eliminar_colaborador(?)}";
            CallableStatement llamada = conexion.prepareCall(consultaSQL);
            llamada.setString(1, codigo);

            int filasAfectadas = llamada.executeUpdate();
            if (filasAfectadas > 0) {
                JOptionPane.showMessageDialog(null, "Colaborador con código " + codigo + " eliminado correctamente.");
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró un colaborador con el código: " + codigo);
            }
            llamada.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar el colaborador: " + e.getMessage());
        }
    }

    // permite acutualizar la informacion de un colaborador por medio de su codigo
    private static void actualizarColaborador() {
        String codigo = JOptionPane.showInputDialog("Ingrese el código del colaborador a actualizar:");
        String nombre1 = JOptionPane.showInputDialog("Ingrese el nuevo nombre 1:");
        String apellido1 = JOptionPane.showInputDialog("Ingrese el nuevo apellido 1:");
        String telefono = JOptionPane.showInputDialog("Ingrese el nuevo teléfono:");

        try (Connection conexion = DriverManager.getConnection(url, usuario, contrasena)) {
            String consultaSQL = "{CALL update_colaborador(?, ?, ?, ?)}";
            CallableStatement llamada = conexion.prepareCall(consultaSQL);
            llamada.setString(1, codigo);
            llamada.setString(2, nombre1);
            llamada.setString(3, apellido1);
            llamada.setString(4, telefono);

            int filasAfectadas = llamada.executeUpdate();
            if (filasAfectadas > 0) {
                JOptionPane.showMessageDialog(null, "Colaborador con código " + codigo + " actualizado correctamente.");
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró un colaborador con el código: " + codigo);
            }
            llamada.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar el colaborador: " + e.getMessage());
        }
    }

    // muestra a todos los colaboradores
    private static void mostrarColaboradores() {
        try (Connection conexion = DriverManager.getConnection(url, usuario, contrasena)) {
            String consultaSQL = "{CALL mostrar_colaboradores()}";
            CallableStatement llamada = conexion.prepareCall(consultaSQL);

            ResultSet resultado = llamada.executeQuery();
            StringBuilder mensaje = new StringBuilder("Lista de Colaboradores:\n");

            while (resultado.next()) {
                String codigo = resultado.getString("codigo");
                String nombre1 = resultado.getString("nombre1");
                String apellido1 = resultado.getString("apellido1");
                String telefono = resultado.getString("telefono");

                mensaje.append(String.format("Código: %s, Nombre 1: %s, Apellido 1: %s, Teléfono: %s\n",
                        codigo, nombre1, apellido1, telefono));
            }

            JOptionPane.showMessageDialog(null, mensaje.toString());
            resultado.close();
            llamada.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al mostrar los colaboradores: " + e.getMessage());
        }
    }
}

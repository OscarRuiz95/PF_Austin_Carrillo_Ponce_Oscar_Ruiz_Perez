package Controlador;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import Modelo.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Panel1 extends JFrame {

    // Conexión a la base de datos
    private static String url = "jdbc:mysql://localhost:3306/verdureria"; // URL de la base de datos
    private static String usuario = "root"; // Usuario de la base de datos
    private static String contrasena = "Devastador95."; // Contraseña de la base de datos

    private JPanel Principal;

    // Método para obtener la conexión a la base de datos
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, usuario, contrasena);
    }

    // Constructor de la clase Panel1
    public Panel1() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 240);
        Principal = new JPanel();
        Principal.setBackground(new Color(46, 46, 46));
        Principal.setToolTipText("Menu Principal");
        Principal.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(Principal);
        Principal.setLayout(null);

        // Centrar la ventana en la pantalla
        setLocationRelativeTo(null);

        JLabel lblBienvenidosAlRegistro = new JLabel("Bienvenido");
        lblBienvenidosAlRegistro.setBounds(80, 11, 350, 23);
        lblBienvenidosAlRegistro.setForeground(Color.WHITE);
        lblBienvenidosAlRegistro.setFont(new Font("Arial", Font.BOLD, 18));
        Principal.add(lblBienvenidosAlRegistro);

        JLabel label = new JLabel("");
        label.setBounds(527, 331, 137, 164);
        Principal.add(label);

        // Botón 
        JButton btnCliente = new JButton("Cliente");
        btnCliente.setBackground(new Color(38, 81, 255));
        btnCliente.setForeground(Color.WHITE);
        btnCliente.setBounds(42, 59, 138, 23);
        btnCliente.setFont(new Font("Arial", Font.BOLD, 12));
        btnCliente.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Instancia la clase PanelCliente y muestra su ventana
                PanelCliente panelCliente = new PanelCliente();
                panelCliente.setVisible(true);
            }
        });
        Principal.add(btnCliente);

        // Botón 
        JButton btnColaborador = new JButton("Colaborador");
        btnColaborador.setBackground(new Color(38, 81, 255));
        btnColaborador.setForeground(Color.WHITE);
        btnColaborador.setFont(new Font("Arial", Font.BOLD, 12));
        btnColaborador.setBounds(261, 59, 138, 23);
        btnColaborador.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Colaborador colaborador = new Colaborador();
                colaborador.setVisible(true);
            }
        });
        Principal.add(btnColaborador);

        // Botón 
        JButton btnOrden = new JButton("Orden");
        btnOrden.setBackground(new Color(38, 81, 255));
        btnOrden.setForeground(Color.WHITE);
        btnOrden.setBounds(42, 104, 138, 23);
        btnOrden.setFont(new Font("Arial", Font.BOLD, 12));
        btnOrden.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Orden orden = new Orden();
                orden.setVisible(true);
            }
        });
        Principal.add(btnOrden);

        // Botón 
        JButton btnPaquete = new JButton("Paquete");
        btnPaquete.setBackground(new Color(38, 81, 255));
        btnPaquete.setForeground(Color.WHITE);
        btnPaquete.setBounds(42, 149, 138, 23);
        btnPaquete.setFont(new Font("Arial", Font.BOLD, 12));
        btnPaquete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Paquete paquete = new Paquete();
                paquete.setVisible(true);
            }
        });
        Principal.add(btnPaquete);


        // Botón para salir
        JButton btnSalir = new JButton();
        ImageIcon icon = new ImageIcon(Cliente.class.getResource("/imagenes/salida.png"));
        btnSalir.setIcon(icon);
        btnSalir.setBounds(350, 150, 50, 50);
        // Eliminar el borde del botón
        btnSalir.setBorderPainted(false);
        btnSalir.setContentAreaFilled(false);
        btnSalir.setFocusPainted(false);
        btnSalir.setOpaque(false);
        btnSalir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int a = JOptionPane.showConfirmDialog(null, "¿Está seguro de que desea salir",
                        "Confirmación", JOptionPane.YES_NO_OPTION);
                if (a == JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(null, "Gracias por preferirnos", "Cerrando sistema",
                            JOptionPane.INFORMATION_MESSAGE);
                    System.exit(0);
                }
            }
        });
        Principal.add(btnSalir);
    }











// Método para insertar datos en la base de datos
private void insertarDatos(int id, String nombre, int plastico, int papel, int vidrio) {
    
    //calculos para la compensación
    int plasticoCalculado = plastico * 100;
    int papelCalculado = papel * 200;
    int vidrioCalculado = vidrio * 300;
    int compensacionTotal = plasticoCalculado + papelCalculado + vidrioCalculado;

    String query = "INSERT INTO " + Datos_TABLE + " (ID, Nombre, Plastico, Papel, Vidrio, Compensación) VALUES (?, ?, ?, ?, ?, ?)";
    try (Connection connection = getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(query)) {
        preparedStatement.setInt(1, id);
        preparedStatement.setString(2, nombre);
        preparedStatement.setInt(3, plastico);
        preparedStatement.setInt(4, papel);
        preparedStatement.setInt(5, vidrio);
        preparedStatement.setInt(6, compensacionTotal);

        preparedStatement.executeUpdate();
        
        //Este JOptionPane muestra los calculos
        JOptionPane.showMessageDialog(this, "Compensación:\n"
                + "Plastico: " + plasticoCalculado + "\n"
                + "Papel: " + papelCalculado + "\n"
                + "Vidrio: " + vidrioCalculado + "\n"
                + "Compensación Total: " + compensacionTotal,
                "", JOptionPane.INFORMATION_MESSAGE);

        JOptionPane.showMessageDialog(this, "Datos insertados exitosamente.");

    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error al insertar datos: " + ex.getMessage(), "Error",
                JOptionPane.ERROR_MESSAGE);
    }
}
    // Método para mostrar los datos almacenados en la base de datos
    private void mostrarDatos() {
        JFrame frame = new JFrame();
        TextArea textArea = new TextArea(30, 70);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        String query = "SELECT * FROM " + Datos_TABLE;
        try (Connection connection = getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery()) {
            StringBuilder datos = new StringBuilder();
            while (resultSet.next()) {
                datos.append("ID: ").append(resultSet.getInt("ID"))
                        .append(", Nombre: ").append(resultSet.getString("Nombre"))
                        .append(", Plástico en KG: ").append(resultSet.getInt("Plastico"))
                        .append(", Papel en KG: ").append(resultSet.getInt("Papel"))
                        .append(", Vidrio en KG: ").append(resultSet.getInt("Vidrio"))
                        .append(", Compensación total: ").append(resultSet.getInt("Compensación"))
                        .append("\n-----------------------------------------------------------------------------------------------------\n");
            }
            textArea.setText(datos.toString());
            JOptionPane.showMessageDialog(frame, scrollPane);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al mostrar datos: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // metodo buscar un dato en especifico de la base de datos 
    private void buscarDatos() {
        Buscar dialog = new Buscar(null); 
        dialog.setVisible(true);

        if (dialog.DatosIngresados()) {
            int idBuscar = dialog.getId();
            String query = "SELECT * FROM datos_reciclaje WHERE id = ?";

            try (Connection connection = getConnection();
                    PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, idBuscar);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    //formato de como se muestran los datos
                    int id = resultSet.getInt("id");
                    String nombre = resultSet.getString("nombre");
                    int plastico = resultSet.getInt("plastico");
                    int papel = resultSet.getInt("papel");
                    int vidrio = resultSet.getInt("vidrio");
                    int compensacionTotal = resultSet.getInt("Compensación"); 

                    JOptionPane.showMessageDialog(this,
                            "ID: " + id + "\nNombre: " + nombre + "\nCantida de plástico en KG: " + plastico + "\nCantida de papel en KG: " + papel
                                    + "\nCantida de vidrio en KG: " + vidrio + "\nCompensación total: " + compensacionTotal,
                            "Datos Encontrados", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "No se encontró ningún registro con el ID especificado.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al buscar datos: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Búsqueda cancelada.", "Cancelado", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Método para eliminar datos de la base de datos
    private void eliminarDatos() {

        Eliminar dialog = new Eliminar(null); 
        dialog.setVisible(true);

        if (dialog.DatosIngresados()) {
            int idEliminar = dialog.getId();
            String query = "DELETE FROM datos_reciclaje WHERE id = ?";

            try (Connection connection = getConnection();
                    PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, idEliminar);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Registro eliminado exitosamente.");
                } else {
                    JOptionPane.showMessageDialog(this, "No se encontró ningún registro con el ID especificado.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al eliminar datos: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Eliminación cancelada.", "Cancelado", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}

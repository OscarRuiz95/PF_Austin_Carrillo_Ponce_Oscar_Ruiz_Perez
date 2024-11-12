package Modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import Controlador.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends JFrame implements ActionListener {
    private JTextField usuarioTextField;
    private JPasswordField passwordField;
    private JButton loginButton;

    // conexión a la base de datos
    private static String url = "jdbc:mysql://localhost:3306/verdureria"; // URL de la base de datos
    private static String usuario = "root"; // Usuario de la base de datos
    private static String contrasena = "208240625."; // Contraseña de la base de datos

    // Tabla de contiene los usuarios en la base de datos
    private static final String ADMIN_TABLE = "administradores"; 

    // Método para obtener la conexión a la base de datos
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, usuario, contrasena);
    }
    
    public Login() {
        // Configurar el frame
        setTitle("Login");
        setSize(350, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Crear panel principal
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(3, 1, 10, 10)); // Espacio entre componentes
        mainPanel.setBackground(Color.LIGHT_GRAY); // Fondo de color 
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Márgenes alrededor del panel
        
        // Campo de texto para el usuario
        usuarioTextField = new JTextField();
        usuarioTextField.setBorder(BorderFactory.createTitledBorder("Usuario"));
        usuarioTextField.setFont(new Font("Arial", Font.PLAIN, 14));
        mainPanel.add(usuarioTextField);

        // Campo de texto para la contraseña
        passwordField = new JPasswordField();
        passwordField.setBorder(BorderFactory.createTitledBorder("Contraseña"));
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        mainPanel.add(passwordField);

        // Botón para iniciar sesión
        loginButton = new JButton("Iniciar Sesión");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setBackground(Color.BLUE); 
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.addActionListener(this);
        mainPanel.add(loginButton);

        // Añadir el panel principal al frame
        add(mainPanel, BorderLayout.CENTER);

        // Centrar la ventana en la pantalla
        setLocationRelativeTo(null);

        // Hacer visible la ventana
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String usuario = usuarioTextField.getText();
        String password = new String(passwordField.getPassword());
         // Autenticar el usuario y enviar mensaje 
        if (autenticarUsuario(usuario, password)) {
            JOptionPane.showMessageDialog(this, "¡Inicio de sesión exitoso!");
            Panel1 pc = new Panel1();
            pc.setVisible(true); 
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos. Inténtalo de nuevo.");
        }
    }
      // Método para autenticar el usuario 
    private boolean autenticarUsuario(String usuario, String password) {
        boolean autenticado = false;
        String query = "SELECT * FROM " + ADMIN_TABLE + " WHERE login = ? AND Clave = ?"; 

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, usuario);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                autenticado = true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return autenticado;
    }
}

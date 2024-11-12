package Controlador;

import java.sql.Connection;
import java.sql.DriverManager;
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
                Cliente panelCliente = new Cliente();
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

}

package ventanas;

import conexion.ConexionUsuario;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.LineBorder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

//Clases del componente logico
import logica.Usuario;

/**
 *
 * @author Pablo Erick Ramírez Cruz
 */
public class Login extends JFrame {

    private JComboBox userField;
    private JPasswordField passwordField;
    private JButton loginButton, viewPassButton;
    private boolean showPassword = false;
    private JFrame contexto = this;

    public Login() {

        //Ventana
        this.setSize(400, 300);
        this.setTitle("Acceso al sistema");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setIconImage(Constantes.icon.getImage());

        //Contenedor
        Container contenedor = this.getContentPane();
        contenedor.setLayout(new GridLayout(1, 5));
        JLabel logo = new JLabel();
        URL ruta = Login.class.getResource("logo.png");
        ImageIcon imagen = new ImageIcon(ruta);
        Image imagenRedimensionada = imagen.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        logo.setIcon(new ImageIcon(imagenRedimensionada));
        logo.setHorizontalAlignment(JLabel.CENTER);
        JPanel panelIzq = new JPanel(new BorderLayout());
        panelIzq.add(logo, BorderLayout.CENTER);
        panelIzq.setBackground(Constantes.colorPrincipal);
        contenedor.add(panelIzq);

        //Panel Derecho
        JPanel panelDer = new JPanel();
        panelDer.setLayout(null);
        panelDer.setBackground(Constantes.colorLight);

        JLabel title = new JLabel("Inicio de Sesión");
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setFont(Constantes.fontBold);
        title.setBounds(25, 15, 150, 20);
        panelDer.add(title);

        JLabel userLabel = new JLabel("Usuario");
        userLabel.setFont(Constantes.fontPlain);
        userLabel.setBounds(10, 60, 60, 20);
        panelDer.add(userLabel);

        ConexionUsuario cu = new ConexionUsuario(null);
        ResultSet rs = cu.obtenerTodos();

        int numFilas = 0;
        String nombresUsuario[] = null;
        try {
            while (rs.next()) {
                numFilas++;
            }

            nombresUsuario = new String[numFilas];
            rs.beforeFirst();
            for (int i = 0; i < nombresUsuario.length; i++) {
                rs.next();
                nombresUsuario[i] = rs.getString(2);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error " + ex.getMessage());
        }

        userField = new JComboBox(nombresUsuario);
        userField.setBounds(10, 80, 170, 20);
        userField.setBorder(new LineBorder(Constantes.colorPrincipal));
        panelDer.add(userField);

        JLabel passwordLabel = new JLabel("Contraseña");
        passwordLabel.setFont(Constantes.fontPlain);
        passwordLabel.setBounds(10, 120, 80, 20);
        panelDer.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(10, 140, 140, 20);
        passwordField.setBorder(new LineBorder(Constantes.colorPrincipal));
        passwordField.setEchoChar('*');
        panelDer.add(passwordField);

        viewPassButton = new JButton();
        viewPassButton.setBounds(160, 140, 20, 20);
        viewPassButton.setIcon(new ImageIcon(Login.class.getResource("vision.png")));
        viewPassButton.setOpaque(true);
        viewPassButton.setBackground(null);
        viewPassButton.setBorder(null);
        viewPassButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewPassButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                passwordField.setEchoChar(showPassword ? '*' : (char) 0);
                viewPassButton.setIcon(showPassword ? new ImageIcon(Login.class.getResource("vision.png")) : new ImageIcon(Login.class.getResource("no-vision.png")));
                showPassword = !showPassword;
            }
        });
        panelDer.add(viewPassButton);

        loginButton = new JButton("Entrar");
        loginButton.setBounds(60, 200, 80, 25);
        loginButton.setBackground(Constantes.colorPrincipal);
        loginButton.setForeground(Constantes.colorLight);
        loginButton.setBorder(null);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.addMouseListener(new ButtonHover(loginButton, ButtonHover.BACKGROUND));
        loginButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                Usuario u = new Usuario(String.valueOf(userField.getSelectedItem()), passwordField.getPassword(), null);
                ConexionUsuario cu = new ConexionUsuario(u);

                String resultado = cu.accesar();

                if (!resultado.equals("denegado")) {

                    ResultSet rs = cu.obtener();
                    try {
                        if (rs.next()) {
                            int id = rs.getInt("id");
                            u.setId(id);
                            u.setRol(resultado);
                        }

                    } catch (SQLException ex) {
                        Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    setVisible(false);
                    Cargando carga = new Cargando(u);
                    carga.setVisible(true);

                } else {
                    JOptionPane.showMessageDialog(contexto, "Contraseña Incorrecta", "ACCESO DENEGADO", JOptionPane.WARNING_MESSAGE);
                }

            }

        });
        panelDer.add(loginButton);

        contenedor.add(panelDer);
        
        passwordField.setText("pablO1234");
    }
}

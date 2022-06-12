package ventanas;

import conexion.ConexionUsuario;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import java.sql.ResultSet;
//Clases del componente lógico
import logica.Usuario;
import logica.Validacion;

/**
 *
 * @author pablo erick ramirez cruz
 */
public class RegistroUsuario extends JFrame {

    private Container contenedor = this.getContentPane();
    private JTextField userField, passField, passConfirmField;
    private JRadioButton admiRadio, subjefeRadio, registradorRadio;
    private JButton registrar, editar, borrar;
    private ButtonGroup opciones;
    private DefaultListModel modelo;
    private JFrame contexto = this;
    Usuario u;

    public RegistroUsuario(Usuario u) {
        this.u = u;

        this.setSize(400, 350);
        this.setTitle("Registro de Usuario");
        //this.setAlwaysOnTop(true);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setIconImage(Constantes.icon.getImage());
        contenedor.setLayout(null);

        JLabel userLabel = new JLabel("Nombre de usuario:");
        userLabel.setBounds(25, 60, 160, 20);
        userLabel.setFont(Constantes.fontBold);
        contenedor.add(userLabel);

        userField = new JTextField();
        userField.setBounds(200, 60, 165, 20);
        userField.setFont(Constantes.fontPlain);
        userField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                Validacion.escribirSoloTexto(e);
            }

        });

        contenedor.add(userField);

        JLabel passLabel = new JLabel("Contraseña:");
        passLabel.setBounds(25, 100, 150, 20);
        passLabel.setFont(Constantes.fontBold);
        contenedor.add(passLabel);

        passField = new JTextField();
        passField.setBounds(200, 100, 165, 20);
        passField.setFont(Constantes.fontPlain);
        contenedor.add(passField);

        JLabel passConfirmLabel = new JLabel("Confirmar contraseña:");
        passConfirmLabel.setBounds(25, 140, 180, 20);
        passConfirmLabel.setFont(Constantes.fontBold);
        contenedor.add(passConfirmLabel);

        passConfirmField = new JTextField();
        passConfirmField.setBounds(200, 140, 165, 20);
        passConfirmField.setFont(Constantes.fontPlain);
        contenedor.add(passConfirmField);

        JLabel rolLabel = new JLabel("Rol:");
        rolLabel.setBounds(25, 180, 180, 20);
        rolLabel.setFont(Constantes.fontBold);
        contenedor.add(rolLabel);

        opciones = new ButtonGroup();
        admiRadio = new JRadioButton("ADMINISTRADOR");
        admiRadio.setActionCommand("ADMINISTRADOR");
        opciones.add(admiRadio);
        subjefeRadio = new JRadioButton("SUBJEFE");
        subjefeRadio.setActionCommand("SUBJEFE");
        opciones.add(subjefeRadio);
        registradorRadio = new JRadioButton("REGISTRADOR");
        registradorRadio.setActionCommand("REGISTRADOR");
        opciones.add(registradorRadio);

        admiRadio.setToolTipText("Tiene todos los permisos disponibles (Modificar usuarios, trabajadores, sueldos, faltas, etc.)");
        subjefeRadio.setToolTipText("Puede modificar faltas, retardos, datos de los trabajadores");
        registradorRadio.setToolTipText("Solo puede moficar retardos y faltas");

        admiRadio.setBounds(60, 180, 125, 20);
        subjefeRadio.setBounds(185, 180, 80, 20);
        registradorRadio.setBounds(265, 180, 120, 20);

        contenedor.add(admiRadio);
        contenedor.add(subjefeRadio);
        contenedor.add(registradorRadio);

        registrar = new JButton("REGISTRAR USUARIO");
        registrar.setBounds(100, 230, 200, 30);
        registrar.setBackground(Constantes.colorPrincipal);
        registrar.setForeground(Color.WHITE);
        registrar.addMouseListener(new ButtonHover(registrar, ButtonHover.BACKGROUND));
        registrar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                //Realizar validaciones

                //Valida campos vacios
                Object matriz[][] = {
                    {userField, passField, passConfirmField},
                    {"Debe ingresar un nombre de usuario", "Debe ingresar una contraseña", "Debe confirmar su contraseña"}
                };
                String errores = Validacion.comprobarVacios(matriz);
                if (!errores.isEmpty()) {
                    JOptionPane.showMessageDialog(contexto, errores, "ERROR", JOptionPane.WARNING_MESSAGE);
                } else {
                    // Valida contraseña
                    if (!passField.getText().isEmpty() && passField.getText().equals(passConfirmField.getText())) {

                        if (Validacion.comprobarContraseña(passConfirmField.getText())) {

                            //Registrar el usuario
                            String nombre = userField.getText();
                            char[] pass = passConfirmField.getText().toCharArray();
                            String rol = opciones.getSelection().getActionCommand();
                            Usuario nuevo = new Usuario(nombre, pass, rol);

                            ConexionUsuario cu = new ConexionUsuario(nuevo);
                            if (cu.insertUsuario() == 1) {
                                JOptionPane.showMessageDialog(null, "Se registró con exito");

                            } else {
                                JOptionPane.showMessageDialog(null, "No se pudo guardar");
                            }

                            try {
                                //Se necesita comprobar si la lista tiene un solo elemento
                                int cantidadFilas = 0;
                                ResultSet rs = cu.obtenerTodos();
                                while (rs.next()) {
                                    cantidadFilas++;
                                }

                                if (cantidadFilas == 1) {
                                    //Se acaba de crear el primer usuario, entonces se abre el programa por primera vez y loggearse
                                    Login login = new Login();
                                    login.setVisible(true);
                                    setVisible(false);
                                } else {
                                    modelo.removeAllElements();
                                    ConexionUsuario cu2 = new ConexionUsuario(null);
                                    ResultSet rs2 = cu2.obtenerTodos();
                                    try {
                                        int i = 0;
                                        while (rs2.next()) {
                                            int id = rs2.getInt("id");
                                            String name = rs2.getString("name");
                                            String rol2 = rs2.getString("rol");

                                            modelo.add(i, id + "-" + name + " " + rol2);
                                            i++;
                                        }
                                    } catch (SQLException ex) {
                                        System.out.println(ex.getMessage());
                                    }
                                }
                            } catch (SQLException ex) {
                                JOptionPane.showMessageDialog(contexto, "Error " + ex.getMessage());
                            }
                        } else {
                            JOptionPane.showMessageDialog(contexto, "Su contraseña debe tener de 8 a 16 caracteres, incluyendo mayusculas, minusculas y números", "CONTRASEÑA INSEGURA", JOptionPane.WARNING_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(contexto, "Las contraseñas no coinciden, compruebe los campos", "CONTRASEÑAS NO IGUALES", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }

        });
        contenedor.add(registrar);

        if (new ConexionUsuario(null).isEmpty()) {
            JLabel mensaje = new JLabel("<html>No hay usuarios registrados debe crear un usuario<br>administrador</html>");
            mensaje.setBounds(25, 5, 380, 40);
            mensaje.setFont(Constantes.fontPlain);
            mensaje.setForeground(Color.RED);
            contenedor.add(mensaje);
            admiRadio.setSelected(true);
            subjefeRadio.setEnabled(false);
            registradorRadio.setEnabled(false);
        } else { //Si ya hay usuarios, se mostrará una lista con ellos y botones de agregar, modificar y borrar
            this.setSize(500, 400);

            userLabel.setBounds(20, 20, 160, 20);
            userField.setBounds(20, 40, 250, 20);
            passLabel.setBounds(20, 70, 150, 20);
            passField.setBounds(20, 90, 250, 20);
            passConfirmLabel.setBounds(20, 120, 180, 20);
            passConfirmField.setBounds(20, 140, 250, 20);
            rolLabel.setBounds(20, 170, 180, 20);
            admiRadio.setBounds(20, 190, 125, 20);
            subjefeRadio.setBounds(20, 210, 80, 20);
            registradorRadio.setBounds(20, 230, 120, 20);
            registrar.setBounds(50, 280, 200, 30);

            JLabel listaTitulo = new JLabel("Usuarios Registrados", JLabel.CENTER);
            listaTitulo.setBounds(320, 20, 140, 30);
            listaTitulo.setFont(Constantes.fontPlain);
            contenedor.add(listaTitulo);

            modelo = new DefaultListModel();
            JList lista = new JList(modelo);

            ConexionUsuario cu = new ConexionUsuario(null);
            ResultSet rs = cu.obtenerTodos();
            try {
                int i = 0;
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    String rol = rs.getString("rol");

                    modelo.add(i, id + "-" + name + " " + rol);
                    i++;
                }
            } catch (SQLException ex) {

            }

            lista.setBounds(310, 50, 160, 220);
            lista.setBorder(BorderFactory.createEtchedBorder());
            lista.setFont(Constantes.fontPlain);
            contenedor.add(lista);

            borrar = new JButton("Borrar");
            borrar.setBounds(310, 280, 75, 30);
            borrar.setBackground(Constantes.colorPrincipal);
            borrar.setForeground(Color.WHITE);
            borrar.addMouseListener(new ButtonHover(borrar, ButtonHover.BACKGROUND));
            borrar.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {

                    ConexionUsuario cu = new ConexionUsuario(null);
                    ResultSet rs = cu.obtenerTodos();
                    int numFilas = 0;
                    try {
                        while (rs.next()) {
                            numFilas++;
                        }
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }

                    if (lista.getSelectedIndex() == -1) { //Si el usuario no selecciona ningun elemento
                        return;

                    }

                    String cadena = String.valueOf(lista.getSelectedValue());
                    int id = Integer.parseInt(cadena.split("-")[0]);

                    if (numFilas == 1) {
                        JOptionPane.showMessageDialog(null, "Debe haber minimo 1 usuario registrado", "NO SE PUEDE BORRAR", JOptionPane.WARNING_MESSAGE);

                    } else if (u.getId() == id) {
                        JOptionPane.showMessageDialog(null, "No puede borrarse a si mismo", "IMPOSIBLE", JOptionPane.WARNING_MESSAGE);
                    } else {

                        Usuario usuarioTemp = new Usuario(String.valueOf(lista.getSelectedValue()), null, null);
                        usuarioTemp.setId(id);

                        ConexionUsuario conexionUsuario = new ConexionUsuario(usuarioTemp);

                        if (conexionUsuario.delUsuario() == 1) {
                            modelo.remove(lista.getSelectedIndex());
                        }

                    }
                }
            });
            contenedor.add(borrar);

            lista.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() != 2) {
                        return;
                    }
                    String cadena = String.valueOf(lista.getSelectedValue());
                    int id = Integer.parseInt(cadena.split("-")[0]);
                    Usuario usuarioAux = new Usuario(null, null, null);
                    usuarioAux.setId(id);
                    ConexionUsuario cu = new ConexionUsuario(usuarioAux);
                    ResultSet rs = cu.obtenerPorId();

                    try {
                        if (rs.next()) {
                            userField.setText(rs.getString("name"));
                            passField.setText(rs.getString("password"));
                        }
                    } catch (SQLException ex) {

                    }

                }
            });

            editar = new JButton("Editar");
            editar.setBounds(395, 280, 75, 30);
            editar.setBackground(Constantes.colorPrincipal);
            editar.setForeground(Color.WHITE);
            editar.addMouseListener(new ButtonHover(editar, ButtonHover.BACKGROUND));
            editar.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (lista.getSelectedIndex() == -1) {
                        return;
                    }
                    String cadena = String.valueOf(lista.getSelectedValue());
                    int id = Integer.parseInt(cadena.split("-")[0]);
                    Usuario usuarioAux = new Usuario(null, null, null);
                    usuarioAux.setId(id);
                    usuarioAux.setNombre(userField.getText());
                    usuarioAux.setPassword(passField.getText().toCharArray());
                    if (opciones.getSelection() == null){
                        return;
                    }
                    usuarioAux.setRol(opciones.getSelection().getActionCommand());
                    ConexionUsuario cu = new ConexionUsuario(usuarioAux);

                    
                    if (!Validacion.comprobarContraseña(passField.getText())){
                        JOptionPane.showMessageDialog(null, "Contraseña no valida");
                        return;
                    }
                    
                    if (!passField.getText().equals(passConfirmField.getText())){
                        JOptionPane.showMessageDialog(null, "La contraseña no coincide");
                        return;
                    }
                    if (cu.editarUsuario() == 1) {
                        modelo.removeAllElements();
                        ConexionUsuario cu2 = new ConexionUsuario(null);
                        ResultSet rs2 = cu2.obtenerTodos();
                        try {
                            int i = 0;
                            while (rs2.next()) {
                                int id2 = rs2.getInt("id");
                                String name = rs2.getString("name");
                                String rol2 = rs2.getString("rol");

                                modelo.add(i, id2 + "-" + name + " " + rol2);
                                i++;
                            }
                        } catch (SQLException ex) {
                            System.out.println(ex.getMessage());
                        }
                    }
                }
            });
            contenedor.add(editar);

        }
    }

}
